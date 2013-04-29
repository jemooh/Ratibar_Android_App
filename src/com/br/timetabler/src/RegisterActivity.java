package com.br.timetabler.src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.br.timetabler.R;
import com.br.timetabler.model.School;
import com.br.timetabler.model.SchoolLibrary;
import com.br.timetabler.service.task.GetUniversityDetailsTask;
import com.br.timetabler.util.DatabaseHandler;
import com.br.timetabler.util.ServerInteractions;
import com.br.timetabler.listener.UniversitySelectListener;
//import android.view.MenuItem;
 
public class RegisterActivity extends SherlockActivity {
    Button btnRegister;
    Button btnLinkToLogin;
    EditText inputFullName;
    EditText inputEmail;
    EditText inputPassword;
    TextView registerErrorMsg;
    
    RegisterTask registerTask;
    ServerInteractions userFunction;
    DatabaseHandler db;
    JSONObject json_user;
    JSONObject json;
    String errorMsg;
    String res;
    Spinner spinInstitutions, spnSchools, spnCourses, spnUnits;
    List<School> schools;
    List<String> instlist;
    /**
     * ArrayAdapter connects the spinner widget to array-based data.
     */
    protected ArrayAdapter<CharSequence> mAdapter;
    /**
     * Fields to contain the current position and display contents of the spinner
     */
    protected int mPos;
    protected String mSelection;
    /**
     *  The initial position of the spinner when it is first installed.
     */
    public static final int DEFAULT_POSITION = 2;
    
