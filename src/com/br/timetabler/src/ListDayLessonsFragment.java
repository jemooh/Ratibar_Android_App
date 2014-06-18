package com.br.timetabler.src;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.br.timetabler.R;
import com.br.timetabler.model.Lesson;
import com.br.timetabler.model.LessonLibrary;
import com.br.timetabler.service.task.GetLessonsTask;
import com.br.timetabler.util.DatabaseHandler_joe;
import com.br.timetabler.util.MyDialogFragment;
import com.br.timetabler.listener.LessonClickListener;
import com.br.timetabler.util.ServerInteractions;
import com.br.timetabler.widget.TodayLessonsListView;


@SuppressLint("HandlerLeak")
public class ListDayLessonsFragment extends SherlockFragment implements LessonClickListener {
	
	// if run on phone, isSinglePane = true
	 // if run on tablet, isSinglePane = false
	 static boolean isSinglePane;
	
	private TodayLessonsListView listView;
	Button btnLogout;
	String dayId, dayTitle;
	Button btnFeedBack, btn_submit;

	private static final int FEEDBACK_DIALOG = 1;
	private static String KEY_SUCCESS = "success";
	AlertDialog alertDialog;
	//SaveFeedbackTask feedBackTsk;
	ServerInteractions userFunction;
	DatabaseHandler_joe db;
	JSONObject json_user;
    JSONObject json;
    String errorMsg, successMsg;
    String res; 
    ServerInteractions server;
    DatabaseHandler_joe dbHandler;
    boolean mDualPane;
    int mCurCheckPosition = 0;
    int today = new GregorianCalendar().get(Calendar.DAY_OF_WEEK);
    private List<Lesson> lessons;
    Bundle savedInstanceState;
    // ListFragment is a very useful class that provides a simple ListView inside of a Fragment.
    // This class is meant to be sub-classed and allows you to quickly build up list interfaces
    // in your app.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view=inflater.inflate(R.layout.list_day_lessons, container, false);
    	
