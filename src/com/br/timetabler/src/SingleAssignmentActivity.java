package com.br.timetabler.src;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.br.timetabler.R;
import com.br.timetabler.listener.CommentClickListener;
import com.br.timetabler.model.Comment;
import com.br.timetabler.model.CommentLibrary;
import com.br.timetabler.service.task.GetCommentsTask;
import com.br.timetabler.util.DatabaseHandler_joe;
import com.br.timetabler.util.ServerInteractions;
import com.br.timetabler.widget.CommentsListView;

public class SingleAssignmentActivity extends SherlockActivity implements CommentClickListener {
	AlertDialog alertDialog;
	Button btn_submit, btnAddComment;
	
	CommentsListView commentsLstView;
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
    String unit_id, dateCreated, dateDue, unitCode, description;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.single_assignment);
		
		Intent in=getIntent();
		Bundle b=in.getExtras();
		
		this.unit_id = b.getString("unit_id");
		this.dateCreated = b.getString("dateCreated");
		this.dateDue = b.getString("dateDue");
		this.unitCode = b.getString("unitCode");
		this.description = b.getString("description");
		
		TextView txtUnitCode = (TextView) findViewById(R.id.txtAssSCode);
		TextView txtDescription = (TextView) findViewById(R.id.txtAssSDescription);
		TextView txtDateCreated = (TextView) findViewById(R.id.txtSDateCreated);
		TextView txtDateDue = (TextView) findViewById(R.id.txtSDateDue);
		
		txtUnitCode.setText(unitCode);
		txtDescription.setText(description);
		//txtDateCreated.setText("Date Created : " + dateCreated);
		txtDateDue.setText(dateDue);
		
		/*btnAddComment = (Button) findViewById(R.id.btnAddComment);
		btnAddComment.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				showDialog(COMMENT_DIALOG);
			}
		});
		commentsLstView = (CommentsListView) findViewById(R.id.commentsListView);
		getCommentsFeed(commentsLstView);*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getSupportMenuInflater().inflate(R.menu.activity_main, menu);
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
                startActivity(i1);
                finish();
                break;

            case R.id.menu_list: //display description
            	Intent i2 = new Intent(getApplicationContext(), ListDayLessons.class);
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
	
	public String createTime(String tme) {
		String pn= "am";
		double c = Float.parseFloat(tme)/100; 
    	
		if(c>12){
    		pn= "pm";
    		c = c-12;
    		if(c<0.59)
    			c=c+1;
    	} 
    	String m =  df.format(c) + pn;
    	m = m.replaceAll("[.]", ":");
        return m;
	}
	
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
    
    /**
     * This method retrieves the Library of videos from the task and passes them to our ListView
     * @param msg
     */
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
    /**
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
		 		dialogbuilder.setTitle("ADD YOUR COMMENT!");
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
							Intent login = new Intent(getApplicationContext(), LoginActivity.class);
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
	 }
	
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

        	String unit_id = params[0].unit_id;
        	String commentContent = params[0].commentContent;
        	db = new DatabaseHandler(getApplicationContext());
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
    }*/
}
