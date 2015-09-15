package com.br.timetabler.adapter;

import java.text.DecimalFormat;

import com.br.timetabler.R;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

			public class LessonNotesCursorAdapter extends CursorAdapter {
			   
				DecimalFormat df = new DecimalFormat("#.##");
				
			   public LessonNotesCursorAdapter(Context context, Cursor c) {
			       super(context, c);
			      
			   }
			
			   @Override
			   public View newView(Context context, Cursor cursor, ViewGroup parent) {
			       // when the view will be created for first time,
			       // we need to tell the adapters, how each item will look
			       LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			       View retView = inflater.inflate(R.layout.note_single_item, parent, false);
			
			       return retView;
			   }
			
			   @Override
			   public void bindView(View view, Context context, Cursor cursor) {
			       // here we are setting our data
			       // that means, take the data from the cursor and put it in views
				   TextView time = (TextView) view.findViewById(R.id.texttime); 
				   TextView note = (TextView) view.findViewById(R.id.textnote);
			      
			       time.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
			       note.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3))));
			       
			       
			       
			       
			   }
			}
			       