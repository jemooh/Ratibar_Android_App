package com.br.timetabler.src;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.br.timetabler.R;
import com.br.timetabler.adapter.GridAdapter;
import com.br.timetabler.model.Lesson;
import com.br.timetabler.model.LessonLibrary;
import com.br.timetabler.model.OneCell;
import com.br.timetabler.service.task.GetLessonsTask;
import com.br.timetabler.util.DatabaseHandler;
import com.br.timetabler.listener.LessonClickListener;
import com.br.timetabler.util.ServerInteractions;
import com.jess.ui.TwoWayAdapterView;
import com.jess.ui.TwoWayAdapterView.OnItemClickListener;
import com.jess.ui.TwoWayGridView;
//import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends SherlockActivity implements LessonClickListener {
	private TwoWayGridView gridView;
	int startTime=815, endTime=1715, duration=100;
    int totalCells;
    int learningDays = 6;
    List<OneCell> gridCells;
    List<Lesson> lessons;
	private LessonClickListener lessonClickListener;
	TextView txtMon, txtTue, txtWed, txtThu, txtFri;
	ServerInteractions server;
	DatabaseHandler dbHandler;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		server = new ServerInteractions();
		dbHandler = new DatabaseHandler(this);
		
		//I used this to help in fixing authentication by loging out the user upon loading the app
		//server.logoutUser(getApplicationContext());
        
        try {         
        	dbHandler.openDataBase();         
        }catch(SQLException sqle){         
        	throw sqle;         
        }
        dbHandler.close();
        
        
		if(server.isUserLoggedIn(getApplicationContext())){
	        setContentView(R.layout.activity_main);
			
			txtMon = (TextView) findViewById(R.id.txtMon);
			txtTue = (TextView) findViewById(R.id.txtTue);
			txtWed = (TextView) findViewById(R.id.txtWed);
			txtThu = (TextView) findViewById(R.id.txtThu);
			txtFri = (TextView) findViewById(R.id.txtFri);
			
			txtMon.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					openDayLessons("1", "Monday");				
				}
			});
			
			txtTue.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					openDayLessons("2", "Tuesday");				
				}
			});
			
			txtWed.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					openDayLessons("3", "Wednesday");				
				}
			});
			
			txtThu.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					openDayLessons("4", "Thursday");				
				}
			});
			
			txtFri.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					openDayLessons("5", "Friday");				
				}
			});
			
			gridView = (TwoWayGridView) findViewById(R.id.gridview);
			UpdateGrid(); 
			getLessonsFeed(gridView);
		} else {
            // user is not logged in show login screen
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Closing dashboard screen
            finish();
       }
	}
	
	private void openDayLessons(String dayId, String dayTitle) {
		Intent si = new Intent(getApplicationContext(), ListDayLessons.class);
        Bundle b=new Bundle();        
        b.putString("dayId", dayId);        
        b.putString("dayTitle", dayTitle);        
        si.putExtras(b);
        startActivity(si);
	}
	// This is the XML onClick listener to retreive a users video feed
    public void getLessonsFeed(View v){
        // We start a new task that does its work on its own thread
        // We pass in a handler that will be called when the task has finished
        new Thread(new GetLessonsTask(responseHandler, "0", false, null)).start();
    }
    
    // This is the handler that receives the response when the YouTube task has finished

    @SuppressLint("HandlerLeak")
	Handler responseHandler = new Handler() {
        public void handleMessage(Message msg) {
        	populateListWithLessons(msg);
        };
    };
    
    /**
     * This method retrieves the Library of videos from the task and passes them to our ListView
     * @param msg
     */
    private void populateListWithLessons(Message msg) {
        // Retreive the videos are task found from the data bundle sent back
        final LessonLibrary lib = (LessonLibrary) msg.getData().get(GetLessonsTask.LIBRARY);
        // Because we have created a custom ListView we don't have to worry about setting the adapter in the activity
        // we can just call our custom method with the list of items we want to display
        lessons = lib.getLessons();
        GridAdapter adapter = new GridAdapter(this, gridCells);
        
        gridView.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(TwoWayAdapterView parent, View v, int position, long id) {
				int lessonPos = getLessonPos(position);
        		onLessonClicked(lessons.get(lessonPos));        		
			}
		});
        ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.GONE);
        gridView.setAdapter(adapter);
        adapter.setLessons(lessons);
        adapter.notifyDataSetChanged();
    }
    
    
    @Override
    protected void onStop() {
        // Make sure we null our handler when the activity has stopped
        // because who cares if we get a callback once the activity has stopped? not me!
        responseHandler = null;
        super.onStop();
    }
   
    
    public void setOnLessonClickListener(LessonClickListener l) {
    	lessonClickListener = l;
    }
    
    // This is the interface method that is called when a video in the listview is clicked!
    // The interface is a contract between this activity and the listview
    @Override
    public void onLessonClicked(Lesson lesson) {
    	String unit_id = lesson.getLessonId();
    	String starttime = lesson.getStarttime();
    	String endtime = lesson.getEndtime();
    	String code = lesson.getCode();
    	String title = lesson.getTitle();
    	String teacher = lesson.getTeacher();
    	String location = lesson.getLocation();
                
        Intent si = new Intent(getApplicationContext(), SingleLessonActivity.class);
        Bundle b=new Bundle();
        
        b.putString("unit_id", unit_id);
        b.putString("starttime", starttime);
        b.putString("endtime", endtime);
        b.putString("code", code);
        b.putString("title", title);
        b.putString("teacher", teacher);
        b.putString("location", location);
        
        si.putExtras(b);
        startActivity(si);
		
	}
    
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
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
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
	
	public void UpdateGrid() {
		this.totalCells = ((endTime - startTime) / duration) * learningDays;
		gridCells = new ArrayList<OneCell>();
		for(int i=0; i<totalCells; i++) {
			gridCells.add(new OneCell(i, i));
		}
	}
	
	private int getLessonPos(int pos){ 
    	int i=0;
    	int z = 0;
    	for(Lesson l : lessons) {
    		String startTime = l.getStarttime();
    		String endTime = l.getEndtime(); 
    		int durTotal = Integer.parseInt(endTime) - Integer.parseInt(startTime);
    		int reps = durTotal/100;
    		int dbPos = Integer.parseInt(l.GetyPos());
    		int extPos = dbPos + (6*(reps-1));
    		int j = Integer.parseInt(l.GetyPos());
    		
    		if(pos==j || pos==extPos){
    			z = i;
    		}
    		i++;
    	}
    	return z;
    }
}
	