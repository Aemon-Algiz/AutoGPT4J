package com.autogpt4j.command;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

public class CommandLog {

    public final Gson gson;

    @Value("${LOG_PATH}")
    private String logPath;

    public CommandLog() {
        this.gson = new Gson();
    }

    public void attemptToExecute(String commandName, Command command) {
        if(!isDuplicate(commandName, command)) {
            command.execute();
            writeToLog(commandName, command);
        }
    }

    private boolean isDuplicate(String commandName, Command command) {
        HashMap<String, List<Command>> commandMap = getCommandMap();

        if(commandMap.containsKey(commandName)) {
            List<Command> commands = commandMap.get(commandName);
            return commands.stream()
                    .anyMatch(it ->
                            command.getDescription().equals(it.getDescription())
                                    && command.getName().equals(it.getName())
                    );
        }

        return false;
    }

    private void writeToLog(String commandName, Command command) {
        HashMap<String, List<Command>> commandMap = getCommandMap();

        if(commandMap.containsKey(commandName)) {
            commandMap.get(commandName).add(command);
        } else {
            commandMap.put(commandName, Lists.newArrayList(command));
        }

        updateLog(commandMap);
    }

    private HashMap<String, List<Command>> getCommandMap() {
        return gson.fromJson(readLogFile(), HashMap.class);
    }

    private void updateLog(HashMap<String, List<Command>> commandMap) {
        try {
            FileUtils.writeStringToFile(new File(logPath), gson.toJson(commandMap), Charset.forName("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private String readLogFile() {
        try {
            return FileUtils.readFileToString(new File(logPath), Charset.forName("UTF-8"));
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
