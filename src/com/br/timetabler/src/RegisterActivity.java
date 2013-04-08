package com.br.timetabler.src;

import org.json.JSONException;
import org.json.JSONObject;
 
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.br.timetabler.R;
import com.br.timetabler.util.DatabaseHandler;
import com.br.timetabler.util.ServerInteractions;
 
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
//import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
 
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
 
    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
 
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
 
        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        // Importing all assets like buttons, text fields
        inputFullName = (EditText) findViewById(R.id.registerName);
        inputEmail = (EditText) findViewById(R.id.registerEmail);
        inputPassword = (EditText) findViewById(R.id.registerPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        registerErrorMsg = (TextView) findViewById(R.id.register_error);
 
        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String usernames = inputFullName.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                
                //start task
                MyUserParams userparams = new MyUserParams(usernames, email, password);
                registerTask = new RegisterTask();
                registerTask.execute(userparams);
                
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
    
    private class RegisterTask extends AsyncTask<MyUserParams, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(MyUserParams... params) {
			userFunction = new ServerInteractions();
			
			String fullname = params[0].fullname;
			String email = params[0].email;
        	String password = params[0].password;
            json = userFunction.registerUser(fullname, email, password);

            // check for login response
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
		            db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));
		            // Launch Dashboard Screen
		            Intent dashboard = new Intent(getApplicationContext(), MainActivity.class);
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
        String fullname, email, password;        

        MyUserParams(String fullname, String email, String password) {
        	this.fullname = fullname;
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