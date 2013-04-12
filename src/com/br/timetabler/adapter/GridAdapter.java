package com.br.timetabler.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.br.timetabler.R;
import com.br.timetabler.model.Lesson;
import com.br.timetabler.model.OneCell;

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
    double globStartTime=815, globEndTime=1715, duration=100.0;
    int totalCells;
    float learningDays = 6;
    DecimalFormat df = new DecimalFormat("#.##");
     
    /**
     * @param context this is the context that the list will be shown in - used to create new list rows
     * @param videos this is a list of videos to display
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
		public TextView code;
	}
 
    //@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null; // If convertView wasn't null it means we have already set it to our list_item_user_video so no need to do it again
        String timeTitleEnd = null;
        if(convertView == null) {
            // This is the layout we are using for each row in our list anything you declare in this layout can then be referenced below
            convertView = mInflater.inflate(R.layout.single_grid_item, parent, false);
			
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.code = (TextView) convertView.findViewById(R.id.lessonCell); 
			convertView.setTag(viewHolder);
        }
		
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
        	
        	holder.code.setText(timeTitleStart + " - " + timeTitleEnd);
        	holder.code.setTextSize(10);
        	holder.code.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        	holder.code.setClickable(false);
        } else {
        	/*for(int i=0; i<strlessons.length; i++) {
        		if(position == Integer.parseInt(strlessons[i][0])) {
        			holder.code.setBackgroundResource(R.drawable.grid_lesson_item_bg);
    	        	holder.code.setText(strlessons[i][1]);
        		}
        	} */
        	holder.code.setText(getLessonCode(position));
        	setLessonBg(position, holder.code);
        	//holder.code.setBackgroundResource(R.drawable.grid_lesson_item_bg);
        	/*for(Lesson l : lessons) {
            	if(position == Integer.parseInt(l.GetyPos())){ // || position % 5 == dayId){
    	        	//Log.i("Day Id : " + l.getDayId());
    	        	holder.code.setBackgroundResource(R.drawable.grid_lesson_item_bg);
    	        	holder.code.setText(l.getCode());
    	        } 
            }*/
        }
         
        return convertView;
    }
    
    private String getLessonCode(int pos){
    	int i=0;
    	String jCode=null;
    	for(Lesson l : lessons) {
    		int j = Integer.parseInt(l.GetyPos());
    		if(j==pos){
    			jCode = l.getCode();
    		}
    	}
    	return jCode;
    }
    
    private void setLessonBg(int pos, TextView v){
    	int i=0;
    	String jCode=null;
    	for(Lesson l : lessons) {
    		int j = Integer.parseInt(l.GetyPos());
    		String startTime = l.getStarttime();
    		String endTime = l.getEndtime();
    		int durTotal = Integer.parseInt(endTime) - Integer.parseInt(startTime);
    		int durOne = durTotal/100;
    		Log.i("no. of grids", "startTime:" + startTime + " endTime:" + endTime);
    		Log.i("no. of grids", "" + durOne);
    		
    		if(j==pos){
    			jCode = l.getCode();
    			v.setWidth(durOne * 65);
    			v.setBackgroundResource(R.drawable.grid_lesson_item_bg);
    		} else {
    			v.setBackgroundResource(R.drawable.grid_empty_item_bg);
    		}
    	}
    	
    	
    }
    
    /**public void assignLessons() {
    	String [][] lessonsAttr =  new String[2][lessons.size()];
    	for (int i=0; i<lessons.size(); i++) {
        	String[] row = new String[2];
        	row[0] = lessons.get(i).GetyPos();
        	row[1] = lessons.get(i).getCode();
        	 
        	lessonsAttr[i] = row;
    	}
    	strlessons = lessonsAttr;
    }*/
}
