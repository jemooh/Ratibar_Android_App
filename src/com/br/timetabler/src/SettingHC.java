package com.br.timetabler.src;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceScreen;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;





import com.br.timetabler.R;
import com.br.timetabler.src.PreferenceListFragment.OnPreferenceAttachedListener;
//import com.ex.ntv.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


public class SettingHC extends SlidingFragmentActivity implements OnPreferenceAttachedListener   {
	private int mTitleRes;
	//protected PreferenceFragment mFrag;

	public SettingHC(int titleRes) {
		mTitleRes = titleRes;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(mTitleRes);
		// set the Behind View
		//setContentView(R.layout.testme);
		setBehindContentView(R.layout.menu_frame);
		 
	              
		/*
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
             .add(R.id.menu_frame, new SettingsFragment()).commit();
			
			
		}
		
		
		
			FragmentTransaction t = this.getSupportFragmentManager()
					.beginTransaction();
			mFrag = new PreferencesFragment();
			t.replace(R.id.menu_frame, mFrag);
			t.commit();
			
		} 
		*/
		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		sm.setMode(SlidingMenu.RIGHT);
 
		/*ActionBar actionBar = getSupportActionBar();
	     actionBar.setDisplayHomeAsUpEnabled(true);
	     //actionBar.setHomeAsUpIndicator(R.id.menu_settings);*/
	    
	    /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setIcon(R.id.menu_settings);*/
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		 
		 case R.id.menu_settings: //display description
      	     toggle();
			 return true;
      	   
      	   
      	   
      	   
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPreferenceAttached(PreferenceScreen root, int xmlId) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
}