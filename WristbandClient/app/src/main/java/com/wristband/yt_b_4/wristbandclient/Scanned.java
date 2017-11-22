package com.wristband.yt_b_4.wristbandclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Handler;
/**
 * Created by paulaguzzetta on 11/6/17.
 */

public class Scanned extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
