package com.br.timetabler.src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.br.timetabler.R;
import com.br.timetabler.model.Course;
import com.br.timetabler.model.CourseLibrary;
import com.br.timetabler.model.School;
import com.br.timetabler.model.Unit;
import com.br.timetabler.model.UnitLibrary;
import com.br.timetabler.service.task.GetCoursesTask;
import com.br.timetabler.util.DatabaseHandler_joe;
import com.br.timetabler.util.ServerInteractions;


public class CourseSetupActivity extends SherlockActivity {
	Spinner spnCourses, spnYear, spnIntake, spnSemester;
	protected ArrayAdapter<String> courseAdapter;
	protected ArrayAdapter<String> yearAdapter;
	protected ArrayAdapter<String> intakeAdapter;
	protected ArrayAdapter<String> semesterAdapter;
	String[] years = {"1st Year", "2nd Year", "3rd Year", "4th Year"};
	String[] semesters = {"1st Semester", "2nd Semester", "3rd Semester"};
	String[] intakes = {"January", "May", "September"};
	Button btnSaveCourse;
	ServerInteractions userFunction;
    DatabaseHandler_joe db;
    SendServerTask sendServerTask;
    JSONObject json_user;
    JSONObject json;
    String errorMsg;
    String res;
    TextView registerErrorMsg;
    String userId, DbSchoolId;
    List<School> schools;
    List<Course> courses;
    private static String KEY_SUCCESS = "success";
    
