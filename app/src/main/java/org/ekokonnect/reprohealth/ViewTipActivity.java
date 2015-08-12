package org.ekokonnect.reprohealth;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ShareActionProvider;

public class ViewTipActivity extends AppCompatActivity {
	private ShareActionProvider mShareActionProvider;
	/*TextView mTipTitle, mTipDate, mTipContent, mTipAuthor;
	private String title, date, content, author;*/
	private Toolbar mToolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tip_detail);
        setupToolbar();

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

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.viewtipactivity, menu);
		
		return true;
	}

	
}
