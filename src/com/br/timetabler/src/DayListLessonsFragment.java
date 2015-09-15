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
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.br.timetabler.R;
import com.br.timetabler.model.Lesson;
import com.br.timetabler.model.LessonLibrary;
import com.br.timetabler.service.task.GetDayLessonsTask;
import com.br.timetabler.service.task.GetLessonsSaved;
import com.br.timetabler.service.task.GetLessonsTask;
import com.br.timetabler.util.DatabaseHandler;
import com.br.timetabler.util.DatabaseHandler_TodayLessons;
import com.br.timetabler.util.DatabaseHandler_joe;
import com.br.timetabler.util.FeedbackDialogFragment;
import com.br.timetabler.adapter.DayLessonsCursorAdapter;
import com.br.timetabler.listener.LessonClickListener;
import com.br.timetabler.util.ServerInteractions;
import com.br.timetabler.widget.TodayLessonsListView;
			
			
			@SuppressLint("HandlerLeak")
			public class DayListLessonsFragment extends Fragment implements LessonClickListener {
				public static String lessonId, lessonCode,lessonColorband, lessonTitle, lessonTeacher, lessonStartTime, lessonEndTime, lessonLocation;
				// if run on phone, isSinglePane = true
				 // if run on tablet, isSinglePane = false
				 static boolean isSinglePane;
				public static String  jsonString;
				private TodayLessonsListView listView;
				Button btnLogout;
				public static String  dayId;
				String  dayTitle,gridData;
				Button btnFeedBack, btn_submit;
			
				static String parseNull(Object obj){
			        return obj == null?"null"  : "Object";
			    }
				
				public DayListLessonsFragment() {
                       // setRetainInstance(true);
		        }
				
				
				private static final int FEEDBACK_DIALOG = 1;
				private static String KEY_SUCCESS = "success";
				AlertDialog alertDialog;
				//SaveFeedbackTask feedBackTsk;
				ServerInteractions userFunction;
				
				JSONObject json_user;
			    JSONObject json;
			    String errorMsg, successMsg;
			    String res; 
			    String userId, email, userPassword;
			    ServerInteractions server;
			    DatabaseHandler dbHandler;
			    DatabaseHandler_TodayLessons dHandler;
			    boolean mDualPane;
			    int mCurCheckPosition = 0;
			    int today = new GregorianCalendar().get(Calendar.DAY_OF_WEEK);
			    String Day_id;
			    private List<Lesson> lessons;
			    public static List<Lesson> lesson ;
			    Bundle savedInstanceState;
			    // ListFragment is a very useful class that provides a simple ListView inside of a Fragment.
			    // This class is meant to be sub-classed and allows you to quickly build up list interfaces
			    // in your app.
			    @Override
			    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			    	 Log.d("FragmentCycle===>", "onCreateView: bundle="+parseNull(savedInstanceState));
			    	View view=inflater.inflate(R.layout.list_day_lessons, container, false);
			    	
			    	String[] daysOfWeek = {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", };
			        
			        dHandler = new DatabaseHandler_TodayLessons(getActivity());
			        savedInstanceState = savedInstanceState;
			       
			  		Calendar c = Calendar.getInstance();
	    		    SimpleDateFormat currentDate2 = new SimpleDateFormat("MMM/dd");
			  		String currentDate = currentDate2.format(c.getTime());
			  	    Day_id = String.valueOf(today-1);
			        Intent in=getActivity().getIntent();
			        String dyT = null;
			        if(in.hasExtra("dayId")) {
			        	Bundle b=in.getExtras();
						this.dayId = b.getString("dayId");
						this.dayTitle = b.getString("dayTitle");
						dyT = dayTitle;
						gridData =("dayId");
					} else {
						
			        	//this.dayId = today+"";
			        	this.dayId = String.valueOf(today-1);	
			        	this.dayTitle = daysOfWeek[today];
			        	dyT = dayTitle + ", " + currentDate;
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
					
					//getting saved current day lessons
					dbHandler = new DatabaseHandler(getActivity());
			    	HashMap<String,String> users = new HashMap<String,String>();
			    	users = dbHandler.getUserLessons();
			       jsonString = users.get("jsonString");
			       
			       //getting clicked grid day lessons
			       dbHandler = new DatabaseHandler(getActivity());
				  lesson = dbHandler.getAllLessons();     
			       
			   
			       //getting user details from db.
			       dbHandler = new DatabaseHandler(getActivity());
			    	HashMap<String,String> user = new HashMap<String,String>();
			    	user = dbHandler.getUserDetails();
			    	userId = user.get("uid");
			    	email = user.get("email");
			    	userPassword = user.get("password");
			       
			    	
					
					//create a listview to hold data
			        listView = (TodayLessonsListView) view.findViewById(R.id.todayListView);
					
					// Here we are adding this activity as a listener for when any row in the List is 'clicked'
			        // The activity will be sent back the video that has been pressed to do whatever it wants with
			        // in this case we will retrieve the URL of the video and fire off an intent to view it
			        listView.setOnLessonClickListener(this);
			        getLessonsFeed(listView);
			       
			        
			        return view;
			    }
			 
				
			/*
			    @Override
			    public void onSaveInstanceState(Bundle outState) {
			    	
			    	Log.d("FragmentCycle===>", "onSaveInstanceState: outState="+parseNull(outState));
			        super.onSaveInstanceState(outState);
			        outState.putInt("index", mCurCheckPosition);
			    }*/
			    
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
			        	listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
					DialogFragment newFragment = FeedbackDialogFragment.newInstance(1);
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
			    	/*if((Day_id.equals(dayId)&&(jsonString!=null))){
			    		
			    		
			    		Log.i("current SavedDb Lessons", dayId);
			    	       new Thread(new GetLessonsSaved(responseHandler2,false)).start();
			    	
			    		}else{
			    			Log.i("current Server Lessons", Day_id);*/
			    	       new Thread(new GetLessonsTask(responseHandler,  null, false, null,null)).start();
			            // new Thread(new GetLessonsTask(responseHandler,  dayId, false, email,userPassword)).start();
			    		}
			    /*	}
			    	//for fetching saved day lessons.
			    	else  {
			    		Log.i("Saved Day Lessons", dayId);
		    		new Thread(new GetDayLessonsTask(responseHandler3)).start();
			    	}*/
			    
			    
			    // This is the handler that receives the response when the YouTube task has finished
				
			  /*  @SuppressLint("HandlerLeak")
				Handler responseHandler3 = new Handler() {
			        public void handleMessage(Message msg) {
			        	populateDayLessons(msg);
			        };
			    };*/
			    
			    /**
			     * This method retrieves the Library of videos from the task and passes them to our ListView
			     * @param msg
			     
			    private void populateDayLessons(Message msg) {
			    	
			    	
			        // Retreive the videos are task found from the data bundle sent back
			        LessonLibrary lib = (LessonLibrary) msg.getData().get(GetDayLessonsTask.LIBRARY);
			        // Because we have created a custom ListView we don't have to worry about setting the adapter in the activity
			        // we can just call our custom method with the list of items we want to display
			        ProgressBar pbpp = (ProgressBar) getView().findViewById(R.id.pbppl);
			        TextView txtMsg = (TextView) getView().findViewById(R.id.progressMsg2);
                    Typeface font_b = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
			        txtMsg.setTypeface(font_b);
					pbpp.setVisibility(View.GONE);
					txtMsg.setVisibility(View.GONE);
			        listView.setLessons(lib.getLessons());
			       /* String jsonString = GetLessonsTask.json;
			        Log.i("jsonString", jsonString);
			        dHandler.addUserLessons(jsonString);*/
					/**if(lib.getLessons().isEmpty()){
						txtMsg.setText("No lessons for today, take a break");			
					} else {
						txtMsg.setVisibility(View.GONE);
						lessons = lib.getLessons();
				        listView.setLessons(lessons);
					}
					getRightPanel();
					
			    }*/
			    
			    
		 /****
			    // This is the handler that receives the response when the YouTube task has finished
			
			    @SuppressLint("HandlerLeak")
				Handler responseHandler2 = new Handler() {
			        public void handleMessage(Message msg) {
			        	populateDBListWithLessons(msg);
			        };
			    };
			    
			    /**
			     * This method retrieves the Library of videos from the task and passes them to our ListView
			     * @param msg
			    
			    private void populateDBListWithLessons(Message msg) {
			        // Retreive the videos are task found from the data bundle sent back
			        LessonLibrary lib = (LessonLibrary) msg.getData().get(GetLessonsSaved.LIBRARY);
			        // Because we have created a custom ListView we don't have to worry about setting the adapter in the activity
			        // we can just call our custom method with the list of items we want to display
			        ProgressBar pbpp = (ProgressBar) getView().findViewById(R.id.pbppl);
			        TextView txtMsg = (TextView) getView().findViewById(R.id.progressMsg2);
                    Typeface font_b = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
			        txtMsg.setTypeface(font_b);
			        listView.setLessons(lib.getLessons());
			       /* String jsonString = GetLessonsTask.json;
			        Log.i("jsonString", jsonString);
			        dHandler.addUserLessons(jsonString);*/
					/**if(lib.getLessons().isEmpty()){
						txtMsg.setText("No lessons for today, take a break");			
					} else {
						pbpp.setVisibility(View.GONE);
						txtMsg.setVisibility(View.GONE);
						lessons = lib.getLessons();
				        listView.setLessons(lessons);
					}
					getRightPanel();
					
			    } */
			    
			
			    
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
			        listView.setLessons(lib.getLessons());
			        String jsonString = GetLessonsTask.json;
			        Log.i("jsonString", jsonString);
			        dbHandler.addUserLessons(jsonString);
					if(lib.getLessons().isEmpty()){
						txtMsg.setText("No lessons for today, take a break");			
					} else {
						pbpp.setVisibility(View.GONE);
						txtMsg.setVisibility(View.GONE);
						lessons = lib.getLessons();
				        listView.setLessons(lessons);
					}
					getRightPanel();
			    
			    
			    }
			    
			    @Override
				public void onLessonClicked(Lesson lesson,int position) {
			    	showDetails(lesson, position);
			    	mCurCheckPosition = position;
			    	//setItemChecked(position, true);
			    	Log.i("clicked pos",""+mCurCheckPosition);
			    	
			    }
			    	
			     	
			   void showDetails(Lesson lesson,int position){
			    	//look for data in position index on array Lessons
			    	String unit_id = lesson.getLessonId();
					String starttime = lesson.getStarttime();
					String endtime = lesson.getEndtime();
					String color = lesson.getColorband();
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
						a.putString("color", color);	
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
			    		
						     Intent si = new Intent(getActivity(), SingleLessonActivity1.class);
						     Bundle b=new Bundle();
						     
						     b.putString("unit_id", unit_id);
						     b.putString("starttime", starttime);
						     b.putString("endtime", endtime);
						     b.putString("code", code);
						     b.putString("color", color);
						     b.putString("title", title);
						     b.putString("teacher", teacher);
						     b.putString("location", location);
						                
						     si.putExtras(b);
						     startActivity(si);
					    }		
			    	}
			
			   @Override
		        public void onCreate(Bundle savedInstanceState) {
		            Log.d("FragmentCycle===>", "onCreate: bundle="+parseNull(savedInstanceState));
		            super.onCreate(savedInstanceState);
		        }

		        @Override
		        public void onAttach(Activity activity) {
		            Log.d("FragmentCycle===>", "onAttach");
		            super.onAttach(activity);
		        }

		        @Override
		        public void onViewCreated(View view, Bundle savedInstanceState) {
		            Log.d("FragmentCycle===>", "onViewCreated: bundle="+parseNull(savedInstanceState));
		            super.onViewCreated(view, savedInstanceState);
		        }

		        @Override
		        public void onActivityCreated(Bundle savedInstanceState) {
		            Log.d("FragmentCycle===>", "onActivityCreated: bundle="+parseNull(savedInstanceState));
		            super.onActivityCreated(savedInstanceState);
		        }

		        @Override
		        public void onViewStateRestored(Bundle savedInstanceState) {
		            Log.d("FragmentCycle===>", "onViewStateRestored: bundle="+parseNull(savedInstanceState));
		            super.onViewStateRestored(savedInstanceState);
		        }

		        @Override
		        public void onStart() {
		            Log.d("FragmentCycle===>", "onStart");
		            super.onStart();
		        }

		        @Override
		        public void onResume() {
		            Log.d("FragmentCycle===>", "onResume");
		            super.onResume();
		        }

		        @Override
		        public void onPause() {
		            Log.d("FragmentCycle===>", "onPause");
		            super.onPause();
		        }

		        @Override
		        public void onStop() {
		            Log.d("FragmentCycle===>", "onStop");
		            super.onStop();
		        }

		        @Override
		        public void onDestroyView() {
		            Log.d("FragmentCycle===>", "onDestroyView");
		            super.onDestroyView();
		        }

		        @Override
		        public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
		            Log.d("FragmentCycle===>", "onInflate: bundle="+parseNull(savedInstanceState));
		            super.onInflate(activity, attrs, savedInstanceState);
		        }

		        @Override
		        public void onSaveInstanceState(Bundle outState) {
		            Log.d("FragmentCycle===>", "onSaveInstanceState: outState="+parseNull(outState));
		           
			        outState.putInt("index", mCurCheckPosition);
		            super.onSaveInstanceState(outState);
		        }

		        @Override
		        public void onConfigurationChanged(Configuration newConfig) {
		            Log.d("FragmentCycle===>", "onConfigurationChanged");
		            super.onConfigurationChanged(newConfig);
		        }

		        @Override
		        public void onDestroy() {
		            Log.d("FragmentCycle===>", "onDestroy");
		            super.onDestroy();
		        }

		        @Override
		        public void onDetach() {
		            Log.d("FragmentCycle===>", "onDetach");
		            super.onDetach();
		        }

		        @Override
		        public void setInitialSavedState(SavedState state) {
		            Log.d("FragmentCycle===>", "setInitialSavedState");
		            super.setInitialSavedState(state);
		        }

		        @Override
		        public void onHiddenChanged(boolean hidden) {
		            Log.d("FragmentCycle===>", "onHiddenChanged");
		            super.onHiddenChanged(hidden);
		        }

		        @Override
		        public void onLowMemory() {
		            Log.d("FragmentCycle===>", "onLowMemory");
		            super.onLowMemory();
		        }
}