package org.ekokonnect.reprohealth.services;

import org.ekokonnect.reprohealth.models.PlaceDetails;
import org.ekokonnect.reprohealth.models.PlacesList;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Places API client
 * Created by oyewale on 8/14/15.
 */
public interface GooglePlacesClient {

    @GET("/search/json?sensor=false")
    PlacesList getNearbyPlaceList(
            @Query("key") String apiKey,
            @Query("location") String location,
            @Query("radius") String radius,
            @Query("types") String types
    );

    @GET("/details/json?sensor=false")
    PlaceDetails getPlaceDetail(
            @Query("key") String apiKey,
            @Query("reference") String reference
    );
}
