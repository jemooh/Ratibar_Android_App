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

import com.br.timetabler.R;
import com.br.timetabler.model.Lesson;
import com.br.timetabler.model.LessonLibrary;
import com.br.timetabler.service.task.GetLessonsTask;
import com.br.timetabler.util.DatabaseHandler_joe;
import com.br.timetabler.util.FeedbackDialogFragment;
import com.br.timetabler.adapter.LessonCommentCursorAdapter;
import com.br.timetabler.listener.LessonClickListener;
import com.br.timetabler.util.ServerInteractions;
import com.br.timetabler.widget.TodayLessonsListView;


	public class CommentDialogFragment extends DialogFragment {
	
		
		
		String unit_id,userId;
		private static final int COMMENT_DIALOG = 1;
		private static String KEY_SUCCESS = "success";
		AlertDialog alertDialog;
	    HashMap<Integer, Dialog> mDialogs = new HashMap<Integer, Dialog>();
		SaveCommentTask commentTsk;
		ServerInteractions userFunction;
		DatabaseHandler db;
		JSONObject json_user;
	    JSONObject json;
	    String errorMsg, successMsg;
	    String res; 
	    DatabaseHandler dbHandler;
	    private NotesDataHelper databaseHelper;
	    private LessonCommentCursorAdapter commentAdapter;

	    boolean mDualPane;
		
	
		public static CommentDialogFragment newInstance(int id){
			
			CommentDialogFragment dialogFragment = new CommentDialogFragment();
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
				case COMMENT_DIALOG:
			 		inflater = LayoutInflater.from(getActivity());
			 		dialogview = inflater.inflate(R.layout.add_comment, null);
		 
			 		dialogbuilder = new AlertDialog.Builder(getActivity());
			 		//dialogbuilder.setTitle("Write comment..!");
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
			  		case COMMENT_DIALOG:
			  			alertDialog = (AlertDialog) dialog;
			  			Button btn_submit = (Button) alertDialog.findViewById(R.id.btn_submit);
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
			
			  public String giveDateTime() {
			       Calendar cal = Calendar.getInstance();
			       SimpleDateFormat currentDate = new SimpleDateFormat("MMM/dd/yyyy HH:mm ");
			       return currentDate.format(cal.getTime());
			    }
	
			private class SaveCommentTask extends AsyncTask<MyCommentParams, Void, String> {
		        @Override
		        protected String doInBackground(MyCommentParams... params) {
		        	userFunction = new ServerInteractions();
		//
		        	String unit_id = params[0].unit_id;
		        	String commentContent = params[0].commentContent;
		        	db = new DatabaseHandler(getActivity());
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
		                	Toast.makeText(getActivity(), "Successfully added a new comment", Toast.LENGTH_SHORT).show();
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
									
	
	    	
	}
