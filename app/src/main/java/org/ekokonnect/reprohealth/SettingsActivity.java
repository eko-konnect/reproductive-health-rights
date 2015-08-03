package org.ekokonnect.reprohealth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;



public class SettingsActivity extends PreferenceActivity{
	public static final String KEY_PREF_LOGOUT = "pref_logout";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_settings);
		for(int i=0;i<getPreferenceScreen().getPreferenceCount(); i++){
			initSummary(getPreferenceScreen().getPreference(i));
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	private void showDefaultValue(Preference p){
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		if(p instanceof EditTextPreference){
			EditTextPreference editPref = (EditTextPreference)p;
			//editPref.setDefaultValue(sharedPref.getString(editPref.getKey(), "Not Set"));
			editPref.setSummary(sharedPref.getString(editPref.getKey(), "Not Set"));
			//editPref.getEditText().setText(sharedPref.getString(editPref.getKey(), "Not Set"));
		}
	}
	private void initSummary(Preference p) {
	      if (p instanceof PreferenceCategory) {
	        PreferenceCategory cat = (PreferenceCategory) p;
	        for (int i = 0; i < cat.getPreferenceCount(); i++) {
	          initSummary(cat.getPreference(i));
	        }
	      } else {
	        showDefaultValue(p);
	      }
	    }
	


}
