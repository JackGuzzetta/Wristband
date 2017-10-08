package com.wristband.yt_b_4.wristbandclient;

/**
 * Created by Mike on 10/7/2017.
 */
import android.content.Intent;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.Switch;
import android.widget.CompoundButton;

import com.wristband.yt_b_4.wristbandclient.R;

/**
 * Created by paulaguzzetta on 10/3/17.
 */

public class CoHost extends AppCompatActivity  {
    int max;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cohost);
    }
    public void buttonClickParty(View view) {
        EditText person_name = (EditText) findViewById(R.id.username);
        EditText person_num = (EditText) findViewById(R.id.Usernumber);
        //text in name box
        String name = person_name.getText().toString();
        //text in number box
        String number = person_num.getText().toString();



        //search by number
        if(name.isEmpty() && !number.isEmpty()){
            //find user from users table and add that user to the party_list table
            //if no user is found from number, search by name
            //check name to see if it is on blacklist, if it is, show toast that person is blacklisted
            //check to see if user has a profile, otherwise add blank entry to party_list table
            Toast pass = Toast.makeText(getApplicationContext(), "Added " + name + " to party", Toast.LENGTH_LONG);
            pass.show();
            //if no user is found from number, give a toast that user number is not found
            //Toast fail = Toast.makeText(getApplicationContext(), "Failed to find person with that user number", Toast.LENGTH_LONG);
            //fail.show();

        }
        else if(!name.isEmpty()){
            Toast pass = Toast.makeText(getApplicationContext(), "Added " + name , Toast.LENGTH_LONG);
            pass.show();
        }
        else{
            Toast blank = Toast.makeText(getApplicationContext(), "Enter the username or number of person to add", Toast.LENGTH_LONG);
            blank.show();
        }
    }

}
