package com.br.timetabler.adapter;

import java.text.DecimalFormat;

import com.br.timetabler.R;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class DownAdapter extends CursorAdapter {
	DecimalFormat df = new DecimalFormat("#.##");
	public TextView code;		
	public TextView title;		
	public TextView lessontime;		
	public TextView teacher;		
	public TextView location;
	public TextView color;
   public DownAdapter(Context context, Cursor c) {
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
	  // Cursor cursor = db.rawQuery(selectQuery, null);
       // Move to first row
	   code = (TextView) view.findViewById(R.id.txtCodel); 
		title = (TextView) view.findViewById(R.id.txtTitle);
	    lessontime = (TextView) view.findViewById(R.id.txtTimes);
		teacher = (TextView) view.findViewById(R.id.txtTeacher);
		location = (TextView) view.findViewById(R.id.txtLocation);
		color = (TextView) view.findViewById(R.id.txtcolor);
	   
	   
       cursor.moveToFirst();
       if(cursor.getCount() > 0){
       // that means, take the data from the cursor and put it in views
    	  code.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));
          title.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(4))));
          lessontime.setText(createTime(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(6))), cursor.getString(cursor.getColumnIndex(cursor.getColumnName(7)))));
          teacher.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(5))));
          location.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(8))));
          // holder.color.setBackgroundColor(Color.parseColor(lesson.getColorband())); 
          color.setBackgroundColor(Color.parseColor((cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3)))))); 
      /* TextView textViewTime = (TextView) view.findViewById(R.id.texttime);
       textViewTime.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
       
       TextView textViewPersonName = (TextView) view.findViewById(R.id.textnote);
       textViewPersonName.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3))));
      */
   }}
   
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
   	String m = df.format(a) + " - " + df.format(c) + pn;
   	m = m.replaceAll("[.]", ":");
       return m;
	}
   
   
   
   
}