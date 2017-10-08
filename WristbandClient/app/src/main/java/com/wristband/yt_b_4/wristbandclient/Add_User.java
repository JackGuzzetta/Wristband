package com.wristband.yt_b_4.wristbandclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Add_User extends AppCompatActivity {
    private Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__user);
        next = (Button) findViewById(R.id.next);
    }

    public void buttonClickParty(View view) {
        //Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText person_name = (EditText) findViewById(R.id.invitee);
        EditText person_num = (EditText) findViewById(R.id.number);
        //text in name box
        String name = person_name.getText().toString();
        //text in number box
        String number = person_num.getText().toString();

        /*This will check the blacklist, users, party, and party_list tables to verify and/or add
        the person to the table.
         */

        //make sure party is not at capacity by checking max people value from party table

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
            //search for person by name, if not found, add person to both users and party_list tables
            Toast pass = Toast.makeText(getApplicationContext(), "Added " + name + " to party", Toast.LENGTH_LONG);
            pass.show();
        }
        else{
            Toast blank = Toast.makeText(getApplicationContext(), "Enter the name or user number of person to add", Toast.LENGTH_LONG);
            blank.show();
        }
    }
    public void buttonClickBlacklist(View view) {
        //Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText blacklist_name = (EditText) findViewById(R.id.invitee);
        EditText blacklist_num = (EditText) findViewById(R.id.number);
        //text in name box
        String name = blacklist_name.getText().toString();
        //text in number box
        String number = blacklist_num.getText().toString();

        /*This will check the blacklist, users, party, and party_list tables to verify and/or add
        the person to the table.  Need checking for multiple entries.
         */
        //search by number
        if(!number.isEmpty()){
            //find user from users table and add that user to the blacklist table
            Toast pass = Toast.makeText(getApplicationContext(), "Added " + name + " to blacklist", Toast.LENGTH_LONG);
            pass.show();
            //if no user is found from number, give a toast that user number is not found
            //Toast fail = Toast.makeText(getApplicationContext(), "Failed to find person with that user number", Toast.LENGTH_LONG);
            //fail.show();
        }
        else if(!name.isEmpty()){
            //search for person by name, if not found, add person to both users and blacklist tables
            Toast pass = Toast.makeText(getApplicationContext(), "Added " + name + " to blacklist", Toast.LENGTH_LONG);
            pass.show();
        }
        //both name and number are empty, send toast to enter a name or number
        else{
            Toast blank = Toast.makeText(getApplicationContext(), "Enter the name or user number of person to blacklist", Toast.LENGTH_LONG);
            blank.show();
        }
    }

    private void back_Homescreen(){
        //Intent intent = new Intent (Add_User.this, Homescreen.class);
        //startActivity(intent);
    }
}
