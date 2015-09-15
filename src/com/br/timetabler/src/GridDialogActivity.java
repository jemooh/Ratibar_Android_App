package com.br.timetabler.src;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.br.timetabler.R;

        public class GridDialogActivity extends ActionBarActivity {
	     public static int startTime, endTime, duration,learningDays;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weekview_layout);
		 FragmentManager manager = getSupportFragmentManager();

	        GriddDialogFragment dialog = new GriddDialogFragment();
	        dialog.show(manager, "dialog");
		 
		 
	}
}