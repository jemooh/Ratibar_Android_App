package com.br.timetabler.src;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.br.timetabler.R;
import com.br.timetabler.model.AssignmentLibrary;
import com.br.timetabler.model.Comment;
import com.br.timetabler.model.CommentLibrary;
import com.br.timetabler.util.NotesDataHelper;
import com.br.timetabler.util.PersonalDatabaseHelper;
import com.br.timetabler.service.task.GetAssignmentsTask;
import com.br.timetabler.service.task.GetCommentsTask;
import com.br.timetabler.adapter.TabsPagerAdapter;
import com.br.timetabler.listener.CommentClickListener;
import com.br.timetabler.util.DatabaseHandler_joe;
import com.br.timetabler.util.ServerInteractions;
import com.br.timetabler.widget.AssignmentsListView;
import com.br.timetabler.widget.CommentsListView;

import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.view.ViewPager;


public class SingleLessonActivity extends SherlockFragmentActivity implements ActionBar.TabListener {  
	public static String unit_id,userId;
	String code;
	String title;
	String startTime, endTime;
	String teacher;
	String location;
	AlertDialog alertDialog;
	Button btn_submit, btnAddnotes,btnAddComments;
	PersonalDatabaseHelper databaseHelper;
	CommentsListView commentsLstView;
	AssignmentsListView listView;
	ListView listViewNotes;
	private static final int ENTER_DATA_REQUEST_CODE = 1;
	NotesDataHelper databaseHelper2;
	private static final int COMMENT_DIALOG = 1;
	private static final int SHARE_DIALOG = 2;
	private static final int LOGIN_DIALOG = 3;
	private static String KEY_SUCCESS = "success";
	//SaveCommentTask commentTsk;
	ServerInteractions userFunction;
	DatabaseHandler_joe db;
	JSONObject json_user;
    JSONObject json;
    String errorMsg, successMsg;
    String res;
    DecimalFormat df = new DecimalFormat("#.##");
	EditText edtcomment;
	    private ViewPager viewPager;
	    private TabsPagerAdapter mAdapter;
	    private ActionBar actionBar;
	    // Tab titles
	    private String[] tabs = { "Comments", "Assignments", "Downloads" ,"My Notes" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.single_lesson);
		
		databaseHelper = new PersonalDatabaseHelper(this);
		Intent in=getIntent();
		Bundle b=in.getExtras();
		
		this.unit_id = b.getString("unit_id");
		this.startTime = b.getString("starttime");
		this.endTime = b.getString("endtime");
		this.code = b.getString("code");
		this.title = b.getString("title");
		this.teacher = b.getString("teacher");
		this.location = b.getString("location");
		
		TextView txtCode = (TextView) findViewById(R.id.txtSCode);
		TextView txtTitle = (TextView) findViewById(R.id.txtSTitle);
		TextView txtTeacher = (TextView) findViewById(R.id.txtSTeacher);
		TextView txtLocation = (TextView) findViewById(R.id.txtSLocation);
		
		
		txtCode.setText(code + " : " + createTime(startTime, endTime));
		txtTitle.setText(title);
		txtTeacher.setText(teacher);
		txtLocation.setText(location);
		
