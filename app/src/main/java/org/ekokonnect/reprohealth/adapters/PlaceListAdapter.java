package org.ekokonnect.reprohealth.adapters;

import java.util.List;

import org.ekokonnect.reprohealth.PlaceListFragment;
import org.ekokonnect.reprohealth.R;
import org.ekokonnect.reprohealth.models.Place;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PlaceListAdapter extends BaseAdapter {
	List<Place> places;
	Activity activity;
	LayoutInflater inflater;
	PlaceListFragment frag;
	
	public PlaceListAdapter(List<Place> places, PlaceListFragment frag){
		this.places = places;
		this.frag = frag;
		this.activity = ((PlaceListFragment)frag).getActivity();
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return places.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View convertView, ViewGroup parentView) {
		View v = convertView;
//		SquaredImageView view = (SquaredImageView) convertView;
		if (convertView == null){
			 v = inflater.inflate(R.layout.place_list_item, null);
			
			 TextView ref = (TextView)v.findViewById(R.id.reference);
			 TextView title = (TextView)v.findViewById(R.id.name);
			 TextView summary = (TextView)v.findViewById(R.id.address);
			 TextView vicinity = (TextView)v.findViewById(R.id.vicinity);
			 TextView distance = (TextView)v.findViewById(R.id.distance);
			 ImageView image = (ImageView)v.findViewById(R.id.iv_avatar);
			 
			 Place place = places.get(position);
			 ref.setText(place.reference);
//			 Log.i("Place", place.reference + ": " + place.name);
			 if (place.formatted_address != null){
				 String shortdescription = place.formatted_address.substring(0, Math.min(250, place.formatted_address.length()));
				 summary.setText(shortdescription + "...");
			 }	
			 if (place.name != null)
				 title.setText(place.name);
			 if (place.vicinity != null)
				 vicinity.setText(place.vicinity);
			 if (place.formatted_phone_number != null)
				 distance.setText("Phone: "+place.formatted_phone_number);
//			 Log.i("Place", place.icon);
			 Picasso.with(activity)
             .load(place.icon)
             .noFade()
             .into(image);
			 
			 Location loc = new Location("Whales");
			 loc.setLatitude(frag.gps.getLatitude());
			 loc.setLongitude(frag.gps.getLongitude());
			 
			 Location loc2 = new Location("Whales");
			 loc2.setLatitude(place.geometry.location.lat);
			 loc2.setLongitude(place.geometry.location.lng);
			 
			 int dist = (int)loc.distanceTo(loc2);
			 
			 distance.setText(String.valueOf(dist)+" meters");
			 
			 
		}
		return v;
	}

}
