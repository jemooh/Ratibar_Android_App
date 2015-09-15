package com.br.timetabler.service.task;

import android.os.Handler;
import android.os.Bundle;
import android.os.Message;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.br.timetabler.model.Comment;
import com.br.timetabler.model.Lesson;
import com.br.timetabler.model.LessonLibrary;
import com.br.timetabler.util.Log;
import com.br.timetabler.util.StreamUtils;

public class GetAssignmentsTask implements Runnable {
	// A reference to retrieve the data when this task finishes
    public static final String LIBRARY = "AssignmentsLibrary";
    // A handler that will be notified when the task is finished
    private final Handler replyTo;
    // The user we are querying on server for lessons
    
    private String SearchQuery;
    private String Url;
    private static String MainURL = "http://syncsoft.co.ke/timetable/lessons_data2.php";
    //private static String MainURL ="http://dev.ratibar.com/app/appAssignmentsList.php";
    //private static String MainURL ="https://www.ratibar.com/app/appAssignmentsList.php";
    //private static String loginURL = MainURL + "/last2.php";
    /**
     * Don't forget to call run(); to start this task
     * @param replyTo - the handler you want to receive the response when this task has finished
     * @param username - the username of who on YouTube you are browsing
     */
    public GetAssignmentsTask(Handler replyTo, String email, String userPassword, boolean Search, String SearchQuery) {
        this.replyTo = replyTo;
        this.SearchQuery = SearchQuery;
        if(Search) {
        	if(SearchQuery !="") {
        		//this.Url = MainURL + "/appAssignmentsList.php?q="+ SearchQuery;
        		this.Url = MainURL; 
        	}
        } else {
        	this.Url = MainURL; // + "/appAssignmentsList.php?userId="+email+"&unitId=" + unit_id;
        	//this.Url = "https://www.ratibar.com/app/appLessonsList.php?email="+ email + "&password="+userPassword;
        }
        Log.i(this.Url);
        
    }
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			// Get a httpclient to talk to the internet
            HttpClient client = new DefaultHttpClient();
            // Perform a GET request to server for a JSON list of all the lessons by a specific user
            HttpUriRequest request = new HttpGet(Url);
            Log.i("Loging on to fetch data");									  
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
	            
		} catch (ClientProtocolException e) {
            Log.e("Feck", e);
        } catch (IOException e) {
            Log.e("Feck", e);
        } catch (JSONException e) {
            Log.e("Feck", e);
        }
	}

}
/** Calendar c = Calendar.getInstance();
	                int t = Integer.parseInt(assDateCreatedU);
	    		    c.setTimeInMillis(t*1000L);
	    		    SimpleDateFormat currentDate = new SimpleDateFormat("MM/dd/yy HH:mm ");
	    		    Log.i( "Hours: " +currentDate.format(c.getTime()));
	                 String assDateCreated = currentDate.format(c.getTime());
	                 
	                 Calendar cal = Calendar.getInstance();
		                int t2 = Integer.parseInt(assDateDueU);
		    		    cal.setTimeInMillis(t2*1000L);
		    		    SimpleDateFormat currentDat = new SimpleDateFormat("MM/dd/yy HH:mm ");
		    		    Log.i( "Hours: " +currentDat.format(cal.getTime()));
		                 String assDateDue = currentDate.format(cal.getTime());*/