    /*** Fields to contain the current position and display contents of the spinner */
    protected int mPos;
    protected String mSelectedCourse="18";
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.course_setup_activity);
        db = new DatabaseHandler_joe(this);
        try {         
        	db.openDataBase();         
        }catch(SQLException sqle){         
        	throw sqle;         
        }
        
        //fetch user id for further transactions
        HashMap<String,String> user = new HashMap<String,String>();
    	user = db.getUserDetails();
    	userId = user.get("uid");
    	DbSchoolId = user.get("school_id");
    	
    	//create the spinners
    	getCoursesFeed(Integer.parseInt(DbSchoolId));
        addYearsOnSpinner();
        addIntakesOnSpinner();
        addSemestersOnSpinner();
        registerErrorMsg = (TextView) findViewById(R.id.register_error2);
        btnSaveCourse = (Button) findViewById(R.id.btnSaveCourse);
        btnSaveCourse.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) { 
				String SpinCourse = mSelectedCourse;
				String SpinYear = spnYear.getSelectedItemPosition()+"";
				String SpinIntake = spnIntake.getSelectedItemPosition()+"";
				String SpinSemester = spnSemester.getSelectedItemPosition()+"";
				
				MyUserParams userparams = new MyUserParams(SpinCourse, SpinYear, SpinSemester, SpinIntake);
				sendServerTask = new SendServerTask();
				sendServerTask.execute(userparams);
			}
		});
	}
	
	/**
	 * setup a simple spinner(dropdown) for years
	 */
	public void addYearsOnSpinner() {		   
		spnYear = (Spinner) findViewById(R.id.spnYear);
		List<String> yList = new ArrayList<String>();
		yList.add("Select your Year");
		for(int i=0; i<years.length; i++) {
	  		yList.add(years[i]);
		}
	  	
	  	yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yList);
	  	yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  	spnYear.setAdapter(yearAdapter);
	}
	/**
	 * setup a simple spinner(dropdown) for years
	 */
	public void addIntakesOnSpinner() {		   
		spnIntake = (Spinner) findViewById(R.id.spnIntake);
	  	List<String> intakeList = new ArrayList<String>();
	  	intakeList.add("Select Intake");
	  	for(int i=0; i<intakes.length; i++) {
	  		intakeList.add(intakes[i]);
		}
	  	
	  	intakeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, intakeList);
	  	intakeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  	spnIntake.setAdapter(intakeAdapter);
	}
	/**
	 * setup a simple spinner(dropdown) for years
	 */
	public void addSemestersOnSpinner() {		   
		spnSemester = (Spinner) findViewById(R.id.spnSemester);
	  	List<String> semList = new ArrayList<String>();
	  	semList.add("Select Semester");
	  	for(int i=0; i<semesters.length; i++) {
	  		semList.add(semesters[i]);
		}
	  	
	  	semesterAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, semList);
	  	semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  	spnSemester.setAdapter(semesterAdapter);
	}
	
	private static class MyUserParams {
        String course, year, semester, intake;        

        MyUserParams(String course, String year, String semester, String intake) {
        	this.course = course;
        	this.year = year;
            this.semester = semester;
            this.intake = intake;
            
        }
    }
	
	private class SendServerTask extends AsyncTask<MyUserParams, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(MyUserParams... params) {
			userFunction = new ServerInteractions();
			
			String course = params[0].course;
			String year = params[0].year;
			String semester = params[0].semester;
			String intake = params[0].intake;
			
//james			//db.addUserUniDetails(DbSchoolId, course, year, intake, semester, userId);
			json = userFunction.registerUserSettings(DbSchoolId, course, year, intake, semester, userId);
            
            // check for server response
            try {
            	JSONArray jsonArray = json.getJSONObject("data").getJSONArray("units");
            	List<Unit> units = new ArrayList<Unit>();
            	if (json.getString(KEY_SUCCESS) != null) {
                    //registerErrorMsg.setText("");
                	errorMsg = "";
                    res = json.getString(KEY_SUCCESS);
                    if(Integer.parseInt(res) == 1){
		            	// Create a list to store are videos in
			            /*
			            // Loop round our JSON list of lessons creating Lesson objects to use within our app
			            for (int i = 1; i < jsonArray.length(); i++) {            
			                JSONObject jsonObject = jsonArray.getJSONObject(i);	                
			                String unit_id = jsonObject.getString("unit_id");
			                String unit_names = jsonObject.getString("unit_names");
			                
			                // Create the school object and add it to our list
			                units.add(new Unit(unit_id, unit_names));
			            }*/
                    }else{
                        // Error in registration
                        errorMsg = "Error occured in registration";
                    }
                }
	         // Create a library to hold our lessons
            	UnitLibrary lib = new UnitLibrary("br", units);	            
            	
            } catch (JSONException e) {
                e.printStackTrace();
            }
			return json_user; 
		}
		
		@Override
		protected void onPostExecute(JSONObject json_user) {
			
			try {
				registerErrorMsg.setText(errorMsg);
				// Clear all previous data in database
	           // userFunction.logoutUser(getApplicationContext());
	            if(Integer.parseInt(res) == 1){
	            	// db.addUser(json_user.getString(KEY_FNAME), json_user.getString(KEY_LNAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(KEY_INST_ID), json_user.getString(KEY_SCHOOL_ID), json_user.getString(KEY_DATE_JOINED));
		            // Launch UnitSetuActivity Screen
		            Intent i = new Intent(getApplicationContext(), UnitSetupActivity.class);
		            Bundle b=new Bundle();
		            String unitsJson = json.toString();
		            b.putString("userId", userId);
		            b.putString("units_json", unitsJson);
		            i.putExtras(b);
		            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		            startActivity(i);
		            // Close Registration Screen
		            finish();
	            }
			} catch(Exception e){
				Log.e(this.getClass().getSimpleName(), "Error during registering settings", e);
				showErrorAlert();
			}
		}
		
		private void showErrorAlert() {
			
			try {
				Builder lBuilder = new AlertDialog.Builder(CourseSetupActivity.this);
				lBuilder.setTitle("Login Error");
				lBuilder.setCancelable(false);
				lBuilder.setMessage("Sorry, there was a problem logging you in");
	
				lBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
	
					@Override
					public void onClick(DialogInterface pDialog, int pWhich) {
						CourseSetupActivity.this.finish();
					}
					
				});
	
				AlertDialog lDialog = lBuilder.create();
				lDialog.show();
			} catch(Exception e){
				Log.e(this.getClass().getSimpleName(), "Problem showing error dialog.", e);
			}
		}
    	
    }
    
	public void getCoursesFeed(int school){
    	new Thread(new GetCoursesTask(HandleSchools, school+"")).start();
    }
    
    @SuppressLint("HandlerLeak")
	Handler HandleSchools = new Handler() {
        public void handleMessage(Message msg) {
        	addCoursesOnSpinner(msg);
        };
    };
    
    //add items into spinner dynamically
    public void addCoursesOnSpinner(Message msg) {
    	Log.i("loci", "I got to spinner");
    	CourseLibrary lib = (CourseLibrary) msg.getData().get(GetCoursesTask.LIBRARY);
        courses = lib.getCourses(); 
    	spnCourses = (Spinner) findViewById(R.id.spnCourses);
	  	List<String> list = new ArrayList<String>();
	  	//list.addAll(lib.getSchools());
	  	for(int i=0; i<lib.getCourses().size(); i++) {
	  		list.add(lib.getCourses().get(i).getCourseAcronyms() + " - " + lib.getCourses().get(i).getCourseName());
	  	}
	  	courseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
	  	courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  	spnCourses.setAdapter(courseAdapter);
	  	//OnItemSelectedListener spinnerListener = new onCourseSelectedListener(this, courseAdapter);
	  	//spnCourses.setOnItemSelectedListener(spinnerListener);
    }
	
	@Override
    public void onPause() {
    	super.onPause();
    	db.close();
    }
	public class onCourseSelectedListener implements OnItemSelectedListener {

        /*
         * provide local instances of the mLocalAdapter and the mLocalContext
         */

        ArrayAdapter<String> mLocalAdapter;
        Activity mLocalContext;

        /**
         *  Constructor
         *  @param c - The activity that displays the Spinner.
         *  @param ad - The Adapter view that
         *    controls the Spinner.
         *  Instantiate a new listener object.
         * @return 
         */
        public onCourseSelectedListener(Activity c, ArrayAdapter<String> ad) {

          this.mLocalContext = c;
          this.mLocalAdapter = ad;

        }

        /**
         * When the user selects an item in the spinner, this method is invoked by the callback
         * chain. Android calls the item selected listener for the spinner, which invokes the
         * onItemSelected method.
         *
         * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(
         *  android.widget.AdapterView, android.view.View, int, long)
         * @param parent - the AdapterView for this listener
         * @param v - the View for this listener
         * @param pos - the 0-based position of the selection in the mLocalAdapter
         * @param row - the 0-based row number of the selection in the View
         */
        public void onItemSelected(AdapterView<?> parent, View v, int pos, long row) {

            CourseSetupActivity.this.mPos = pos;
            String courseId = null;
            for(int i=0; 1<courses.size(); i++) {
            	if(i==pos) {
            		courseId = courses.get(pos).getCourseId();
            	}
            }
            CourseSetupActivity.this.mSelectedCourse = courseId;
        }

        /**
         * The definition of OnItemSelectedListener requires an override
         * of onNothingSelected(), even though this implementation does not use it.
         * @param parent - The View for this Listener
         */
        public void onNothingSelected(AdapterView<?> parent) {

            // do nothing

        }
    }


}
