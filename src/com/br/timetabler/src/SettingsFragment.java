package com.br.timetabler.src;

import com.br.timetabler.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
		public class SettingsFragment extends PreferenceListFragment implements
		SharedPreferences.OnSharedPreferenceChangeListener,
		PreferenceListFragment.OnPreferenceAttachedListener {
		public static final String SHARED_PREFS_NAME = "settings";
		
		@Override
		public void onCreate(Bundle icicle) {
		
		super.onCreate(icicle);
		PreferenceManager preferenceManager = getPreferenceManager();
		preferenceManager.setSharedPreferencesName(SHARED_PREFS_NAME);
		addPreferencesFromResource(R.xml.preferences);
		preferenceManager.getSharedPreferences()
		        .registerOnSharedPreferenceChangeListener(this);
		
		}
		
		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
		    String key) {
		}
		
		@Override
		public void onPreferenceAttached(PreferenceScreen root, int xmlId) {
		if (root == null)
		    return;
		}
		}