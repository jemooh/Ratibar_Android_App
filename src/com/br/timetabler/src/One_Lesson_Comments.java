package com.br.timetabler.src;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.timetabler.R;
import com.br.timetabler.model.CommentLibrary;
import com.br.timetabler.service.task.GetCommentsTask;
import com.br.timetabler.widget.CommentsListView;

public class One_Lesson_Comments  extends ActionBarActivity {
	
	CommentsListView commentsLstView;
	String userId;
	String password = DashboardActivity.password;
	String email   = DashboardActivity.email;
	String unit_id = SingleLessonActivity1.unit_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.one_lesson_comments);
		
		commentsLstView = (CommentsListView) findViewById(R.id.commentsListView);
		getCommentsFeed(commentsLstView);
		
	}
	// This is the XML onClick listener to retreive a users video feed
    public void getCommentsFeed(View v){
        // We start a new task that does its work on its own thread
        // We pass in a handler that will be called when the task has finished
        // We also pass in the name of the user we are searching YouTube for
        new Thread(new GetCommentsTask(responseHandler,email,password, unit_id, false, null)).start();
    }
    
    // This is the handler that receives the response when the YouTube task has finished

    @SuppressLint("HandlerLeak")
	Handler responseHandler = new Handler() {
        public void handleMessage(Message msg) {
        	populateListWithComments(msg);
        };
    };
    
    
    /** This method retrieves the Library of videos from the task and passes them to our ListView
     * @param ms*/
     
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
   
	
}



