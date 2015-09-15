package com.br.timetabler.service.task;

import android.os.Handler;
import android.os.Bundle;
import android.os.Message;

import java.io.IOException;
import java.sql.Date;
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

import com.br.timetabler.model.Comment;
import com.br.timetabler.model.CommentLibrary;
import com.br.timetabler.util.Log;
import com.br.timetabler.util.StreamUtils;
public class GetCommentsTask implements Runnable {
	// A reference to retrieve the data when this task finishes
    public static final String LIBRARY = "CommentsLibrary";
    // A handler that will be notified when the task is finished
    private final Handler replyTo;
    // The user we are querying on server for lessons
    
    private String SearchQuery;
    private String Url;
    private static String MainURL = "http://syncsoft.co.ke/timetable/lessons_data2.php";
    //private static String MainURL = "http://dev.ratibar.com/app/appCommentsList.php?email=iopondo@gmail.com&password=iopondo&unit_id=28
    //private static String loginURL = MainURL + "/last2.php";
   // http://dev.ratibar.com/app/appCommentsList.php?email=iopondo@gmail.com&password=iopondo&unit_id=28
    
    /**
     * Don't forget to call run(); to start this task
     * @param replyTo - the handler you want to receive the response when this task has finished
     * @param username - the username of who on YouTube you are browsing
     */
    public GetCommentsTask(Handler replyTo, String email, String userPassword, String unit_id, boolean Search, String SearchQuery) {
        this.replyTo = replyTo;
        this.SearchQuery = SearchQuery;
        if(Search) {
        	if(SearchQuery !="") {
        		this.Url = MainURL; //+ "/lessons_data.php?q="+ SearchQuery;
        		//this.Url = MainURL + "/mobile_commentsList.php?q="+ SearchQuery;
        	}
        } else {
        	this.Url = MainURL; // + "/mobile_commentsList.php?unit_id="+unit_id;
        	//this.Url = "https://www.ratibar.com/app/appLessonsList.php?email="+ email + "&password="+userPassword+"&unit_id="+unit_id;
        	//this.Url = "http://dev.ratibar.com/app/appCommentsList.php?email=iopondo@gmail.com&password=iopondo&unit_id="+unit_id;
        	
            }
        Log.i(this.Url);
        
    }
    /*
    public String GetDate(){
    	
		    Calendar c = Calendar.getInstance();
		    c.setTimeInMillis(1336425840*1000);
		    SimpleDateFormat currentDate = new SimpleDateFormat("MMM/dd/yyyy HH:mm ");
		    Log.i( "Hours: " +currentDate.format(c.getTime()));
		    
    return currentDate.format(c.getTime());
    
    }*/
    
    
    
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
            Log.i(jsonString);
            // Get are search result items
            JSONArray commentsArray = json.getJSONArray("data"); 
           // JSONArray LessonsArray = json.getJSONObject("data").getJSONArray("lessons"); 
          //  Log.i("commentsArray"+commentsArray);
            
            
            
          /*  
            for (int i = 0; i < LessonsArray.length(); i++) {
                JSONObject jobj =LessonsArray.getJSONObject(i);
                */
          
                //JSONObject comm = jobj.getJSONObject("comments");
                //JSONArray commentsArray= jobj.getJSONArray("comments");
                Log.i("commentsArray"+commentsArray);
            // Create a list to store are videos in
            List<Comment> comments = new ArrayList<Comment>();
            // Loop round our JSON list of lessons creating Lesson objects to use within our app
            for (int j = 0; j < commentsArray.length(); j++) {
            	JSONObject jsonObject = commentsArray.getJSONObject(j);
                String commentId = jsonObject.getString("id");
                String lessonId = jsonObject.getString("unit_id");
                String createdBy = jsonObject.getString("created_by");
                String strComments = jsonObject.getString("comments");
                String createdt = jsonObject.getString("created_on");
                String thumbUrl = jsonObject.getString("thumbUrl");
                
                //converting time to Simple format for Unix Time.
                Calendar c = Calendar.getInstance();
                int t = Integer.parseInt(createdt);
    		    c.setTimeInMillis(t*1000L);
    		    SimpleDateFormat currentDate = new SimpleDateFormat("MMM/dd/yyyy HH:mm ");
    		    Log.i( "Hours: " +currentDate.format(c.getTime()));
                 String createdOn = currentDate.format(c.getTime());
                // Create the video object and add it to our list
                comments.add(new Comment(lessonId, commentId, createdBy, createdOn, strComments,thumbUrl));
            }
            // Create a library to hold our lessons
            CommentLibrary lib = new CommentLibrary("br", comments);
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
            Log.e("Feck1", e);
        } catch (IOException e) {
            Log.e("Feck2", e);
        } catch (JSONException e) {
            Log.e("Feck3", e);
        }
	}

}
