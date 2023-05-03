package com.autogpt4j.prompts;

public class Prompt {

    public static final String DEFAULT_PROMPT = "Determine which next command to use, and respond using the format "
            + "specified above:";

    public static final String WORD_LIMIT = "~4000 word limit for short term memory. Your short term memory is short, "
            + "so immediately save important information to files.";

    public static final String REMEMBER = "If you are unsure how you previously did something or want to recall past"
            + " events, thinking about similar events will help you remember.";

    public static final String NO_USER = "No user assistance";

    public static final String EXCLUSIVELY_USE_COMMANDS = "Exclusively use the commands listed in double quotes "
            + "e.g. \"command name\"";

    public static final String COMMANDS = "\"Task Complete (Shutdown)\", \"task_complete\", {\"reason\": \"<reason>\"}";

    public static final String INTERNET = "Internet access for searches and information gathering.";

    public static final String MEMORY = "Long Term memory management.";

    public static final String AGENTS = "GPT-3.5 powered Agents for delegation of simple tasks.";

    public static final String FILE_OUTPUT = "File output.";

    public static final String ANALYZE = "Continuously review and analyze your actions to ensure you are performing to"
            + " the best of your abilities.";

    public static final String CRITICIZE = "Constructively self-criticize your big-picture behavior constantly.";

    public static final String REFLECT = "Reflect on past decisions and strategies to refine your approach.";

    public static final String EFFICIENT = "Every command has a cost, so be smart and efficient. Aim to complete tasks "
            + "in the least number of steps.";

    public static final String WRITE_CODE_TO_FILE = "Write all code to a file.";

    public static final String SUMMARY_PROMPT = "Your task is to create a concise running summary of actions and "
            + "information results in the provided text, focusing on key and potentially important information to "
            + "remember. You will receive the current summary and the your latest actions. Combine them, "
            + "adding relevant key information from the latest development in 1st person past tense and keeping "
            + "the summary concise.";
}