    UniversitySelectListener uniSelectListener ;
    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_FNAME = "fname";
    private static String KEY_LNAME = "lname";
    private static String KEY_EMAIL = "email";
    private static String KEY_INST_ID = "inst_id";
    private static String KEY_SCHOOL_ID = "school_id";
    private static String KEY_DATE_JOINED = "date_joined";
    String[][] uniString= {
			{"Select University", "0"},
			{"Nairobi University", "1"},
			{"Strathmore University", "12"},
			{"USIU University", "15"},
	};
 
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        
        // Importing all assets like buttons, text fields
        inputFullName = (EditText) findViewById(R.id.registerName);
        inputEmail = (EditText) findViewById(R.id.registerEmail);
        inputPassword = (EditText) findViewById(R.id.registerPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        registerErrorMsg = (TextView) findViewById(R.id.register_error);
        //addInstitutionsOnSpinner();
        getSchoolsFeed(12);
        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String usernames = inputFullName.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                int SchId=0;
                int SpinPos = spnSchools.getSelectedItemPosition();
                String schoolId = null;
                for(int i=0; i<schools.size(); i++) {
                	if(i==SpinPos) {
                		schoolId = schools.get(i).getSchoolId().toString();
                	}
                }
                
                //start task
                MyUserParams userparams = new MyUserParams(usernames, email, password, schoolId);
                registerTask = new RegisterTask();
                registerTask.execute(userparams);
                
                /**Toast.makeText(getApplicationContext(),
            	  		"OnClickListener : " + 
            	                  "\nSpinner 2 : "+ String.valueOf(spinInstitutions.getSelectedItem()),
            	  			Toast.LENGTH_SHORT).show();*/
                
            }
        });
 
        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                // Close Registration View
                finish();
            }
        });
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    
    //add items into spinner dynamically
    /*public void addInstitutionsOnSpinner() {
    	
    	
    	spinInstitutions = (Spinner) findViewById(R.id.spinInstitutions);
	  	instlist = new ArrayList<String>();
	  	instlist.add("Select University");
	  	instlist.add("Strathmore University");
	  	instlist.add("USIU University");
	  	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, instlist);
	  	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  	spinInstitutions.setAdapter(dataAdapter);
	  	OnItemSelectedListener spinnerListener = new onUniversitySelectedListener(this, dataAdapter);
        spinInstitutions.setOnItemSelectedListener(spinnerListener);
    }
    */
    
    private class RegisterTask extends AsyncTask<MyUserParams, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(MyUserParams... params) {
			userFunction = new ServerInteractions();
			
			String fullname = params[0].fullname;
			String email = params[0].email;
			String password = params[0].password;
			String school = params[0].school;
            json = userFunction.registerUser(fullname, email, password, school);

            // check for server response
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    //registerErrorMsg.setText("");
                	errorMsg = "";
                    res = json.getString(KEY_SUCCESS);
                    if(Integer.parseInt(res) == 1){
                        // user successfully registred, Store user details in SQLite Database
                        db = new DatabaseHandler(getApplicationContext());
                        json_user = json.getJSONObject("user");
                    }else{
                        // Error in registration
                        
                        errorMsg = "Error occured in registration";
                    }
                }
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
	            userFunction.logoutUser(getApplicationContext());
	            if(Integer.parseInt(res) == 1){
		            db.addUser(json_user.getString(KEY_FNAME), json_user.getString(KEY_LNAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(KEY_INST_ID), json_user.getString(KEY_SCHOOL_ID), json_user.getString(KEY_DATE_JOINED));
		            // Launch Dashboard Screen
		            Intent dashboard = new Intent(getApplicationContext(), CourseSetupActivity.class);
		            // Close all views before launching Dashboard
		            dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		            startActivity(dashboard);
		            // Close Registration Screen
		            finish();
	            }
			} catch(Exception e){
				Log.e(this.getClass().getSimpleName(), "Error during registration", e);
				showErrorAlert();
			}
		}
		
		private void showErrorAlert() {
			
			try {
				Builder lBuilder = new AlertDialog.Builder(RegisterActivity.this);
				lBuilder.setTitle("Login Error");
				lBuilder.setCancelable(false);
				lBuilder.setMessage("Sorry, there was a problem logging you in");
	
				lBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
	
					@Override
					public void onClick(DialogInterface pDialog, int pWhich) {
						RegisterActivity.this.finish();
					}
					
				});
	
				AlertDialog lDialog = lBuilder.create();
				lDialog.show();
			} catch(Exception e){
				Log.e(this.getClass().getSimpleName(), "Problem showing error dialog.", e);
			}
		}
    	
    }
    
    private static class MyUserParams {
        String fullname, email, password, school;        

        MyUserParams(String fullname, String email, String password, String school) {
        	this.fullname = fullname;
        	this.email = email;
            this.password = password;
            this.school = school;
            
        }
    }
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, Settings.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

    public void getSchoolsFeed(int uni){
    	new Thread(new GetUniversityDetailsTask(HandleSchools, "12")).start();
    }
    
    @SuppressLint("HandlerLeak")
	Handler HandleSchools = new Handler() {
        public void handleMessage(Message msg) {
        	addSchoolsOnSpinner(msg);
        };
    };
    
    //add items into spinner dynamically
    public void addSchoolsOnSpinner(Message msg) {
    	// Retreive the videos are task found from the data bundle sent back
        SchoolLibrary lib = (SchoolLibrary) msg.getData().get(GetUniversityDetailsTask.LIBRARY);
        schools = lib.getSchools();
    	spnSchools = (Spinner) findViewById(R.id.spnSchools);
	  	List<String> list = new ArrayList<String>();
	  	//list.addAll(lib.getSchools());
	  	for(int i=0; i<lib.getSchools().size(); i++) {
	  		list.add(lib.getSchools().get(i).getSchoolName());
	  	}
	  	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
	  	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  	spnSchools.setAdapter(dataAdapter);
	  	//spinInstitutions.setOnItemSelectedListener(new UniversitySelectListener());
    }
    

    /**
     *  A callback listener that implements the
     *  {@link android.widget.AdapterView.OnItemSelectedListener} interface
     *  For views based on adapters, this interface defines the methods available
     *  when the user selects an item from the View.
     *
     */
    public class onUniversitySelectedListener implements OnItemSelectedListener {

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
        public onUniversitySelectedListener(Activity c, ArrayAdapter<String> ad) {

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

            RegisterActivity.this.mPos = pos;
            RegisterActivity.this.mSelection = parent.getItemAtPosition(pos).toString();
            /*
             * Set the value of the text field in the UI
             */
            getSchoolsFeed(RegisterActivity.this.mPos);
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