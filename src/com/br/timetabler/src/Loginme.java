package com.br.timetabler.src;


import java.util.ArrayList;
import java.util.HashMap;
 




import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.br.timetabler.R;


import com.br.timetabler.util.DatabaseHandler_jemo;
import com.br.timetabler.util.DatabaseHandler_joe;
import com.br.timetabler.util.UserFunctions;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
 

 
public class Loginme extends SherlockActivity {
    Button btnLogin;
    Button btnLinkToReg;
    EditText logreg_no;
    EditText logpass;
    TextView loginErrorMsg;
    
	private SharedPreferences prfs;
	private String prefName = "report";
 
    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";         
    private static String KEY_REG_NO = "reg_no";
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
        logreg_no = (EditText) findViewById(R.id.logreg_no);
        logpass = (EditText) findViewById(R.id.logpass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToReg = (Button) findViewById(R.id.btnLinkToReg);
        loginErrorMsg = (TextView) findViewById(R.id.login_error);
 
        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View view) {
            	
            	
            	
                String reg_no = logreg_no.getText().toString();
                String password = logpass.getText().toString();
                
                UserFunctions userFunction = new UserFunctions();
                JSONObject json = userFunction.loginUser(reg_no, password);
                
                
 
                // check for login response
                try {
                    if (json.getString(KEY_SUCCESS) != null) {
                        loginErrorMsg.setText("");
                        String res = json.getString(KEY_SUCCESS);
                        if(Integer.parseInt(res) == 1){
                            // user successfully logged in
                            // Store user details in SQLite Database
                            DatabaseHandler_joe db = new DatabaseHandler_joe(getApplicationContext());
                            JSONObject json_user = json.getJSONObject("user");
                             
                            // Clear all previous data in database
                            userFunction.logoutUser(getApplicationContext());
                            db.addUser(json_user.getString(KEY_NAME),
                            		   json_user.getString(KEY_REG_NO),
                            		   json.getString(KEY_UID),
                            		   json_user.getString(KEY_CREATED_AT));                       
                             
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
 
        // Link to Register Screen
        btnLinkToReg.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    
	
    
    
}

