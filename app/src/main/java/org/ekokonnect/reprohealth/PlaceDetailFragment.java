package org.ekokonnect.reprohealth;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.ekokonnect.reprohealth.models.GooglePlaces;
import org.ekokonnect.reprohealth.models.PlaceDetails;
import org.ekokonnect.reprohealth.utils.AlertDialogManager;
import org.ekokonnect.reprohealth.utils.ConnectionDetector;

/**
 * A fragment representing a single Place detail screen. This fragment is either
 * contained in a {@link PlaceListActivity} in two-pane mode (on tablets) or a
 * {@link PlaceDetailActivity} on handsets.
 */
public class PlaceDetailFragment extends Fragment {
	
	Boolean isInternetPresent = false;
	 
    // Connection detector class
    ConnectionDetector cd;
     
    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();
 
    // Google Places
    GooglePlaces googlePlaces;
     
    // Place Details
    PlaceDetails placeDetails;
    
    private View progressContainer;
	private View detailsContainer;
     
    // KEY Strings
    public static String KEY_REFERENCE = "reference"; // id of the place
 
	
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
//	private DummyContent.DummyItem mItem;

	private TextView nameText;

	private TextView addressText;

	private TextView phoneText;

	private TextView locationText;
	private GoogleMap mMap;
	private MapView mMapView;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public PlaceDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			KEY_REFERENCE = getArguments().getString(ARG_ITEM_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_place_detail,
				container, false);
		
		detailsContainer = (View) rootView.findViewById(R.id.detailsContainer);
		progressContainer = (View) rootView.findViewById(R.id.detailProgressContainer);

		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoCondensed-Bold.ttf");		
		Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
		
		nameText = (TextView) rootView.findViewById(R.id.name);
		nameText.setTypeface(font);
		
		addressText = (TextView) rootView.findViewById(R.id.address);
		addressText.setTypeface(font2);
		
		phoneText = (TextView) rootView.findViewById(R.id.phone);
		phoneText.setTypeface(font2);
		
		locationText = (TextView) rootView.findViewById(R.id.location);
		locationText.setTypeface(font2);
//		mMap = ((SupportMapFragment)getFragmentManager().findFragmentById(R.id.loc_map)).getMap();
		mMapView = (MapView) rootView.findViewById(R.id.loc_map);
		mMapView.onCreate(savedInstanceState);
		mMapView.onResume();
		mMap = mMapView.getMap();
//		mMap.getUiSettings().setMyLocationButtonEnabled(false);
//		mMap.setMyLocationEnabled(true);
		/*// Needs to call MapsInitializer before doing any CameraUpdateFactory calls
	    try {
	        MapsInitializer.initialize(this.getActivity());
	    } catch (GooglePlayServicesNotAvailableException e) {
	        e.printStackTrace();
	    }*/
		

		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		new LoadSinglePlaceDetails().execute(KEY_REFERENCE);
	}
	
	@Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();        
    }
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        mMapView.onLowMemory();
        super.onLowMemory();

    }
	
	private void initializeMap(Double lat, Double lng, Bundle bundle){
//		mMapView.onCreate(bundle);
        // Get GoogleMap from MapView
//        mMap = mMapView.getMap();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
            
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(
                    lat, lng));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.moveCamera(center);
            mMap.moveCamera(zoom);

            // Add a marker to this location
            addMarker(mMap, lat,
                    lng);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	private void addMarker(GoogleMap map, double lat, double lon) {
        map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));
    }
	
	/**
     * Background Async Task to Load Google places
     * */
    class LoadSinglePlaceDetails extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressContainer.setVisibility(View.VISIBLE);
            detailsContainer.setVisibility(View.GONE);
        }
 
        /**
         * getting Profile JSON
         * */
        protected String doInBackground(String... args) {
            String reference = args[0];
             
            // creating Places class object
            googlePlaces = new GooglePlaces(getActivity().getApplicationContext());
 
            // Check if used is connected to Internet
            try {
                placeDetails = googlePlaces.getPlaceDetails(reference);
 
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            
            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                	
                	progressContainer.setVisibility(View.GONE);
                    detailsContainer.setVisibility(View.VISIBLE);
                	
                    /**
                     * Updating parsed Places into LISTVIEW
                     * */
                    if(placeDetails != null){
                        String status = placeDetails.status;
                         
                        // check place deatils status
                        // Check for all possible status
                        if(status.equals("OK")){
                            if (placeDetails.result != null) {
                                String name = placeDetails.result.name;
                                String address = placeDetails.result.formatted_address;
                                String phone = placeDetails.result.formatted_phone_number;
                                String latitude = Double.toString(placeDetails.result.geometry.location.lat);
                                String longitude = Double.toString(placeDetails.result.geometry.location.lng);
                                 
                                Log.d("Place ", name + address + phone + latitude + longitude);
                                 
                                // Displaying all the details in the view
                                // single_place.xml
                                
                                
                                 
                                // Check for null data from google
                                // Sometimes place details might missing
                                name = name == null ? "Not present" : name; // if name is null display as "Not present"
                                address = address == null ? "Not present" : address;
                                phone = phone == null ? "Not present" : phone;
                                latitude = latitude == null ? "Not present" : latitude;
                                longitude = longitude == null ? "Not present" : longitude;
                                 
                                nameText.setText(name);
                                addressText.setText(address);
                                phoneText.setText(Html.fromHtml("<b>Phone:</b> " + phone));
                                locationText.setText(Html.fromHtml("<b>Latitude:</b> " + latitude + ", <b>Longitude:</b> " + longitude));
                                initializeMap(placeDetails.result.geometry.location.lat, placeDetails.result.geometry.location.lng, null);
                            }
                        }
                        else if(status.equals("ZERO_RESULTS")){
                        	AlertDialogManager.showAlertDialog(getFragmentManager(), "Near Places",
                                    "Sorry no place found.",
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
                    }else{
                        AlertDialogManager.showAlertDialog(getFragmentManager(), "Places Error",
                                "Sorry error occured.",
                                false);
                    }
                     
                     
                }
            });
 
        }
 
    }
	
}
