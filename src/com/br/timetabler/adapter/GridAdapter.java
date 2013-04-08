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

/**
 * This adapter is used to show our Video objects in a ListView
 * It hasn't got many memory optimisations, if your list is getting bigger or more complex
 * you may want to look at better using your view resources: http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/view/List14.html
 * @author paul.blundell
 */
public class GridAdapter extends BaseAdapter {
	List<Lesson> lessons;
    // An inflator to use when creating rows
    private LayoutInflater mInflater;
    Context mContext;
     
    /**
     * @param context this is the context that the list will be shown in - used to create new list rows
     * @param videos this is a list of videos to display
     */
    public GridAdapter(Context context, List<Lesson> lessons) {
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
        Lesson lesson = lessons.get(position);
        
        holder.code.setText(lesson.getCode());
         
        return convertView;
    }
}
