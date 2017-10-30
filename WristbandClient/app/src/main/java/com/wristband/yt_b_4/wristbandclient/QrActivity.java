package com.wristband.yt_b_4.wristbandclient;

/**
 * Created by Jackguzzetta on 10/26/17.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class QrActivity extends AppCompatActivity {
    private Button btnBack;

    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        btnBack = (Button) findViewById(R.id.btnBack);
        imageView = (ImageView) this.findViewById(R.id.imageView);
        Bitmap bitmap = getIntent().getParcelableExtra("pic");
        imageView.setImageBitmap(bitmap);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                goBack(view);
            }

        });

    }

    private void goBack(View view) {
        Intent intent = new Intent(QrActivity.this, GuestScreen.class);
        startActivity(intent);
    }

}

