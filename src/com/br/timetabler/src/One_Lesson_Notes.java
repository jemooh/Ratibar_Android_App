package com.br.timetabler.src;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import com.br.timetabler.R;
import com.br.timetabler.model.AssignmentLibrary;
import com.br.timetabler.model.Comment;
import com.br.timetabler.model.CommentLibrary;
import com.br.timetabler.util.NotesDataHelper;
import com.br.timetabler.util.PersonalDatabaseHelper;
import com.br.timetabler.service.task.GetAssignmentsTask;
import com.br.timetabler.service.task.GetCommentsTask;
import com.br.timetabler.listener.CommentClickListener;
import com.br.timetabler.util.DatabaseHandler_joe;
import com.br.timetabler.util.ServerInteractions;
import com.br.timetabler.widget.AssignmentsListView;
import com.br.timetabler.widget.CommentsListView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

public class One_Lesson_Notes extends ActionBarActivity  {

	String unit_id,userId;
	AlertDialog alertDialog;
	Button btnSaveNotes, btnAddnotes,btnShareNotes;
	ListView listViewNotes;
	private static final int NOTES_DIALOG = 1;
	private static final int SHARE_DIALOG = 2;
	private static final int LOGIN_DIALOG = 3;
	private static String KEY_SUCCESS = "success";
	SaveNotesTask notesTsk;
	ServerInteractions userFunction;
	DatabaseHandler_joe db;
	JSONObject json_user;
    JSONObject json;
    String errorMsg, successMsg;
    String res;
    DecimalFormat df = new DecimalFormat("#.##");
	EditText edtcomment;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.one_lesson_notes);
		
		
		

		
		//btnAddnotes = (Button) findViewById(R.id.btnAddnotes);
		btnAddnotes = (Button) findViewById(R.id.btnAddnotes);
		btnAddnotes.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				showDialog(NOTES_DIALOG);
			}
		});
		
		
		
		
	}
		/**commentsLstView = (CommentsListView) findViewById(R.id.commentsListView);
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
		*/
	
		
		
		

	    
	    @Override
		protected Dialog onCreateDialog(int id) {	
			LayoutInflater inflater;
			AlertDialog.Builder dialogbuilder;
			View dialogview;
			AlertDialog dialogDetails = null;		 
			switch (id) {
				case NOTES_DIALOG:
			 		inflater = LayoutInflater.from(this);
			 		dialogview = inflater.inflate(R.layout.add_notes, null);
		 
			 		dialogbuilder = new AlertDialog.Builder(this);
			 		dialogbuilder.setTitle("ADD YOUR NOTES!");
			 		//dialogbuilder.setIcon(R.drawable.reviews);
			 		dialogbuilder.setView(dialogview);
			 		dialogDetails = dialogbuilder.create();	  
			 	break;
			 }	 
		  return dialogDetails;
		 }
		
		@Override
		protected void onPrepareDialog(int id, Dialog dialog) {
		 
		  	switch (id) {
		  		case NOTES_DIALOG:
		  			alertDialog = (AlertDialog) dialog;
		  			btnShareNotes = (Button) alertDialog.findViewById(R.id.btn_share);
		  			btnSaveNotes = (Button) alertDialog.findViewById(R.id.btn_save);
		  			Button cancelbutton = (Button) alertDialog.findViewById(R.id.btn_cancel);
		  			final EditText txtAddNotes = (EditText) alertDialog.findViewById(R.id.txtAddNotes);
				   
		  			btnShareNotes.setOnClickListener(new View.OnClickListener() {
		 
		  				@Override
		  				public void onClick(View v) {
		  					String notes = txtAddNotes.getText().toString();
		  					alertDialog.dismiss();
	
		  					
		  					//start task
		  	                MyNotesParams params = new MyNotesParams(unit_id, notes);
		  	              notesTsk = new SaveNotesTask();
		  	              notesTsk.execute(params);
		  				}
		  			});
		 
		  			btnSaveNotes.setOnClickListener(new View.OnClickListener() {
		  				 
		  				@Override
		  				public void onClick(View v) {
		  					String notes = txtAddNotes.getText().toString();
		  					alertDialog.dismiss();
	
		  					
		  					//start task
		  	                MyNotesParams params = new MyNotesParams(unit_id, notes);
		  	              notesTsk = new SaveNotesTask();
		  	              notesTsk.execute(params);
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
		private class SaveNotesTask extends AsyncTask<MyNotesParams, Void, String> {
	        @Override
	        protected String doInBackground(MyNotesParams... params) {
	        	userFunction = new ServerInteractions();
	//
	        	String unit_id = params[0].unit_id;
	        	String notesContent = params[0].notesContent;
	        	db = new DatabaseHandler_joe(getApplicationContext());
	        	HashMap<String,String> user = new HashMap<String,String>();
	        	user = db.getUserDetails();
	        	String userId = user.get("uid");
	    //james    	//json = userFunction.postNotes(notesContent, userId, unit_id); //100 refers to example user id
	            try {
	                if (json.getString(KEY_SUCCESS) != null) {
	                	errorMsg = "";
	                    res = json.getString(KEY_SUCCESS);
	                    if(Integer.parseInt(res) == 1){
	                    	successMsg = "Successfully shared a new notes";
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
	                	Toast.makeText(getApplicationContext(), "Successfully shared a new notes", Toast.LENGTH_SHORT).show();
	                	//getCommentsFeed(listView, strVideoId);
	                  	alertDialog.dismiss();
	                }
	        	} catch(Exception e){
					Log.e(this.getClass().getSimpleName(), "Error Saving Notes", e);
				}
	        }        
	    }
		
		private static class MyNotesParams {
	        String userId, unit_id, notesContent;
	        
	
	        MyNotesParams(String unit_id, String notesContent) {
	            this.unit_id = unit_id;
	            this.notesContent = notesContent;
	            
	        }
	    }
								
}
	/**
	
	
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
	    }*/
