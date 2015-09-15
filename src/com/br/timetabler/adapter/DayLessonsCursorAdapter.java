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

			public class DayLessonsCursorAdapter extends CursorAdapter {
			   
				DecimalFormat df = new DecimalFormat("#.##");
				
			   public DayLessonsCursorAdapter(Context context, Cursor c) {
			       super(context, c);
			      
			   }
			
			   @Override
			   public View newView(Context context, Cursor cursor, ViewGroup parent) {
			       // when the view will be created for first time,
			       // we need to tell the adapters, how each item will look
			       LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			       View retView = inflater.inflate(R.layout.single_item_lesson, parent, false);
			
			       return retView;
			   }
			
			   @Override
			   public void bindView(View view, Context context, Cursor cursor) {
			       // here we are setting our data
			       // that means, take the data from the cursor and put it in views
				   TextView code = (TextView) view.findViewById(R.id.txtCodel); 
				   TextView title = (TextView) view.findViewById(R.id.txtTitle);
				   TextView lessontime = (TextView) view.findViewById(R.id.txtTimes);
				   TextView teacher = (TextView) view.findViewById(R.id.txtTeacher);
				   TextView location = (TextView) view.findViewById(R.id.txtLocation);
				   TextView txtPm = (TextView) view.findViewById(R.id.textView1);
				   TextView color = (TextView) view.findViewById(R.id.txtcolor);
			      
			       code.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3))));
			       title.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(4))));
			       teacher.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(8))));
			       location.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(9))));
			       color.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(7))));
			       
			       Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
			        Typeface font_a = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
			        Typeface font_b = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
			        Typeface font_c = Typeface.createFromAsset(context.getAssets(), "fonts/DistProTh.otf");
			        Typeface font_d = Typeface.createFromAsset(context.getAssets(), "fonts/DroidSans.ttf");
			        code.setTypeface(font_b);
			        lessontime.setTypeface(font_c);
			        title.setTypeface(font_a);
			        teacher.setTypeface(font_b);
			        location.setTypeface(font_b);
			        txtPm.setTypeface(font_b);
			       
			       
						       String startTime = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(5)));
								String endTime = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(6)));
								String pn= "am";
								double a = Float.parseFloat(startTime)/100; 
								double c = Float.parseFloat(endTime)/100; 
								
								if (a>12){
						   		//pn="am";
						   		txtPm.setText(pn);	
						   		a = a-12;
						   		if(a<1)
						   			a=a+1;
								}
								
								if (c>12){
						   		pn="pm"	;
						   		txtPm.setText(pn);
						   		c = c-12;
						   		if(c<0.59)
						   			c=c+1;
								}else{
									pn="am"	;
								   txtPm.setText(pn);
								}
								String m = df.format(a) + "-" + df.format(c);
								m = m.replaceAll("[.]", ":");
								lessontime.setText(m);
			       
			
			   }
			}
