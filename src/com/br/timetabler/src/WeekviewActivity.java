package com.br.timetabler.src;


import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.br.timetabler.R;
import com.br.timetabler.util.DatabaseHandler;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.view.ActionBarPolicy;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
		public class WeekviewActivity extends  SettingHC {
			public static int startTime, endTime, duration,learningDays;
			public static String currentTime;
			
			public WeekviewActivity() {
				super(R.string.grid_name);
			}
			
			 static String parseNull(Object obj){
			        return obj == null?"null"  : "Object";
			    }
			
			@Override
			public void onCreate(Bundle savedInstanceState) {
				 Log.e("ActivityCycle", "onCreate: bundle="+parseNull(savedInstanceState));
				super.onCreate(savedInstanceState);
				
				setContentView(R.layout.weekview_layout);
				ActionBar actionBar = getSupportActionBar();
				
			     actionBar.setDisplayHomeAsUpEnabled(true);
			     
			     ActionBarPolicy.get(this).showsOverflowMenuButton();
			     setBehindContentView(R.layout.menu_frame);
			     
			     
			     if (savedInstanceState == null) {
						
						getSupportFragmentManager().beginTransaction()
				                 .add(R.id.menu_frame, new SettingsFragment()).commit();
						
						getSupportFragmentManager().beginTransaction()
						         .add(R.id.container2, new GridFragment()).commit();
					
					}
			     
			     
			   //  getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
					//setSlidingActionBarEnabled(true);
			     
			    
			        
			        DatabaseHandler  dbHandler = new DatabaseHandler(getApplicationContext());
			    	HashMap<String,String> user = new HashMap<String,String>();
			    	user = dbHandler.getUserDetails();
		
			    	
			    	startTime = Integer.parseInt(user.get("startTime"));
			    	endTime = Integer.parseInt(user.get("endTime"));
			    	duration = Integer.parseInt(user.get("duration"));
			    	learningDays = Integer.parseInt(user.get("learningDays"));
				 
				 
			
			}
			
			/*
			@Override
			public boolean onPrepareOptionsMenu(Menu menu) {
			    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			        menu.removeItem(R.id.a_More);
			    }
			    return super.onPrepareOptionsMenu(menu);
			}
			
			*/
			@Override
			public boolean onMenuOpened(int featureId, Menu menu)
			{
				if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
					        if(menu.getClass().getSimpleName().equals("MenuBuilder")){
					            try{
					                Method m = menu.getClass().getDeclaredMethod(
					                    "setOptionalIconsVisible", Boolean.TYPE);
					                m.setAccessible(true);
					                m.invoke(menu, true);
					            }
					            catch(NoSuchMethodException e){
					               // Log.i( "onMenuOpened", e);
					            }
					            catch(Exception e){
					                throw new RuntimeException(e);
					            }
					        }
					    }
					    return super.onMenuOpened(featureId, menu);
					}
	
			
			
			
		
			@Override
			public boolean onCreateOptionsMenu(Menu menu) {
				// Inflate the menu; this adds items to the action bar if it is present.
				getMenuInflater().inflate(R.menu.activity_main, menu);
				return true;
			}
			public boolean onOptionsItemSelected(MenuItem item) {
		        switch (item.getItemId()) {
		        
		            case android.R.id.home://dsipaly video
		                //Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
		                Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
		                startActivity(i);
		                finish();
		                break;
		                /*
		            case R.id.menu_grid: //display description
		            	Intent i1 = new Intent(getApplicationContext(), WeekviewActivity.class);
		            	//i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		                startActivity(i1);
		                //finish();
		                break;*/
		
		            case R.id.menu_list: //display description
		            	Intent i2 = new Intent(getApplicationContext(), AllNotesListActivity.class);
		            	//i2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		                startActivity(i2);
		               // finish();
		                break;
		
		            case R.id.menu_assignments: //display reviews
		            	Intent i3 = new Intent(getApplicationContext(), AssignmentsListActivity.class);
		                startActivity(i3);
		                //finish();
		                break;
		            case R.id.profile:
		            	Intent i6 = new Intent(getApplicationContext(),ProfileActivity.class);
		            	startActivity(i6);
		            	break;
		            	
		            	
		            case R.id.menu_settings: //display reviews
		        	    toggle();
		  			    return true;
		                
		           /*      
		           case R.id.menu_refresh: //display reviews
		            	Intent i5 = new Intent(getApplicationContext(), WeekviewActivity.class);
		                startActivity(i5);
		                //finish();
		                break;
		               
		            case R.id.a_More:
		            	openOptionsMenuDeferred();
		                break;
		            */
		            
		        }
		        return super.onOptionsItemSelected(item);
		    }
			
			
			 @Override
			    public void onConfigurationChanged(Configuration newConfig) {
			        super.onConfigurationChanged(newConfig);
			        Log.e("GridActivityCycle", "onConfigurationChanged");
			    }

			    @Override
			    public View onCreateView(String name, Context context, AttributeSet attrs) {
			        Log.e("GridActivityCycle", "onCreateView");
			        return super.onCreateView(name, context, attrs);
			    }

			    @Override
				public void onPostCreate(Bundle savedInstanceState) {
			        Log.e("GridActivityCycle", "onPostCreate: bundle="+parseNull(savedInstanceState));
			        super.onPostCreate(savedInstanceState);
			    }

			    @Override
			    protected void onResume() {
			        Log.e("GridActivityCycle", "onResume");
			        super.onResume();
			    }

			    @Override
			    protected void onPostResume() {
			        Log.e("GridActivityCycle", "onPostResume");
			        super.onPostResume();
			    }

			    @Override
			    protected void onResumeFragments() {
			        Log.e("ActivityCycle", "onResumeFragments");
			        super.onResumeFragments();
			    }

			    @Override
			    protected void onStop() {
			        Log.e("GridActivityCycle", "onStop");
			        super.onStop();
			    }

			    @Override
			    protected void onPause() {
			        Log.e("GridActivityCycle", "onPause");
			        super.onPause();
			    }

			    @Override
			    protected void onDestroy() {
			        Log.e("GridActivityCycle", "onDestroy");
			        super.onDestroy();
			    }

			    @Override
			    protected void onSaveInstanceState(Bundle outState) {
			        Log.e("GridActivityCycle", "onSaveInstanceState: outState="+parseNull(outState));
			          super.onSaveInstanceState(outState);
			    }

			    @Override
			    public void onAttachFragment(Fragment fragment) {
			        Log.e("GridActivityCycle", "onAttachFragment");
			        super.onAttachFragment(fragment);
			    }

			    @Override
			    protected void onRestoreInstanceState(Bundle savedInstanceState) {
			        Log.e("GridActivityCycle", "onRestoreInstanceState: bundle="+parseNull(savedInstanceState));
			        super.onRestoreInstanceState(savedInstanceState);
			    }

			    @Override
			    protected void onRestart() {
			        Log.e("GridActivityCycle", "onRestart");
			        super.onRestart();
			    }

			    @Override
			    public void onAttachedToWindow() {
			        Log.e("GridActivityCycle", "onAttachedToWindow");
			        super.onAttachedToWindow();
			    }

			    @Override
			    public void onDetachedFromWindow() {
			        Log.e("GridActivityCycle", "onDetachedFromWindow");
			        super.onDetachedFromWindow();
			    }

			    @Override
			    protected void onStart() {
			        Log.e("GridActivityCycle", "onStart");
			        super.onStart();
			    }

			    @Override
			    public void onWindowFocusChanged(boolean hasFocus) {
			        Log.e("GridActivityCycle", "onWindowFocusChanged");
			        super.onWindowFocusChanged(hasFocus);
			    }

			    @Override
			    public void onLowMemory() {
			        Log.e("GridActivityCycle", "onLowMemory");
			        super.onLowMemory();
			    }
			/*
			private Handler handler = new Handler(Looper.getMainLooper());
			public void openOptionsMenuDeferred() {
			    handler.post(new Runnable() {
			        @Override
			        public void run() {
			            openOptionsMenu();
			        }
			    }
			    );
			}*/
		}
		
