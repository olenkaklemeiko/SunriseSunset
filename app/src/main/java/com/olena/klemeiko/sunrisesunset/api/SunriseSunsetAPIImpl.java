package com.olena.klemeiko.sunrisesunset.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SunriseSunsetAPIImpl {

  private static SunriseSunsetAPI api;

  private SunriseSunsetAPIImpl() {
  }

  public static SunriseSunsetAPI getInstance() {
    if (api == null) {
      api = new Retrofit.Builder()
          .baseUrl("https://api.sunrise-sunset.org/")
          .addConverterFactory(GsonConverterFactory.create())
          .build().create(SunriseSunsetAPI.class);
    }
    return api;
  }
}
