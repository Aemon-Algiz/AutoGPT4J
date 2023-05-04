package com.autogpt4j.command;

import org.springframework.beans.factory.annotation.Value;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterCommand extends Command {

    private final String tweetText;

    @Value("${TWITTER_CONSUMER_KEY}")
    String consumerKey = System.getenv("TW_CONSUMER_KEY");

    @Value("${TWITTER_CONSUMER_SECRET}")
    String consumerSecret = System.getenv("TW_CONSUMER_SECRET");

    @Value("${TWITTER_ACCESS_TOKEN}")
    String accessToken = System.getenv("TW_ACCESS_TOKEN");

    @Value("${TWITTER_ACCESS_TOKEN_SECRET}")
    String accessTokenSecret = System.getenv("TW_ACCESS_TOKEN_SECRET");

    public TwitterCommand(String tweetText) {
        this.tweetText = tweetText;
    }

    public String execute() {
        return sendTweet();
    }

    private String sendTweet() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        try {
            Status status = twitter.updateStatus(tweetText);
            return status.getText();
        } catch (TwitterException e) {
            return "Error sending tweet: " + e.getErrorMessage();
        }
    }
}