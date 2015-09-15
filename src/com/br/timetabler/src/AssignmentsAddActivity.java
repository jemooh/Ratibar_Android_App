package com.br.timetabler.src;

import com.br.timetabler.R;
import com.br.timetabler.src.PreferenceListFragment.OnPreferenceAttachedListener;

import android.os.Bundle;
import android.preference.PreferenceScreen;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

 public class AssignmentsAddActivity extends ActionBarActivity implements OnPreferenceAttachedListener {
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testme);
        if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new SettingsFragment()).commit();
		}
        
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
 
    

	@Override
	public void onPreferenceAttached(PreferenceScreen root, int xmlId) {
		// TODO Auto-generated method stub
		
	}
}