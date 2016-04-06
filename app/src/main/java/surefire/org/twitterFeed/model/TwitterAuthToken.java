package surefire.org.twitterFeed.model;

/**
 * Created by HP on 3/04/2016.
 */
public class TwitterAuthToken {

        private String token_type;
        private String access_token;

    public String getAuthTokenType() {
        return token_type;
    }

    public String getAuthAccessToken() {
        return access_token;
    }
    }
