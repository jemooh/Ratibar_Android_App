package com.br.timetabler.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.timetabler.R;
import com.br.timetabler.model.Lesson;

/**
 * This adapter is used to show our Video objects in a ListView
 * It hasn't got many memory optimisations, if your list is getting bigger or more complex
 * you may want to look at better using your view resources: http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/view/List14.html
 * @author paul.blundell
 */
public class TodayLessonsAdapter extends BaseAdapter {
	List<Lesson> lessons;
	DecimalFormat df = new DecimalFormat("#.##");
    // An inflator to use when creating rows
    private LayoutInflater mInflater;
    Context mContext;
     
    /**
     * @param context this is the context that the list will be shown in - used to create new list rows
     * @param videos this is a list of videos to display
     */
    public TodayLessonsAdapter(Context context, List<Lesson> lessons) {
        this.lessons = lessons;
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }
 
    
    //@Override
    public int getCount() {
        return lessons.size();
    }
 
    //@Override
    public Object getItem(int position) {
        return lessons.get(position);
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
		public TextView color;
		public TextView txtPm;
	}
 
    //@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null; // If convertView wasn't null it means we have already set it to our list_item_user_video so no need to do it again
		if(convertView == null) {
            // This is the layout we are using for each row in our list anything you declare in this layout can then be referenced below
            convertView = mInflater.inflate(R.layout.single_item_lesson, parent, false);
			
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.code = (TextView) convertView.findViewById(R.id.txtCodel); 
			viewHolder.title = (TextView) convertView.findViewById(R.id.txtTitle);
			viewHolder.lessontime = (TextView) convertView.findViewById(R.id.txtTimes);
			viewHolder.teacher = (TextView) convertView.findViewById(R.id.txtTeacher);
			viewHolder.location = (TextView) convertView.findViewById(R.id.txtLocation);
			viewHolder.txtPm = (TextView) convertView.findViewById(R.id.textView1);
			viewHolder.color = (TextView) convertView.findViewById(R.id.txtcolor);
			convertView.setTag(viewHolder);
        }
		
		ViewHolder holder = (ViewHolder) convertView.getTag();
	        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Light.ttf");
	        Typeface font_a = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Medium.ttf");
	        Typeface font_b = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Regular.ttf");
	        Typeface font_c = Typeface.createFromAsset(mContext.getAssets(), "fonts/DistProTh.otf");
	        Typeface font_d = Typeface.createFromAsset(mContext.getAssets(), "fonts/DroidSans.ttf");
	        holder.code.setTypeface(font_b);
	        holder.lessontime.setTypeface(font_c);
	        holder.title.setTypeface(font_a);
	        holder.teacher.setTypeface(font_b);
	        holder.location.setTypeface(font_b);
	        holder.txtPm.setTypeface(font_b);
		
		
        // Get a single video from our list
        Lesson lesson = lessons.get(position);
        
        holder.code.setText(lesson.getCode());
        holder.title.setText(lesson.getTitle());
        //holder.lessontime.setText(createTime(lesson.getStarttime(), lesson.getEndtime()));
        holder.teacher.setText(lesson.getTeacher());
        holder.location.setText(lesson.getLocation());
       // holder.color.setBackgroundColor(Color.parseColor(lesson.getColorband())); 
        //holder.code.setTextColor(Color.parseColor(lesson.getColorband()));
        holder.color.setBackgroundColor(Color.parseColor(lesson.getColorband()));
        //text.setTextColor(Color.parseColor("#3F00FF"));
        //for(Lesson l : lessons) {
    		String startTime = lesson.getStarttime();
    		String endTime = lesson.getEndtime();
    		String pn= "am";
    		double a = Float.parseFloat(startTime)/100; 
    		double c = Float.parseFloat(endTime)/100; 
    		
    		if (a>12){
	    		//pn="am";
	    		holder.txtPm.setText(pn);	
	    		a = a-12;
	    		if(a<1)
	    			a=a+1;
    		}
    		
    		if (c>12){
        		pn="pm"	;
        		holder.txtPm.setText(pn);
        		c = c-12;
        		if(c<0.59)
        			c=c+1;
    		}else{
    			pn="am"	;
    		    holder.txtPm.setText(pn);
    		}
    		String m = df.format(a) + "-" + df.format(c);
    		m = m.replaceAll("[.]", ":");
    		holder.lessontime.setText(m);
    		
    		
        
      //  }
        
        
        return convertView;
    }
    
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
    	String m = df.format(a) + "-" + df.format(c) + pn;
    	m = m.replaceAll("[.]", ":");
        return m;
	}
}
