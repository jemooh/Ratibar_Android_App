package com.br.timetabler.src;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import com.br.timetabler.R;
import com.br.timetabler.model.Lesson;
import com.br.timetabler.model.LessonLibrary;
import com.br.timetabler.model.ListUnits;
import com.br.timetabler.model.ListUnitsLibrary;
import com.br.timetabler.service.task.GetLessonsTask;
import com.br.timetabler.service.task.GetUnitsTask;
import com.br.timetabler.util.DatabaseHandler;
import com.br.timetabler.util.ServerInteractions;
import com.br.timetabler.widget.TodayLessonsListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.view.ActionBarPolicy;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

	public class ProfileActivity extends SettingHC  {
	      
		Button btnLogout;
		private List<ListUnits> lessons;
		ServerInteractions server;
		String userId,userPassword,email;
		ListView listView;
		public ProfileActivity() {
			super(R.string.profile);
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    setContentView(R.layout.profile);
		    ActionBar actionBar = getSupportActionBar();
		     actionBar.setDisplayHomeAsUpEnabled(true);
		     
		     getOverflowMenu();
		     ActionBarPolicy.get(this).showsOverflowMenuButton();
		     setBehindContentView(R.layout.menu_frame);
		     
		     if (savedInstanceState == null) {
					
					getSupportFragmentManager().beginTransaction()
			                 .add(R.id.menu_frame, new SettingsFragment()).commit();
				
				}
		    ImageView  img = (ImageView) findViewById(R.id.imageView1);
		    img.setScaleType(ScaleType.FIT_XY);
		    
		    
		    TextView txtname = (TextView) findViewById(R.id.txtname);
			TextView txtCourse = (TextView) findViewById(R.id.txtCourse_name);
			TextView txtYear = (TextView) findViewById(R.id.txtYear);
			TextView txtInst = (TextView) findViewById(R.id.txtIsnt_name);
			
			 DatabaseHandler  dbHandler = new DatabaseHandler(getApplicationContext());
		    	HashMap<String,String> user = new HashMap<String,String>();
		    	user = dbHandler.getUserDetails();

		    	String fname = user.get("fname");
	        	String lname = user.get("lname");
	        	String inst_name = user.get("inst_name");
	        	String course_name = user.get("course_name");
	        	String campus_name = user.get("campus_name");
	        	String year = user.get("year");
	        	String semester = user.get("semester");
			
	        Typeface font_a = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Regular.ttf");
	        Typeface font_b = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Light.ttf");
	        Typeface font_c = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Medium.ttf");
	        txtCourse.setTypeface(font_a);
	        txtInst.setTypeface(font_a);
	        txtname.setTypeface(font_c);
	        txtYear.setTypeface(font_a);
	        
	        txtname.setText(fname+" "+lname);
	        txtInst.setText(inst_name+"("+campus_name+")");
	        txtCourse.setText(course_name);
	        txtYear.setText("Year"+" "+year+",Semester "+semester);
	        
	        
	        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			setSlidingActionBarEnabled(true);
	        
			 
		       
		       dbHandler = new DatabaseHandler(this);
		    	HashMap<String,String> userU = new HashMap<String,String>();
		    	userU = dbHandler.getUserDetails();
		    	userId = userU.get("uid");
		    	email = userU.get("email");
		    	userPassword = userU.get("password");
		       
		}
				
				
				//create a listview to hold data
		        //listView = (ListView) findViewById(R.id.unitslistView);
				
				// Here we are adding this activity as a listener for when any row in the List is 'clicked'
		        // The activity will be sent back the video that has been pressed to do whatever it wants with
		        // in this case we will retrieve the URL of the video and fire off an intent to view it
		        
		        //getLessonsFeed(listView);
		        
		       
		    	/*
			   }else {
					//user in not logged in show login screen
					Intent login = new Intent (getActivity(),Loginme.class);
					login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(login);
					
				}
		        */
	  
	
		

		//getLessonsFeed(View view)--- used to getView and get email n pass from view
		// This is the XML onClick listener to retreive a users video feed
		  /** public void getLessonsFeed(View view){
			// String email = DashboardActivity.email;
			// String userPassword = DashboardActivity.password;
		        // We start a new task that does its work on its own thread
		        // We pass in a handler that will be called when the task has finished
		        //new Thread(new GetLessonsTask(responseHandler, Leo, false, email, userPassword)).start();
		        new Thread(new GetLessonsTask(responseHandler, null, false, null, null)).start();
		        
		       
		    }
	    
	    // This is the handler that receives the response when the YouTube task has finished
	
	    @SuppressLint("HandlerLeak")
		Handler responseHandler = new Handler() {
	        public void handleMessage(Message msg) {
	        	populateListWithLessons(msg);
	        };
	    };
	    
	  
	     * This method retrieves the Library of videos from the task and passes them to our ListView
	     * @param msg
	    
	    private void populateListWithLessons(Message msg) {
	        // Retreive the videos are task found from the data bundle sent back
	        ListUnitsLibrary lib = (ListUnitsLibrary) msg.getData().get(GetUnitsTask.LIBRARY);
	        // Because we have created a custom ListView we don't have to worry about setting the adapter in the activity
	        // we can just call our custom method with the list of items we want to display
	        ProgressBar pbpp = (ProgressBar) findViewById(R.id.pbppl);
	        TextView txtMsg = (TextView)findViewById(R.id.progressMsg2);
			pbpp.setVisibility(View.GONE);
			txtMsg.setVisibility(View.GONE);
	        //listView.setLessons(lib.getLessons());
			if(lib.getLessons().isEmpty()){
				txtMsg.setText("No lessons for today, take a break");			
			} else {
				txtMsg.setVisibility(View.GONE);
				lessons = lib.getLessons();
		       // listView.setLessons(lessons);
			}
			
	    } */
		
		
		
		/**
		public void onClickLogout (View btnLogout){
			
			new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle("Logout?")
			.setMessage("Do you want to Logout?")
			.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
								
							//server.logoutUser(getApplicationContext());
							//ProfileActivity.this.finish();
							/*
							Intent myintent = new Intent (((Dialog)dialog).getContext(),LoginActivity.class);
							startActivity(myintent);
							return;*//*
							 Intent login = new Intent(getApplicationContext(), LoginActivity.class);
					            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					            startActivity(login);
					            // Closing dashboard screen
					            finish();
						}

					}).setNegativeButton("No", null).show();
	
}*/
	
		
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
		
		private void getOverflowMenu() {
			 
		    try {
		 
		       ViewConfiguration config = ViewConfiguration.get(this);
		       java.lang.reflect.Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
		       if(menuKeyField != null) {
		           menuKeyField.setAccessible(true);
		           menuKeyField.setBoolean(config, false);
		       }
		   } catch (Exception e) {
		       e.printStackTrace();
		   }
		}
		 // so that we know something was triggered
		    public void showToast(String msg){
		        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		    
		 
		}
				//remove the menu button for 3.0 < 
			    /*@Override
				public boolean onPrepareOptionsMenu(Menu menu) {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					        menu.removeItem(R.id.a_More);
					    }
					    return super.onPrepareOptionsMenu(menu);
					}
					
		
			*/
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
		
		            case R.id.menu_grid: //display description
		            	Intent i1 = new Intent(getApplicationContext(), WeekviewActivity.class);
		            	i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		                startActivity(i1);
		               // finish();
		                break;
		
		            case R.id.menu_list: //display description
		            	Intent i2 = new Intent(getApplicationContext(),  AllNotesListActivity.class);
		            	i2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		                startActivity(i2);
		                //finish();
		                break;
		
		            case R.id.menu_assignments: //display reviews
		            	Intent i3 = new Intent(getApplicationContext(), AssignmentsListActivity.class);
		                startActivity(i3);
		                //finish();
		                break;
		           
		            case R.id.menu_settings: //display reviews
		        	    toggle();
		  			    return true;
		           
		            
		            
		        }
		        return super.onOptionsItemSelected(item);
		    }

	
}