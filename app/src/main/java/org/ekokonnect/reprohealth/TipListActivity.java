package org.ekokonnect.reprohealth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import java.util.ArrayList;

import models.Tip;

public class TipListActivity extends AppCompatActivity implements TipListFragment.Callbacks {
	//private TipDataSource tipDataSource;
	
	public ArrayList<Tip> tips;
	private static final String TAG = "TipListActivity";
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	private Toolbar mToolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(!isSignedIn()){
			finish();
			startActivity(new Intent(getApplicationContext(), LoginActivity.class));
		}
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUND‌​S);
		setContentView(R.layout.activity_tip_list);
        setupToolbar();

		if (findViewById(R.id.tip_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((TipListFragment) getSupportFragmentManager().findFragmentById(
					R.id.tip_list)).setActivateOnItemClick(true);
		}

	}

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
    }


    private boolean isSignedIn(){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean isSignedIn = sharedPreferences.getBoolean("isSignedIn", false);
		return isSignedIn;
		
//		return true;
	}
		
	
	@Override
	public void onItemSelected(int id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			
			Tip tip = tips.get(id);
//			Intent intent = new Intent(getApplicationContext(), ViewTipActivity.class);
//			intent.putExtra("title", tip.getTitle());
			
			arguments.putString("title", tip.getTitle());
			arguments.putString("date", tip.getDate());
			arguments.putString("content", tip.getContent());
			arguments.putString("author", tip.getAuthor());
			arguments.putString("img", tip.getImgUrl());
			
			TipDetailFragment fragment = new TipDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.tip_detail_container, fragment).commit();

		} else {
			Tip tip = tips.get(id);
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, ViewTipActivity.class);
			detailIntent.putExtra("title", tip.getTitle());
			detailIntent.putExtra("date", tip.getDate());
			detailIntent.putExtra("content", tip.getContent());
			detailIntent.putExtra("author", tip.getAuthor());
			detailIntent.putExtra("img", tip.getImgUrl());
			
			startActivity(detailIntent);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
	
}
