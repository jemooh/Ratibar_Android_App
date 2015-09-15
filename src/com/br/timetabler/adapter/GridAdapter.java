package com.br.timetabler.adapter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.br.timetabler.R;
import com.br.timetabler.model.Lesson;
import com.br.timetabler.model.OneCell;
import com.br.timetabler.src.GridFragment;
import com.br.timetabler.src.SingleLessonActivity1;
import com.br.timetabler.src.WeekviewActivity;
import com.br.timetabler.util.DatabaseHandler;

/**
 * This adapter is used to show our Video objects in a ListView
 * It hasn't got many memory optimisations, if your list is getting bigger or more complex
 * you may want to look at better using your view resources: http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/view/List14.html
 * @author paul.blundell
 */
public class GridAdapter extends BaseAdapter {
	List<Lesson> lessons;
	List<OneCell> gridCells;
	String[][] strlessons;
    // An inflator to use when creating rows
    private LayoutInflater mInflater;
    Context mContext;
    //double globStartTime=700, globEndTime=1700, duration=100.0;
    int totalCells;
    //float learningDays = 6;
    long currentT,startT;
    String lessonsTable;
    
    DecimalFormat df = new DecimalFormat("#.##");
    double globStartTime = WeekviewActivity.startTime;
    double globEndTime = WeekviewActivity.endTime;
    double duration = WeekviewActivity.duration;
    double learningDays = WeekviewActivity.learningDays;
   
    
     /* @param context this is the context that the list will be shown in - used to create new list rows
     * @param gridCells this is a list of gridCells to display
     */
    public GridAdapter(Context context, List<OneCell> gridCells) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context; 
        this.gridCells = gridCells;
    }
 
    //@Override
    public int getCount() {
        return gridCells.size();
    }
 
    //@Override
    public Object getItem(int position) {
        return gridCells.get(position);
    }
 
    //@Override
    public long getItemId(int position) {
        return position;
    }
    
    public void setLessons(List<Lesson> lessons) {
    	this.lessons = lessons;
    	//assignLessons();
    }
	
	static class ViewHolder {
		public TextView code,txtglow;
	}
 
    //@Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
        View view = null; // If convertView wasn't null it means we have already set it to our list_item_user_video so no need to do it again
        String timeTitleEnd = null;
   //......james commet out coz tz reusing views    // if(convertView == null) {
            // This is the layout we are using for each row in our list anything you declare in this layout can then be referenced below
        	LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	convertView = inflater.inflate(R.layout.single_grid_item, parent, false);
           // Uses ViewHolder class for storing view handles to reduce number of times findViewById() is called
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.code = (TextView) convertView.findViewById(R.id.lessonCell); 
			viewHolder.txtglow = (TextView) convertView.findViewById(R.id.txtGlow); 
			convertView.setTag(viewHolder);
        //}
		
		ViewHolder holder = (ViewHolder) convertView.getTag();
		
        // Get a single cell from our grid
        OneCell cell = gridCells.get(position);
        
        //Lesson l = lessons.get(getPos(position));
        if(Math.floor(position % learningDays) ==0) {
        	//here we are setting the time titles
        	double t = ((position / learningDays)) + (globStartTime/100);
        	String pn;
        	if(t>12){
        		t = t - 12;
        		if(t<1) 
        			t = t+12;
        		pn = "pm";
        	} else 
        		pn = "am";         	
        	
        	//first we plot out the starting times
        	String timeTitleStart = df.format(t) +"" ; 
        	
        	//here we have cleanout t for plotting for the ending times
        	if(t>12){ 
        		t = t - 12;        		
        	}
        	
        	//next we plot out the end time title
        	double ent =  (duration/ 100) + t;
        	timeTitleEnd = df.format(ent) + pn; // ((duration/ 100) + t) + pn;
        	
        	//here we replace all the dots with colons
        	timeTitleStart = timeTitleStart.replaceAll("[.]", ":");
        	timeTitleEnd = timeTitleEnd.replaceAll("[.]", ":");
        	//color txtTime
        	//String color = "#EFEFEF";
        	holder.code.setBackgroundResource(R.drawable.grid_time_bg);
        	//holder.code.setBackgroundColor(Color.parseColor(color));
        	holder.code.setText(timeTitleStart + " - " + timeTitleEnd);
        	Typeface font_c = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Regular.ttf");
    		holder.code.setTypeface(font_c);
        	holder.code.setTextSize(11);
        	holder.code.setGravity(Gravity.CENTER);
        	holder.code.setClickable(false);
        } else {
        	/*for(int i=0; i<strlessons.length; i++) {
        		if(position == Integer.parseInt(strlessons[i][0])) {
        			holder.code.setBackgroundResource(R.drawable.grid_lesson_item_bg);
    	        	holder.code.setText(strlessons[i][1]);
        		}
        	} */
        	//holder.code.setText(getLessonCode(position));
        	//String color = "#FF3c61c1";
        	//holder.code.setBackgroundColor(Color.parseColor(color));
        	
        	for(Lesson l : lessons) {
        		//get current time.
        		int today = new GregorianCalendar().get(Calendar.DAY_OF_WEEK);
        		/*
        		 SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				 String currentTime = sdf.format(new Date());
				 currentTime = currentTime.replaceAll("[:]",""); 
				 

        		 SimpleDateFormat dfm = new SimpleDateFormat("HHmm");  
        	      dfm.setTimeZone(TimeZone.getTimeZone("GMT"));  
        		 
        	      try {
					 currentT = dfm.parse("currentTime").getTime();
				 
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	      
        	    
        	      
        	      try {
  					startT = dfm.parse("startTime").getTime();
  				 
  				} catch (ParseException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}
				 */
				 
        		//String currentTime = WeekviewActivity.currentTime;
        		String startTime = l.getStarttime();
        		String endTime = l.getEndtime(); 
        		//String Day_id = l.getDayId();
        		String Today = String.valueOf(today-1);
        		int durTotal = Integer.parseInt(endTime) - Integer.parseInt(startTime);
        		int reps = durTotal/100;
        		int dbPos = Integer.parseInt(l.GetyPos());
        		int extPos = dbPos + (6*(reps-1));
        		long glowTime = (startT - currentT)/1000;
        		String Day_id = l.getDayId();
            	if(position == dbPos || position == extPos) { // || position % 5 == dayId){
    	        	//holder.code.setBackgroundResource(R.drawable.grid_lesson_item_bg);
            	//used for saving clicked day lessons
            	/*String unit_id = l.getLessonId();
					String starttime = l.getStarttime();
					String endtime = l.getEndtime();
					String color = l.getColorband();
					String code = l.getCode();
					String title = l.getTitle();
					String teacher = l.getTeacher();
					String location = l.getLocation();
					DatabaseHandler dbHandler = new DatabaseHandler(mContext);
            	//if((SavedDayLessons(mContext)==false)){
            		
            	//adding day lessons to db.	
            		if (Day_id.equals("1")){
            			Log.i("day....", Day_id);
            			
    					dbHandler.addLessons(Day_id,unit_id,code, title, starttime, endtime, color, teacher, location);
            			
            		}
            		else if (Day_id.equals("2")){
            			Log.i("day....", Day_id);
            			
    					dbHandler.addLessons(Day_id,unit_id,code, title, starttime, endtime, color, teacher, location);
            		}
            		else if (Day_id.equals("3")){
            			Log.i("day....", Day_id);
            			
    					dbHandler.addLessons(Day_id,unit_id,code, title, starttime, endtime, color, teacher, location);
            		}
            		else if (Day_id.equals("4")){
            			Log.i("day....", Day_id);
            			
    					dbHandler.addLessons(Day_id,unit_id,code, title, starttime, endtime, color, teacher, location);
            			
            		}
            		else if (Day_id.equals("5")){
            			Log.i("day....", Day_id);
            			
    					dbHandler.addLessons(Day_id,unit_id,code, title, starttime, endtime, color, teacher, location);
            			
            		}
            	//}
            		
            	    /*
            		if ((glowTime>1)&&(glowTime<=1800)&&(Day_id.equals(Today))){
            			holder.txtglow.setBackgroundColor(Color.parseColor("#00FF00"));
            		}*/
            		holder.code.setBackgroundColor(Color.parseColor(l.getColorband()));
            		holder.code.setText(l.getCode());
            		
            		Typeface font_c = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Medium.ttf");
            		holder.code.setTypeface(font_c);
    	        }
            	
            }
        	
        	
        }
          
        return convertView;
    }
    
    private String getLessonCode(int pos){
    	int i=0;
    	String jCode=null;
    	for(Lesson l : lessons) {
    		String startTime = l.getStarttime();
    		String endTime = l.getEndtime(); 
    		int durTotal = Integer.parseInt(endTime) - Integer.parseInt(startTime);
    		int reps = durTotal/100;
    		//Log.i("no. of grids", "startTime:" + startTime + " endTime:" + endTime);
    		//Log.i("no. of grids", "" + durOne);
    		int dbPos = Integer.parseInt(l.GetyPos());
    		int extPos = dbPos + (6*(reps-1));
    		
    		if(pos==dbPos || pos==extPos){ 
    			jCode = l.getCode();
    		}
    		
    	}
    	return jCode; 
    }
    
     
    /**
     * Function get Login status
     * */
    public boolean SavedDayLessons(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        int count = db.TbDayLessonsgetRowCount();
        if(count > 0){
            // user logged in
            return true;
        }
        return false;
    }
 
	
    
		  
    		
    	
    
}
