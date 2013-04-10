package com.br.timetabler.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.timetabler.R;
import com.br.timetabler.model.Lesson;
import com.br.timetabler.model.OneCell;
import com.br.timetabler.util.Log;

/**
 * This adapter is used to show our Video objects in a ListView
 * It hasn't got many memory optimisations, if your list is getting bigger or more complex
 * you may want to look at better using your view resources: http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/view/List14.html
 * @author paul.blundell
 */
public class GridAdapter extends BaseAdapter {
	List<Lesson> lessons;
	List<OneCell> gridCells;
    // An inflator to use when creating rows
    private LayoutInflater mInflater;
    Context mContext;
    int globStartTime=715, globEndTime=1915, duration=100;
    int totalCells;
    int learningDays = 6;
     
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
    }
	
	static class ViewHolder {
		public TextView code;
	}
 
    //@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null; // If convertView wasn't null it means we have already set it to our list_item_user_video so no need to do it again
        Date dt = null;
        if(convertView == null) {
            // This is the layout we are using for each row in our list anything you declare in this layout can then be referenced below
            convertView = mInflater.inflate(R.layout.single_grid_item, parent, false);
			
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.code = (TextView) convertView.findViewById(R.id.lessonCell); 
			convertView.setTag(viewHolder);
        }
		
		ViewHolder holder = (ViewHolder) convertView.getTag();
		
        // Get a single video from our list
        OneCell cell = gridCells.get(position);
        
        if(position % learningDays ==0) {
        	int t = ((position / learningDays)*100) + globStartTime;
        	String pn;
        	if(t>1200){
        		t = t - 1200;
        		pn = "pm";
        	} else 
        		pn = "am";        	
        	
        	String timeTitleStart = t/100 + pn ; 
        	String timeTitleEnd = (duration + t)/ 100 + pn;
        	holder.code.setText(timeTitleStart + " - " + timeTitleEnd);
        	holder.code.setTextSize(10);
        	holder.code.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        } else {
        	holder.code.setText(""+position);
        }
        /*for(Lesson l : lessons) {
        	if(cell.getGridPos() == Integer.parseInt(l.GetyPos())){ // || position % 5 == dayId){
	        	//Log.i("Day Id : " + l.getDayId());
	        	holder.code.setBackgroundResource(R.drawable.grid_lesson_item_bg);
	        	holder.code.setText(l.getCode());
	        } else {
	        	holder.code.setBackgroundResource(R.drawable.grid_empty_item_bg);
	        	holder.code.setText("");
	        }
        }*/
         
        return convertView;
    }
    
    /*public String[][] assignLessons() {
    	String [][] lessonsAttr =  new String[4][5];
    	String pos;
    	
    	for (int i=0; i<lessons.size(); i++) {
    		lessonsAttr[i][pos] = lessons.get(i).GetyPos();
    	}
    	return lessonsAttr;
    }*/
}
