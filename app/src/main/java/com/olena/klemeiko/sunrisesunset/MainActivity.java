package com.olena.klemeiko.sunrisesunset;

import android.os.Bundle;

import com.olena.klemeiko.sunrisesunset.base.BaseActivity;

import androidx.fragment.app.Fragment;

public class MainActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (savedInstanceState == null) {
      replaceFragment(MainFragment.getInstance());
    }
  }

  public void replaceFragment(Fragment fragment) {
    super.replaceFragment(fragment, R.id.container);
  }
}
