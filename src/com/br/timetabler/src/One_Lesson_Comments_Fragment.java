package com.br.timetabler.src;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.br.timetabler.R;
import com.br.timetabler.adapter.LessonCommentCursorAdapter;
import com.br.timetabler.adapter.LessonNotesCursorAdapter;
import com.br.timetabler.model.CommentLibrary;
import com.br.timetabler.service.task.GetCommentsTask;
import com.br.timetabler.util.CommentDialogFragment;
import com.br.timetabler.util.DatabaseHandler;
import com.br.timetabler.util.Log;
import com.br.timetabler.util.NotesDataHelper;
import com.br.timetabler.util.NotesDialogFragment;
import com.br.timetabler.widget.CommentsListView;

		public class One_Lesson_Comments_Fragment  extends Fragment implements OnClickListener {
			
			ListView commentsLstView;
			String userId;
			private String password = DashboardActivity.password;
			private String email   = DashboardActivity.email;
			private String unit_id = SingleLessonActivity1.unit_id;
			private LessonCommentCursorAdapter customAdapter;
		    private NotesDataHelper databaseHelper;
		    DatabaseHandler dbHandler ;
		    
			@Override
		    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		    	View v=inflater.inflate(R.layout.one_lesson_comments, container, false);
		    	
		    	  dbHandler = new DatabaseHandler(getActivity());
		        	HashMap<String,String> user = new HashMap<String,String>();
		        	user = dbHandler.getUserDetails();
		        	final String userId = user.get("uid");
		        	final String fname = user.get("fname");
		        	final String lname = user.get("lname");
				
				Log.i(email+password+unit_id);
				databaseHelper = new NotesDataHelper(getActivity());
				commentsLstView = (ListView) v.findViewById(R.id.commentsListView);
				
				//getCommentsFeed(commentsLstView);
				
				commentsLstView.setOnItemClickListener(new OnItemClickListener() {
					
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						
						//Log.i( "clicked on item: " + position, "**");
						databaseHelper = new NotesDataHelper(getActivity());
						databaseHelper.delete(id);
						//Log.i( "clicked on item: " + position, "**");
						

					}
				});
		 
		        // Database query can be a time consuming task ..
		        // so its safe to call database query in another thread
		        // Handler, will handle this stuff for you <img src="http://s0.wp.com/wp-includes/images/smilies/icon_smile.gif?m=1129645325g" alt=":)" class="wp-smiley">
		 
		       new Handler().post(new Runnable() {
		            @Override
		            public void run() {
		            	ProgressBar pbpp = (ProgressBar) getView().findViewById(R.id.pbppl);
				        TextView txtMsg = (TextView) getView().findViewById(R.id.progressMsg2);
		            	
							
						
		            	customAdapter = new LessonCommentCursorAdapter(getActivity(), databaseHelper.getAllComments());
		            	pbpp.setVisibility(View.GONE);
						txtMsg.setVisibility(View.GONE);
		                commentsLstView.setAdapter(customAdapter);
		            }
		        });
				
		       
		       
				final Button btnAddComment = (Button) v.findViewById(R.id.btnAddComment);
				final EditText EdTxtComment = (EditText) v.findViewById(R.id.edtcomment);
				
				btnAddComment.setOnClickListener(new OnClickListener() {			
					@Override
					public void onClick(View v) {
						
							
							String comment = EdTxtComment.getText().toString();
							String time     = giveDateTime();
							String name     = fname+" "+lname;
							String unit_id  = SingleLessonActivity1.unit_id;		
				            databaseHelper.insertComment(userId, time, unit_id, comment, name);
				            
				            customAdapter.changeCursor(databaseHelper.getAllComments());
				            
				            EdTxtComment.setText("");
				        
					}
				});
				
			
				
				
				
			return v;	
			}
			
			//getting the current time.
		    public String giveDateTime() {
			       Calendar cal = Calendar.getInstance();
			       SimpleDateFormat currentDate = new SimpleDateFormat("MMM/dd/yyyy HH:mm ");
			       return currentDate.format(cal.getTime());
			    }
			
			
			
		
			// This is the XML onClick listener to retreive a users video feed
		  /**   public void getCommentsFeed(View v){
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
		    
		    
		    
		    This method retrieves the Library of videos from the task and passes them to our ListView
		     * @param ms*/
		     
		 /**private void populateListWithComments(Message msg) {
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
		    
			
			* Creates a new instance of our dialog and displays it.
			*/
			private void showDialog() {
				DialogFragment newFragment = CommentDialogFragment.newInstance(1);
				newFragment.show(getFragmentManager(), "dialog");
			}


			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		    
			
		}
		
		


