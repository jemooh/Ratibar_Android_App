package com.br.timetabler.adapter;

import java.util.List;

import android.content.Context;
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
    int globStartTime=700, globEndTime=1900, duration=100;
    int totalCells;
    int learningDays = 5;
     
    /**
     * @param context this is the context that the list will be shown in - used to create new list rows
     * @param videos this is a list of videos to display
     */
    public GridAdapter(Context context, List<Lesson> lessons, List<OneCell> gridCells) {
        this.lessons = lessons;
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
	
	static class ViewHolder {
		public TextView code;		
		public TextView title;		
		public TextView lessontime;		
		public TextView teacher;		
		public TextView location;		
	}
 
    //@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null; // If convertView wasn't null it means we have already set it to our list_item_user_video so no need to do it again
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
        holder.code.setClickable(false);
    	holder.code.setFocusable(false);
        
        //Log.i("position : " + position);
        //Lesson lesson = lessons.get(position);
        for(Lesson l : lessons) {
        	String lessonBegins = l.getStarttime();
        	int dayId = Integer.parseInt(l.getDayId());
        	int yRange = Integer.parseInt(lessonBegins) - globStartTime;
        	int yColumnCount = yRange / duration;
        	int yCellCount = yColumnCount * learningDays;
        	int yPosition = yCellCount + dayId; //gives the number of cell to count from 0,0
        	//Log.i("dayId : " + dayId);
	        if(yPosition==cell.getGridPos()){ // || position % 5 == dayId){
	        	//Log.i("Day Id : " + l.getDayId());
	        	holder.code.setBackgroundResource(R.drawable.item_background_focused);
	        	holder.code.setText(l.getCode());
	        	holder.code.setClickable(true);
	        	holder.code.setFocusable(true);
	        } 
        }
        
        
         
        return convertView;
    }
    
    public void assignLessons() {
    	
    }
}
