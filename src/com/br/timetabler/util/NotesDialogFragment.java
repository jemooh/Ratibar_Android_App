package com.br.timetabler.util;

import java.util.HashMap;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.br.timetabler.R;
import com.br.timetabler.model.Lesson;
import com.br.timetabler.model.LessonLibrary;
import com.br.timetabler.service.task.GetLessonsTask;
import com.br.timetabler.util.DatabaseHandler_joe;
import com.br.timetabler.util.MyDialogFragment;
import com.br.timetabler.listener.LessonClickListener;
import com.br.timetabler.util.ServerInteractions;
import com.br.timetabler.widget.TodayLessonsListView;


	public class NotesDialogFragment extends DialogFragment {
	
		
		
		String unit_id,userId;
		private static final int NOTES_DIALOG = 1;
		private static String KEY_SUCCESS = "success";
		AlertDialog alertDialog;
	    HashMap<Integer, Dialog> mDialogs = new HashMap<Integer, Dialog>();
		SaveNotesTask notesTsk;
		ServerInteractions userFunction;
		DatabaseHandler_joe db;
		JSONObject json_user;
	    JSONObject json;
	    String errorMsg, successMsg;
	    String res; 
	    DatabaseHandler_joe dbHandler;
	    boolean mDualPane;
		
	
		public static MyDialogFragment newInstance(int id){
			
			MyDialogFragment dialogFragment = new MyDialogFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("id", id);
			dialogFragment.setArguments(bundle);
			
			return dialogFragment;
		}
	@Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {	
		int id =getArguments().getInt("id");	
			LayoutInflater inflater;
			AlertDialog.Builder dialogbuilder;
			View dialogview;
			AlertDialog dialogDetails = null;		 
			switch (id) {
				case NOTES_DIALOG:
			 		inflater = LayoutInflater.from(getActivity());
			 		dialogview = inflater.inflate(R.layout.add_notes, null);
		 
			 		dialogbuilder = new AlertDialog.Builder(getActivity());
			 		dialogbuilder.setTitle("ADD YOUR NOTES!");
			 		//dialogbuilder.setIcon(R.drawable.reviews);
			 		dialogbuilder.setView(dialogview);
			 		dialogDetails = dialogbuilder.create();	  
			 	break;
			 }	 
		  return dialogDetails;
		 }
	
		
			
			@Override
			public void onResume(){
				super.onResume();
				
				
				Dialog dialog =getDialog();
				int id =getArguments().getInt("id");
				  	switch (id) {
				  	
				  	case NOTES_DIALOG:
			  			alertDialog = (AlertDialog) dialog;
			  			Button btnShareNotes = (Button) alertDialog.findViewById(R.id.btn_share);
			  			Button btnSaveNotes = (Button) alertDialog.findViewById(R.id.btn_save);
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
				  	
				  	/*
				  		case NOTES_DIALOG:
				  			alertDialog = (AlertDialog) dialog;
				  			btn_submit = (Button) alertDialog.findViewById(R.id.btn_submit);
				  			Button cancelbutton = (Button) alertDialog.findViewById(R.id.btn_cancel);
				  			final EditText txtAddFeedback = (EditText) alertDialog.findViewById(R.id.txtAddFeedback);
						   
				  			btn_submit.setOnClickListener(new View.OnClickListener() {
				 
				  				@Override
				  				public void onClick(View v) {
				  					String comments = txtAddFeedback.getText().toString();
				  					alertDialog.dismiss();
			
				  					//start task
				  	                MyCommentParams params = new MyCommentParams(comments);
				  	                feedBackTsk = new SaveFeedbackTask();
				  	                feedBackTsk.execute(params);
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
			*/
	
			private class SaveNotesTask extends AsyncTask<MyNotesParams, Void, String> {
		        @Override
		        protected String doInBackground(MyNotesParams... params) {
		        	userFunction = new ServerInteractions();
		//
		        	String unit_id = params[0].unit_id;
		        	String notesContent = params[0].notesContent;
		        	db = new DatabaseHandler_joe(getActivity());
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
		                	Toast.makeText(getActivity(), "Successfully shared a new notes", Toast.LENGTH_SHORT).show();
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
