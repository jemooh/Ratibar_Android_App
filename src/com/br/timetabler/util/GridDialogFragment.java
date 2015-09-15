package com.br.timetabler.util;
import java.util.HashMap;

import org.json.JSONObject;


import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import com.br.timetabler.service.task.GetLessonsTask;
import com.br.timetabler.util.DatabaseHandler_joe;
import com.br.timetabler.util.FeedbackDialogFragment;
import com.br.timetabler.listener.LessonClickListener;
import com.br.timetabler.util.ServerInteractions;
import com.br.timetabler.widget.TodayLessonsListView;


public class GridDialogFragment extends DialogFragment {

	Button btnFeedBack, btn_submit;
	

	private static final int FEEDBACK_DIALOG = 1;
	private static String KEY_SUCCESS = "success";
	AlertDialog alertDialog;
    HashMap<Integer, Dialog> mDialogs = new HashMap<Integer, Dialog>();
	
	ServerInteractions userFunction;
	DatabaseHandler_joe db;
	JSONObject json_user;
    JSONObject json;
    String errorMsg, successMsg;
    String res; 
    DatabaseHandler_joe dbHandler;
    boolean mDualPane;
	

	public static GridDialogFragment newInstance(int id){
		
		GridDialogFragment dialogFragment = new GridDialogFragment();
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
	  			btn_submit = (Button) alertDialog.findViewById(R.id.btn_submit);
	  			Button cancelbutton = (Button) alertDialog.findViewById(R.id.btn_cancel);
	  			final EditText txtAddFeedback = (EditText) alertDialog.findViewById(R.id.txtAddFeedback);
			   
	  			btn_submit.setOnClickListener(new View.OnClickListener() {
	 
	  				@Override
	  				public void onClick(View v) {
	  					String comments = txtAddFeedback.getText().toString();
	  					alertDialog.dismiss();

	  					//start task
	  	                
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
	}}