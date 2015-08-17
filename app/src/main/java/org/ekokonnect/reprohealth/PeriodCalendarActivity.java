package org.ekokonnect.reprohealth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.ekokonnect.reprohealth.models.CalendarAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
//import android.support.v4.app.DialogFragment;

public class PeriodCalendarActivity extends AppCompatActivity {
	public Calendar month;
	public CalendarAdapter adapter;
	public Handler handler;
	public ArrayList<String> items; // container to store some random calendar items
	private int lastCycleDay;
	private boolean undo = false;
	private CaldroidFragment caldroidFragment;
	private Toolbar mToolbar;

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		setContentView(R.layout.calendar);
		setupToolbar();
		final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
		onNewIntent(getIntent());
		// Setup caldroid fragment
		// **** If you want normal CaldroidFragment, use below line ****
		caldroidFragment = new CaldroidFragment();

		// //////////////////////////////////////////////////////////////////////
		// **** This is to show customized fragment. If you want customized
		// version, uncomment below line ****
		// caldroidFragment = new CaldroidSampleCustomFragment();

		// Setup arguments

		// If Activity is created after rotation
		if (savedInstanceState != null) {
			caldroidFragment.restoreStatesFromKey(savedInstanceState,
					"CALDROID_SAVED_STATE");
		}
		// If activity is created from fresh
		else {
			Bundle args = new Bundle();
			Calendar cal = Calendar.getInstance();
			args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
			args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

			// Uncomment this to customize startDayOfWeek
			// args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
			// CaldroidFragment.TUESDAY); // Tuesday
			caldroidFragment.setArguments(args);
		}

		setCustomResourceForDates();

		// Attach to the activity
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.calendar1, caldroidFragment);
		t.commit();

		// Setup listener
		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onSelectDate(Date date, View view) {
				Toast.makeText(getApplicationContext(), formatter.format(date),
						Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onChangeMonth(int month, int year) {
				String text = "month: " + month + " year: " + year;
				Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLongClickDate(Date date, View view) {
				Toast.makeText(getApplicationContext(),
						"Long click " + formatter.format(date),
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onCaldroidViewCreated() {
				if (caldroidFragment.getLeftArrowButton() != null) {
					Toast.makeText(getApplicationContext(),
							"Caldroid view is created", Toast.LENGTH_SHORT)
							.show();
				}
			}

		};

		// Setup Caldroid
		caldroidFragment.setCaldroidListener(listener);
		setCalendar();

	}

	private void setupToolbar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	
	private void setCustomResourceForDates() {
		Calendar cal = Calendar.getInstance();

		// Min date is last 7 days
		cal.add(Calendar.DATE, -18);
		Date blueDate = cal.getTime();

		// Max date is next 7 days
		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 16);
		Date greenDate = cal.getTime();

		if (caldroidFragment != null) {
			caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_holo_blue_light,
					blueDate);
			caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_darker_gray,
					greenDate);
			caldroidFragment.setTextColorForDate(R.color.white, blueDate);
			caldroidFragment.setTextColorForDate(R.color.white, greenDate);
		}
	}
	
	public void onNewIntent(Intent intent) {
		int day = intent.getIntExtra("cycledate", 0);
		this.lastCycleDay = day;
//		String date = "2014-01-22";
//		String[] dateArr = date.split("-"); // date format is yyyy-mm-dd
//		month.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1]), Integer.parseInt(dateArr[2]));
	}
	
	/**
	 * Save current states of the Caldroid here
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		if (caldroidFragment != null) {
			caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.period_calendar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void setCalendar(){
		Calendar cal = TipListFragment.getCycleDate(getApplicationContext());
		HashMap<Date, Integer> map = new HashMap<Date, Integer>();
		for(int i=0; i<5; i++) {
			Calendar c = (Calendar) cal.clone();
			c.roll(Calendar.DATE, i);
			map.put(c.getTime(), R.color.caldroid_holo_blue_dark);
			//items.add(Integer.toString(newI));
		}

		for(int i=21; i<26; i++) {
			Calendar c = (Calendar) cal.clone();
			c.roll(Calendar.DATE, i);
			map.put(c.getTime(), R.color.caldroid_holo_blue_dark);
		}
		caldroidFragment.setBackgroundResourceForDates(map);
		caldroidFragment.refreshView();
	}
	

}