    	String[] daysOfWeek = {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesady", "Thursday", "Friday", };
        dbHandler = new DatabaseHandler_joe(getActivity());
        savedInstanceState = savedInstanceState;
        /*try {         
        	dbHandler.createDataBase();         
        } catch (IOException ioe) {         
        	throw new Error("Unable to create database");         
        }*/
         
        
        //create a today date for display
  		String currentDate = DateFormat.getDateInstance().format(new Date());
  		
        Intent in=getActivity().getIntent();
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
        
        TextView txtDateToday = (TextView) view.findViewById(R.id.txtDateToday);
  		txtDateToday.setText(dyT);
  		
        btnFeedBack = (Button) view.findViewById(R.id.btnFeedBack);
		btnFeedBack.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				//display feedback dialog
				//getActivity().showDialog(FEEDBACK_DIALOG);	
			    showDialog();
			}
			});
		
		//check if user is logged in.
		
		
		
		//create a listview to hold data
        listView = (TodayLessonsListView) view.findViewById(R.id.todayListView);
		
		// Here we are adding this activity as a listener for when any row in the List is 'clicked'
        // The activity will be sent back the video that has been pressed to do whatever it wants with
        // in this case we will retrieve the URL of the video and fire off an intent to view it
        listView.setOnLessonClickListener(this);
        getLessonsFeed(listView);
        
		
		
		

        
        return view;
    }
 
	

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("index", mCurCheckPosition);
    }
    
    public void getRightPanel() {
    	View detailsFrame = getActivity().findViewById(R.id.details);
		mDualPane = detailsFrame != null 
				&& detailsFrame.getVisibility() == View.VISIBLE;
		if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("index", 0);
        }
	 	Log.i("after savedInstanceState: ", "one");
        if (mDualPane) { 
        	// In dual-pane mode, the list view highlights the selected item.
        	//getListView().setChoiceMode(listView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
        	Lesson lesson = lessons.get(0); //0
        	String title = lesson.getTitle();
        	showDetails(lesson, mCurCheckPosition);
        }else{
            System.out.println("NOT DUAL");
        }	
    }
    
	/**
	* Creates a new instance of our dialog and displays it.
	*/
	private void showDialog() {
		DialogFragment newFragment = MyDialogFragment.newInstance(1);
		newFragment.show(getFragmentManager(), "dialog");
	}
    
    
    
	public void getDayOfWeek() {
		Calendar calendar = new GregorianCalendar();
		Date trialTime = new Date();
		calendar.setTime(trialTime);
		int dy = calendar.get(Calendar.DAY_OF_WEEK);


	}
	
	
	
	// This is the XML onClick listener to retreive a users video feed
    public void getLessonsFeed(View v){
        // We start a new task that does its work on its own thread
        // We pass in a handler that will be called when the task has finished
        // We also pass in the name of the user we are searching YouTube for
        new Thread(new GetLessonsTask(responseHandler, dayId, false, null, null)).start();
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
        ProgressBar pbpp = (ProgressBar) getView().findViewById(R.id.pbppl);
        TextView txtMsg = (TextView) getView().findViewById(R.id.progressMsg2);
		pbpp.setVisibility(View.GONE);
		txtMsg.setVisibility(View.GONE);
        listView.setLessons(lib.getLessons());
		if(lib.getLessons().isEmpty()){
			txtMsg.setText("No lessons for today, take a break");			
		} else {
			txtMsg.setVisibility(View.GONE);
			lessons = lib.getLessons();
	        listView.setLessons(lessons);
		}
		getRightPanel();
		
    }
    /*
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
      //  showDetails(position);
    }*/
    /* if it has we can then tell the listener 'hey a video has just been clicked' also passing the video
    @Override
	public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
    	if(lessonClickListener != null){
    		lessonClickListener.onLessonClicked(lessons.get(position)); */
        
    // This is the interface method that is called when a video in the listview is clicked!
    // The interface is a contract between this activity and the listview
    @Override
	public void onLessonClicked(Lesson lesson,int position) {
    	showDetails(lesson, position);
    	mCurCheckPosition = position;
    	Log.i("clicked pos",""+mCurCheckPosition);
    	
    }
    	
     	
    	//SingleLessonFragment det = (SingleLessonFragment)getFragmentManager().findFragmentById(R.id.details);
    	
    /*8
    	 // if run on phone, isSinglePane = true
    	 // if run on tablet, isSinglePane = false
    	if( isSinglePane==false){
		    //get reference to MyDetailFragment
    		SingleLessonFragment myDetailFragment = ( SingleLessonFragment)getFragmentManager().findFragmentById(R.id.one);
			myDetailFragment.updateDetail(code,title,teacher,location);
	*/
    void showDetails(Lesson lesson,int position){
    	//look for data in position index on array Lessons
    	String unit_id = lesson.getLessonId();
		String starttime = lesson.getStarttime();
		String endtime = lesson.getEndtime();
		String code = lesson.getCode();
		String title = lesson.getTitle();
		String teacher = lesson.getTeacher();
		String location = lesson.getLocation();
		
    	//mCurCheckPosition = 0;
    	if (mDualPane) {
	        
		
			Bundle a=new Bundle();
			 
			a.putString("unit_id", unit_id);
			a.putString("starttime", starttime);
			a.putString("endtime", endtime);
			a.putString("code", code);
			a.putString("title", title);
			a.putString("teacher", teacher);
			a.putString("location", location);
			a.putInt("index", position);
			Log.i("index***",""+position);
     
			   
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
           // getListView().setItemChecked(index, true);
 
            // Check what fragment is currently shown, replace if needed.
            SingleLessonFragment details = (SingleLessonFragment) getFragmentManager().findFragmentById(R.id.details);
           /** if (details != null) {
            	Log.i("getShownIndex",""+details.getShownIndex());   */
            
            if (details == null  || details.getShownIndex() != position) {
                // Make new fragment to show this selection.
                details = SingleLessonFragment.newInstance(a);
 
                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager()
                        .beginTransaction();
                ft.replace(R.id.details, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
		
		    }
    	}else {
    		
			     Intent si = new Intent(getActivity(), SingleLessonActivity.class);
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
    	}



}