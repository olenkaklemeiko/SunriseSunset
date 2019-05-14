package com.olena.klemeiko.sunrisesunset.api;

import com.olena.klemeiko.sunrisesunset.model.sunrisesunset.ResultSunriseSunsetDAO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SunriseSunsetAPI {
  @GET("json")
  Call<ResultSunriseSunsetDAO> getData(@Query("lat")Double lat,@Query("lng")Double lng);
}
