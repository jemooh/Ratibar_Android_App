package com.br.timetabler.adapter;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.br.timetabler.R;
import com.br.timetabler.model.Notes;

/**
 * This adapter is used to show our Video objects in a ListView
 * It hasn't got many memory optimisations, if your list is getting bigger or more complex
 * you may want to look at better using your view resources: http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/view/List14.html
 * @author paul.blundell
 */
public class NoteArrayAdapter extends BaseAdapter {
	List<Notes> notes;
    // An inflator to use when creating rows
    private LayoutInflater mInflater;
    Context mContext;
     
    /**
     * @param context this is the context that the list will be shown in - used to create new list rows
     * @param videos this is a list of videos to display
     */
    public NoteArrayAdapter(Context context, List<Notes> notes) {
        this.notes = notes;
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }
 
    //@Override
    public int getCount() {
        return notes.size();
    }
 
    //@Override
    public Object getItem(int position) {
        return notes.get(position);
    }
 
    //@Override
    public long getItemId(int position) {
        return position;
    }
	
	static class ViewHolder {
		public TextView noteText;
		public TextView noteTime;
	}
 
    //@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null; // If convertView wasn't null it means we have already set it to our list_item_user_video so no need to do it again
		if(convertView == null) {
            // This is the layout we are using for each row in our list anything you declare in this layout can then be referenced below
            convertView = mInflater.inflate(R.layout.note_single_item, parent, false);
			
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.noteText = (TextView) convertView.findViewById(R.id.textnote);
			viewHolder.noteTime = (TextView) convertView.findViewById(R.id.texttime);
			convertView.setTag(viewHolder);
        }
		
		ViewHolder holder = (ViewHolder) convertView.getTag();
		
        // Get a single video from our list
		Notes note = notes.get(position);
        
        holder.noteText.setText(note.getNoteText());
        holder.noteTime.setText(note.getNoteTime());
         
        return convertView;
    }
}
