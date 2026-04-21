package com.apitap.app.controller;

import com.apitap.app.model.reservation.AddEditReservationRequest;
import com.apitap.app.model.reservation.AddEditReservationResponse;
import com.apitap.app.model.reservation.NearbyStoreRequest;
import com.apitap.app.model.reservation.ReservationLocationResponse;
import com.apitap.app.model.reservation.ViewReservationRequest;
import com.apitap.app.model.reservation.ViewReservationResponseItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReservationApi {

    @Headers("Content-Type: application/json")
    @POST("swagger/service-aioorders/addEditReservation")
    Call<AddEditReservationResponse> addEditReservation(
            @Body AddEditReservationRequest request
    );

    @Headers("Content-Type: application/json")
    @POST("swagger/service-aioorders/reservations")
    Call<List<ViewReservationResponseItem>> getReservations(
            @Body ViewReservationRequest request
    );

    @Headers("Content-Type: application/json")
    @POST("api/aiolocations/getAllMerchantStoresByPointFilteredByCategory/726")
    Call<List<Integer>> getAllMerchantStoresByPointFilteredByCategory(
            @Body NearbyStoreRequest request
    );

    @Headers("Content-Type: application/json")
    @GET("swagger/service-aiolocations/locationsByCompanySearch/{categoryId}")
    Call<List<ReservationLocationResponse>> getReservationLocation(
            @Path("categoryId") int categoryId);
}