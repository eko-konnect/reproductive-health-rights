package org.ekokonnect.reprohealth.models;


import android.content.Context;

import org.ekokonnect.reprohealth.services.GooglePlacesClient;
import org.ekokonnect.reprohealth.services.ServiceClient;
 

public class GooglePlaces {
 
    // Google API Key
    private static final String API_KEY = "AIzaSyDGvskfOPt78L0EHpcncTURb5rNQUWFank";

    private static final String PLACES_URL = "https://maps.googleapis.com/maps/api/place";

    private Context mContext;

    public GooglePlaces(Context context){
         mContext = context;
    }
 
    /**
     * Searching places
     * @param latitude - latitude of place
     * @param longitude - longitude of place
     * @param radius - radius of searchable area
     * @param types - type of place to search
     * @return list of places
     * */
    public PlacesList search(double latitude, double longitude, double radius, String types){

        GooglePlacesClient client = ServiceClient.getInstance()
                .getClient(mContext, GooglePlacesClient.class, PLACES_URL);
        return client.getNearbyPlaceList(API_KEY, latitude + "," + longitude,
                String.valueOf(radius), types);
    }
 
    /**
     * Searching single place full details
     * @param reference - reference id of place
     *                 - which you will get in search api request
     * */
    public PlaceDetails getPlaceDetails(String reference) throws Exception {

        GooglePlacesClient client = ServiceClient.getInstance()
                .getClient(mContext, GooglePlacesClient.class, PLACES_URL);
        return client.getPlaceDetail(API_KEY, reference);
    }
 
}
