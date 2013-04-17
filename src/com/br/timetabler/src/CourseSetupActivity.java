package com.br.timetabler.src;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockActivity;
import com.br.timetabler.R;
import com.br.timetabler.util.DatabaseHandler;
import com.br.timetabler.util.Log;
import com.br.timetabler.util.ServerInteractions;


public class CourseSetupActivity extends SherlockActivity {
	Spinner spnCourses, spnYear, spnIntake, spnSemester;
	protected ArrayAdapter<String> yearAdapter;
	protected ArrayAdapter<String> intakeAdapter;
	protected ArrayAdapter<String> semesterAdapter;
	String[] years = {"1st Year", "2nd Year", "3rd Year", "4th Year"};
	String[] semesters = {"1st Semester", "2nd Semester", "3rd Semester"};
	String[] intakes = {"January", "May", "September"};
	Button btnSaveCourse;
	ServerInteractions userFunction;
    DatabaseHandler db;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.course_setup_activity);
        addCoursesOnSpinner();
        addYearsOnSpinner();
        addIntakesOnSpinner();
        addSemestersOnSpinner();
        btnSaveCourse = (Button) findViewById(R.id.btnSaveCourse);
        btnSaveCourse.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				String SpinCourse = spnCourses.getSelectedItemPosition()+"";
				String SpinYear = spnYear.getSelectedItemPosition()+"";
				String SpinIntake = spnIntake.getSelectedItemPosition()+"";
				String SpinSemester = spnSemester.getSelectedItemPosition()+"";
				Log.i("SpinCourse:"+SpinCourse+" SpinYear: "+SpinYear+" SpinIntake:"+SpinIntake+" SpinSemester:"+SpinSemester);
				db.addUserUniDetails("1", SpinCourse, SpinYear, SpinIntake, SpinSemester);
				// Launch Dashboard Screen
	            Intent setupUnits = new Intent(getApplicationContext(), UnitSetupActivity.class);
	            // Close all views before launching setupUnits
	            setupUnits.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(setupUnits);
	            // Close Login Screen
	            finish();
			}
		});
	}
	/**
	 * setup a simple spinner(dropdown) for years
	 */
	public void addCoursesOnSpinner() {		   
		spnCourses = (Spinner) findViewById(R.id.spnYear);
	  	List<String> yList = new ArrayList<String>();
	  	for(int i=0; i<years.length; i++) {
	  		yList.add(years[i]);
		}
	  	
	  	yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yList);
	  	yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  	spnCourses.setAdapter(yearAdapter);
	}
	/**
	 * setup a simple spinner(dropdown) for years
	 */
	public void addYearsOnSpinner() {		   
		spnYear = (Spinner) findViewById(R.id.spnYear);
	  	List<String> yList = new ArrayList<String>();
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
	  	for(int i=0; i<semesters.length; i++) {
	  		semList.add(semesters[i]);
		}
	  	
	  	semesterAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, semList);
	  	semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  	spnSemester.setAdapter(semesterAdapter);
	}
}
