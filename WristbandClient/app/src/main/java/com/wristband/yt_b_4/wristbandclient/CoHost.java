package com.wristband.yt_b_4.wristbandclient;

/**
 * Created by Mike on 10/7/2017.
 */
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.Switch;
import android.widget.CompoundButton;

import com.facebook.login.LoginManager;
import com.wristband.yt_b_4.wristbandclient.R;

/**
 * Created by paulaguzzetta on 10/3/17.
 */

public class CoHost extends AppCompatActivity {
    int max;
    private Button done, save,btnBack;
    String name, date, time, loc, prev_class;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cohost);
        done = (Button) findViewById(R.id.done);
        save = (Button) findViewById(R.id.save);
        btnBack = (Button) findViewById(R.id.btnBack);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoHost.this, HomeScreen.class);
                startActivity(intent);

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack(view);
            }

        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goNext(view);
            }
        });
        Intent intent = getIntent();
        name = intent.getStringExtra("eventname");
        date = intent.getStringExtra("Date");
        time = intent.getStringExtra("Time");
        loc = intent.getStringExtra("loc");
        prev_class = intent.getStringExtra("prev");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                //startActivity(new Intent(this, About.class));
                return true;
            case R.id.logout:
                LoginManager.getInstance().logOut();
                SharedPreferences settings = getSharedPreferences("account", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(this, Login.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void buttonClickParty(View view) {
        EditText person_name = (EditText) findViewById(R.id.username);
        EditText person_num = (EditText) findViewById(R.id.Usernumber);
        //text in name box
        String name = person_name.getText().toString();
        //text in number box
        String number = person_num.getText().toString();



        //search by number
        if (name.isEmpty() && !number.isEmpty()) {
            //find user from users table and add that user to the party_list table
            //if no user is found from number, search by name
            //check name to see if it is on blacklist, if it is, show toast that person is blacklisted
            //check to see if user has a profile, otherwise add blank entry to party_list table
            Toast pass = Toast.makeText(getApplicationContext(), "Added " + name + " to party", Toast.LENGTH_LONG);
            pass.show();
            //if no user is found from number, give a toast that user number is not found
            //Toast fail = Toast.makeText(getApplicationContext(), "Failed to find person with that user number", Toast.LENGTH_LONG);
            //fail.show();

        } else if (!name.isEmpty()) {
            Toast pass = Toast.makeText(getApplicationContext(), "Added " + name, Toast.LENGTH_LONG);
            pass.show();
        } else {
            Toast blank = Toast.makeText(getApplicationContext(), "Enter the username or number of person to add", Toast.LENGTH_LONG);
            blank.show();
        }
    }

    private void Add_Users() {
        Intent intent = new Intent(CoHost.this, Add_User.class);
        startActivity(intent);
    }

    private void goBack(View view) {
        if(prev_class.equals("party")) {
            Intent intent = new Intent(CoHost.this, Create_Party.class);
            intent.putExtra("eventname", name);
            intent.putExtra("Date", date);
            intent.putExtra("Time", time);
            intent.putExtra("loc", loc);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(CoHost.this, GuestScreen.class);
            intent.putExtra("prev", prev_class);
            startActivity(intent);
        }
    }

    private void goNext(View view) {
        Intent intent = new Intent(CoHost.this, Add_User.class);
        intent.putExtra("prev", "party");
        intent.putExtra("eventname", name);
        intent.putExtra("Date", date);
        intent.putExtra("Time", time);
        intent.putExtra("loc", loc);
        startActivity(intent);
    }
}