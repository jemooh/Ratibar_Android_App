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

import com.br.timetabler.model.Course;
import com.br.timetabler.model.CourseLibrary;
import com.br.timetabler.util.Log;
import com.br.timetabler.util.StreamUtils;

public class GetCoursesTask implements Runnable {
	// A reference to retrieve the data when this task finishes
    public static final String LIBRARY = "CourseLibrary";
    // A handler that will be notified when the task is finished
    private final Handler replyTo;
    // The user we are querying on server for lessons
    
    private String SearchQuery;
    private String Url;
    private static String MainURL = "http://10.0.2.2/timetabler";
    //private static String MainURL = "http://www.tujenge-ea.com/ti";
    /**
     * Don't forget to call run(); to start this task
     * @param replyTo - the handler you want to receive the response when this task has finished
     * @param username - the username of who on YouTube you are browsing
     */
    public GetCoursesTask(Handler replyTo, String schId) {
        this.replyTo = replyTo;
        this.SearchQuery = SearchQuery;
        this.Url = MainURL + "/coursesList.php?sch="+ schId;
    }
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			// Get a httpclient to talk to the internet
            HttpClient client = new DefaultHttpClient();
            // Perform a GET request to server for a JSON list of all the lessons by a specific user
            HttpUriRequest request = new HttpGet(Url);
            Log.i("Loging on to fetch courses data");									  
            Log.i(Url);									  
            // Get the response that YouTube sends back
            HttpResponse response = client.execute(request);
            // Convert this response into a readable string
            String jsonString = StreamUtils.convertToString(response.getEntity().getContent());
            // Create a JSON object that we can use from the String
            JSONObject json = new JSONObject(jsonString);
            Bundle data = new Bundle();
            Log.i(jsonString);
            JSONObject varObj = null;
            try { 
        	    json = new JSONObject(jsonString); 
        	    varObj = json.getJSONObject("data");
        	} 
        	catch (JSONException ignored) {}
            
        	// Get are search result items
        	JSONArray jsonArray = json.getJSONObject("data").getJSONArray("courses");            
            
            // Create a list to store are courses in
            List<Course> courses = new ArrayList<Course>();
            // Loop round our JSON list of courses creating course objects to use within our app
            courses.add(new Course("0", "", "Select Your School/Faculty"));            
        	for (int i = 0; i < jsonArray.length(); i++) {            
                JSONObject jsonObject = jsonArray.getJSONObject(i);	                
                String course_id = jsonObject.getString("course_id");
                String course_acronyms = jsonObject.getString("course_acronyms");
                String course_names = jsonObject.getString("course_names");
                
                // Create the school object and add it to our list
                courses.add(new Course(course_id, course_acronyms, course_names));
            }
        	
            // Create a library to hold our courses
        	CourseLibrary lib = new CourseLibrary("br", courses);
            // Pack the Library into the bundle to send back to the Activity	            
            data.putSerializable(LIBRARY, lib);
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
