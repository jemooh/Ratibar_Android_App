 package com.br.timetabler.src;

import com.actionbarsherlock.app.SherlockActivity;
import com.br.timetabler.R;
import com.br.timetabler.model.AssignmentLibrary;
import com.br.timetabler.service.task.GetAssignmentsTask;
import com.br.timetabler.widget.AssignmentsListView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


	public class One_Lesson_Assignment extends SherlockActivity{
		AssignmentsListView listView;
		String unit_id,userId;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);	
			setContentView(R.layout.one_lesson_assgn);
			
			
			
			listView=  (AssignmentsListView) findViewById(R.id.assignmentsListView);
			getAssignmentsFeed(listView);
			
		}
		

	    
		// This is the XML onClick listener to retreive a users video feed
	    public void getAssignmentsFeed(View v){
	        // We start a new task that does its work on its own thread
	        // We pass in a handler that will be called when the task has finished
	        // We also pass in the name of the user we are searching YouTube for
	        new Thread(new GetAssignmentsTask(responseHandler, userId, unit_id, false, null)).start();
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
