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
	import android.widget.ImageButton;
	import android.widget.ProgressBar;
	import android.widget.TextView;
	import android.widget.Toast;
	
	import com.br.timetabler.R;
	import com.br.timetabler.model.Lesson;
	import com.br.timetabler.model.LessonLibrary;
	import com.br.timetabler.service.task.GetLessonsTask;
	import com.br.timetabler.util.DatabaseHandler_joe;
	import com.br.timetabler.util.FeedbackDialogFragment;
	import com.br.timetabler.listener.LessonClickListener;
	import com.br.timetabler.util.ServerInteractions;
	import com.br.timetabler.widget.TodayLessonsListView;
	
	
	public class FeedbackDialogFragment extends DialogFragment {
	
		ImageButton btnFeedBack, btn_submit;
		
	
		private static final int FEEDBACK_DIALOG = 1;
		private static String KEY_SUCCESS = "success";
		AlertDialog alertDialog;
	    HashMap<Integer, Dialog> mDialogs = new HashMap<Integer, Dialog>();
		SaveFeedbackTask feedBackTsk;
		ServerInteractions userFunction;
		DatabaseHandler db;
		JSONObject json_user;
	    JSONObject json;
	    String errorMsg, successMsg;
	    String res; 
	    DatabaseHandler dbHandler;
	    boolean mDualPane;
		
	
		public static FeedbackDialogFragment newInstance(int id){
			
			FeedbackDialogFragment dialogFragment = new FeedbackDialogFragment();
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
				case FEEDBACK_DIALOG:
			 		inflater = LayoutInflater.from(getActivity());
			 		dialogview = inflater.inflate(R.layout.add_feedback, null);
		 
			 		dialogbuilder = new AlertDialog.Builder(getActivity());
			 		dialogbuilder.setTitle("SHARE YOUR FEEDBACK!");
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
		  		case FEEDBACK_DIALOG:
		  			alertDialog = (AlertDialog) dialog;
		  			btn_submit = (ImageButton) alertDialog.findViewById(R.id.btn_submit);
		  			ImageButton cancelbutton = (ImageButton) alertDialog.findViewById(R.id.btn_cancel);
		  			final EditText txtAddFeedback = (EditText) alertDialog.findViewById(R.id.txtAddFeedback);
				   
		  			btn_submit.setOnClickListener(new View.OnClickListener() {
		 
		  				@Override
		  				public void onClick(View v) {
		  					String feedbck = txtAddFeedback.getText().toString();
		  					alertDialog.dismiss();
	
		  					//start task
		  	                MyCommentParams params = new MyCommentParams(feedbck);
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
		
	
		private class SaveFeedbackTask extends AsyncTask<MyCommentParams, Void, String> {
	        @Override
	        protected String doInBackground(MyCommentParams... params) {
	        	userFunction = new ServerInteractions();
	
	        	String feedbackContent = params[0].feedbackContent;
	        	
	        	//get user email from database.
	        	db = new DatabaseHandler(getActivity());
	        	HashMap<String,String> user = new HashMap<String,String>();
	        	user = db.getUserDetails();
	        	String email = user.get("email");
	        	
	        	json = userFunction.postFeedback(feedbackContent, email); //100 refers to example user id
	            try {
	                if (json.getString(KEY_SUCCESS) != null) {
	                	errorMsg = "";
	                    res = json.getString(KEY_SUCCESS);
	                    if(Integer.parseInt(res) == 1){
	                    	successMsg = "Successfully sent a feedback, thank you";
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
	                	Toast.makeText(getActivity(), "Successfully sent a feedback", Toast.LENGTH_SHORT).show();
	                	//getCommentsFeed(listView, strVideoId);
	                  	alertDialog.dismiss();
	                }
	        	} catch(Exception e){
					Log.e(this.getClass().getSimpleName(), "Error Saving feedback", e);
				}
	        }        
	    }
		
		private static class MyCommentParams {
	        String email, feedbackContent;
	        MyCommentParams(String feedbackContent) {
	            this.feedbackContent = feedbackContent;
	            
	        }
	    }
	    	
	}
