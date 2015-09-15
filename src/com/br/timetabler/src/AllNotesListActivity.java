package com.br.timetabler.src;

import java.lang.reflect.Method;
import java.text.DecimalFormat;

import org.json.JSONObject;

import com.br.timetabler.R;
import com.br.timetabler.adapter.LessonNotesCursorAdapter;
import com.br.timetabler.adapter.NoteArrayAdapter;
import com.br.timetabler.util.DatabaseHandler_joe;
import com.br.timetabler.util.NotesDataHelper;
import com.br.timetabler.util.NotesDialogFragment;
import com.br.timetabler.util.ServerInteractions;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

			public class AllNotesListActivity extends SettingHC {
				
				AlertDialog alertDialog;
				ImageButton btnSaveNotes, btnAddnotes,btnShareNotes;
				ListView listViewNotes;
				private static final int ENTER_DATA_REQUEST_CODE = 1;
				private static String KEY_SUCCESS = "success";
				private LessonNotesCursorAdapter customAdapter;
			    private NotesDataHelper databaseHelper;
			    private NoteArrayAdapter  notAdpter;
				//SaveNotesTask notesTsk;
				ServerInteractions userFunction;
				DatabaseHandler_joe db;
				JSONObject json_user;
			    JSONObject json;
			    String errorMsg, successMsg;
			    String res;
			    DecimalFormat df = new DecimalFormat("#.##");
				EditText edtcomment;
				String unit_id;
				
				public AllNotesListActivity() {
					super(R.string.notes);
				}
				
				@Override
				public void onCreate(Bundle savedInstanceState) {
					// TODO Auto-generated method stub
					super.onCreate(savedInstanceState);
					setContentView(R.layout.one_lesson_notes);
					
					getOverflowMenu();
					ActionBar actionBar = getSupportActionBar();
				     actionBar.setDisplayHomeAsUpEnabled(true);
				     
				     ActionBarPolicy.get(this).showsOverflowMenuButton();
				     setBehindContentView(R.layout.menu_frame);
				     
				     getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
						setSlidingActionBarEnabled(true);
				     
				     if (savedInstanceState == null) {
							
							getSupportFragmentManager().beginTransaction()
					                 .add(R.id.menu_frame, new SettingsFragment()).commit();
						
						}
				     
					btnAddnotes = (ImageButton) findViewById(R.id.btnAddnotes);
					btnAddnotes.setOnClickListener(new OnClickListener() {			
						@Override
						public void onClick(View v) {
							startActivityForResult(new Intent(getApplicationContext(), AddNoteActivity.class), ENTER_DATA_REQUEST_CODE);
							//showDialog();
						}
					});
					
					
			
					databaseHelper = new NotesDataHelper(getApplicationContext());
					listViewNotes = (ListView) findViewById(android.R.id.list);
					//listViewNotes = (ListView) v.findViewById(R.id.lvnot);
			
					
					listViewNotes.setOnItemClickListener(new OnItemClickListener() {
			
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						
							Log.i( "clicked on item: " + position, "**");
							databaseHelper = new NotesDataHelper(getApplicationContext());
							databaseHelper.remove(id);
							Log.i( "clicked on item: " + position, "**");
						}
					});
			 
			        // Database query can be a time consuming task ..
			        // so its safe to call database query in another thread
			        // Handler, will handle this stuff for you <img src="http://s0.wp.com/wp-includes/images/smilies/icon_smile.gif?m=1129645325g" alt=":)" class="wp-smiley">
			 
			        new Handler().post(new Runnable() {
			            @Override
			            public void run() {
			                customAdapter = new LessonNotesCursorAdapter(getApplicationContext(), databaseHelper.getAllNotesData());
			                listViewNotes.setAdapter(customAdapter);
			            }
			        });
					
					
					
				}
				
				
			
				/**
				* Creates a new instance of our dialog and displays it.
				
				private void showDialog() {
					DialogFragment newFragment = NotesDialogFragment.newInstance(1);
					newFragment.show(getFragmentManager(), "dialog");
				}*/
			    
				    @Override
				    public void onActivityResult(int requestCode, int resultCode, Intent data) {
				
				        super.onActivityResult(requestCode, resultCode, data);
				
				        if (requestCode == ENTER_DATA_REQUEST_CODE ) {
				
				            databaseHelper.insertData(data.getExtras().getString("tag_time"), null, data.getExtras().getString("tag_note"));
				            
				            customAdapter.changeCursor(databaseHelper.getAllNotesData());
				        }
				    }
			

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
	}
						
						
						
