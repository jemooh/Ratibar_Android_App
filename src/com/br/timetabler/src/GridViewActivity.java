package com.br.timetabler.src;

import com.br.timetabler.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class GridViewActivity extends Activity {
	GridView gridView;
	 
	static final String[] MOBILE_OS = new String[] { 
		"Android", "iOS","Windows", "Blackberry", "Android", "iOS","Windows", "Blackberry", "Android", "iOS","Windows", "Blackberry", "Android", "iOS","Windows", "Blackberry", };
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
 
		//gridView = (GridView) findViewById(R.id.lessonsGridView);
 
		gridView.setAdapter(new ImageAdapter(this, MOBILE_OS));
 
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText( getApplicationContext(), ((TextView) v.findViewById(R.id.grid_item_label)).getText(), Toast.LENGTH_SHORT).show();
 
			}
		});
 
	}
}
