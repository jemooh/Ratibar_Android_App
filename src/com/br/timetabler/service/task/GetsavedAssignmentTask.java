package com.br.timetabler.service.task;

import android.app.Activity;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.br.timetabler.model.Assignment;
import com.br.timetabler.model.AssignmentLibrary;
import com.br.timetabler.model.Lesson;
import com.br.timetabler.model.LessonLibrary;
import com.br.timetabler.src.DayListLessonsFragment;
import com.br.timetabler.src.GridFragment;
import com.br.timetabler.src.One_Lesson_Assignment;
import com.br.timetabler.src.One_Lesson_Assignment_Fragment;
import com.br.timetabler.util.DatabaseHandler;
import com.br.timetabler.util.DatabaseHandler_TodayLessons;
import com.br.timetabler.util.Log;
import com.br.timetabler.util.StreamUtils;

public class GetsavedAssignmentTask extends  Activity implements Runnable {
	// A reference to retrieve the data when this task finishes
	public static String json;
    public static final String LIBRARY = "LessonsLibrary";
    // A handler that will be notified when the task is finished
    private final Handler replyTo;
    // The user we are querying on server for lessons
    DatabaseHandler_TodayLessons dbHandler;
    private String email;
    private String Url;
    // private String jsonStringL = DayListLessonsFragment.jsonString;
    //private String jsonStringG = GridFragment.jsonString;
    private String jsonString;
    //private static String MainURL = "http://10.0.2.2/lessons_data2.php";
  // private static String MainURL = "http://dev.ratibar.com/app/appLessonsList.php?email=iopondo@gmail.com&password=iopondo/";
    /**
     * Don't forget to call run(); to start this task
     * @param replyTo - the handler you want to receive the response when this task has finished
     * @param username - the username of who on YouTube you are browsing
     */
    public GetsavedAssignmentTask(Handler replyTo) {
    	
        this.replyTo = replyTo;
        this.jsonString = One_Lesson_Assignment_Fragment.jsonString;
         	//this.Url = MainURL;  // + "/appLessonsList.php?reg_no="+ email + "&password="+userPassword;
        		//this.Url = "http://dev.ratibar.com/app/appLessonsList.php?email="+ email + "&password="+userPassword;
        
    }
    
    
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			//check if user disabled updates
			//if No
			//if online
			//refresh data/then save to dbase with timestamp
			// Get a httpclient to talk to the internet
          /**  HttpClient client = new DefaultHttpClient();
            // Perform a GET request to server for a JSON list of all the lessons by a specific user
            HttpUriRequest request = new HttpGet(Url);
            Log.i("Loging on to fetch data");									  
            // Get the response that YouTube sends back
            HttpResponse response = client.execute(request);
            // Convert this response into a readable string
            String jsonString = StreamUtils.convertToString(response.getEntity().getContent());
            json=jsonString;
            
            */
            
            //if Yes
            //check if data exists in dbase
			//fetch from dbase
           /* 
			dbHandler = new DatabaseHandler_jemo(getApplicationContext());
	    	HashMap<String,String> user = new HashMap<String,String>();
	    	user = dbHandler.getUserLessons();
	       jsonString = user.get("jsonString");*/
            
            // Create a JSON object that we can use from the String
            Log.i("jsonString"+jsonString);
            
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
            	JSONArray LessonsArray = json.getJSONObject("data").getJSONArray("lessons");            
	            
	            // Create a list to store are videos in
	            List<Assignment> assignments = new ArrayList<Assignment>();

	            
	            for (int i = 0; i < LessonsArray.length(); i++) {
	                JSONObject jobj =LessonsArray.getJSONObject(i);
	                
	          
	                //JSONObject comm = jobj.getJSONObject("comments");
	                JSONArray assignmentsArray= jobj.getJSONArray("assignments");
	                Log.i("assignmentsArray"+assignmentsArray);
	            // Create a list to store are videos in

	            
	            // Loop round our JSON list of lessons creating Lesson objects to use within our app
	                        
            	for (int j = 0; j < assignmentsArray.length(); j++) {            
	                JSONObject jsonObject = assignmentsArray.getJSONObject(j);	                
	                String unitId = jsonObject.getString("unit_id");
	                String unitCode = jsonObject.getString("unit_names");
	                String assDescription = jsonObject.getString("description");
	                String assDateCreated = jsonObject.getString("date_created");
	                String assDateDue = jsonObject.getString("date_due");
	                String assStatus = jsonObject.getString("status");
	                boolean st = assStatus =="1" ? true  : false;
	               
	               
	                   
	                // Create the video object and add it to our list
	                assignments.add(new Assignment(unitId, unitCode, assDateCreated, assDateDue, assDescription, st));
	            }
            
	            // Create a library to hold our lessons
            	AssignmentLibrary lib = new AssignmentLibrary("br", assignments);
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
	  } 
	            
		 /*catch (ClientProtocolException e) {
            Log.e("Feck", e);
        } catch (IOException e) {
            Log.e("Feck", e);*/
        } catch (JSONException e) {
            Log.e("Feck", e);
        }
	}

}