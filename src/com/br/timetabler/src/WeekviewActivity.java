package com.br.timetabler.src;


import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.br.timetabler.R;
import android.content.Intent;
import android.os.Bundle;
public class WeekviewActivity extends SherlockFragmentActivity {
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weekview_layout);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        
            case android.R.id.home://dsipaly video
                //Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(i);
                finish();
                break;
/**
            case R.id.menu_grid: //display description
            	Intent i1 = new Intent(getApplicationContext(), WeekviewActivity.class);
            	//i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i1);
                //finish();
                break;

            case R.id.menu_list: //display description
            	Intent i2 = new Intent(getApplicationContext(), DashboardActivity.class);
            	//i2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i2);
               // finish();
                break;

            case R.id.menu_assignments: //display reviews
            	Intent i3 = new Intent(getApplicationContext(), AssignmentsListActivity.class);
                startActivity(i3);
                //finish();
                break;*/
            
            case R.id.menu_settings: //display reviews
            	Intent i4 = new Intent(getApplicationContext(), Preferences.class);
                startActivity(i4);
                //finish();
                break;
            
            
        }
        return super.onOptionsItemSelected(item);
    }
}

