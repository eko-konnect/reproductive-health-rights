package org.ekokonnect.reprohealth;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;


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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar_settings, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
