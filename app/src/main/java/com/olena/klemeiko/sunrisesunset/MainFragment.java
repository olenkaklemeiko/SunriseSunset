package com.olena.klemeiko.sunrisesunset;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.olena.klemeiko.sunrisesunset.api.SunriseSunsetAPIImpl;
import com.olena.klemeiko.sunrisesunset.base.BaseFragment;
import com.olena.klemeiko.sunrisesunset.model.sunrisesunset.ResultSunriseSunsetDAO;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class MainFragment extends BaseFragment implements LocationListener, OnMapReadyCallback {

  private static final String API_KEY = "";
  private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
  private static final int REQUEST_CODE = 1;

  //region Binding
  @BindView(R.id.sunrise)
  TextView sunrise;
  @BindView(R.id.sunset)
  TextView sunset;
  @BindView(R.id.city)
  TextView city;
  @BindView(R.id.map_view)
  MapView mapView;
  @BindView(R.id.progress_bar)
  ProgressBar progressBar;
  @BindView(R.id.sunrise_image)
  ImageView sunrise_image;
  @BindView(R.id.sunset_image)
  ImageView sunset_image;
  // endregion

  private GoogleMap mMap;
  private Marker marker;

  public static MainFragment getInstance() {
    return new MainFragment();
  }

  @Override
  protected int layoutResId() {
    return R.layout.fragment_main;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    Places.initialize(getActivity().getApplicationContext(), API_KEY);

    LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
    if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
          MY_PERMISSION_ACCESS_COARSE_LOCATION);
    }

    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
    if (resultCode == AutocompleteActivity.RESULT_OK) {
      Place place = Autocomplete.getPlaceFromIntent(intent);
      String cityName = place.getName();
      place.getAddress();
      double lat = place.getLatLng().latitude;
      double lon = place.getLatLng().longitude;
      getResponse(lat, lon);
      city.setText(cityName);

      LatLng newLocation = new LatLng(lat, lon);

      setMarker(newLocation, cityName);
    } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
      Status status = Autocomplete.getStatusFromIntent(intent);
      Log.i(TAG, status.getStatusMessage());
    }
    super.onActivityResult(requestCode, resultCode, intent);
  }

  private void getResponse(Double lat, Double lon) {
    SunriseSunsetAPIImpl.getInstance().getData(lat, lon)
        .enqueue(new Callback<ResultSunriseSunsetDAO>() {
          @Override
          public void onResponse(Call<ResultSunriseSunsetDAO> call, Response<ResultSunriseSunsetDAO> response) {
            if (response.body() != null) {
              sunrise.setText(" " + response.body().getResults().getSunrise() + "  " +
                  getString(R.string.sunrise));
              sunset.setText(
                  " " + response.body().getResults().getSunset() + "  " + getString(R.string.sunset));
            } else {
              Log.e(TAG, "onResponse: " + response.errorBody());
            }
          }

          @Override
          public void onFailure(Call<ResultSunriseSunsetDAO> call, Throwable t) {
            System.out.println("error:" + t.getMessage());
          }
        });
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    LatLng lviv = new LatLng(49.86661544, 24.00256895);
    setMarker(lviv, "Lviv");
  }

  //region LocationListener
  @Override
  public void onLocationChanged(Location location) {
    double latitude = location.getLatitude();
    double longitude = location.getLongitude();
    if (TextUtils.isEmpty(sunrise.getText()) && TextUtils.isEmpty(sunset.getText())) {
      getResponse(latitude, longitude);
      setMarker(new LatLng(latitude, longitude), "Lviv");
    } else {
      progressBar.setVisibility(ProgressBar.GONE);
      sunrise_image.setVisibility(View.VISIBLE);
      sunset_image.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {

  }

  @Override
  public void onProviderEnabled(String provider) {

  }

  @Override
  public void onProviderDisabled(String provider) {

  }
  //endregion

  //region Lifecycle
  @Override
  public void onResume() {
    mapView.onResume();
    super.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  //endregion

  @OnClick(R.id.map)
  public void getMap() {
    PlacesFieldSelector fieldSelector = new PlacesFieldSelector();
    Intent autocompleteIntent =
        new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fieldSelector.getAllFields())
            .build(getActivity());
    startActivityForResult(autocompleteIntent, REQUEST_CODE);
  }

  private void setMarker(LatLng latLng, String city) {
    if (marker == null) {
      marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in " + city));
    } else {
      marker.setPosition(latLng);
      marker.setTitle("Marker in " + city);
    }
    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
  }
}
