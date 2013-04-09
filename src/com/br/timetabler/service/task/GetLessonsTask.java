package com.br.timetabler.service.task;

import android.os.Handler;
import android.os.Bundle;
import android.os.Message;

import java.io.IOException;
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

import com.br.timetabler.model.Lesson;
import com.br.timetabler.model.LessonLibrary;
import com.br.timetabler.util.Log;
import com.br.timetabler.util.StreamUtils;

public class GetLessonsTask implements Runnable {
	// A reference to retrieve the data when this task finishes
    public static final String LIBRARY = "LessonsLibrary";
    // A handler that will be notified when the task is finished
    private final Handler replyTo;
    // The user we are querying on server for lessons
    
    private String SearchQuery;
    private String Url;
    /**
     * Don't forget to call run(); to start this task
     * @param replyTo - the handler you want to receive the response when this task has finished
     * @param username - the username of who on YouTube you are browsing
     */
    public GetLessonsTask(Handler replyTo, boolean Search, String SearchQuery) {
        this.replyTo = replyTo;
        this.SearchQuery = SearchQuery;
        if(Search) {
        	if(SearchQuery !="") {
        		this.Url = "http://10.0.2.2/timetabler/lessonsList.php?q="+ SearchQuery;
        	}
        } else {
        	this.Url = "http://10.0.2.2/timetabler/lessonsList.php";
        }
        //Log.i(this.Url);
        
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
            
            // Get are search result items
            JSONArray jsonArray = json.getJSONObject("data").getJSONArray("lessons");            
            
            // Create a list to store are videos in
            List<Lesson> lessons = new ArrayList<Lesson>();
            // Loop round our JSON list of lessons creating Lesson objects to use within our app
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                
                String lessonId = jsonObject.getString("unit_id");
                String lessonCode = jsonObject.getString("unit_acronyms");
                String lessonTitle = jsonObject.getString("unit_names");
                String lessonTeacher = jsonObject.getString("teacher");
                String lessonStartTime = jsonObject.getString("unit_time_start");
                String lessonEndTime = jsonObject.getString("unit_time_end");
                String lessonLocation = jsonObject.getString("room_names");
                String lessonDayId = jsonObject.getString("day_id");
                 
                // Create the video object and add it to our list
                lessons.add(new Lesson(lessonId, lessonCode, lessonTitle, lessonTeacher, lessonStartTime, lessonEndTime, lessonLocation, lessonDayId));
            }
            // Create a library to hold our lessons
            LessonLibrary lib = new LessonLibrary("br", lessons);
            // Pack the Library into the bundle to send back to the Activity
            Bundle data = new Bundle();
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
