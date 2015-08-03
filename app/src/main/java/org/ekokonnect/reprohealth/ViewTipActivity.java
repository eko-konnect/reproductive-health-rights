package org.ekokonnect.reprohealth;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.TextView;

public class ViewTipActivity extends FragmentActivity {
	private ShareActionProvider mShareActionProvider;
	/*TextView mTipTitle, mTipDate, mTipContent, mTipAuthor;
	private String title, date, content, author;*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tip_detail);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString("title", getIntent()
					.getStringExtra("title"));
			arguments.putString("date", getIntent()
					.getStringExtra("date"));
			arguments.putString("content", getIntent()
					.getStringExtra("content"));
			arguments.putString("author", getIntent()
					.getStringExtra("author"));
			arguments.putString("img", getIntent()
					.getStringExtra("img"));
			
			TipDetailFragment fragment = new TipDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.tip_detail_container, fragment).commit();
		}
	}
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.viewtipactivity, menu);
		
		return true;
	}

	
}
