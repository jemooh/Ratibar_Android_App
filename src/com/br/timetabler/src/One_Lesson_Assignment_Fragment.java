package com.br.timetabler.src;


import java.util.HashMap;

import com.br.timetabler.R;
import com.br.timetabler.listener.AssignmentClickListener;
import com.br.timetabler.model.Assignment;
import com.br.timetabler.model.AssignmentLibrary;
import com.br.timetabler.service.task.GetAssignmentsTask;
import com.br.timetabler.service.task.GetsavedAssignmentTask;
import com.br.timetabler.util.DatabaseHandler;
import com.br.timetabler.widget.AssignmentsListView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


	public class One_Lesson_Assignment_Fragment  extends Fragment implements AssignmentClickListener  {
		AssignmentsListView listView;
		String unit_id = SingleLessonActivity1.unit_id;
		String email;
		public static String jsonString;	
		 
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    	View v=inflater.inflate(R.layout.one_lesson_assgn, container, false);
			
	    	DatabaseHandler   dbHandler = new DatabaseHandler(getActivity());
	    	HashMap<String,String> users = new HashMap<String,String>();
	    	users = dbHandler.getUserGridLessons();
	        jsonString = users.get("jsonString");
	        
	        dbHandler= new DatabaseHandler(getActivity());
	    	HashMap<String,String> user = new HashMap<String,String>();
	    	user = dbHandler.getUserDetails();
	    	email = user.get("email");
			
			listView=  (AssignmentsListView) v.findViewById(R.id.assignmentsListView);
			listView.setOnAssignmentClickListener(this);
			getAssignmentsFeed(listView);
			
			return v;
		}
		

	    
		// This is the XML onClick listener to retreive a users video feed
	    public void getAssignmentsFeed(View v){
	        // We start a new task that does its work on its own thread
	        // We pass in a handler that will be called when the task has finished
	        // We also pass in the name of the user we are searching YouTube for
	    	//new Thread(new GetsavedAssignmentTask(responseHandler)).start();
	       new Thread(new GetAssignmentsTask(responseHandler, email, unit_id, false, null)).start();
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
	        ProgressBar pbpp = (ProgressBar) getView().findViewById(R.id.pbpplasignmnt);
	        TextView txtMsg = (TextView) getView().findViewById(R.id.progressMsg3);
			pbpp.setVisibility(View.GONE);
			//txtMsg.setVisibility(View.GONE);
	        //listView.setAssignments(lib.getAssignments());
			if(lib.getAssignments().isEmpty()){
				txtMsg.setText("No Asignments for today");			
			} else {
				txtMsg.setVisibility(View.GONE);
		        listView.setAssignments(lib.getAssignments());
			}
			
	    }

	    
	    //menu items on fragments
		 /*public class DetailFragment extends Fragment {
		
		    @Override
		    public void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        setHasOptionsMenu(true);
		    }
		
		    @Override
		    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		       super.onCreateOptionsMenu(menu, inflater);
		       inflater.inflate(R.menu.detail_view_menu, menu);
		    }
		    }*/

	    @Override
		public void onAssignmentClicked(Assignment assignment) {
	    	String unit_id = assignment.getUnitId();
	    	String dateCreated = assignment.getDateCreated();
	    	String dateDue = assignment.getDateDue();
	    	String unitCode = assignment.getUnitCode();
	    	String description = assignment.getDescription();
	    	        
	        Intent si = new Intent(getActivity(), SingleAssignmentActivity.class);
	        Bundle b=new Bundle();
	        
	        b.putString("unit_id", unit_id);
	        b.putString("dateCreated", dateCreated);
	        b.putString("dateDue", dateDue);
	        b.putString("unitCode", unitCode);
	        b.putString("description", description);
	        
	        si.putExtras(b);
	        startActivity(si);
			
		}
	  


}
