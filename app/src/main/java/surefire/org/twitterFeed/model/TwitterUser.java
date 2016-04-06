package surefire.org.twitterFeed.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HP on 3/04/2016.
 */
public class TwitterUser {

    @SerializedName("screen_name")
    private String screenName;

    @SerializedName("name")
    private String name;

    @SerializedName("profile_image_url")
    private String profileImageUrl;



    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString(){

        StringBuilder sb=new StringBuilder();

        sb.append(name).append(" ").append(screenName).append(" ").append(profileImageUrl);
        return sb.toString();
    }
}
