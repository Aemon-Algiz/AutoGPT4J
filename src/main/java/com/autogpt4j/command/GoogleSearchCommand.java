package com.autogpt4j.command;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.customsearch.v1.Customsearch;
import com.google.api.services.customsearch.v1.model.Result;
import com.google.api.services.customsearch.v1.model.Search;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GoogleSearchCommand extends Command {

    private final String query;

    private final Integer numResults;

    @Value("${GOOGLE_API_KEY}")
    private String GOOGLE_API_KEY = "your_google_api_key";

    @Value("${CUSTOM_SEARCH_ENGINE}")
    private String CUSTOM_SEARCH_ENGINE_ID = "your_custom_search_engine_id";

    public GoogleSearchCommand(String query, Integer numResults) {
        this.query = query;
        this.numResults = numResults;
    }

    @Override
    public String execute() {
        return googleOfficialSearch();
    }

    public String googleSearch() {
        return "DuckDuckGo search is not implemented at this time.";
    }

    public String googleOfficialSearch() {
        List<String> searchResultsLinks = new ArrayList<>();

        try {
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

            HttpRequestInitializer httpRequestInitializer = request -> {
                request.setConnectTimeout(60000);
                request.setReadTimeout(60000);
            };

            Customsearch customsearch = new Customsearch
                    .Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, httpRequestInitializer)
                    .setApplicationName("GoogleSearch")
                    .build();

            Customsearch.Cse.List request = customsearch.cse().list();
            request.setQ(query);
            request.setKey(GOOGLE_API_KEY);
            request.setCx(CUSTOM_SEARCH_ENGINE_ID);
            request.setNum(numResults);

            Search results = request.execute();
            if (results.getItems() != null) {
                searchResultsLinks = results.getItems().stream().map(Result::getLink).collect(Collectors.toList());
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        return safeGoogleResults(searchResultsLinks).stream()
                .collect(Collectors.joining("\n"));
    }

    private List<String> safeGoogleResults(List<String> results) {
        JSONArray jsonArray = new JSONArray(results);
        List<String> safeResults = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            safeResults.add(jsonObject.toString());
        }

        return safeResults;
    }
}