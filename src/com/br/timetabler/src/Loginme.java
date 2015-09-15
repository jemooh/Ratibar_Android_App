package com.br.timetabler.src;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.br.timetabler.R;
import com.br.timetabler.util.DatabaseHandler;
import com.br.timetabler.util.DatabaseHandler_TodayLessons;
import com.br.timetabler.util.DatabaseHandler_joe;
import com.br.timetabler.util.ServerInteractions;
import com.br.timetabler.util.UserFunctions;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
 

 
public class Loginme extends ActionBarActivity {
    Button btnLogin;
    Button btnLinkToReg;
    EditText logemail;
    EditText logpass;
    TextView loginErrorMsg;
    
	private SharedPreferences prfs;
	private String prefName = "report";
	 ServerInteractions userFunction;
	JSONObject json_user, jsonInstSettings;
    JSONObject json;
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
    private static String KEY_INST_NAME = "inst_name";
    private static String KEY_COURSE_NAME = "course_name";
    private static String KEY_YEAR = "year";
    private static String KEY_DATE_JOINED = "date_joined";
    private static String KEY_GLOB_LEARNINGDAYS = "learningDays";
    private static String KEY_GLOB_STARTTIME = "startTime";
    private static String KEY_GLOB_ENDTIME = "endTime";
    private static String KEY_GLOB_DURATION = "duration";
    private static String  SpinYear;
    private static String KEY_CREATED_AT = "created_at";
    protected ArrayAdapter<String> yearAdapter;
    Spinner spnYear;
    String[] years = {"1st Year", "2nd Year", "3rd Year", "4th Year"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
 
        // Importing all assets like buttons, text fields
        logemail = (EditText) findViewById(R.id.logreg_no);
        logpass = (EditText) findViewById(R.id.logpass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
       // btnLinkToReg = (Button) findViewById(R.id.btnLinkToReg);
        loginErrorMsg = (TextView) findViewById(R.id.login_error);
 
        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View view) {
            	
            	
            	
                String email = logemail.getText().toString();
                String password = logpass.getText().toString();
                
              userFunction = new  ServerInteractions();
                JSONObject json = userFunction.loginUser(email, password);
                
                
 
                // check for login response
                try {
                    if (json.getString(KEY_SUCCESS) != null) {
                        loginErrorMsg.setText("");
                        String res = json.getString(KEY_SUCCESS);
                        if(Integer.parseInt(res) == 1){
                            // user successfully logged in
                            // Store user details in SQLite Database
                            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                            JSONObject json_user = json.getJSONObject("user");
                             
                            // Clear all previous data in database
                            userFunction.logoutUser(getApplicationContext());
                            db.addUser(json_user.getString(KEY_FNAME), 
				                		json_user.getString(KEY_LNAME), 
				                		json_user.getString(KEY_EMAIL),
				                		password,
				                		json.getString(KEY_UID), 
				                		json_user.getString(KEY_INST_ID), 
				                		json_user.getString(KEY_SCHOOL_ID), 
				                		json_user.getString(KEY_DATE_JOINED),
				                		json_user.getString(KEY_COURSE_NAME),
				                		json_user.getString(KEY_COURSE_NAME),
				                		json_user.getString(KEY_YEAR),
				                		json_user.getString(KEY_INST_NAME),
				                		jsonInstSettings.getString(KEY_GLOB_LEARNINGDAYS),
				                		jsonInstSettings.getString(KEY_GLOB_STARTTIME),
				                		jsonInstSettings.getString(KEY_GLOB_ENDTIME),
				                		jsonInstSettings.getString(KEY_GLOB_DURATION));                     
	                             
                            // Launch Dashboard Screen
                            Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
                             
                            // Close all views before launching Dashboard
                            dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(dashboard);
                             
                            // Close Login Screen
                            finish();
                        }else{
                            // Error in login
                            loginErrorMsg.setText("Incorrect username/password");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
 /*
        // Link to Register Screen
        btnLinkToReg.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });*/
    }
    
	
    
    
}

