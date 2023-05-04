package com.autogpt4j.command;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log
public class ExecuteCodeCommand extends Command {

    private final String fileName;

    @Value("${FILES_LOCATION}")
    private String filesLocation;

    public ExecuteCodeCommand(String fileName) {
        this.fileName = fileName;
    }

    public String execute() {
        return executePythonCode();
    }

    private String executePythonCode() {
        if(!getExtension().equals("py")) {
            return "Error: Invalid file type. Only .py files are allowed.";
        } else if (!fileExists()) {
            return String.format("Error: File %s does not exist.", fileName);
        }

        return runPythonFile().stream()
                    .collect(Collectors.joining(" "));

    }

    private List<String> runPythonFile() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", getFile().getAbsolutePath());
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            List<String> results = readProcessOutput(process.getInputStream());

            return results;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> readProcessOutput(InputStream inputStream) {
        try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream))) {
            return output.lines()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private File getFile() {
        return Paths.get(filesLocation, fileName).toFile();
    }

    private boolean fileExists() {
        return getFile().exists();
    }

    private String getExtension() {
        return getExtensionByStringHandling()
                .orElseThrow(() -> new RuntimeException("File does not have an extension."));
    }

    private Optional<String> getExtensionByStringHandling() {
        return Optional.ofNullable(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.lastIndexOf(".") + 1));
    }

    private boolean isDockerContainer() {
        return new File("./.dockerenv").exists();
    }
}
