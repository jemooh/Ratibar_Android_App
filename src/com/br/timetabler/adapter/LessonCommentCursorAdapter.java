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

				public class LessonCommentCursorAdapter extends CursorAdapter {
				   
					DecimalFormat df = new DecimalFormat("#.##");
					
				   public LessonCommentCursorAdapter(Context context, Cursor c) {
				       super(context, c);
				      
				   }
				
				   @Override
				   public View newView(Context context, Cursor cursor, ViewGroup parent) {
				       // when the view will be created for first time,
				       // we need to tell the adapters, how each item will look
				       LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				       View retView = inflater.inflate(R.layout.single_item_comment, parent, false);
				
				       return retView;
				   }
				
				   @Override
				   public void bindView(View view, Context context, Cursor cursor) {
				       // here we are setting our data
				       // that means, take the data from the cursor and put it in views
					   TextView time = (TextView) view.findViewById(R.id.txtDatetime); 
					   TextView comment = (TextView) view.findViewById(R.id.txtComment);
					   TextView Creator = (TextView) view.findViewById(R.id.txtCreator);
				      
				       time.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));
				       comment.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(5))));
				       Creator.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(4))));
				       
				       
				   
				   }
				   
}
				       