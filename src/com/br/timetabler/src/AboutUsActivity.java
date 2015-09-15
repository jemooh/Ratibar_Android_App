package com.br.timetabler.src;

import com.br.timetabler.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.view.ActionBarPolicy;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.TextView;

		public class AboutUsActivity extends SettingHC	 {
		
			
			public AboutUsActivity() {
				super(R.string.aboutus);
			}
			

		public void onCreate(Bundle savedInstanceState){
	    	   super.onCreate(savedInstanceState);
	    	   setContentView(R.layout.aboutus);
	    	   
	    	   ActionBar actionBar = getSupportActionBar();
			     actionBar.setDisplayHomeAsUpEnabled(true);
			     
			     getOverflowMenu();
			     ActionBarPolicy.get(this).showsOverflowMenuButton();
			     setBehindContentView(R.layout.menu_frame);
			     
			     if (savedInstanceState == null) {
						
						getSupportFragmentManager().beginTransaction()
				                 .add(R.id.menu_frame, new SettingsFragment()).commit();
	    	   
			     }
			
			     TextView about = (TextView) findViewById(R.id.about);
			     TextView title = (TextView) findViewById(R.id.title);
			     TextView whatwedo = (TextView) findViewById(R.id.whatwedo);
			     TextView whatweB = (TextView) findViewById(R.id.whatweB);
			     TextView contactUsT = (TextView) findViewById(R.id.contactT);
			     TextView contactUsB = (TextView) findViewById(R.id.contactB);
			     
			     Typeface font_b = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Light.ttf");
			     Typeface font_c = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Bold.ttf");
			     Typeface font_e = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Regular.ttf");
			     Typeface font_d = Typeface.createFromAsset(this.getAssets(), "fonts/Comfortaa-Bold.ttf");
			     Typeface font_f = Typeface.createFromAsset(this.getAssets(), "fonts/DroidSans.ttf");
			     title.setTypeface(font_d);
			     about.setTypeface(font_e);
			     whatwedo.setTypeface(font_c);
			     whatweB.setTypeface(font_b);
			     contactUsT.setTypeface(font_f);
			     contactUsB.setTypeface(font_f);
			     
			     about.setText(getString(R.string.about));
			     whatweB.setText(Html.fromHtml(getString(R.string.what_we_do)));
			     contactUsB.setText(Html.fromHtml(getString(R.string.contact_us)));
			     
			     getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					setSlidingActionBarEnabled(true);
			     
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
			
			
			
			
			
			}
