package com.br.timetabler.src;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.br.timetabler.R;
import com.actionbarsherlock.app.SherlockFragment;
import com.br.timetabler.model.CommentLibrary;
import com.br.timetabler.service.task.GetCommentsTask;
import com.br.timetabler.widget.CommentsListView;

		public class One_Lesson_Comments_Fragment  extends SherlockFragment {
			
			CommentsListView commentsLstView;
			String unit_id,userId;
			@Override
		    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		    	View v=inflater.inflate(R.layout.one_lesson_comments, container, false);
				
				
				commentsLstView = (CommentsListView) v.findViewById(R.id.commentsListView);
				getCommentsFeed(commentsLstView);
				
				
			return v;	
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
		    
		    
		    /** This method retrieves the Library of videos from the task and passes them to our ListView
		     * @param ms*/
		     
		    private void populateListWithComments(Message msg) {
		        // Retreive the videos are task found from the data bundle sent back
		        CommentLibrary lib = (CommentLibrary) msg.getData().get(GetCommentsTask.LIBRARY);
		        // Because we have created a custom ListView we don't have to worry about setting the adapter in the activity
		        // we can just call our custom method with the list of items we want to display
		        ProgressBar pbpp = (ProgressBar) getView().findViewById(R.id.pbppl);
		        TextView txtMsg = (TextView) getView().findViewById(R.id.progressMsg2);
				pbpp.setVisibility(View.GONE);
				txtMsg.setVisibility(View.GONE);
				commentsLstView.setComments(lib.getComments());
		    }
		    
		  
			
		}
		
		


