 package com.br.timetabler.src;

import java.util.HashMap;

import com.br.timetabler.R;
import com.br.timetabler.model.Assignment;
import com.br.timetabler.model.AssignmentLibrary;
import com.br.timetabler.service.task.GetAssignmentsTask;
import com.br.timetabler.service.task.GetLessonsSaved;
import com.br.timetabler.service.task.GetsavedAssignmentTask;
import com.br.timetabler.util.DatabaseHandler;
import com.br.timetabler.widget.AssignmentsListView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


	public class One_Lesson_Assignment extends ActionBarActivity {
		AssignmentsListView listView;
		String unit_id,userId,email,userPassword;
		public static String jsonString;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);	
			setContentView(R.layout.one_lesson_assgn);
			
			//check if user is logged in.
			DatabaseHandler   dbHandler = new DatabaseHandler(getApplicationContext());
	    	HashMap<String,String> users = new HashMap<String,String>();
	    	users = dbHandler.getUserGridLessons();
	        jsonString = users.get("jsonString");
	        
	        dbHandler = new DatabaseHandler(getApplicationContext());
	    	HashMap<String,String> user = new HashMap<String,String>();
	    	user = dbHandler.getUserDetails();
	    	email = user.get("email");
	    	userPassword = user.get("password");
			
			listView=  (AssignmentsListView) findViewById(R.id.assignmentsListView);
			getAssignmentsFeed(listView);
			
		}
		

	    
		// This is the XML onClick listener to retreive a users video feed
	    public void getAssignmentsFeed(View v){
	        // We start a new task that does its work on its own thread
	        // We pass in a handler that will be called when the task has finished
	        // We also pass in the name of the user we are searching YouTube for
	    	//new Thread(new GetsavedAssignmentTask(responseHandler)).start();
	    	 new Thread(new GetAssignmentsTask(responseHandler, email, userPassword, false, null)).start();
	        //new Thread(new GetAssignmentsTask(responseHandler, userId, unit_id, false, null)).start();
	    }
	    
	    // This is the handler that receives the response when the YouTube task has finished

	    @SuppressLint("HandlerLeak")
		Handler responseHandler = new Handler() {
	        public void handleMessage(Message msg) {
	        	populateListWithAssignments(msg);
	        };
	    };
		
		
		
		
	    private void populateListWithAssignments(Message msg) {
	        // Retreive the videos are task found from the data bundle sent back
	    	AssignmentLibrary lib = (AssignmentLibrary) msg.getData().get(GetAssignmentsTask.LIBRARY);
	        // Because we have created a custom ListView we don't have to worry about setting the adapter in the activity
	        // we can just call our custom method with the list of items we want to display
	        ProgressBar pbpp = (ProgressBar) findViewById(R.id.pbpplasignmnt);
	        TextView txtMsg = (TextView) findViewById(R.id.progressMsg3);
			pbpp.setVisibility(View.GONE);
			txtMsg.setVisibility(View.GONE);
	        listView.setAssignments(lib.getAssignments());
			if(lib.getAssignments().isEmpty()){
				txtMsg.setText("No Asignments for today");			
			} else {
				txtMsg.setVisibility(View.GONE);
		        listView.setAssignments(lib.getAssignments());
			}
			
	    }
	    
	  
		
		@Override
	    protected void onStop() {
	        // Make sure we null our handler when the activity has stopped
	        // because who cares if we get a callback once the activity has stopped? not me!
	        responseHandler = null;
	        super.onStop();
	    }
	   
		
		
		


}
