package com.autogpt4j.command;

import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Paths;

@Log
public class FileCommand extends Command {

    public final static String WRITE = "write";
    public final static String APPEND = "append";
    public final static String DELETE = "delete";
    public final static String READ = "read";

    private final String commandName;
    private final String fileName;
    private final String text;
    private final CommandLog commandLog;

    @Value("${FILES_LOCATION}")
    private String filesLocation;

    public FileCommand(String commandName, String fileName, String text) {
        this.commandName = commandName;
        this.fileName = fileName;
        this.text = text;
        this.commandLog = new CommandLog();
    }

    public String execute() {
        String output = "";

        switch (fileName) {
            case WRITE:  writeFile();
                         break;
            case APPEND: appendToFile();
                         break;
            case DELETE: deleteFile();
                         break;
            case READ:   output = readFile();
                         break;
        }

        return output;
    }

    private void deleteFile() {
        log.warning("DELETING FILE: " + getFile().getAbsolutePath());

        try {
            FileUtils.delete(getFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void appendToFile() {
        log.warning("APPENDING TO FILE: " + getFile().getAbsolutePath());

        try {
            FileUtils.writeStringToFile(getFile(), text, Charset.forName("UTF-8"), true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void writeFile() {
        log.warning("WRITING FILE: " + getFile().getAbsolutePath());

        try {
            FileUtils.writeStringToFile(getFile(), text, Charset.forName("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String readFile() {
        log.warning("READING FILE: " + getFile().getAbsolutePath());

        try {
            return FileUtils.readFileToString(getFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private File getFile() {
        return Paths.get(filesLocation, fileName).toFile();
    }
}
