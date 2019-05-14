package com.olena.klemeiko.sunrisesunset.model.sunrisesunset;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultSunriseSunsetDAO {

  @Expose
  @SerializedName("results")
  private SunriseSunsetDAO results;

  public SunriseSunsetDAO getResults() {
    return results;
  }

  public void setResults(SunriseSunsetDAO results) {
    this.results = results;
  }
}
