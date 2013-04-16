package com.br.timetabler.src;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.br.timetabler.R;
import com.br.timetabler.util.DatabaseHandler;
import com.br.timetabler.util.ServerInteractions;
//import android.view.MenuItem;
 
public class LoginActivity extends SherlockActivity {
    Button btnLogin;
    Button btnLinkToRegister;
    EditText inputEmail;
    EditText inputPassword;
    TextView loginErrorMsg;
    LoginTask loginTask;
    ServerInteractions userFunction;
    DatabaseHandler db;
    JSONObject json_user;
    JSONObject json;
    String errorMsg;
    String res;
 
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
 
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        db = new DatabaseHandler(this);
        
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
        
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        // Importing all assets like buttons, text fields
        inputEmail = (EditText) findViewById(R.id.loginEmail);
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        loginErrorMsg = (TextView) findViewById(R.id.login_error);
        loginErrorMsg.setText("");
        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString(); 
                db = new DatabaseHandler(getApplicationContext());
                //start task
                MyLoginParams params = new MyLoginParams(email, password);
                loginTask = new LoginTask();
                loginTask.execute(params);
            }
        });
 
        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    private class LoginTask extends AsyncTask<MyLoginParams, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(MyLoginParams... params) {
        	userFunction = new ServerInteractions();

        	String email = params[0].email;
        	String password = params[0].password;
        	
        	json = userFunction.loginUser(email, password);
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                	errorMsg = "";
                    res = json.getString(KEY_SUCCESS);
                    if(Integer.parseInt(res) == 1){
                        json_user = json.getJSONObject("user");
                    }else{
                        // Error in login
                    	errorMsg = "Incorrect username/password";
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
        		loginErrorMsg.setText(errorMsg);
        		if(isCancelled())        	
				return;
        		
        		// Clear all previous data in database
                userFunction.logoutUser(getApplicationContext());
                if(Integer.parseInt(res) == 1){
	                // user successfully logged in
	                // Store user details in SQLite Database
	                db.addUser(json_user.getString(KEY_FNAME), json_user.getString(KEY_LNAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(KEY_INST_ID), json_user.getString(KEY_SCHOOL_ID), json_user.getString(KEY_DATE_JOINED));
		                           
		        	// Launch Dashboard Screen
		            Intent dashboard = new Intent(getApplicationContext(), MainActivity.class);
		            // Close all views before launching Dashboard
		            dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		            startActivity(dashboard);
		            // Close Login Screen
		            finish();
                }
        	} catch(Exception e){
				Log.e(this.getClass().getSimpleName(), "Error Logging in", e);
				showErrorAlert();
			}
        }
        private void showErrorAlert() {
			
			try {
				Builder lBuilder = new AlertDialog.Builder(LoginActivity.this);
				lBuilder.setTitle("Login Error");
				lBuilder.setCancelable(false);
				lBuilder.setMessage("Sorry, there was a problem logging you in");
	
				lBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
	
					@Override
					public void onClick(DialogInterface pDialog, int pWhich) {
						LoginActivity.this.finish();
					}
					
				});
	
				AlertDialog lDialog = lBuilder.create();
				lDialog.show();
			} catch(Exception e){
				Log.e(this.getClass().getSimpleName(), "Problem showing error dialog.", e);
			}
		}
    }
    private static class MyLoginParams {
        String email, password;        

        MyLoginParams(String email, String password) {
            this.email = email;
            this.password = password;
            
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
}