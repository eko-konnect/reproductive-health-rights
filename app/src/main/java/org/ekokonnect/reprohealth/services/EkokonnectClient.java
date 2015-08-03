package org.ekokonnect.reprohealth.services;


import org.ekokonnect.reprohealth.models.http.UserAuthResponse;

import java.util.List;

import models.Tip;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Eko Konnect API CLient
 * Created by oyewale on 8/1/15.
 */
public interface EkokonnectClient {

    @FormUrlEncoded
    @POST("/user")
    void authenticateUser(@Field("action") String action, @Field("email") String email,
                          @Field("firstName") String firstName,
                          @Field("lastName") String lastName,
                          @Field("gender") String gender,
                          @Field("gcmid") String gcmid,
                          Callback<UserAuthResponse> callback);

    @GET("/tips")
    void downloadTips(Callback<List<Tip>> callback);
}
