package com.example.rishi.herbscout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.rishi.herbscout.Activity.HomeActivity;

public class SplashActivity extends AppCompatActivity {

    ImageView ivSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ivSplash=findViewById(R.id.ivSplash);
        Animation animation=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoomin);
        ivSplash.startAnimation(animation);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(3000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                }
            }
        };
        timer.start();
    }
}
