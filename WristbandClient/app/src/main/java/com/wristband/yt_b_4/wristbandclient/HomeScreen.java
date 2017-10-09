package com.wristband.yt_b_4.wristbandclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class HomeScreen extends AppCompatActivity {

    Button GuestButton;
    Button NewPartyButton;
    Button btnlogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen2);
        GuestButton = (Button) findViewById(R.id.button2);
        NewPartyButton = (Button) findViewById(R.id.button3);
        btnlogout = (Button) findViewById(R.id.logout);
        initializeControls();
    }

    private void initializeControls()  {
        GuestButton = (Button) findViewById(R.id.button2);
        GuestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                guestScreen();
            }
        });
        NewPartyButton = (Button) findViewById(R.id.button3);
        NewPartyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                newParty();
            }
        });
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //back to login screen
                logout(view);
            }

        });

    }
    public void guestScreen(){
        Intent intent = new Intent(this, GuestScreen.class);
        startActivity(intent);
    }

    public void newParty(){
        Intent intent = new Intent(this, Create_Party.class);
        startActivity(intent);
    }

    public void logout(View view){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

}
