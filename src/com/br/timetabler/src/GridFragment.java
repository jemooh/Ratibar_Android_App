package com.br.timetabler.src;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.br.timetabler.R;
import com.br.timetabler.adapter.GridAdapter;
import com.br.timetabler.model.Lesson;
import com.br.timetabler.model.LessonLibrary;
import com.br.timetabler.model.OneCell;
import com.br.timetabler.service.task.GetLessonsSaved;
import com.br.timetabler.service.task.GetLessonsTask;
import com.br.timetabler.util.DatabaseHandler;
import com.br.timetabler.util.DatabaseHandler_joe;
import com.br.timetabler.util.GridDialogFragment;
import com.br.timetabler.util.Log;
import com.br.timetabler.util.FeedbackDialogFragment;
import com.br.timetabler.listener.LessonClickListener;
import com.br.timetabler.listener.LessonLongClickListener;
import com.br.timetabler.util.ServerInteractions;
import com.jess.ui.TwoWayAdapterView;
import com.jess.ui.TwoWayAdapterView.OnItemClickListener;
import com.jess.ui.TwoWayAdapterView.OnItemLongClickListener;
import com.jess.ui.TwoWayGridView;
//import android.widget.AdapterView.OnItemClickListener;
		
		public class GridFragment extends Fragment implements LessonClickListener,LessonLongClickListener {
		    public static String Dteacher,Dlocation;
		    public static String jsonString;
			private TwoWayGridView gridView;
		    //int startTime=800, endTime=1700, duration=100,learningDays=6;
			int startTime, endTime, duration,learningDays;
		    int totalCells;
		    List<OneCell> gridCells;
		    List<Lesson> lessons;
			private LessonClickListener lessonClickListener;
			TextView txtMon, txtTue, txtWed, txtThu, txtFri;
			TextView txtCmon,txtCtue,txtCwed,txtCthu,txtCfri,txt,txtCourse_name;
			ServerInteractions server;
			DatabaseHandler dbHandler;
			String userId, email, userPassword,inst_id,course_name,year,campus_name,semester;
			int today = new GregorianCalendar().get(Calendar.DAY_OF_WEEK);
			String Day_Id;
			int currentXPosition,currentYPosition;
			boolean mDualPane;
		    int mCurCheckPosition = 0;
		    Bundle savedInstanceState;
			@Override
			public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveInstanceState){
				
				View v = inflater.inflate(R.layout.grid_main,container, false);
				
				server = new ServerInteractions();
				dbHandler = new DatabaseHandler(getActivity());
				
				//I used this to help in fixing authentication by loging out the user upon loading the app
				//server.logoutUser(getApplicationContext());
				
				try {         
					dbHandler.createDataBase();         
		        } catch (IOException ioe) {         
		        	throw new Error("Unable to create database");         
		        }         
		        
		        try {         
		        	dbHandler.openDataBase();         
		        }catch(SQLException sqle){         
		        	throw sqle;
		        	
		        }
		        dbHandler.close();
		        
		        
				//if(server.isUserLoggedIn(getActivity())){
		        Day_Id= String.valueOf(today-1);
		        // current day  
			        txtCmon = (TextView) v.findViewById(R.id.txtColorMon);
					txtCtue = (TextView) v.findViewById(R.id.txtColorTue);
					txtCwed = (TextView) v.findViewById(R.id.txtColorWed);
					txtCthu = (TextView) v.findViewById(R.id.txtColorThr);
					txtCfri = (TextView) v.findViewById(R.id.txtColorFri); 
			       //color indicator for current day.
                    String c = "#00FF00";
					if(Day_Id.equals("1"))
						txtCmon.setBackgroundColor(Color.parseColor(c));
					if(Day_Id.equals("2"))
						txtCtue.setBackgroundColor(Color.parseColor(c));
					if(Day_Id.equals("3"))
						txtCwed.setBackgroundColor(Color.parseColor(c));
					if(Day_Id.equals("4"))
						txtCthu.setBackgroundColor(Color.parseColor(c));
					if(Day_Id.equals("5"))
						txtCfri.setBackgroundColor(Color.parseColor(c));
					//txtCourse_name = (TextView) v.findViewById(R.id.txtCourse_name);	
					txtMon = (TextView) v.findViewById(R.id.txtMon);
					txtTue = (TextView) v.findViewById(R.id.txtTue);
					txtWed = (TextView) v.findViewById(R.id.txtWed);
					txtThu = (TextView) v.findViewById(R.id.txtThu);
					txtFri = (TextView) v.findViewById(R.id.txtFri);
					
				        Typeface font_c = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
				        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Medium.ttf");
				        txtMon.setTypeface(font_c);
				        txtTue.setTypeface(font_c);
				        txtThu.setTypeface(font_c);
				        txtWed.setTypeface(font_c);
				        txtFri.setTypeface(font_c);
				        //txtCourse_name.setTypeface(font);
						
						
			
					
					txtMon.setOnClickListener(new OnClickListener() {			
						@Override
						public void onClick(View v) {
							openDayLessons("1", "Monday");				
						}
					});
					
					txtTue.setOnClickListener(new OnClickListener() {			
						@Override
						public void onClick(View v) {
							openDayLessons("2", "Tuesday");				
						}
					});
					
					txtWed.setOnClickListener(new OnClickListener() {			
						@Override
						public void onClick(View v) {
							openDayLessons("3", "Wednesday");				
						}
					});
					
					txtThu.setOnClickListener(new OnClickListener() {			
						@Override
						public void onClick(View v) {
							openDayLessons("4", "Thursday");				
						}
					});
					
					txtFri.setOnClickListener(new OnClickListener() {			
						@Override
						public void onClick(View v) {
							openDayLessons("5", "Friday");				
						}
					});
					
					//check if user is logged in.
					dbHandler = new DatabaseHandler(getActivity());
			    	HashMap<String,String> users = new HashMap<String,String>();
			    	users = dbHandler.getUserGridLessons();
			        jsonString = users.get("jsonString");

					
					
				
					dbHandler = new DatabaseHandler(getActivity());
		        	HashMap<String,String> user = new HashMap<String,String>();
		        	user = dbHandler.getUserDetails();
		        	userId = user.get("uid");
		        	email = user.get("email");
		        	userPassword = user.get("password");
		        	inst_id = user.get("inst_id");
		        	//year = user.get("year");
		        	//course_name = user.get("course_name");
		        	//campus_name = user.get("campus_name");
		        	//semester = user.get("semester");
		        	
		        	
		        	startTime = Integer.parseInt(user.get("startTime"));
		        	endTime = Integer.parseInt(user.get("endTime"));
		        	duration = Integer.parseInt(user.get("duration"));
		        	learningDays = Integer.parseInt(user.get("learningDays"));
		        	
		        	//txtCourse_name.setText(course_name+", Year "+year+", Semester "+semester);
		        	
		        	
		        	
					gridView = (TwoWayGridView) v.findViewById(R.id.gridview);
					UpdateGrid(); 
					getLessonsFeed(gridView);
					
					// getting x n y coodinates to use in toast of location 
					gridView.setOnTouchListener(new View.OnTouchListener() {
				            @Override
				            public boolean onTouch(View v, MotionEvent event) {
				                
				                currentXPosition = ((int)event.getX());
				                currentYPosition = ((int)event.getY());
				                
				                    return false;
				            }
				        });
				
				/*} 
				
				else {
		            // user is not logged in show login screen
		            Intent login = new Intent(getActivity(), LoginActivity.class);
		            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		            startActivity(login);
		            // Closing dashboard screen
		            
		       }
				*/
					
					
					
				return v;
				
			}
			
			 @Override
			    public void onSaveInstanceState(Bundle outState) {
			        super.onSaveInstanceState(outState);
			        outState.putInt("index", mCurCheckPosition);
			    }
			    
			    public void getRightPanel() {
			    	View detailsFrame = getActivity().findViewById(R.id.details);
					mDualPane = detailsFrame != null 
							&& detailsFrame.getVisibility() == View.VISIBLE;
					if (savedInstanceState != null) {
			            // Restore last state for checked position.
			            mCurCheckPosition = savedInstanceState.getInt("index", 0);
			        }
				 	Log.i("after savedInstanceState: ");
			        if (mDualPane) { 
			        	// In dual-pane mode, the list view highlights the selected item.
			        	//listView .setChoiceMode(listView.CHOICE_MODE_SINGLE);
			            // Make sure our UI is in the correct state.
			        	Lesson lesson = lessons.get(0); //0
			        	String title = lesson.getTitle();
			        	showDetails(lesson, mCurCheckPosition);
			        }else{
			            System.out.println("NOT DUAL");
			        }	
			    }
			
			
			
			
			
			
			private void openDayLessons(String dayId, String dayTitle) {
				
				Intent si = new Intent(getActivity(), DayviewActivity.class);
		        Bundle b=new Bundle();        
		        b.putString("dayId", dayId);        
		        b.putString("dayTitle", dayTitle);        
		        si.putExtras(b);
		        startActivity(si);
			}
			
			//checking for internet connection
			/*public boolean isConn() {
		        ConnectivityManager connectivity = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		        if (connectivity.getActiveNetworkInfo() != null) {
		            if (connectivity.getActiveNetworkInfo().isConnected())
		                return true;
		        }
		        return false;
			
			}*/
			
			
			// This is the XML onClick listener to retreive a users video feed
		    public void getLessonsFeed(View v){
		        // We start a new task that does its work on its own thread
		        // We pass in a handler that will be called when the task has finished..(email, userPassword)
		    	/*if (jsonString!=null){
		    		Log.i("Saved MobileDb GridLessons");
		    		new Thread(new GetLessonsSaved(responseHandler2,true)).start();
		    	}else
		    		Log.i("Server  GridLessons");*/
		        new Thread(new GetLessonsTask(responseHandler, "0", true, email,userPassword)).start();
		       //new Thread(new GetLessonsTask(responseHandler, "0", false, null, null)).start();
		       //Log.i(email+userPassword+"frag**");
		    	
		    }
		    
		    // This is the handler that receives the response when the YouTube task has finished
		
		    @SuppressLint("HandlerLeak")
			Handler responseHandler = new Handler() {
		        public void handleMessage(Message msg) {
		        	populateListWithLessons(msg);
		        	   Log.i("msg............."+msg);
		        };
		    };
		 
		    /**
		     * This method retrieves the Library of lessons from the task and passes them to our ListView
		     * @param msg
		     */
		    private void populateListWithLessons(Message msg) {
		        // Retreive the videos are task found from the data bundle sent back
		      //  final LessonLibrary lib = (LessonLibrary) msg.getData().get(GetLessonsTask.LIBRARY);
		        LessonLibrary lib = (LessonLibrary) msg.getData().get(GetLessonsTask.LIBRARY);
		        
		        // Because we have created a custom ListView we don't have to worry about setting the adapter in the activity
		        // we can just call our custom method with the list of items we want to display
		       
		        lessons = lib.getLessons();
		        GridAdapter adapter = new GridAdapter(getActivity(), gridCells ); //the adapter that sets the grid content
		           
		        
		        gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

                    public boolean onItemLongClick(TwoWayAdapterView<?> arg0, View arg1,
                            int position, long arg3) {
                      int lessonPos = getLessonPos(position);
						Log.i("position:"+position);
						Log.i("lessonPos:"+lessonPos);
						onLessonLongClicked(lessons.get(lessonPos),position );
						//showDialog();
                        return true;
                    }
                }); 
		        
		        
		        
		        
		        gridView.setOnItemClickListener(new OnItemClickListener() {
		        	@Override
		        	public void onItemClick(TwoWayAdapterView<?> parent, View view, int position, long id) {
						int lessonPos = getLessonPos(position);
						Log.i("position:"+position);
						Log.i("lessonPos:"+lessonPos);
		        		onLessonClicked(lessons.get(lessonPos),position );        		
					}
				});
		        
		         
		        ProgressBar pb = (ProgressBar) getView().findViewById(R.id.progressBar1);
		        pb.setVisibility(View.GONE);
		        gridView.setAdapter(adapter);
		        Log.i("got past setAdapter!");
		        adapter.setLessons(lessons);
		        String grid_jsonString = GetLessonsTask.json;
		        Log.i("grid_jsonString" +grid_jsonString);
		        dbHandler.addUserGridLessons(grid_jsonString);
		        
		        Log.i("got past setLessons!");
		        adapter.notifyDataSetChanged();
		        
		        getRightPanel();
		    }
		    
		    
		    
		 // This is the handler that receives the response when the YouTube task has finished
			
		    @SuppressLint("HandlerLeak")
			Handler responseHandler2 = new Handler() {
		        public void handleMessage(Message msg) {
		        	populateListWithDbLessons(msg);
		        	   Log.i("msg"+msg);
		        };
		    };
		 
		    /**
		     * This method retrieves the Library of lessons from the task and passes them to our ListView
		     * @param msg
		     */
		    private void populateListWithDbLessons(Message msg) {
		        // Retreive the videos are task found from the data bundle sent back
		      //  final LessonLibrary lib = (LessonLibrary) msg.getData().get(GetLessonsTask.LIBRARY);
		        LessonLibrary lib = (LessonLibrary) msg.getData().get(GetLessonsSaved.LIBRARY);
		        
		        // Because we have created a custom ListView we don't have to worry about setting the adapter in the activity
		        // we can just call our custom method with the list of items we want to display
		       
		        lessons = lib.getLessons();
		        GridAdapter adapter = new GridAdapter(getActivity(), gridCells ); //the adapter that sets the grid content
		        gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

                    public boolean onItemLongClick(TwoWayAdapterView<?> arg0, View arg1,
                            int position, long arg3) {
                      int lessonPos = getLessonPos(position);
						Log.i("position:"+position);
						Log.i("lessonPos:"+lessonPos);
						//gridView.getLocationOnScreen(location);
						//position.getRight()+5;		
						onLessonLongClicked(lessons.get(lessonPos),position );
                        return true;
                    }
                }); 
		       
		        
		        gridView.setOnItemClickListener(new OnItemClickListener() {
		        	@Override
		        	public void onItemClick(TwoWayAdapterView<?> parent, View view, int position, long id) {
						int lessonPos = getLessonPos(position);
						Log.i("position:"+position);
						Log.i("lessonPos:"+lessonPos);
		        		onLessonClicked(lessons.get(lessonPos),position );        		
					}
				});
		         
		        ProgressBar pb = (ProgressBar) getView().findViewById(R.id.progressBar1);
		        pb.setVisibility(View.GONE);
		        gridView.setAdapter(adapter);
		        Log.i("got past setAdapter!");
		        adapter.setLessons(lessons);
		        Log.i("got past setLessons!");
		        adapter.notifyDataSetChanged();
		        
		        getRightPanel();
		    }
		    
		    
		 
		    
		    public void setOnLessonClickListener(LessonClickListener l) {
		    	lessonClickListener = l;
		    }
		    
		    
		    // This is the interface method that is called when a video in the listview is clicked!
		    // The interface is a contract between this activity and the listview
		    @Override
			public void onLessonClicked(Lesson lesson,int position) {
		    	showDetails(lesson, position);
		    	mCurCheckPosition = position;
		    	
		    }
		    	
		     	
		    	//SingleLessonFragment det = (SingleLessonFragment)getFragmentManager().findFragmentById(R.id.details);
		    	
		    /*8
		    	 // if run on phone, isSinglePane = true
		    	 // if run on tablet, isSinglePane = false
		    	if( isSinglePane==false){
				    //get reference to MyDetailFragment
		    		SingleLessonFragment myDetailFragment = ( SingleLessonFragment)getFragmentManager().findFragmentById(R.id.one);
					myDetailFragment.updateDetail(code,title,teacher,location);
			*/
		    void showDetails(Lesson lesson,int position){
		    	//look for data in position index on array Lessons
		    	String unit_id = lesson.getLessonId();
				String starttime = lesson.getStarttime();
				String endtime = lesson.getEndtime();
				String color = lesson.getColorband();
				String code = lesson.getCode();
				String title = lesson.getTitle();
				String teacher = lesson.getTeacher();
				String location = lesson.getLocation();
				
		    	//mCurCheckPosition = 0;
		    	if (mDualPane) {
			        
				
					Bundle a=new Bundle();
					 
					a.putString("unit_id", unit_id);
					a.putString("starttime", starttime);
					a.putString("endtime", endtime);
					a.putString("code", code);
					a.putString("color", color);	
					a.putString("title", title);
					a.putString("teacher", teacher);
					a.putString("location", location);
					a.putInt("index", position);
		     
					   
		            // We can display everything in-place with fragments, so update
		            // the list to highlight the selected item and show the data.
		           // getListView().setItemChecked(index, true);
		 
		            // Check what fragment is currently shown, replace if needed.
		            SingleLessonFragment details = (SingleLessonFragment) getFragmentManager().findFragmentById(R.id.details);
		           /** if (details != null) {
		            	Log.i("getShownIndex",""+details.getShownIndex());   */
		            
		            if (details == null  || details.getShownIndex() != position) {
		                // Make new fragment to show this selection.
		                details = SingleLessonFragment.newInstance(a);
		 
		                // Execute a transaction, replacing any existing fragment
		                // with this one inside the frame.
		                FragmentTransaction ft = getFragmentManager()
		                        .beginTransaction();
		                ft.replace(R.id.details, details);
		                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		                ft.commit();
				
				    }
		    	}else {
		    		
					     Intent si = new Intent(getActivity(), SingleLessonActivity1.class);
					     Bundle b=new Bundle();
					     
					     b.putString("unit_id", unit_id);
					     b.putString("starttime", starttime);
					     b.putString("endtime", endtime);
					     b.putString("code", code);
					     b.putString("color", color);
					     b.putString("title", title);
					     b.putString("teacher", teacher);
					     b.putString("location", location);
					                
					     si.putExtras(b);
					     startActivity(si);
				    }		
		    	}
		
		    		    
		   //custom toast of teacher & location.
		    @Override
		    public void onLessonLongClicked(Lesson lesson, int position) {
		    	
		    	Dteacher = lesson.getTeacher();
		    	Dlocation = lesson.getLocation();
		    	/*
		    	Toast.makeText(getActivity(), Dteacher+" "+Dlocation,
		    			   Toast.LENGTH_LONG).show();*/
		    	
		    	
		    	Toast toast=new Toast(getActivity());
		        LayoutInflater inflater=getActivity().getLayoutInflater();
		        View toastView=inflater.inflate(R.layout.grid_dialog_list, 
		        		(ViewGroup) getView().findViewById(R.id.toastView));
		        TextView txtt=(TextView)toastView.findViewById(R.id.txtTeacher);
		        TextView txtl=(TextView)toastView.findViewById(R.id.txtLocation);
		        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
		        txtt.setTypeface(font);
		        txtl.setTypeface(font);
		        txtt.setText(Dteacher);
		        txtl.setText(Dlocation);
		        toast.setGravity(Gravity.TOP|Gravity.LEFT,currentXPosition+50 ,currentYPosition-10 );
		        toast.setDuration(Toast.LENGTH_LONG);
		        toast.setView(toastView);
		        toast.show();
		                
		    	/*
		    	FragmentManager manager = getFragmentManager();

		        GridDialogFragment dialog = new GridDialogFragment();
		        dialog.show(manager, "dialog");
		    	
		        Intent si = new Intent(getActivity(), GridDialogActivity.class);
		        Bundle b=new Bundle();
		        
		        b.putString("teacher", teacher);
		        b.putString("location", location);
		        
		        si.putExtras(b);
		        startActivity(si);
				*/
			}
		    
		    private void showDialog() {
		    	GridDialogFragment newFragment = GridDialogFragment.newInstance(1);
				newFragment.show(getFragmentManager(), "dialog");
			}
		    
		   
		    /**
			@Override
			public boolean onCreateOptionsMenu(Menu menu) {
				// Inflate the menu; this adds items to the action bar if it is present.
				getSupportMenuInflater().inflate(R.menu.activity_main, menu);
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
		            	i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		                startActivity(i1);
		                finish();
		                break;
		
		            case R.id.menu_list: //display description
		            	Intent i2 = new Intent(getApplicationContext(), ListDayLessons.class);
		            	i2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
			*/
			public void UpdateGrid() {
				
				
			
				this.totalCells = ((endTime - startTime) / duration) * learningDays;
				gridCells = new ArrayList<OneCell>();
				for(int i=0; i<totalCells; i++) {
					gridCells.add(new OneCell(i, i));
				}
			}
			
			private int getLessonPos(int pos){ 
		    	int i=0;
		    	int z = 0;
		    	for(Lesson l : lessons) {
		    		String startTime = l.getStarttime();
		    		String endTime = l.getEndtime(); 
		    		int durTotal = Integer.parseInt(endTime) - Integer.parseInt(startTime);
		    		int reps = durTotal/100;
		    		int dbPos = Integer.parseInt(l.GetyPos());
		    		int extPos = dbPos + (6*(reps-1));
		    		int j = Integer.parseInt(l.GetyPos());
		    		
		    		if(pos==j || pos==extPos){
		    			z = i;
		    		}
		    		i++;
		    	}
		    	return z;
		    }
			
			

			

			
}
	
	