package com.br.timetabler.src;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.br.timetabler.R;
import com.br.timetabler.model.Comment;
import com.br.timetabler.model.CommentLibrary;
import com.br.timetabler.model.Lesson;
import com.br.timetabler.model.LessonLibrary;
import com.br.timetabler.service.task.GetCommentsTask;
import com.br.timetabler.service.task.GetLessonsTask;
import com.br.timetabler.adapter.LessonNotesCursorAdapter;
import com.br.timetabler.adapter.TabsPagerAdapter;
import com.br.timetabler.listener.CommentClickListener;
import com.br.timetabler.util.DatabaseHandler_joe;
import com.br.timetabler.util.FeedbackDialogFragment;
import com.br.timetabler.util.NotesDataHelper;
import com.br.timetabler.listener.LessonClickListener;
import com.br.timetabler.util.ServerInteractions;
import com.br.timetabler.widget.CommentsListView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SingleLessonFragment extends Fragment    {
	
	
	private LessonNotesCursorAdapter customAdapter;
    private NotesDataHelper databaseHelper;
    private static final int ENTER_DATA_REQUEST_CODE = 1;
	String unit_id;
	String code,color;
	String title;
	String startTime, endTime;
	String teacher;
	String location;
	AlertDialog alertDialog;
	Button btn_submit, btnAddComment;
	 HashMap<Integer, Dialog> mDialogs = new HashMap<Integer, Dialog>();
	CommentsListView commentsLstView;
	TextView txtTeacher,txtLocation,txtCode,txtTitle;
	ImageButton btnSaveNotes, btnAddnotes,btnShareNotes;
	//SaveCommentTask commentTsk;
	ServerInteractions userFunction;
	DatabaseHandler_joe db;
	JSONObject json_user;
    JSONObject json;
    String errorMsg, successMsg;
    String res;
    ListView listViewNotes;
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
   // private String[] tabs = { "Comments", "Assignments", "Downloads" ,"My Notes" };
    private String[] tabs = { "Notes" };
    
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
    static String parseNull(Object obj){
        return obj == null?"null"  : "Object";
    }
    
     
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	 Log.d("FragmentCycle===>", "onCreateView: bundle="+parseNull(savedInstanceState));
    	View v=inflater.inflate(R.layout.single_lesson, container, false);

		
		this.unit_id =box.getString("unit_id");
		this.startTime = box.getString("starttime");
		this.endTime = box.getString("endtime");
		this.code = box.getString("code");
		this.color = box.getString("color");
		this.title = box.getString("title");
		this.teacher = box.getString("teacher");
		this.location = box.getString("location");
		
		TextView txtCode = (TextView) v.findViewById(R.id.txtSCode);
		TextView txtTime = (TextView) v.findViewById(R.id.txtTime);
		TextView txtColor = (TextView) v.findViewById(R.id.txtColor);
		TextView txtTitle = (TextView) v.findViewById(R.id.txtSTitle);
		TextView txtTeacher = (TextView) v.findViewById(R.id.txtSTeacher);
		TextView txtLocation = (TextView) v.findViewById(R.id.txtSLocation);
		TextView txtpm = (TextView) v.findViewById(R.id.textView2);
		
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");
        Typeface font_a = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface font_b = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
        Typeface font_d = Typeface.createFromAsset(getActivity().getAssets(), "fonts/DistProTh.otf");
        Typeface font_c = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Medium.ttf");
        txtTime.setTypeface(font_d);
        txtCode.setTypeface(font_a);
        txtTitle.setTypeface(font_c);
        txtTeacher.setTypeface(font_a);
        txtLocation.setTypeface(font_a);
        txtpm.setTypeface(font_a);
		
		txtCode.setText(code);
		//txtCode.setTextColor(Color.parseColor(color));
		txtColor.setBackgroundColor(Color.parseColor(color));
		//txtTime.setText(createTime(startTime, endTime));
		txtTitle.setText(title);
		txtTeacher.setText(teacher);
		txtLocation.setText(location);
		String pn= "am";
		double a = Float.parseFloat(startTime)/100; 
		double c = Float.parseFloat(endTime)/100; 
    	
		if(a>12){
    		//pn= "pm";
    		a = a-12;
    		if(a<1)
    			a=a+1;
    	} 
		if(c>12){
    		pn= "pm";
    		
    		txtpm.setText(pn);
    		c = c-12;
    		if(c<0.59)
    			c=c+1;
    	} 
    	String m = df.format(a) + "-" + df.format(c);
    	m = m.replaceAll("[.]", ":");
    	txtTime.setText(m);
		
    	
    	
    	
      return v;	
    	
	}
	
	/**
	* Creates a new instance of our dialog and displays it.
	
	private void showDialog() {
		DialogFragment newFragment = NotesDialogFragment.newInstance(1);
		newFragment.show(getFragmentManager(), "dialog");
	}*/
    
	   
		// Initilization
    	/**viewPager = (ViewPager) v.findViewById(R.id.pager);
        actionBar = ((DashboardActivity)getActivity()).getSupportActionBar();
        mAdapter = new TabsPagerAdapter(getActivity().getSupportFragmentManager());
 
        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);       
 
        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }
 
        
        /* on swiping the viewpager make respective tab selected
          
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
 
            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }
 
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
 
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });*/
    	
    	
    	
    	
    
 /* @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }
 
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }
 
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }*/
    
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("FragmentCycle2===>", "onCreate: bundle="+parseNull(savedInstanceState));
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d("FragmentCycle2===>", "onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d("FragmentCycle2===>", "onViewCreated: bundle="+parseNull(savedInstanceState));
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("FragmentCycle2===>", "onActivityCreated: bundle="+parseNull(savedInstanceState));
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        Log.d("FragmentCycle2===>", "onViewStateRestored: bundle="+parseNull(savedInstanceState));
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d("FragmentCycle2===>", "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d("FragmentCycle2===>", "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("FragmentCycle2===>", "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("FragmentCycle2===>", "onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d("FragmentCycle2===>", "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        Log.d("FragmentCycle2===>", "onInflate: bundle="+parseNull(savedInstanceState));
        super.onInflate(activity, attrs, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("FragmentCycle2===>", "onSaveInstanceState: outState="+parseNull(outState));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("FragmentCycle2===>", "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroy() {
        Log.d("FragmentCycle2===>", "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d("FragmentCycle2===>", "onDetach");
        super.onDetach();
    }

    @Override
    public void setInitialSavedState(SavedState state) {
        Log.d("FragmentCycle2===>", "setInitialSavedState");
        super.setInitialSavedState(state);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.d("FragmentCycle2===>", "onHiddenChanged");
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onLowMemory() {
        Log.d("FragmentCycle2===>", "onLowMemory");
        super.onLowMemory();
    }
    
    
    
    
    
    
    
 }