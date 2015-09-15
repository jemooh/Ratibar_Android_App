package com.br.timetabler.src;

import com.br.timetabler.util.DatabaseHandler;
import com.br.timetabler.util.ServerInteractions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class LogOutActivity extends ActionBarActivity {
	
	 ServerInteractions	server;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	        server = new ServerInteractions();
		
		//I used this to help in fixing authentication by loging out the user upon loading the app
		server.logoutUser(getApplicationContext());
		
		Intent login = new Intent(getApplicationContext(), DashboardActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
        finish();
		
 }
}