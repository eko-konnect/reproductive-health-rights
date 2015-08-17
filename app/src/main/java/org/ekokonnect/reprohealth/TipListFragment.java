package org.ekokonnect.reprohealth;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import com.facebook.login.LoginManager;

import org.ekokonnect.reprohealth.adapters.TipListAdapter;

import java.util.Calendar;
//import org.ekokonnect.reprohealth.TipListActivity.DatePickerFragment;

/**
 * A list fragment representing a list of Places. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link PlaceDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class TipListFragment extends ListFragment {

	private static final String TAG = "TipListFragment";
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;
	
	private View listContainer;
	
	ListView listview;
	
	TipListAdapter adapter;
	private Menu optionsMenu;
	private ProgressDialog pd;
	private Button safeSexButton;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(int id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(int id) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TipListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		adapter = new TipListAdapter(this);
		setListAdapter(adapter);
		setHasOptionsMenu(true);
	}	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.place_list_layout, container, false);	

		safeSexButton = (Button)view.findViewById(R.id.button1);
		safeSexButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				safeSexClick();
			}
		});
		
		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
Log.e(TAG, "Activated Position: "+ STATE_ACTIVATED_POSITION);
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
		
		return view;
	}
	
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onActivityCreated(savedInstanceState);
//		adapter.refresh();
//	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		adapter.refresh();
	}
	
	private void safeSexClick(){
		Calendar cal = getCycleDate(getActivity().getApplicationContext());
		if ( cal.get(Calendar.YEAR)== 2){
			showDatePickerDialog();
		} else {
			startActivity(new Intent(getActivity().getApplicationContext(), PeriodCalendarActivity.class));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

//		String reference = ((TextView) view.findViewById(R.id.reference)).getText().toString();
        
		
		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.onItemSelected(position);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		Log.e(TAG, "Activate OnClick");
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		this.optionsMenu = menu;
//		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.tiplistactivity, menu);
//		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.tiplist:
			adapter.refresh();
			return true;										
		case R.id.calendar:
			safeSexClick();			
			return true;
		case R.id.locate:
			startActivity(new Intent(getActivity().getApplicationContext(), PlaceListActivity.class));
			return true;
		case R.id.chat_menu:
			startActivity(new Intent(getActivity().getApplicationContext(), ChatActivity.class));
			return true;
		case R.id.logout:
			logOut();
			return true;
		case R.id.settings:
			startActivity(new Intent(getActivity().getApplicationContext(), SettingsActivity.class));
			return true;
		case R.id.help:
			startActivity(new Intent(getActivity().getApplicationContext(), AboutActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	
	public void logOut(){
		Context appContext = getActivity().getApplicationContext();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
		Editor prefEditor = preferences.edit();
		prefEditor.clear().apply();

        LoginManager.getInstance().logOut();
		getActivity().finish();
		
		// TODO: Fix Logout Issue
//		Intent intent = new Intent(Intent.ACTION_MAIN);
//		intent.addCategory(Intent.CATEGORY_HOME);
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
		
	}
	
	
	public static class DatePickerFragment extends DialogFragment
	    implements DatePickerDialog.OnDateSetListener {
	
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			
			// Create a new instance of DatePickerDialog and return it
			DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this, year, month, day);
			datePicker.setTitle("Select your Last Menstrual Bleeding Date");
			return datePicker;
		}
		
		public void onDateSet(DatePicker view, int year, int month, int day) {
		// Do something with the date chosen by the user
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt("cycleday", day);
			editor.putInt("cyclemonth", month);
			editor.putInt("cycleyear", year);
			editor.commit();
			startActivity(new Intent(getActivity().getApplicationContext(), PeriodCalendarActivity.class));
		}
	}
	public void showDatePickerDialog() {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getActivity().getFragmentManager(), "datePicker");
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setRefreshActionButtonState(final boolean refreshing) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
		    if (optionsMenu != null) {
		        final MenuItem refreshItem = optionsMenu
		            .findItem(R.id.tiplist);
		        if (refreshItem != null) {
		            if (refreshing) {
		                refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
		            } else {
		                refreshItem.setActionView(null);
		            }
		        }
		    }
		} else {
			if (refreshing)
				pd = ProgressDialog.show(getActivity(),"Refreshing","Please Wait...");
			else
				pd.dismiss();
		}
	}
	
	public static Calendar getCycleDate(Context context){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		int cycleDate = sharedPreferences.getInt("cycleday", 0);
		int cycleMonth = sharedPreferences.getInt("cyclemonth", 0);
		int cycleYear = sharedPreferences.getInt("cycleyear", 0);
		
		Calendar cal = Calendar.getInstance();
		cal.set(cycleYear, cycleMonth, cycleDate);
		
		return cal;
		
//		return false;
	}
	
}
