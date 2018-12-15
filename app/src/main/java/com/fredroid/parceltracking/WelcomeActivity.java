package com.fredroid.parceltracking;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Timer timer = new Timer();
        timer.schedule(task, 4000);
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            finish();
        }
    };
}
