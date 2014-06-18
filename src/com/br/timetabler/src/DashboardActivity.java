package com.br.timetabler.src;

import com.br.timetabler.R;

import java.io.IOException;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;


public class DashboardActivity extends SherlockFragmentActivity {
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	     setContentView(R.layout.dayview_layout);
	}
		/**

		ActionBar actionBar = getSupportActionBar();

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.Tab newTab0 = getSupportActionBar().newTab();
		newTab0.setIcon(R.drawable.collections_view_as_list);
		TabListener<ListDayLessonsFragment> tl = new TabListener<ListDayLessonsFragment>(
				this, "Dayview", ListDayLessonsFragment.class);
		newTab0.setTabListener(tl);
		actionBar.addTab(newTab0);

		ActionBar.Tab newTab1 = getSupportActionBar().newTab();
		newTab1.setIcon(R.drawable.collections_view_as_grid);
		TabListener<GridFragment> tl2 = new TabListener<GridFragment>(
				this, "Weekview", GridFragment.class);

		
		ActionBar.Tab newTab2 = getSupportActionBar().newTab();
		newTab2.setIcon(R.drawable.action_assignments);
		TabListener<AssignmentsFragment> tl3 = new TabListener<AssignmentsFragment>(
				this, "Assignments", AssignmentsFragment.class);
		
		//adding tabs
		newTab1.setTabListener(tl2);
		newTab2.setTabListener(tl3);
		actionBar.addTab(newTab1);
		actionBar.addTab(newTab2);
		
		
		
	

	}
		
		
 
    
			   //TabHost tabHost = getTabHost();
			   TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
			   // Tab for week
			   TabSpec listspec = tabHost.newTabSpec("Dayview");
			   // setting Title and Icon for the Tab
			   listspec.setIndicator("Dayview", getResources().getDrawable(R.drawable.collections_view_as_list));
			   Intent dayIntent = new Intent(this, ListDayLessons.class);
			   listspec.setContent(dayIntent);
			   
			   // Tab for week
			   TabSpec gridspec = tabHost.newTabSpec("Weekview");
			   // setting Title and Icon for the Tab
			   gridspec.setIndicator("Weekview", getResources().getDrawable(R.drawable.collections_view_as_grid));
			   Intent gridIntent = new Intent(this, MainActivity.class);
			   gridspec.setContent(gridIntent);
			   
			 
			   // Adding all TabSpec to TabHost
			   tabHost.addTab(listspec); // Adding day tab
			   tabHost.addTab(gridspec); // Adding grid tab
			   
		}
 
  
  TabHost tabHost = getTabHost();
		   
		   // Tab for list of day lessons
		   View tabView = createTabView(this, "comments");
		   TabSpec comspec = tabHost.newTabSpec("comments");
		   comspec.setIndicator(tabView);
		   Intent gridIntent = new Intent(this, One_Lesson_Comments.class);
		   comspec.setContent(gridIntent);
		   
		   
		   // Tab for week
		   tabView = createTabView(this, "Assignments");
		   TabSpec assignspec = tabHost.newTabSpec("Assignments");
		   // setting Title and Icon for the Tab
		   assignspec.setIndicator(tabView);
		   Intent assignIntent = new Intent(this, One_Lesson_Assignment.class);
		   assignspec.setContent(assignIntent);
		   
		   // Tab for week
		   tabView = createTabView(this, "Downloads");
		   TabSpec downspec = tabHost.newTabSpec("Downloads");
		   // setting Title and Icon for the Tab
		   downspec.setIndicator(tabView);
		   Intent downIntent = new Intent(this, One_Lesson_Notes.class);
		   downspec.setContent(downIntent);
		   
		   // Tab for week
		   tabView = createTabView(this, "Notes");
		   TabSpec notspec = tabHost.newTabSpec("Notes");
		   // setting Title and Icon for the Tab
		   notspec.setIndicator(tabView);
		   Intent notIntent = new Intent(this, One_Lesson_Notes.class);
		   notspec.setContent(notIntent);
		   
		 	   // Adding all TabSpec to TabHost
		   tabHost.addTab(comspec); // Adding grid tab
		   tabHost.addTab(assignspec); // Adding day tab
		   tabHost.addTab(downspec); // Adding grid tab
		   tabHost.addTab(notspec); // Adding day tab
  
  
  
}


	private class TabListener<T extends Fragment> implements
			ActionBar.TabListener {
		private Fragment mFragment;
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;

		/**
		 * Constructor used each time a new tab is created.
		 * 
		 * @param activity
		 *            The host Activity, used to instantiate the fragment
		 * @param tag
		 *            The identifier tag for the fragment
		 * @param clz
		 *            The fragment's Class, used to instantiate the fragment
		*/
		/**public TabListener(Activity activity, String tag, Class<T> clz) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// Check if the fragment is already initialized
			if (mFragment == null) {
				// If not, instantiate and add it to the activity
				mFragment = Fragment.instantiate(mActivity, mClass.getName());
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				// If it exists, simply attach it in order to show it
				ft.show(mFragment);
			}
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				// Detach the fragment, because another one is being attached
				ft.hide(mFragment);
			}
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// User selected the already selected tab. Usually do nothing.
		}
	} */
    
    
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
          
            case R.id.menu_grid: //display description
            	Intent i1 = new Intent(getApplicationContext(), MainActivity.class);
            	//i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i1);
                //finish();
                break;

            case R.id.menu_list: //display description
            	Intent i2 = new Intent(getApplicationContext(), DashboardActivity.class);
            	//i2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i2);
                //finish();
                break;

            case R.id.menu_assignments: //display reviews
            	Intent i3 = new Intent(getApplicationContext(), AssignmentsListActivity.class);
                startActivity(i3);
                //finish();
                break;
                
        
            case R.id.menu_settings: //display reviews
            	Intent i4 = new Intent(getApplicationContext(), Preferences.class);
                startActivity(i4);
                //finish();
                break;
            
            
        }
        return super.onOptionsItemSelected(item);
    }
	
}

