package com.br.timetabler.util;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.br.timetabler.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MyAlertDialog extends DialogFragment {

    private static final String KEY_SAVE_RATING_BAR_VALUE = "KEY_SAVE_RATING_BAR_VALUE";

	Button btnFeedBack, btn_submit;
	

	private static final int FEEDBACK_DIALOG = 1;
	private static String KEY_SUCCESS = "success";
	AlertDialog alertDialog;
    HashMap<Integer, Dialog> mDialogs = new HashMap<Integer, Dialog>();
	SaveFeedbackTask feedBackTsk;
	ServerInteractions userFunction;
	DatabaseHandler_joe db;
	JSONObject json_user;
    JSONObject json;
    String errorMsg, successMsg;
    String res; 
    DatabaseHandler_joe dbHandler;
    boolean mDualPane;

    public static MyAlertDialog newInstance() {
    	MyAlertDialog frag = new MyAlertDialog();
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.add_feedback, null);

        alertDialogBuilder.setView(view);
        alertDialogBuilder.setTitle(getString(R.string.dialog_title));
        final EditText txtAddFeedback = (EditText) view.findViewById(R.id.txtAddFeedback);
        alertDialogBuilder.setPositiveButton(getString(R.string.dialog_positive_text), new DialogInterface.OnClickListener() {
           
        	@Override
            public void onClick(DialogInterface dialog, int which) {
  					String comments = txtAddFeedback.getText().toString();
  					alertDialog.dismiss();

  					//start task
  	                MyCommentParams params = new MyCommentParams(comments);
  	                feedBackTsk = new SaveFeedbackTask();
  	                feedBackTsk.execute(params);
  				}
  			});
        alertDialogBuilder.setNegativeButton(getString(R.string.dialog_negative_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
  					alertDialog.dismiss();
  				}
  			});
        return alertDialogBuilder.create();
    }
	private class SaveFeedbackTask extends AsyncTask<MyCommentParams, Void, String> {
        @Override
        protected String doInBackground(MyCommentParams... params) {
        	userFunction = new ServerInteractions();

        	String feedbackContent = params[0].feedbackContent;
        	db = new DatabaseHandler_joe(getActivity());
        	HashMap<String,String> user = new HashMap<String,String>();
        	user = db.getUserDetails();
        	String userId = user.get("uid");
        	json = userFunction.postFeedback(feedbackContent, userId); //100 refers to example user id
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
        String userId, feedbackContent;
        MyCommentParams(String feedbackContent) {
            this.feedbackContent = feedbackContent;
            
        }
    }
    	
}
