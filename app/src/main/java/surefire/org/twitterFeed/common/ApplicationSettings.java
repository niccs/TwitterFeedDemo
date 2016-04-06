package surefire.org.twitterFeed.common;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by HP on 3/04/2016.
 */
public class ApplicationSettings extends Application {


    public static final String AUTH_TOKEN = "AUTH_TOKEN";
    public static final String ERROR_MESSAGE = "ERROR_MESSAGE";
    public static final String PROFILE_IMAGE = "PROFILE_IMAGE";


    private static SharedPreferences sharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }



    private static void saveString(Context context, String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setAuthToken(Context context, String authToken) {
        saveString(context, AUTH_TOKEN, authToken);
    }
    public static String getAuthToken(Context context) {
        return sharedPreferences(context).getString(AUTH_TOKEN, null);
    }

    public static void setErrorMessage(Context context, String errMsg) {
        saveString(context, ERROR_MESSAGE, errMsg);
    }
    public static String getErrorMessage(Context context) {
        return sharedPreferences(context).getString(ERROR_MESSAGE, "No results fetched ");
    }

    public static void setProfileImage(Context context, String imageBase64) {
        saveString(context,PROFILE_IMAGE,imageBase64);
    }
    public static String getProfileImage(Context context) {
        return sharedPreferences(context).getString(PROFILE_IMAGE, " ");
    }

}
