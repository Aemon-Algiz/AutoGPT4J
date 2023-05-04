package com.autogpt4j.command;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.UUID;

public class ImageGenerationCommand extends Command {

    private final String prompt;

    private final String fileName;

    private final Integer size;

    @Value("${FILES_LOCATION}")
    private String filesLocation;

    @Value("${IMAGE_PROVIDER}")
    private String imageProvider;

    @Value("${HUGGING_FACE_IMAGE_MODEL}")
    private String huggingFaceImageModel;

    @Value("${HUGGING_FACE_API_KEY}")
    private String huggingFaceApiKey;

    @Value("${SD_WEBUI_URL}")
    private String sdWebuiUrl;

    @Value("${SD_WEBUI_AUTH}")
    private String sdWebuiAuth;

    @Value("${OPENAI_API_KEY}")
    private String apiKey;

    public ImageGenerationCommand(String prompt, Integer size) {
        this.prompt = prompt;
        this.fileName = Paths.get(filesLocation, UUID.randomUUID() + ".jpg").toString();
        this.size = size;
    }

    public String execute() {
        return generateImage();
    }

    public String generateImage() {
        try {
            if ("dalle".equals(imageProvider)) {
                return generateImageWithDalle();
            } else if ("huggingface".equals(imageProvider)) {
                return generateImageWithHuggingFace();
            } else if ("sdwebui".equals(imageProvider)) {
                return generateImageWithSDWebUI();
            }
            return "No Image Provider Set";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generateImageWithHuggingFace() throws IOException {
        String apiUrl = String.format("https://api-inference.huggingface.co/models/%s", huggingFaceImageModel);

        if (huggingFaceApiKey== null) {
            throw new IllegalArgumentException("You need to set your Hugging Face API token in the config file.");
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost postRequest = new HttpPost(apiUrl);
            postRequest.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + huggingFaceApiKey);
            postRequest.setHeader("X-Use-Cache", "false");

            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("inputs", prompt);
            postRequest.setEntity(new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON));

            try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    byte[] imageBytes = EntityUtils.toByteArray(response.getEntity());
                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
                    ImageIO.write(image, "jpg", new File(fileName));
                    return fileName;
                } else {
                    throw new IOException("Failed to generate image with HuggingFace: " + response.getStatusLine().getReasonPhrase());
                }
            }
        }
    }

    private String generateImageWithDalle() throws IOException {
        String apiUrl = "https://api.openai.com/v1/images/generations";

        if (apiKey == null) {
            throw new IllegalArgumentException("You need to set your OpenAI API key in the config file.");
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost postRequest = new HttpPost(apiUrl);
            postRequest.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
            postRequest.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);

            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("prompt", prompt);
            requestBody.addProperty("n", 1);
            requestBody.addProperty("size", size + "x" + size);
            postRequest.setEntity(new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON));

            return writeImage(httpClient, postRequest);
        }
    }

    private String generateImageWithSDWebUI() throws IOException {
        String apiUrl = sdWebuiUrl + "/sdapi/v1/txt2img";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost postRequest = new HttpPost(apiUrl);
            if (sdWebuiAuth != null) {
                String[] authParts = sdWebuiAuth.split(":");
                String username = authParts[0];
                String password = authParts.length > 1 ? authParts[1] : "";
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
                postRequest.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + new String(encodedAuth));
            }

            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("prompt", prompt);
            requestBody.addProperty("sampler_index", "DDIM");
            requestBody.addProperty("steps", 20);
            requestBody.addProperty("cfg_scale", 7.0);
            requestBody.addProperty("width", size);
            requestBody.addProperty("height", size);
            requestBody.addProperty("n_iter", 1);
            // Add any extra parameters from the configuration here.
            postRequest.setEntity(new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON));

            return writeImage(httpClient, postRequest);
        }
    }

    private String writeImage(CloseableHttpClient httpClient, HttpPost postRequest) {
        try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String responseJson = EntityUtils.toString(response.getEntity());
                JsonObject responseObject = new Gson().fromJson(responseJson, JsonObject.class);
                String base64Image = responseObject.get("images").getAsJsonArray().get(0).getAsString();
                byte[] imageBytes = Base64.decodeBase64(base64Image.split(",", 2)[1]);
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
                ImageIO.write(image, "jpg", new File(fileName));
                return fileName;
            } else {
                throw new RuntimeException("Failed to generate image with SD WebUI: " +
                        response.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
