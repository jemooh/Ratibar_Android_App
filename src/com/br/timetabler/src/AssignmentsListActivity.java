package com.br.timetabler.src;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.br.timetabler.R;
import com.br.timetabler.listener.AssignmentClickListener;
import com.br.timetabler.model.Assignment;
import com.br.timetabler.model.AssignmentLibrary;
import com.br.timetabler.service.task.GetAssignmentsTask;
import com.br.timetabler.util.DatabaseHandler;
import com.br.timetabler.util.ServerInteractions;
import com.br.timetabler.widget.AssignmentsListView;

@SuppressLint("HandlerLeak")
public class AssignmentsListActivity extends SherlockActivity implements AssignmentClickListener {
	private AssignmentsListView listView;
	Button btnLogout;
	String userId;
	Button btnFeedBack, btn_submit;

	private static final int FEEDBACK_DIALOG = 1;
	private static String KEY_SUCCESS = "success";
	AlertDialog alertDialog;
	String unitId;
	SaveFeedbackTask feedBackTsk;
	ServerInteractions userFunction;
	DatabaseHandler db;
	JSONObject json_user;
    JSONObject json;
    String errorMsg, successMsg;
    String res; 
    int today = new GregorianCalendar().get(Calendar.DAY_OF_WEEK);;
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_assignments);
        
        Intent in=getIntent();
        if(in.hasExtra("unitId")) {
        	Bundle b=in.getExtras();
			this.unitId = b.getString("unitId");
		} else {
        	this.unitId = null;
        }
        db = new DatabaseHandler(getApplicationContext());
    	HashMap<String,String> user = new HashMap<String,String>();
    	user = db.getUserDetails();
    	userId = user.get("uid");
    	
        btnFeedBack = (Button) findViewById(R.id.btnFeedBack);
		btnFeedBack.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				//display feedback dialog
				showDialog(FEEDBACK_DIALOG);			
			}
		});
		
		
		//create a listview to hold data
        listView = (AssignmentsListView) findViewById(R.id.assignmentsListView);
		
		// Here we are adding this activity as a listener for when any row in the List is 'clicked'
        // The activity will be sent back the video that has been pressed to do whatever it wants with
        // in this case we will retrieve the URL of the video and fire off an intent to view it
        listView.setOnAssignmentClickListener(this);
        getAssignmentsFeed(listView);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://dsipaly video
                //Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.menu_grid: //display grid
            	Intent i1 = new Intent(getApplicationContext(), MainActivity.class);
            	i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i1);
                finish();
                break;

            case R.id.menu_list: //display list of lessons
            	Intent i2 = new Intent(getApplicationContext(), ListDayLessons.class);
            	i2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i2);
                finish();
                break;

            case R.id.menu_assignments: //display assignments
            	Intent i3 = new Intent(getApplicationContext(), AssignmentsListActivity.class);
                startActivity(i3);
                //finish();
                break;
            
            case R.id.menu_settings: //display settings
            	Intent i4 = new Intent(getApplicationContext(), Preferences.class);
                startActivity(i4);
                //finish();
                break;
            
            
        }
        return super.onOptionsItemSelected(item);
    }
	
	// This is the XML onClick listener to retreive a users video feed
    public void getAssignmentsFeed(View v){
        // We start a new task that does its work on its own thread
        // We pass in a handler that will be called when the task has finished
        // We also pass in the name of the user we are searching YouTube for
        new Thread(new GetAssignmentsTask(responseHandler, userId, unitId, false, null)).start();
    }
    
    // This is the handler that receives the response when the YouTube task has finished

    @SuppressLint("HandlerLeak")
	Handler responseHandler = new Handler() {
        public void handleMessage(Message msg) {
        	populateListWithAssignments(msg);
        };
    };
    
    /**
     * This method retrieves the Library of videos from the task and passes them to our ListView
     * @param msg
     */
    private void populateListWithAssignments(Message msg) {
        // Retreive the videos are task found from the data bundle sent back
    	AssignmentLibrary lib = (AssignmentLibrary) msg.getData().get(GetAssignmentsTask.LIBRARY);
        // Because we have created a custom ListView we don't have to worry about setting the adapter in the activity
        // we can just call our custom method with the list of items we want to display
        ProgressBar pbpp = (ProgressBar) findViewById(R.id.pbppl);
        TextView txtMsg = (TextView) findViewById(R.id.progressMsg2);
		pbpp.setVisibility(View.GONE);
		txtMsg.setVisibility(View.GONE);
        listView.setAssignments(lib.getAssignments());
		if(lib.getAssignments().isEmpty()){
			txtMsg.setText("No lessons for today, take a break");			
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
    
    // This is the interface method that is called when a video in the listview is clicked!
    // The interface is a contract between this activity and the listview
    @Override
	public void onAssignmentClicked(Assignment assignment) {
    	String unit_id = assignment.getUnitId();
    	String dateCreated = assignment.getDateCreated();
    	String dateDue = assignment.getDateDue();
    	String unitCode = assignment.getUnitCode();
    	String description = assignment.getDescription();
    	        
        Intent si = new Intent(getApplicationContext(), SingleAssignmentActivity.class);
        Bundle b=new Bundle();
        
        b.putString("unit_id", unit_id);
        b.putString("dateCreated", dateCreated);
        b.putString("dateDue", dateDue);
        b.putString("unitCode", unitCode);
        b.putString("description", description);
        
        si.putExtras(b);
        startActivity(si);
		
	}
    
    @Override
	protected Dialog onCreateDialog(int id) {	
		LayoutInflater inflater;
		AlertDialog.Builder dialogbuilder;
		View dialogview;
		AlertDialog dialogDetails = null;		 
		switch (id) {
			case FEEDBACK_DIALOG:
		 		inflater = LayoutInflater.from(this);
		 		dialogview = inflater.inflate(R.layout.add_feedback, null);
	 
		 		dialogbuilder = new AlertDialog.Builder(this);
		 		dialogbuilder.setTitle("ADD YOUR COMMENT!");
		 		//dialogbuilder.setIcon(R.drawable.reviews);
		 		dialogbuilder.setView(dialogview);
		 		dialogDetails = dialogbuilder.create();	  
		 	break;
		 	
		 }	 
	  return dialogDetails;
	 }
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
	 
	  	switch (id) {
	  		case FEEDBACK_DIALOG:
	  			alertDialog = (AlertDialog) dialog;
	  			btn_submit = (Button) alertDialog.findViewById(R.id.btn_submit);
	  			Button cancelbutton = (Button) alertDialog.findViewById(R.id.btn_cancel);
	  			final EditText txtAddFeedback = (EditText) alertDialog.findViewById(R.id.txtAddFeedback);
			   
	  			btn_submit.setOnClickListener(new View.OnClickListener() {
	 
	  				@Override
	  				public void onClick(View v) {
	  					String comments = txtAddFeedback.getText().toString();
	  					alertDialog.dismiss();

	  					//start task
	  	                MyCommentParams params = new MyCommentParams(comments);
	  	                feedBackTsk = new SaveFeedbackTask();
	  	                feedBackTsk.execute(params);
	  				}
	  			});
	 
	  			cancelbutton.setOnClickListener(new View.OnClickListener() {
	 
	  				@Override
	  				public void onClick(View v) {
	  					alertDialog.dismiss();
	  				}
	  			});
	  			break;
	  	}
	}

	private class SaveFeedbackTask extends AsyncTask<MyCommentParams, Void, String> {
        @Override
        protected String doInBackground(MyCommentParams... params) {
        	userFunction = new ServerInteractions();

        	String feedbackContent = params[0].feedbackContent;
        	db = new DatabaseHandler(getApplicationContext());
        	HashMap<String,String> user = new HashMap<String,String>();
        	user = db.getUserDetails();
        	String userId = user.get("uid");
        	json = userFunction.postFeedback(feedbackContent, userId); //100 refers to example user id
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                	errorMsg = "";
                    res = json.getString(KEY_SUCCESS);
                    if(Integer.parseInt(res) == 1){
                    	successMsg = "Successfully sent a feedback, thank you";
                    }else{
                        // Error in login
                    	successMsg = "Something went wrong, please verify your sentence";
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
			return successMsg; 
        }
        
        @Override
        protected void onPostExecute(String json_user) {        	
        	try {
        		if(isCancelled())        	
				return;
        		
        		if(Integer.parseInt(res) == 1){
                	Toast.makeText(getApplicationContext(), "Successfully sent a feedback", Toast.LENGTH_SHORT).show();
                	//getCommentsFeed(listView, strVideoId);
                  	alertDialog.dismiss();
                }
        	} catch(Exception e){
				Log.e(this.getClass().getSimpleName(), "Error Saving feedback", e);
			}
        }        
    }
	
	private static class MyCommentParams {
        String userId, feedbackContent;
        MyCommentParams(String feedbackContent) {
            this.feedbackContent = feedbackContent;
            
        }
    }

    
}
