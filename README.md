# AutoGPT4J README

## Introduction

AutoGPT4J is an adaptation of the popular AutoGPT project, which is originally written in Python, into Java. This library allows you to use the powerful GPT-4 model in your Java projects. It provides an interface for interacting with OpenAI's GPT-4 model and Pinecone, a vector database.

## Setup

To get started with AutoGPT4J, you will need to set up some environment variables. You can set these variables in your `application.properties` file or as environment variables in your system. The required variables are as follows:

### File settings

1. `FILES_LOCATION`: This sets the location where files will be saved.

### Log settings

2. `log_path`: Specifies the path for log files.

### OpenAI settings

3. `OPEN_AI_EMBEDDING_MODEL`: The name of the embedding model to use.
4. `OPEN_AI_CONTEXT_MODEL`: The name of the context model to use.
5. `OPENAI_API_KEY`: Your OpenAI API key.
6. `OPEN_AI_MODEL`: The name of the GPT-4 model to use.

### Pinecone settings

7. `PINECONE_API_KEY`: Your Pinecone API key.
8. `PINECONE_ENVIRONMENT`: The Pinecone environment you want to use.
9. `PINECONE_PROJECT_NAME`: The name of your Pinecone project.
10. `PINECONE_INDEX_NAME`: The name of the index to use in Pinecone.
11. `PINECONE_NAMESPACE_INDEX`: The namespace index for Pinecone.

### Contributing
We welcome contributions to AutoGPT4J! If you'd like to contribute, please fork the repository and submit a pull request with your changes. Make sure to follow the code style guidelines and provide tests for any new features.

### License
AutoGPT4J is licensed under the MIT License.

## Future Work

We have planned several enhancements and features for future releases of AutoGPT4J:

1. Add additional commands: We aim to expand the functionality of AutoGPT4J by adding more commands that allow for better interaction and control of the GPT-4 model.
2. Implement iterative agents: To improve the overall user experience, we plan on implementing iterative agents that can engage in more interactive and dynamic conversations with users.
3. Add additional support for open-source projects: We're committed to fostering an open-source community around AutoGPT4J and will work towards providing better support and integration for other open-source projects including open-source LLM's.
