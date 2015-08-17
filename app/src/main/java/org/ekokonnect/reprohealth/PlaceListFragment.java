package org.ekokonnect.reprohealth;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.ekokonnect.reprohealth.adapters.PlaceListAdapter;
import org.ekokonnect.reprohealth.models.GooglePlaces;
import org.ekokonnect.reprohealth.models.Place;
import org.ekokonnect.reprohealth.models.PlacesList;
import org.ekokonnect.reprohealth.utils.AlertDialogManager;
import org.ekokonnect.reprohealth.utils.ConnectionDetector;
import org.ekokonnect.reprohealth.utils.GPSTracker;

import java.util.ArrayList;

/**
 * A list fragment representing a list of Places. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link PlaceDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */

public class PlaceListFragment extends ListFragment {

	private static final String TAG = "PlaceListFragment";
	// flag for Internet connection status
    Boolean isInternetPresent = false;
 
    // Connection detector class
    ConnectionDetector cd;
     
    // Alert Dialog Manager
//    AlertDialogManager alert = new AlertDialogManager();
 
    // Google Places
    GooglePlaces googlePlaces;
 
    // Places List
    PlacesList nearPlaces;
 
    // GPS Location
    public GPSTracker gps;
 
    // Button
    Button btnShowOnMap;
 
    // Progress dialog
    //ProgressDialog pDialog;
     
    // Places Listview
//    ListView lv;
     
    PlaceListAdapter adapter;
    // ListItems data
    ArrayList<Place> placesListItems = new ArrayList<Place>();
     
     
    // KEY Strings
    public static String KEY_REFERENCE = "reference"; // id of the place
    public static String KEY_NAME = "name"; // name of the place
    public static String KEY_VICINITY = "vicinity"; // Place area name
	
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

	private View progressContainer;
	private View listContainer;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(String id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String id) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public PlaceListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		cd = new ConnectionDetector(getActivity().getApplicationContext());
//
//        // Check if Internet present
//        isInternetPresent = cd.isConnectingToInternet();
//        if (!isInternetPresent) {
//            // Internet Connection is not present
//            AlertDialogManager.showAlertDialog(getFragmentManager(), "Internet Connection Error",
//                    "Please connect to working Internet connection", false);
//            // stop executing code by return
//            return;
//        }
 
        // creating GPS Class object
        gps = new GPSTracker(getActivity().getApplicationContext());
 
        // check if GPS location can get
        if (gps.canGetLocation()) {
            Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
        } else {
            // Can't get user's current location
            AlertDialogManager.showAlertDialog(getFragmentManager(), "GPS Status",
                    "Couldn't get location information. Please enable GPS",
                    false);
            // stop executing code by return
            return;
        }
 
        adapter = new PlaceListAdapter(placesListItems, this);
        
        
		setListAdapter(adapter);
        Log.d(TAG, "onCreate Fragment");
	}	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.place_list_layout, container, false);
		
//		btnShowOnMap = (Button) view.findViewById(R.id.btn_show_map);
//        if (btnShowOnMap == null){
//        	Log.e(TAG, "Null Progress Container Elements");
//        } else {
//        	Log.d(TAG, "Btn UI Elements");
//        }
        
        progressContainer = (View) view.findViewById(R.id.progressContainer);
//        if (progressContainer == null){
//        	Log.e(TAG, "Null Progress Container Elements");
//        } else {
//        	Log.d(TAG, "Real UI Elements");
//        }
        
        listContainer = (View) view.findViewById(R.id.listContainer);

		
		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
Log.e(TAG, "Activated Position: "+ STATE_ACTIVATED_POSITION);
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
		
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		new LoadPlaces().execute();
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

		String reference = ((TextView) view.findViewById(R.id.reference)).getText().toString();
        
		
		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.onItemSelected(reference);
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
	
	/**
     * Background Async Task to Load Google places
     * */
    class LoadPlaces extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute");
