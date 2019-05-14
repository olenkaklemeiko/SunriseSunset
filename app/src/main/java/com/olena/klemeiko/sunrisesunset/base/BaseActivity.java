package com.olena.klemeiko.sunrisesunset.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public abstract class BaseActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  public void replaceFragment(Fragment fragment, int container) {
    replaceFragment(fragment, container, false);
  }

  public void replaceFragment(Fragment fragment, int container, boolean addToBackStack) {
    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    transaction.replace(container, fragment, fragment.getTag());
    if (addToBackStack) {
      transaction.addToBackStack(fragment.getClass().getName());
    }
    transaction.commit();
  }
}
