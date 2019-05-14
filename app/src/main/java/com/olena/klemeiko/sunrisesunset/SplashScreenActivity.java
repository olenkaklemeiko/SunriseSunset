package com.olena.klemeiko.sunrisesunset;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.olena.klemeiko.sunrisesunset.base.BaseActivity;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreenActivity extends BaseActivity {

  //region Binding
  @BindView(R.id.sun)
  ImageView imageSun;
  @BindView(R.id.splash_layout)
  RelativeLayout splashLayout;
  @BindView(R.id.mountains)
  RelativeLayout mountains;
  @BindView(R.id.mountains_light)
  RelativeLayout mountainsLight;
  @BindView(R.id.yellow_back)
  RelativeLayout yellowBackground;
  // endregion

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.splash_screen);

    ButterKnife.bind(this);

    new Handler().postDelayed(() -> {
      startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
      finish();
    }, 4000);

    animationsInit();
  }

  private void animationsInit() {
    AnimationDrawable animation = (AnimationDrawable) splashLayout.getBackground();
    animation.setEnterFadeDuration(1000);
    animation.setExitFadeDuration(1000);
    animation.start();

    Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
    mountains.startAnimation(fadeOut);

    Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
    mountainsLight.startAnimation(fadeIn);

    Animation upAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up_anim);
    imageSun.startAnimation(upAnim);
    upAnim.setFillAfter(true);

    Animation backFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.backround_fadein);
    yellowBackground.startAnimation(backFadeIn);
  }
}
