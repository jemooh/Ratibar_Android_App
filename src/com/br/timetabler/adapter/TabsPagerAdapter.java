package com.br.timetabler.adapter;

import com.br.timetabler.src.One_Lesson_Assignment_Fragment;
import com.br.timetabler.src.One_Lesson_Comments_Fragment;
import com.br.timetabler.src.One_Lesson_Downloads_Fragment;
import com.br.timetabler.src.One_Lesson_Notes_Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
	 
	public class TabsPagerAdapter extends FragmentPagerAdapter {
	 
	    public TabsPagerAdapter(FragmentManager fm) {
	        super(fm);
	    }
	 
	    @Override
	    public Fragment getItem(int index) {
	 
	        switch (index) {
	        case 0:
	            // Top Rated fragment activity
	            return new One_Lesson_Comments_Fragment();
	        case 1:
	            // Games fragment activity
	            return new One_Lesson_Assignment_Fragment();
	        case 2:
	            // Movies fragment activity
	            return new One_Lesson_Downloads_Fragment();
	            
	        case 3:
	            // Movies fragment activity
	            return new One_Lesson_Notes_Fragment();
	        }
	 
	        return null;
	    }
	 
	    @Override
	    public int getCount() {
	        // get item count - equal to number of tabs
	        return 4;
	    }
	 
}

