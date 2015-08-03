package org.ekokonnect.reprohealth;

import java.util.ArrayList;
import java.util.Calendar;

import models.Tip;

import org.ekokonnect.reprohealth.adapters.TipListAdapter;
import org.ekokonnect.reprohealth.utils.AlertDialogManager;
import org.ekokonnect.reprohealth.utils.ConnectionDetector;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

public class TipListActivity extends FragmentActivity implements TipListFragment.Callbacks {
	//private TipDataSource tipDataSource;
	
	public ArrayList<Tip> tips;
	private static final String TAG = "TipListActivity";
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(!isSignedIn()){
			finish();
			startActivity(new Intent(getApplicationContext(), LoginActivity.class));
		}
		
		setContentView(R.layout.activity_tip_list);			

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
		Log.d(TAG, "onCreate Called");

		// TODO: If exposing deep links into your app, handle intents here.
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
