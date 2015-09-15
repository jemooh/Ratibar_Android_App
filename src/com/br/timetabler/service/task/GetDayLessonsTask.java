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

import com.br.timetabler.model.Lesson;
import com.br.timetabler.model.LessonLibrary;
import com.br.timetabler.src.DayListLessonsFragment;
import com.br.timetabler.src.GridFragment;
import com.br.timetabler.util.DatabaseHandler;
import com.br.timetabler.util.DatabaseHandler_TodayLessons;
import com.br.timetabler.util.Log;
import com.br.timetabler.util.StreamUtils;
			
			public class GetDayLessonsTask extends  Activity implements Runnable {
				// A reference to retrieve the data when this task finishes
				public static String day_id;
				public static String json;
			    public static final String LIBRARY = "LessonsLibrary";
			    // A handler that will be notified when the task is finished
			    private final Handler replyTo;
			    // The user we are querying on server for lessons
			    DatabaseHandler dbHandler;
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
			    public GetDayLessonsTask(Handler replyTo) {
			    	
			        this.replyTo = replyTo;
			        //dy_id = day_id;
			         
			    }
			    
			    
			    
				@Override
				public void run() {
				       
					   Bundle data = new Bundle();
			           
					  
					   List<Lesson> lessons = new ArrayList<Lesson>();
					  lessons = DayListLessonsFragment.lesson;
				                     
				               
					              
			            
				            // Create a library to hold our lessons
				            LessonLibrary lib = new LessonLibrary("br", lessons);
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
			
			}
