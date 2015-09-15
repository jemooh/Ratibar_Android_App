package com.br.timetabler.src;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.br.timetabler.R;
import com.br.timetabler.model.Lesson;
import com.br.timetabler.model.LessonLibrary;
import com.br.timetabler.service.task.GetLessonsTask;
import com.br.timetabler.util.DatabaseHandler;
import com.br.timetabler.util.DatabaseHandler_joe;
import com.br.timetabler.listener.LessonClickListener;
import com.br.timetabler.util.ServerInteractions;
import com.br.timetabler.widget.TodayLessonsListView;

@SuppressLint("HandlerLeak")
public class ListDayLessons extends ActionBarActivity implements LessonClickListener {
	private TodayLessonsListView listView;
	Button btnLogout;
	String dayId, dayTitle;
	Button btnFeedBack, btn_submit;

	private static final int FEEDBACK_DIALOG = 1;
	private static String KEY_SUCCESS = "success";
	AlertDialog alertDialog;
	
	SaveFeedbackTask feedBackTsk;
	ServerInteractions userFunction;
	JSONObject json_user;
    JSONObject json;
    String errorMsg, successMsg;
    String res; 
    DatabaseHandler db;
    ServerInteractions server;
	String userId, email, userPassword;
    int today = new GregorianCalendar().get(Calendar.DAY_OF_WEEK);
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_day_lessons);
        String[] daysOfWeek = {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", };
       // dbHandler = new DatabaseHandler_joe(this);
        getOverflowMenu();
        server = new ServerInteractions();
		db = new DatabaseHandler(this);
		
		//I used this to help in fixing authentication by loging out the user upon loading the app
		server.logoutUser(getApplicationContext());
		
		try {   
			
			db.createDataBase();         
        } catch (IOException ioe) {         
        	throw new Error("Unable to create database");         
        }         
        
        try {  
        	
        	db.openDataBase();         
        }catch(SQLException sqle){         
        	throw sqle;         
        }
        db.close();

        
        /*try {         
        	dbHandler.createDataBase();         
        } catch (IOException ioe) {         
        	throw new Error("Unable to create database");         
        }*/
         
        //check if the user is locked in.
        
       // if(server.isUserLoggedIn(getApplicationContext())){
        	//setContentView(R.layout.list_day_lessons);
       
        //create a today date for display
  		String currentDate = DateFormat.getDateInstance().format(new Date());
  		
        Intent in=getIntent();
        String dyT = null;
        if(in.hasExtra("dayId")) {
        	Bundle b=in.getExtras();
			this.dayId = b.getString("dayId");
			this.dayTitle = b.getString("dayTitle");
			dyT = dayTitle;
		} else {
        	this.dayId = today+"";
        	this.dayTitle = daysOfWeek[today];
        	dyT = "Today is " + dayTitle + ", " + currentDate;
        }
   
		
        
        TextView txtDateToday = (TextView) findViewById(R.id.txtDateToday);
  		txtDateToday.setText(dyT);
  		
        btnFeedBack = (Button) findViewById(R.id.btnFeedBack);
		btnFeedBack.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				//display feedback dialog
				showDialog(FEEDBACK_DIALOG);			
			}
		});
		
		
 //check if user is logged in.
        
     
		db = new DatabaseHandler(getApplicationContext());
    	HashMap<String,String> user = new HashMap<String,String>();
    	user = db.getUserDetails();
    	userId = user.get("uid");
    	email = user.get("email");
    	userPassword = user.get("password");
		
		
		//create a listview to hold data
        listView = (TodayLessonsListView) findViewById(R.id.todayListView);
		
		// Here we are adding this activity as a listener for when any row in the List is 'clicked'
        // The activity will be sent back the video that has been pressed to do whatever it wants with
        // in this case we will retrieve the URL of the video and fire off an intent to view it
        listView.setOnLessonClickListener(this);
        getLessonsFeed(listView);
        
    	
    	/**
	   }else {
			//user in not logged in show login screen
			Intent login = new Intent (getApplicationContext(),Loginme.class);
			login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(login);
			
		}
        */
        
        }
        
        
    
	public void getDayOfWeek() {
		Calendar calendar = new GregorianCalendar();
		Date trialTime = new Date();
		calendar.setTime(trialTime);
		int dy = calendar.get(Calendar.DAY_OF_WEEK);


	}
	
	
	
	private void getOverflowMenu() {
		 
	    try {
	 
	       ViewConfiguration config = ViewConfiguration.get(this);
	       java.lang.reflect.Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	       if(menuKeyField != null) {
	           menuKeyField.setAccessible(true);
	           menuKeyField.setBoolean(config, false);
	       }
	   } catch (Exception e) {
	       e.printStackTrace();
	   }
	}
	 
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        
            case android.R.id.home://dsipaly video
                //Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(i);
                finish();
                break;
        
            case R.id.menu_grid: //display description
            	Intent i1 = new Intent(getApplicationContext(), WeekviewActivity.class);
            	//i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i1);
                //finish();
                break;

            case R.id.menu_list: //display description
            	Intent i2 = new Intent(getApplicationContext(), DashboardActivity.class);
            	//i2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i2);
                //finish();
                break;
            /*
            case R.id.menu_settings: //display reviews
            	Intent i4 = new Intent(getApplicationContext(), Preferences.class);
                startActivity(i4);
                //finish();
                break;
            */
            
        }
        return super.onOptionsItemSelected(item);
    }
	// This is the XML onClick listener to retreive a users video feed
    public void getLessonsFeed(View v){
        // We start a new task that does its work on its own thread
        // We pass in a handler that will be called when the task has finished
        // We also pass in the name of the user we are searching YouTube for
        new Thread(new GetLessonsTask(responseHandler, dayId, false, email, userPassword)).start();
    }
    
    // This is the handler that receives the response when the YouTube task has finished

    @SuppressLint("HandlerLeak")
	Handler responseHandler = new Handler() {
        public void handleMessage(Message msg) {
        	populateListWithLessons(msg);
        };
    };
    
    /**
     * This method retrieves the Library of videos from the task and passes them to our ListView
     * @param msg
     */
    private void populateListWithLessons(Message msg) {
        // Retreive the videos are task found from the data bundle sent back
        LessonLibrary lib = (LessonLibrary) msg.getData().get(GetLessonsTask.LIBRARY);
        // Because we have created a custom ListView we don't have to worry about setting the adapter in the activity
        // we can just call our custom method with the list of items we want to display
        ProgressBar pbpp = (ProgressBar) findViewById(R.id.pbppl);
        TextView txtMsg = (TextView) findViewById(R.id.progressMsg2);
		pbpp.setVisibility(View.GONE);
		txtMsg.setVisibility(View.GONE);
        listView.setLessons(lib.getLessons());
		if(lib.getLessons().isEmpty()){
			txtMsg.setText("No lessons for today, take a break");			
		} else {
			txtMsg.setVisibility(View.GONE);
	        listView.setLessons(lib.getLessons());
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
	public void onLessonClicked(Lesson lesson, int position) {
    	String unit_id = lesson.getLessonId();
    	String starttime = lesson.getStarttime();
    	String endtime = lesson.getEndtime();
    	String code = lesson.getCode();
    	String title = lesson.getTitle();
    	String teacher = lesson.getTeacher();
    	String location = lesson.getLocation();
                
        Intent si = new Intent(getApplicationContext(), SingleLessonActivity1.class);
        Bundle b=new Bundle();
              
        b.putString("unit_id", unit_id);
        b.putString("starttime", starttime);
        b.putString("endtime", endtime);
        b.putString("code", code);
        b.putString("title", title);
        b.putString("teacher", teacher);
        b.putString("location", location);
        
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
		 		dialogbuilder.setTitle("SHARE YOUR FEEDBACK!");
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
        	String email = user.get("email");
        	json = userFunction.postFeedback(feedbackContent, email); //100 refers to example user id
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
        String email, feedbackContent;
        MyCommentParams(String feedbackContent) {
            this.feedbackContent = feedbackContent;
            
        }
    }
    
}
