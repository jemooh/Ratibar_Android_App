package com.br.timetabler.service.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.br.timetabler.model.School;
import com.br.timetabler.model.SchoolLibrary;
import com.br.timetabler.util.Log;
import com.br.timetabler.util.StreamUtils;

public class GetUniversityDetailsTask implements Runnable {
	// A reference to retrieve the data when this task finishes
    public static final String LIBRARY = "SchoolLibrary";
    // A handler that will be notified when the task is finished
    private final Handler replyTo;
    // The user we are querying on server for lessons
    
    private String SearchQuery;
    private String Url;
    private static String MainURL = "http://10.0.2.2/timetabler";
    //private static String MainURL = "http://time.tujenge-ea.com/";
    /**
     * Don't forget to call run(); to start this task
     * @param replyTo - the handler you want to receive the response when this task has finished
     * @param username - the username of who on YouTube you are browsing
     */
    public GetUniversityDetailsTask(Handler replyTo, String uniId) {
        this.replyTo = replyTo;
        this.SearchQuery = SearchQuery;
        this.Url = MainURL + "/fetchSchoolData.php?uni="+ uniId;
    }
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			// Get a httpclient to talk to the internet
            HttpClient client = new DefaultHttpClient();
            // Perform a GET request to server for a JSON list of all the lessons by a specific user
            HttpUriRequest request = new HttpGet(Url);
            Log.i("Loging on to fetch university data");									  
            // Get the response that YouTube sends back
            HttpResponse response = client.execute(request);
            // Convert this response into a readable string
            String jsonString = StreamUtils.convertToString(response.getEntity().getContent());
            // Create a JSON object that we can use from the String
            JSONObject json = new JSONObject(jsonString);
            Bundle data = new Bundle();
            
            JSONObject varObj = null;
            try { 
        	    json = new JSONObject(jsonString); 
        	    varObj = json.getJSONObject("data");
        	} 
        	catch (JSONException ignored) {}
            
            //if (varObj != null) {
            	// Get are search result items
            	JSONArray jsonArray = json.getJSONObject("data").getJSONArray("schools");            
	            
	            // Create a list to store are videos in
	            List<School> schools = new ArrayList<School>();
	            // Loop round our JSON list of lessons creating Lesson objects to use within our app
	            schools.add(new School("0", "Select Your School/Faculty"));            
            	for (int i = 1; i < jsonArray.length(); i++) {            
	                JSONObject jsonObject = jsonArray.getJSONObject(i);	                
	                String school_id = jsonObject.getString("school_id");
	                String school_names = jsonObject.getString("school_names");
	                
	                // Create the school object and add it to our list
	                schools.add(new School(school_id, school_names));
	            }
            	
	            // Create a library to hold our lessons
            	SchoolLibrary lib = new SchoolLibrary("br", schools);
	            // Pack the Library into the bundle to send back to the Activity	            
	            data.putSerializable(LIBRARY, lib);
            /*} else {
            	LessonLibrary lib = new LessonLibrary("br", null);
	            data.putSerializable(LIBRARY, lib);
            }*/
            // Send the Bundle of data (our LessonLibrary) back to the handler (our Activity)
            Message msg = Message.obtain();
            msg.setData(data);
            replyTo.sendMessage(msg);
            // We don't do any error catching, just nothing will happen if this task falls over
            // an idea would be to reply to the handler with a different message so your Activity can act accordingly
            
		} catch (ClientProtocolException e) {
            Log.e("Feck", e);
        } catch (IOException e) {
            Log.e("Feck", e);
        } catch (JSONException e) {
            Log.e("Feck", e);
        }
	}

}
