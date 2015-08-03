package org.ekokonnect.reprohealth;

import org.ekokonnect.reprohealth.utils.AlertDialogManager;
import org.ekokonnect.reprohealth.utils.ConnectionDetector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;


public class PlaceListActivity extends FragmentActivity implements
		PlaceListFragment.Callbacks {

	private static final String TAG = "PlaceListActivity";
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_list);
		
		cd = new ConnectionDetector(getApplicationContext());		
		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			Toast.makeText(this, "No active internet network detected. " +
					"This application requires an active internet connection to work perfectly.",
					Toast.LENGTH_LONG).show();
//			alert.showAlertDialog(getApplicationContext(),
//					"Internet Connection Error",
//					"Please connect to working Internet connection", false);
//			showDialog();
			AlertDialogManager.showAlertDialog(getSupportFragmentManager(),
					"Network Error!", "Unable to Connect to the Internet. Please check your" +
							"connection and try again.", false);
			// stop executing code by finishinng activity
			finish();
		}

		if (findViewById(R.id.place_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((PlaceListFragment) getSupportFragmentManager().findFragmentById(
					R.id.place_list)).setActivateOnItemClick(true);
		}
		Log.d(TAG, "onCreate Called");

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link PlaceListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(PlaceDetailFragment.ARG_ITEM_ID, id);
			PlaceDetailFragment fragment = new PlaceDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.place_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, PlaceDetailActivity.class);
			detailIntent.putExtra(PlaceDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
}
