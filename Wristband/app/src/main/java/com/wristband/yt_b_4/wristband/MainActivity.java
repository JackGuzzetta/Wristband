package com.wristband.yt_b_4.wristband;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button createEventButton;
    private Button myEventsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;

        //creates 2 buttons
        createEventButton = (Button) this.findViewById(R.id.createEvent);
        myEventsButton = (Button) this.findViewById(R.id.myEvents);

        //Waits for someone to click on the create event button
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateEvent.class);
                context.startActivity(intent);
            }
        });

        myEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyEvents.class);
                context.startActivity(intent);
            }
        });
    }
}