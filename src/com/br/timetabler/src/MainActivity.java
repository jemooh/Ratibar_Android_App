package com.br.timetabler.src;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.br.timetabler.R;
import com.br.timetabler.adapter.GridAdapter;
import com.br.timetabler.model.Lesson;
import com.br.timetabler.model.LessonLibrary;
import com.br.timetabler.model.OneCell;
import com.br.timetabler.service.task.GetLessonsTask;
import com.br.timetabler.util.LessonClickListener;

import com.jess.ui.TwoWayAdapterView;
import com.jess.ui.TwoWayAdapterView.OnItemClickListener;
import com.jess.ui.TwoWayGridView;

public class MainActivity extends SherlockActivity implements LessonClickListener {
	private TwoWayGridView gridView;
	int startTime=700, endTime=1900, duration=100;
    int totalCells;
    int learningDays = 5;
    List<OneCell> gridCells;
    List<Lesson> lessons;
	private LessonClickListener lessonClickListener;
	TextView txtMon, txtTue, txtWed, txtThu, txtFri;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		txtMon = (TextView) findViewById(R.id.txtMon);
		txtTue = (TextView) findViewById(R.id.txtTue);
		txtWed = (TextView) findViewById(R.id.txtWed);
		txtThu = (TextView) findViewById(R.id.txtThu);
		txtFri = (TextView) findViewById(R.id.txtFri);
		
		txtMon.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				openDayLessons("1");				
			}
		});
		
		txtTue.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				openDayLessons("2");				
			}
		});
		
		txtWed.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				openDayLessons("3");				
			}
		});
		
		txtThu.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				openDayLessons("4");				
			}
		});
		
		txtFri.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				openDayLessons("5");				
			}
		});
		
		gridView = (TwoWayGridView) findViewById(R.id.gridview);
		UpdateGrid(); 
		getLessonsFeed(gridView);
	}
	
	private void openDayLessons(String dayId) {
		Intent si = new Intent(getApplicationContext(), ListDayLessons.class);
        Bundle b=new Bundle();        
        b.putString("dayId", dayId);        
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
        /*gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			   //Toast.makeText(getApplicationContext(), ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
			   if(lessonClickListener != null){
		    		lessonClickListener.onLessonClicked(lib.getLessons().get(position));
		        }
			}
		});*/
        gridView.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(TwoWayAdapterView parent, View v, int position, long id) {
				
				//Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
				//Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				//startActivity(intent);
        		Log.i("position", "poslition: " + position);
        		int lessonPos = getLessonPos(position);
        		Log.i("getLessonPos", "getLessonPos: " + lessonPos);
        		onLessonClicked(lessons.get(lessonPos));
        		
				/*for(Lesson l : lessons) {
					if(Integer.parseInt(l.GetyPos())==position){
						Log.i("", "position: " + position + " | yPos : " + l.GetyPos());
						onLessonClicked(lessons.get(Integer.parseInt(l.GetyPos())));
					}
				}*/
			}
		});
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
    	String jCode=null;
    	for(Lesson l : lessons) {
    		int j = Integer.parseInt(l.GetyPos());
    		if(pos==j){
    			//jCode = l.getCode();
    			//i = lessons.;
    			z = i;
    		}
    		i++;
    	}
    	return z;
    }
}
	