package com.br.timetabler.src;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.br.timetabler.R;
import com.br.timetabler.adapter.UnitsAdapter;
import com.br.timetabler.model.Unit;

public class UnitSetupActivity extends SherlockActivity {
	ListView lstUnits;
	static Context mContext;
	Button btnSave;
	List<Unit> units;
	String unitsJSON;
	String[] selectedUnits;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.units_setup_activity);
	    
	    Intent in=getIntent();
		Bundle b=in.getExtras();
		
		this.unitsJSON = b.getString("units_json");
		try {
			setupJSON();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    lstUnits = (ListView) findViewById(R.id.lstUnits);
	    setupList();
	    btnSave = (Button)findViewById(R.id.btnSave);
	    mContext = this;
	    btnSave.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	        	Toast.makeText(getBaseContext(), " count list: " + lstUnits.getCount(), Toast.LENGTH_SHORT).show();
	        	String[] val = null;
	        	for (int i = 0; i <lstUnits.getCount() ; i++) {
	        		View vListSortOrder;
	        		vListSortOrder=lstUnits.getChildAt(i);     
	        		try{
	        			//EditText edit=(EditText)vListSortOrder.findViewById(R.id.share_comment_edit);
	        			//String temp=edit.getText().toString();
	        			CheckBox chk = (CheckBox) vListSortOrder.findViewById(R.id.check);
	        			if(chk.isChecked()) {
	        				val[i] = i+"";
	        				Log.i("unit items", "val[]" + val[i] + " :" + i);
	        			}

	        		} catch(Exception e){}
	        	}
	        	selectedUnits = val;	        	
	        }
	    });
	}
	
	public void setupList() {
		UnitsAdapter uAdapter = new UnitsAdapter(getApplicationContext(), units);
        lstUnits.setAdapter(uAdapter);
	}
	
	public void setupJSON() throws JSONException {
		Log.i("the unitsJSON data", unitsJSON);
		String jsonString = unitsJSON;
		JSONObject json = new JSONObject(jsonString);
		// Get are search result items
    	JSONArray jsonArray = json.getJSONObject("data").getJSONArray("units");            
        
        // Create a list to store are courses in
        units = new ArrayList<Unit>();
        // Loop round our JSON list of courses creating course objects to use within our app
        //units.add(new Unit("0", "", "Select Your School/Faculty"));            
    	for (int i = 0; i < jsonArray.length(); i++) {            
            JSONObject jsonObject = jsonArray.getJSONObject(i);	                
            String unit_id = jsonObject.getString("unit_id");
            String unit_acronyms = jsonObject.getString("unit_acronyms");
            String unit_names = jsonObject.getString("unit_names");
            
            // Create the school object and add it to our list
            units.add(new Unit(unit_id, unit_names, unit_acronyms, true));
        }
	}
}
