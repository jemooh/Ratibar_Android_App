package com.br.timetabler.src;

import java.text.DecimalFormat;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.br.timetabler.R;
import com.br.timetabler.model.Comment;
import com.br.timetabler.model.CommentLibrary;
import com.br.timetabler.model.Lesson;
import com.br.timetabler.model.LessonLibrary;
import com.br.timetabler.service.task.GetCommentsTask;
import com.br.timetabler.service.task.GetLessonsTask;
import com.br.timetabler.listener.CommentClickListener;
import com.br.timetabler.util.DatabaseHandler_joe;
import com.br.timetabler.util.MyDialogFragment;
import com.br.timetabler.listener.LessonClickListener;
import com.br.timetabler.util.ServerInteractions;
import com.br.timetabler.widget.CommentsListView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
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

public class SingleLessonFragment extends SherlockFragment implements CommentClickListener {
	
	String unit_id;
	String code;
	String title;
	String startTime, endTime;
	String teacher;
	String location;
	AlertDialog alertDialog;
	Button btn_submit, btnAddComment;
	 HashMap<Integer, Dialog> mDialogs = new HashMap<Integer, Dialog>();
	CommentsListView commentsLstView;
	TextView txtTeacher,txtLocation,txtCode,txtTitle;
	//SaveCommentTask commentTsk;
	ServerInteractions userFunction;
	DatabaseHandler_joe db;
	JSONObject json_user;
    JSONObject json;
    String errorMsg, successMsg;
    String res;
    DecimalFormat df = new DecimalFormat("#.##");
	static Bundle box;
  
    static int i = 0;
    
    public static SingleLessonFragment newInstance(Bundle a) {
    	
    	SingleLessonFragment f = new SingleLessonFragment();
 
    	box=a;
    	int index = a.getInt("index");
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
 
        //i = index;
        //f.setArguments(args);
 
        return f;
    }
 
    public int getShownIndex() {
        return box.getInt("index", 0);
    }
 
    
     
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v=inflater.inflate(R.layout.single_lesson, container, false);
		
		
		
		this.unit_id =box.getString("unit_id");
		this.startTime = box.getString("starttime");
		this.endTime = box.getString("endtime");
		this.code = box.getString("code");
		this.title = box.getString("title");
		this.teacher = box.getString("teacher");
		this.location = box.getString("location");
		
		 txtCode = (TextView) v.findViewById(R.id.txtSCode);
		 txtTitle = (TextView) v.findViewById(R.id.txtSTitle);
		 txtTeacher = (TextView) v.findViewById(R.id.txtSTeacher);
		 txtLocation = (TextView) v.findViewById(R.id.txtSLocation);
		
		txtCode.setText(code + " : " + createTime(startTime, endTime));
		txtTitle.setText(title);
		txtTeacher.setText(teacher);
		txtLocation.setText(location);
		
		return v;
		
    }
		/**
    	txtCode.setText(code + " : " + createTime(startTime, endTime));
    	txtCode = (TextView) v.findViewById(R.id.txtSCode);
		 txtTitle = (TextView) v.findViewById(R.id.txtSTitle);
		 txtTeacher = (TextView) v.findViewById(R.id.txtSTeacher);
		 txtLocation = (TextView) v.findViewById(R.id.txtSLocation);
    	 txtTitle = (TextView) v.findViewById(R.id.txtSTitle);
		btnAddComment = (Button) v.findViewById(R.id.btnAddComments);
		/*
		btnAddComment.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				showDialog();
			}
		});*/
		/**commentsLstView = (CommentsListView) v.findViewById(R.id.commentsListView);
		getCommentsFeed(commentsLstView);
		
		return v;
	}
    
    
    private void showDialog() {
    	CommentsDialogFragment newFragment = CommentsDialogFragment.newInstance(2);
    	newFragment.show(getFragmentManager(), "dialog");
    	}*/
    /**
    
    public void updateDetail(String code,String title,String teacher,String location) {
    	txtCode.setText(code);
		txtTitle.setText(title);
		txtTeacher.setText(teacher);
		txtLocation.setText(location);
		txtCode.setText(code + " : " + createTime(startTime, endTime));}*/
		
	 
    	   
    	   
    	   
    	 
    
   
	public String createTime(String start, String end) {
		String pn= "am";
		double a = Float.parseFloat(start)/100; 
		double c = Float.parseFloat(end)/100; 
    	
		if(a>12){
    		pn= "pm";
    		a = a-12;
    		if(a<1)
    			a=a+1;
    	} 
		if(c>12){
    		pn= "pm";
    		c = c-12;
    		if(c<0.59)
    			c=c+1;
    	} 
    	String m = df.format(a) + " - " + df.format(c) + pn;
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
        ProgressBar pbpp = (ProgressBar) getView().findViewById(R.id.pbppl);
        TextView txtMsg = (TextView) getView().findViewById(R.id.progressMsg2);
		pbpp.setVisibility(View.GONE);
		txtMsg.setVisibility(View.GONE);
		commentsLstView.setComments(lib.getComments());
    }
	

    
    // This is the interface method that is called when a comment in the listview is clicked!
    // The interface is a contract between this activity and the listview
    @Override
	public void onCommentClicked(Comment comment) {
		String comments = comment.getComment();
    	//display popup with OKOnly Buttons


    }
    
}
