package com.br.timetabler.util;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
 
import android.content.Context;
 
public class UserFunctions {
     
    private JSONParser jsonParser;
     
    // Testing in localhost using wamp or xampp
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/
    private static String loginURL = "http://10.0.2.2/last2.php";
    private static String registerURL = "http://10.0.2.2/last2.php";
    private static String notesURL = "http://10.0.2.2/last2.php";
    private static String uploadURL = "http://10.0.2.2/last2.php";
     
    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String notes_tag = "not";
    private static String upload_tag = "uploads";
    
     
    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }
     
    /**
     * function make Login Request
     * @param email
     * @param password
     * */
    public JSONObject loginUser(String reg_no, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("reg_no", reg_no));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }
     
    /**
     * function make Login Request
     * @param name
     * @param email
     * @param password
     * */
    public JSONObject registerUser(String name, String reg_no, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("reg_no", reg_no));
        params.add(new BasicNameValuePair("password", password));
         
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }
    
    
    
    
    
    
    public JSONObject uploadFiles(String scul, String course, String unit,String link, String receiver){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", upload_tag));
        params.add(new BasicNameValuePair("scul", scul));
        params.add(new BasicNameValuePair("course", course));
        params.add(new BasicNameValuePair("unit", unit));
        params.add(new BasicNameValuePair("link", link));
        params.add(new BasicNameValuePair("receiver", receiver));
       
         
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(uploadURL, params);
        // return json
        return json;
    }
    
    
    
    public JSONObject shareNotes(String lact, String unit_t, String notes){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", notes_tag));
        params.add(new BasicNameValuePair("lact", lact));
        params.add(new BasicNameValuePair("unit_t", unit_t));
        params.add(new BasicNameValuePair("notes", notes));
         
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(notesURL, params);
        // return json
        return json;
    }
     
    /**
     * Function get Login status
     * */
    public boolean isUserLoggedIn(Context context){
        DatabaseHandler_jemo db = new DatabaseHandler_jemo(context);
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
        DatabaseHandler_jemo db = new DatabaseHandler_jemo(context);
        db.resetTables();
        return true;
    }
     
}
