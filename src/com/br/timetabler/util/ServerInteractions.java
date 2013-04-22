package com.br.timetabler.util;
 
import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
 
import android.content.Context;
 
public class ServerInteractions {
 
    private JSONParser jsonParser;
 
    // Testing in localhost using wamp or xampp
    // use http://10.0.2.2/ to connect to localhost ie http://localhost/feedback
    private static String loginURL = "http://10.0.2.2/timetabler/regLogin.php";
    private static String registerURL = "http://10.0.2.2/timetabler/regLogin.php";
    private static String regSettingsURL = "http://10.0.2.2/timetabler/regSettings.php";
    private static String commentURL = "http://10.0.2.2/timetabler/saveReviews.php";
    private static String feedbackURL = "http://10.0.2.2/timetabler/saveFeedback.php";
    
    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String comment_tag = "comments";
    private static String feedback_tag = "feedback";
    
    // constructor
    public ServerInteractions(){
        jsonParser = new JSONParser();
    }
 
    /**
     * function make Login Request
     * @param email
     * @param password
     * */
    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }
 
    /**
     * function make register Request
     * @param name
     * @param email
     * @param password
     * */
    public JSONObject registerUser(String name, String email, String password, String school){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("fullnames", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("schoolId", school));
        
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }
    /**
     * function make register Request
     * @param name
     * @param email
     * @param password
     * */
    public JSONObject registerUserSettings(String schoolId, String course, String year, String intake, String semester, String userId){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("schoolId", schoolId));
        params.add(new BasicNameValuePair("course", course));
        params.add(new BasicNameValuePair("year", year));
        params.add(new BasicNameValuePair("intake", intake));
        params.add(new BasicNameValuePair("semester", semester));
        params.add(new BasicNameValuePair("userId", userId));
        
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(regSettingsURL, params);
        // return json
        return json;
    }
 
    /**
     * function make comment save Request
     * @param comments
     * */
    public JSONObject postComment(String comments, String userId, String unit_id){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", comment_tag));
        params.add(new BasicNameValuePair("unit_id", unit_id));
        params.add(new BasicNameValuePair("comments", comments));
        params.add(new BasicNameValuePair("user_id", userId));
        
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(commentURL, params);
        // return json
        return json;
    }
 
    /**
     * function make comment save Request
     * @param comments
     * */
    public JSONObject postFeedback(String feedback, String userId){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", feedback_tag));
        params.add(new BasicNameValuePair("feedback", feedback));
        params.add(new BasicNameValuePair("user_id", userId));
        
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(feedbackURL, params);
        // return json
        return json;
    }
 
    /**
     * Function get Login status
     * */
    public boolean isUserLoggedIn(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        int count = db.getRowCount();
        if(count > 0){
            // user logged in
            return true;
        }
        return false;
    }
 
    /**
     * Function to logout user
     * Reset Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }
 
}