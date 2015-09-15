package com.br.timetabler.adapter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.br.timetabler.R;
import com.br.timetabler.model.Assignment;

/**
 * This adapter is used to show our Video objects in a ListView
 * It hasn't got many memory optimisations, if your list is getting bigger or more complex
 * you may want to look at better using your view resources: http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/view/List14.html
 * @author paul.blundell
 */
public class AssignmentsAdapter extends BaseAdapter {
	List<Assignment> assignments;
	DecimalFormat df = new DecimalFormat("#.##");
    // An inflator to use when creating rows
    private LayoutInflater mInflater;
    Context mContext;
     
    /**
     * @param context this is the context that the list will be shown in - used to create new list rows
     * @param videos this is a list of videos to display
     */
    public AssignmentsAdapter(Context context, List<Assignment> assignments) {
        this.assignments = assignments;
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }
 
    //@Override
    public int getCount() {
        return assignments.size();
    }
 
    //@Override
    public Object getItem(int position) {
        return assignments.get(position);
    }
 
    //@Override
    public long getItemId(int position) {
        return position;
    }
	
	static class ViewHolder {
		public TextView code;		
		public TextView title;		
		public TextView dateCreated;		
		public TextView dateDue;		
	}
 
    //@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null; // If convertView wasn't null it means we have already set it to our list_item_user_video so no need to do it again
		if(convertView == null) {
            // This is the layout we are using for each row in our list anything you declare in this layout can then be referenced below
            convertView = mInflater.inflate(R.layout.single_item_assignment, parent, false);
			
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.code = (TextView) convertView.findViewById(R.id.txtCodel); 
			viewHolder.title = (TextView) convertView.findViewById(R.id.txtTitle);
			viewHolder.dateCreated = (TextView) convertView.findViewById(R.id.dateCreated);
			viewHolder.dateDue = (TextView) convertView.findViewById(R.id.dateDue);
			convertView.setTag(viewHolder);
        }
		
		ViewHolder holder = (ViewHolder) convertView.getTag();
		
        // Get a single video from our list
        Assignment assignment = assignments.get(position);
        //change date formate
         Calendar c = Calendar.getInstance();
        int t = Integer.parseInt(assignment.getDateCreated());
	    c.setTimeInMillis(t*1000L);
	    SimpleDateFormat currentDate = new SimpleDateFormat("MM/dd/yy HH:mm ");
         String assDateCreated = currentDate.format(c.getTime());
         
         Calendar cal = Calendar.getInstance();
            int t2 = Integer.parseInt(assignment.getDateDue());
		    cal.setTimeInMillis(t2*1000L);
		    SimpleDateFormat currentDat = new SimpleDateFormat("MM/dd/yy HH:mm ");
		   //set Roboto font
             String assDateDue = currentDat.format(cal.getTime());
             Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Light.ttf");
 	        Typeface font_a = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Medium.ttf");
 	        Typeface font_b = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Regular.ttf");
 	        holder.code.setTypeface(font_b);
 	        holder.title.setTypeface(font_a);
 	        holder.dateCreated.setTypeface(font_b);
 	        holder.dateDue.setTypeface(font_b);
        
             
        holder.code.setText(assignment.getUnitCode());
        holder.title.setText(assignment.getDescription());
        holder.dateCreated.setText("Created On " + assDateCreated);
        holder.dateDue.setText("Due On " + assDateDue);
         
        return convertView;
    }
    
    public String createTime(String end) {
		String pn= "am";
		double c = Float.parseFloat(end)/100; 
    	
		if(c>12){
    		pn= "pm";
    		c = c-12;
    		if(c<0.59)
    			c=c+1;
    	} 
    	String m = df.format(c) + pn;
    	m = m.replaceAll("[.]", ":");
        return m;
	}
}
