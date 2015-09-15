package com.br.timetabler.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.br.timetabler.R;
import com.br.timetabler.adapter.UnitsAdapter;
import com.br.timetabler.model.Unit;
import com.br.timetabler.util.DatabaseHandler_joe;
import com.br.timetabler.util.ServerInteractions;
import com.br.timetabler.widget.UnitsListView;

public class UnitSetupActivity extends ActionBarActivity {
	UnitsListView lstUnits;
	static Context mContext;
	Button btnSave;
	List<Unit> units;
	String unitsJSON, userId;
	String[] selectedUnits;
	HashMap<String, String> unitsMap;
	ServerInteractions server;
	JSONObject json_user;
    JSONObject json;
    String errorMsg;
    String res;
    DatabaseHandler_joe db; 
    RegisterUnitsTask regUnitsTask;
    private static String KEY_SUCCESS = "success";
    
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.units_setup_activity);
	    
	    Intent in=getIntent();
		Bundle b=in.getExtras();
		
		this.unitsJSON = b.getString("units_json");
		this.userId = b.getString("userId");
		try {
			setupJSON();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    lstUnits = (UnitsListView) findViewById(R.id.lstUnits);
	    lstUnits.setUnits(units);
	    //units = setupList();
	    btnSave = (Button)findViewById(R.id.btnSave);
	    mContext = this;
	    btnSave.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	        	Toast.makeText(getBaseContext(), " count list: " + lstUnits.getCount(), Toast.LENGTH_SHORT).show();
	        	int j = 0;
				String nv="";
	        	for (int i = 0; i <lstUnits.getCount() ; i++) {
	        		View vw;
	        		vw=lstUnits.getChildAt(i);
	        		
	        		CheckBox ch = (CheckBox) vw.findViewById(R.id.check);
	        		TextView edit=(TextView)vw.findViewById(R.id.hiddenUnitValue);
	        		String unitId = edit.getText().toString();
	        		
	        		if(ch.isChecked()) {
        				Log.i("check", "check:true");
        				//val[i] = i+"";
        				nv += unitId+",";
        				j++;
        			} else {
        				Log.i("check", "check:false");
        				nv += "";
        			}
	        		//unitsMap.put(i+"", nv);	        		
	        	}
	        	Log.i("unit list", nv);
	        	//selectedUnits = val;
	        	MyUserParams userparams = new MyUserParams(userId, nv);
	        	regUnitsTask = new RegisterUnitsTask();
	        	regUnitsTask.execute(userparams);
	        }
	    });
	}
	/*
	public void setupList() {
		UnitsAdapter uAdapter = new UnitsAdapter(getApplicationContext(), units);
        lstUnits.setAdapter(uAdapter);
	}*/
	
	public void setupJSON() throws JSONException {
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

	private class RegisterUnitsTask extends AsyncTask<MyUserParams, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(MyUserParams... params) {
			server = new ServerInteractions();
			
			String student_id = params[0].student_id;
			String unit_ids = params[0].unit_ids;
        	json = server.registerUnits(student_id, unit_ids);

            // check for login response
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    //registerErrorMsg.setText("");
                	res = json.getString(KEY_SUCCESS);
                    if(Integer.parseInt(res) == 1){
                        // user successfully registred, Store user details in SQLite Database
                        db = new DatabaseHandler_joe(getApplicationContext());
                        json_user = json.getJSONObject("data");
                    }else{
                        // Error in registration
                        
                        errorMsg = "Error occured in registration";
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
			return json_user; 
		}
		
		@Override
		protected void onPostExecute(JSONObject json_user) {
			
			try {
				//registerErrorMsg.setText(errorMsg);
				// Clear all previous data in database
	            //server.logoutUser(getApplicationContext());
	            if(Integer.parseInt(res) == 1){
		            // Launch Dashboard Screen
		            Intent dashboard = new Intent(getApplicationContext(), MainActivity.class);
		            // Close all views before launching Dashboard
		            dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		            startActivity(dashboard);
		            // Close Registration Screen
		            finish();
	            }
			} catch(Exception e){
				Log.e(this.getClass().getSimpleName(), "Error during saving your units", e);
				showErrorAlert();
			}
		}
		
		private void showErrorAlert() {
			
			try {
				Builder lBuilder = new AlertDialog.Builder(UnitSetupActivity.this);
				lBuilder.setTitle("Login Error");
				lBuilder.setCancelable(false);
				lBuilder.setMessage("Sorry, there was a problem saving your units");
	
				lBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
	
					@Override
					public void onClick(DialogInterface pDialog, int pWhich) {
						UnitSetupActivity.this.finish();
					}
					
				});
	
				AlertDialog lDialog = lBuilder.create();
				lDialog.show();
			} catch(Exception e){
				Log.e(this.getClass().getSimpleName(), "Problem showing error dialog.", e);
			}
		}
    	
    }
    
    private static class MyUserParams {
        String student_id, unit_ids;        

        MyUserParams(String student_id, String unit_ids) {
        	this.student_id = student_id;
        	this.unit_ids = unit_ids;
            
        }
    }
    
	
}