		 // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getSupportActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
 
        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);       
 
        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }
 
        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
 
            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }
 
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
 
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }
 
    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }
 
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }
 
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }
		
  /**
		
		TabHost tabHost = getTabHost();
		   
		   // Tab for list of day lessons
		   View tabView = createTabView(this, "comments");
		   TabSpec comspec = tabHost.newTabSpec("comments");
		   comspec.setIndicator(tabView);
		   Intent gridIntent = new Intent(this, One_Lesson_Comments.class);
		   comspec.setContent(gridIntent);
		   
		   
		   // Tab for week
		   tabView = createTabView(this, "Assignments");
		   TabSpec assignspec = tabHost.newTabSpec("Assignments");
		   // setting Title and Icon for the Tab
		   assignspec.setIndicator(tabView);
		   Intent assignIntent = new Intent(this, One_Lesson_Assignment.class);
		   assignspec.setContent(assignIntent);
		   
		   // Tab for week
		   tabView = createTabView(this, "Downloads");
		   TabSpec downspec = tabHost.newTabSpec("Downloads");
		   // setting Title and Icon for the Tab
		   downspec.setIndicator(tabView);
		   Intent downIntent = new Intent(this, One_Lesson_Notes.class);
		   downspec.setContent(downIntent);
		   
		   // Tab for week
		   tabView = createTabView(this, "Notes");
		   TabSpec notspec = tabHost.newTabSpec("Notes");
		   // setting Title and Icon for the Tab
		   notspec.setIndicator(tabView);
		   Intent notIntent = new Intent(this, One_Lesson_Notes.class);
		   notspec.setContent(notIntent);
		   
		 	   // Adding all TabSpec to TabHost
		   tabHost.addTab(comspec); // Adding grid tab
		   tabHost.addTab(assignspec); // Adding day tab
		   tabHost.addTab(downspec); // Adding grid tab
		   tabHost.addTab(notspec); // Adding day tab
		   
		  
		}
	
    private static View createTabView(Context context, String tabText) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null, false);
        TextView tv = (TextView) view.findViewById(R.id.tabTitleText);
        tv.setText(tabText);
        return view;
    }
	
	*/
	
	
		/**
		//btnAddnotes = (Button) findViewById(R.id.btnAddnotes);
		btnAddComments = (Button) findViewById(R.id.btnAddComments);
		edtcomment=(EditText)findViewById(R.id.edtcomments);
		btnAddComments.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				showDialog(COMMENT_DIALOG);
			}
		});
		commentsLstView = (CommentsListView) findViewById(R.id.commentsListView);
		getCommentsFeed(commentsLstView);
		
		listView=  (AssignmentsListView) findViewById(R.id.assignmentsListView);
		getAssignmentsFeed(listView);
		
		
		btnAddComments.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				
			
		db = new DatabaseHandler_joe(getApplicationContext());
    	HashMap<String,String> user = new HashMap<String,String>();
    	user = db.getUserDetails();
    	String userId = user.get("name");
    	String comment= edtcomment.getText().toString();
	  	String time = "May 26, 2013, 13:35";
				//	alertDialog.dismiss();

					//start task
       databaseHelper.insertData(userId,time, comment);

       customAdapter.changeCursor(databaseHelper.getAllData());
       edtcomment.setText("");
		    Log.i("*******james*******", time);
		    
			}
		});
		
		
		
		
		
		
		
		databaseHelper2 = new NotesDataHelper(this);
		 
		listViewNotes = (ListView) findViewById(R.id.listViewnotes);
        // Database query can be a time consuming task ..
        // so its safe to call database query in another thread
        // Handler, will handle this stuff for you <img src="http://s0.wp.com/wp-includes/images/smilies/icon_smile.gif?m=1129645325g" alt=":)" class="wp-smiley">
 
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                notesAdapter = new NotesAdapter(SingleLessonActivity.this, databaseHelper2.getAllData());
                listViewNotes.setAdapter(notesAdapter);
            }
        });
		
	}
		
		   // Database query can be a time consuming task ..
		   // so its safe to call database query in another thread
		   // Handler, will handle this stuff for you <img src="http://s0.wp.com/wp-includes/images/smilies/icon_smile.gif?m=1129645325g" alt=":)" class="wp-smiley">

		   new Handler().post(new Runnable() {
		       @Override
		       public void run() {
		           customAdapter = new CustomCursorAdapter(SingleLessonActivity.this, databaseHelper.getAllData());
		           commentsLstView .setAdapter(customAdapter);
		       }
		   });
		
		
	}
	*/
		public String createTime(String start, String end) {
			String pn= "am";
			double a = Float.parseFloat(start)/100; 
			double c = Float.parseFloat(end)/100; 
	    	
			if(a>12){
	    		pn= "pm";
	    		a = a-12;
	    		if(a<1)
	    			a=a+1;
	    	} 
			if(c>12){
	    		pn= "pm";
	    		c = c-12;
	    		if(c<0.59)
	    			c=c+1;
	    	} 
	    	String m = df.format(a) + " - " + df.format(c) + pn;
	    	m = m.replaceAll("[.]", ":");
	        return m;
		}
		
		
		
		
		
			/**
			// This is the XML onClick listener to retreive a users video feed
		    public void getCommentsFeed(View v){
		        // We start a new task that does its work on its own thread
		        // We pass in a handler that will be called when the task has finished
		        // We also pass in the name of the user we are searching YouTube for
		        new Thread(new GetCommentsTask(responseHandler, unit_id, false, null)).start();
		    }
		    
		    
		    
		    
		    // This is the handler that receives the response when the YouTube task has finished
		
		    @SuppressLint("HandlerLeak")
			Handler responseHandler = new Handler() {
		        public void handleMessage(Message msg) {
		        	populateListWithComments(msg);
		        };
		    };
		    */
		    
		    /** This method retrieves the Library of videos from the task and passes them to our ListView
		     * @param ms
		     
		    private void populateListWithComments(Message msg) {
		        // Retreive the videos are task found from the data bundle sent back
		        CommentLibrary lib = (CommentLibrary) msg.getData().get(GetCommentsTask.LIBRARY);
		        // Because we have created a custom ListView we don't have to worry about setting the adapter in the activity
		        // we can just call our custom method with the list of items we want to display
		        ProgressBar pbpp = (ProgressBar) findViewById(R.id.pbppl);
		        TextView txtMsg = (TextView) findViewById(R.id.progressMsg2);
				pbpp.setVisibility(View.GONE);
				txtMsg.setVisibility(View.GONE);
				commentsLstView.setComments(lib.getComments());
		    }
		    
		    
		    
		    
		    
			// This is the XML onClick listener to retreive a users video feed
		    public void getAssignmentsFeed(View v){
		        // We start a new task that does its work on its own thread
		        // We pass in a handler that will be called when the task has finished
		        // We also pass in the name of the user we are searching YouTube for
		        new Thread(new GetAssignmentsTask(responseHandler2, userId, unit_id, false, null)).start();
		    }
		    
		    // This is the handler that receives the response when the YouTube task has finished
		
		    @SuppressLint("HandlerLeak")
			Handler responseHandler2 = new Handler() {
		        public void handleMessage(Message msg) {
		        	populateListWithAssignments(msg);
		        };
		    };
		    */
		    
		    
		    
		    /**
		     * This method retrieves the Library of videos from the task and passes them to our ListView
		     * @param msg
		     
		    private void populateListWithAssignments(Message msg) {
		        // Retreive the videos are task found from the data bundle sent back
		    	AssignmentLibrary lib = (AssignmentLibrary) msg.getData().get(GetAssignmentsTask.LIBRARY);
		        // Because we have created a custom ListView we don't have to worry about setting the adapter in the activity
		        // we can just call our custom method with the list of items we want to display
		        ProgressBar pbpp = (ProgressBar) findViewById(R.id.pbpplasignmnt);
		        TextView txtMsg = (TextView) findViewById(R.id.progressMsg3);
				pbpp.setVisibility(View.GONE);
				txtMsg.setVisibility(View.GONE);
		        listView.setAssignments(lib.getAssignments());
				if(lib.getAssignments().isEmpty()){
					txtMsg.setText("No Asignments for today");			
				} else {
					txtMsg.setVisibility(View.GONE);
			        listView.setAssignments(lib.getAssignments());
				}
				
		    }
		    
		  
			
			@Override
		    protected void onStop() {
		        // Make sure we null our handler when the activity has stopped
		        // because who cares if we get a callback once the activity has stopped? not me!
		        responseHandler = null;
		        super.onStop();
		    }
		    
		    // This is the interface method that is called when a comment in the listview is clicked!
		    // The interface is a contract between this activity and the listview
		    @Override
			public void onCommentClicked(Comment comment) {
				String comments = comment.getComment();
		    	//display popup with OKOnly Buttons
			}
		    
		    
		    
		    
		    
		    public void onClickAdd(View btnAdd) {
		    	 
		        startActivityForResult(new Intent(this, Notes.class), ENTER_DATA_REQUEST_CODE);
		 
		    }
		 
		    @Override
		    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		        super.onActivityResult(requestCode, resultCode, data);
		
		        if (requestCode == ENTER_DATA_REQUEST_CODE && resultCode == RESULT_OK) {
		
		            databaseHelper2.insertData(data.getExtras().getString("tag_lact"), data.getExtras().getString("tag_notes"), data.getExtras().getString("tag_unit"));
		
		            notesAdapter.changeCursor(databaseHelper2.getAllData());
		        }
		    }
		    
		
		    
		    @Override
			protected Dialog onCreateDialog(int id) {	
				LayoutInflater inflater;
				AlertDialog.Builder dialogbuilder;
				View dialogview;
				AlertDialog dialogDetails = null;		 
				switch (id) {
					case COMMENT_DIALOG:
				 		inflater = LayoutInflater.from(this);
				 		dialogview = inflater.inflate(R.layout.add_comment, null);
			 
				 		dialogbuilder = new AlertDialog.Builder(this);
				 		dialogbuilder.setTitle("ADD YOUR NOTES!");
				 		//dialogbuilder.setIcon(R.drawable.reviews);
				 		dialogbuilder.setView(dialogview);
				 		dialogDetails = dialogbuilder.create();	  
				 	break;
				 	case LOGIN_DIALOG:
				 		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
				 		// set title
						alertDialogBuilder.setTitle("LOGIN REQUIRED");
				 
						// set dialog message
						alertDialogBuilder
							.setMessage("Login is required to add comments. Click yes to login/register")
							.setCancelable(false)
							.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked, close
									/*Intent login = new Intent(getApplicationContext(), LoginActivity.class);
						            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						            startActivity(login);
						            finish();
								}
							  })
							.setNegativeButton("No",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									//do nothing
									dialog.cancel();
								}
							});
				 			AlertDialog alertDialog = alertDialogBuilder.create();		
							alertDialog.show();
						
				 		
							
				 	break;
				 }	 
			  return dialogDetails;
			 }*/
			/**
			@Override
			protected void onPrepareDialog(int id, Dialog dialog) {
			 
			  	switch (id) {
			  		case COMMENT_DIALOG:
			  			alertDialog = (AlertDialog) dialog;
			  			btn_submit = (Button) alertDialog.findViewById(R.id.btn_submit);
			  			Button cancelbutton = (Button) alertDialog.findViewById(R.id.btn_cancel);
			  			final EditText txtAddComments = (EditText) alertDialog.findViewById(R.id.txtAddComments);
					   
			  			btn_submit.setOnClickListener(new View.OnClickListener() {
			 
			  				@Override
			  				public void onClick(View v) {
			  					String comments = txtAddComments.getText().toString();
			  					alertDialog.dismiss();
		
			  					
			  					//start task
			  	                MyCommentParams params = new MyCommentParams(unit_id, comments);
			  	                commentTsk = new SaveCommentTask();
			  	                commentTsk.execute(params);
			  				}
			  			});
			 
			  			cancelbutton.setOnClickListener(new View.OnClickListener() {
			 
			  				@Override
			  				public void onClick(View v) {
			  					alertDialog.dismiss();
			  				}
			  			});
			  			break;
			  	}
			}
			private class SaveCommentTask extends AsyncTask<MyCommentParams, Void, String> {
		        @Override
		        protected String doInBackground(MyCommentParams... params) {
		        	userFunction = new ServerInteractions();
		//
		        	String unit_id = params[0].unit_id;
		        	String commentContent = params[0].commentContent;
		        	db = new DatabaseHandler_joe(getApplicationContext());
		        	HashMap<String,String> user = new HashMap<String,String>();
		        	user = db.getUserDetails();
		        	String userId = user.get("uid");
		        	json = userFunction.postComment(commentContent, userId, unit_id); //100 refers to example user id
		            try {
		                if (json.getString(KEY_SUCCESS) != null) {
		                	errorMsg = "";
		                    res = json.getString(KEY_SUCCESS);
		                    if(Integer.parseInt(res) == 1){
		                    	successMsg = "Successfully added a new comment";
		                    }else{
		                        // Error in login
		                    	successMsg = "Something went wrong, please verify your sentence";
		                    }
		                }
		            } catch (JSONException e) {
		                e.printStackTrace();
		            }
					return successMsg; 
		        }
		        
		        @Override
		        protected void onPostExecute(String json_user) {        	
		        	try {
		        		if(isCancelled())        	
						return;
		        		
		        		if(Integer.parseInt(res) == 1){
		                	Toast.makeText(getApplicationContext(), "Successfully added a new comment", Toast.LENGTH_SHORT).show();
		                	//getCommentsFeed(listView, strVideoId);
		                  	alertDialog.dismiss();
		                }
		        	} catch(Exception e){
						Log.e(this.getClass().getSimpleName(), "Error Saving Comments", e);
					}
		        }        
		    }
			
			private static class MyCommentParams {
		        String userId, unit_id, commentContent;
		        
		
		        MyCommentParams(String unit_id, String commentContent) {
		            this.unit_id = unit_id;
		            this.commentContent = commentContent;
		            
		        }
		    }
			*/
			
			@Override
			public boolean onCreateOptionsMenu(Menu menu) {
				// Inflate the menu; this adds items to the action bar if it is present.
				getSupportMenuInflater().inflate(R.menu.activity_main, menu);
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
		            	Intent i1 = new Intent(getApplicationContext(), MainActivity.class);
		            	i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		                startActivity(i1);
		                finish();
		                break;
		
		            case R.id.menu_list: //display description
		            	Intent i2 = new Intent(getApplicationContext(), ListDayLessons.class);
		            	i2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		                startActivity(i2);
		                finish();
		                break;
		
		            case R.id.menu_assignments: //display reviews
		            	Intent i3 = new Intent(getApplicationContext(), AssignmentsListActivity.class);
		                startActivity(i3);
		                //finish();
		                break;
		            
		            case R.id.menu_settings: //display reviews
		            	Intent i4 = new Intent(getApplicationContext(), Preferences.class);
		                startActivity(i4);
		                //finish();
		                break;
		            
		            
		        }
		        return super.onOptionsItemSelected(item);
		    }

	
		}