//            pDialog = new ProgressDialog(getActivity().getApplicationContext());
//            pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
            progressContainer.setVisibility(View.VISIBLE);
            listContainer.setVisibility(View.GONE);
        }
 
        /**
         * getting Places JSON
         * */
        protected String doInBackground(String... args) {
        	Log.d(TAG, "doInBackground");
            // creating Places class object
            googlePlaces = new GooglePlaces(getActivity().getApplicationContext());

			String types = "hospital|health"; // Listing places only cafes, restaurants

			// Radius in meters - increase this value if you don't find any places
			double radius = 5000; // 1000 meters

			// get nearest places
			nearPlaces = googlePlaces.search(gps.getLatitude(),
					gps.getLongitude(), radius, types);
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * and show the data in UI
         * Always use runOnUiThread(new Runnable()) to update UI from background
         * thread, otherwise you will get error
         * **/
        protected void onPostExecute(String file_url) {
        	Log.d(TAG, "onPostExecute");
            // dismiss the dialog after getting all products
//            pDialog.dismiss();        	
        	// updating UI from Background Thread            
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                	listContainer.setVisibility(View.VISIBLE);
                	progressContainer.setVisibility(View.GONE);
                    /**
                     * Updating parsed Places into LISTVIEW
                     * */
                    // Get json response status
                	if (nearPlaces == null){
                		AlertDialogManager.showAlertDialog(getFragmentManager(), "Network Error",
                                "Sorry error occured.",
                                false);
                	} else {
                		String status = nearPlaces.status;
                        //Log.i("Frag", nearPlaces.results);
                        // Check for all possible status
                        if(status.equals("OK")){
                            // Successfully got places details
                            if (nearPlaces.results != null) {
//                            	placesListItems.addAll(placesListItems);
                                // loop through each place
                                for (Place p : nearPlaces.results) {
//                                    HashMap<String, String> map = new HashMap<String, String>();
                                     
                                    // Place reference won't display in listview - it will be hidden
                                    // Place reference is used to get "place full details"
//                                    map.put(KEY_REFERENCE, p.reference);
                                     
                                    // Place name
//                                    map.put(KEY_NAME, p.name);
                                     
                                     
                                    // adding HashMap to ArrayList
                                    placesListItems.add(p);
                                    
                                }
                                // list adapter
//                                ListAdapter adapter = new SimpleAdapter(getActivity().getApplicationContext(), placesListItems,
//                                        R.layout.place_list_item,
//                                        new String[] { KEY_REFERENCE, KEY_NAME}, new int[] {
//                                                R.id.reference, R.id.name });
                                 adapter.notifyDataSetChanged();
                                // Adding data into listview
//                                Log.d("PlaceListFragment", "setting Adapter");
                                getListView().setAdapter(adapter);
                            }
                        }
                        else if(status.equals("ZERO_RESULTS")){
                            // Zero results found
                        	AlertDialogManager.showAlertDialog(getFragmentManager(), "Near Places",
                                    "Sorry no places found. Try to change the types of places",
                                    false);
                        }
                        else if(status.equals("UNKNOWN_ERROR"))
                        {
                        	AlertDialogManager.showAlertDialog(getFragmentManager(), "Places Error",
                                    "Sorry unknown error occured.",
                                    false);
                        }
                        else if(status.equals("OVER_QUERY_LIMIT"))
                        {
                        	AlertDialogManager.showAlertDialog(getFragmentManager(), "Places Error",
                                    "Sorry query limit to google places is reached",
                                    false);
                        }
                        else if(status.equals("REQUEST_DENIED"))
                        {
                        	AlertDialogManager.showAlertDialog(getFragmentManager(), "Places Error",
                                    "Sorry error occured. Request is denied",
                                    false);
                        }
                        else if(status.equals("INVALID_REQUEST"))
                        {
                        	AlertDialogManager.showAlertDialog(getFragmentManager(), "Places Error",
                                    "Sorry error occured. Invalid Request",
                                    false);
                        }
                        else
                        {
                            AlertDialogManager.showAlertDialog(getFragmentManager(), "Places Error",
                                    "Sorry error occured.",
                                    false);
                        }
                	}                	
                    
                }
            });
 
        }
 
    }
	
}
