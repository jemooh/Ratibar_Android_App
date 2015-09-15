package com.br.timetabler.src;

import com.br.timetabler.R;






import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class GriddDialogFragment extends DialogFragment  {

    String teacher = GridFragment.Dteacher;
    String location = GridFragment.Dlocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.grid_dialog_list, null, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
       
			
			Intent in=getActivity().getIntent();
			Bundle b=in.getExtras();
			
			this.teacher = b.getString("teacher");
			this.location = b.getString("location");
			
			TextView txtLocation = (TextView) view.findViewById(R.id.txtLocation);
			TextView txtTeacher = (TextView) view.findViewById(R.id.txtTeacher);
			Typeface font_c = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoCondensed-Light.ttf");
	        txtTeacher.setTypeface(font_c);
	        txtLocation.setTypeface(font_c);
	        
			//txtTime.setText(createTime(startTime, endTime));
			
			txtTeacher.setText(teacher);
			txtLocation.setText(location);
		return view;
			
	}
}