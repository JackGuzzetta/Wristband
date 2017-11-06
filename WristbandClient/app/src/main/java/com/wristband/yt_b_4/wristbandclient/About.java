package com.wristband.yt_b_4.wristbandclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;

public class About extends AppCompatActivity {
    private Button btnBack;
    private TextView txtuser, txtid, txtfirst, txtlast;
    private String user_id, user_name, fname, lname, fullname, prev_class;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        btnBack = (Button) findViewById(R.id.btnBack);
        txtuser = (TextView) findViewById(R.id.usertxt);
        //txtfirst = (TextView) findViewById(R.id.firsttxt);
        //txtlast = (TextView) findViewById(R.id.lasttxt);
        txtid = (TextView) findViewById(R.id.idtxt);
        //fullname = getIntent().getStringExtra("user_name");
        SharedPreferences settings = getSharedPreferences("account", Context.MODE_PRIVATE);
        user_id = settings.getString("id", "default");
        user_name = settings.getString("username", "default");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                goBack(view);
            }

        });
        prev_class = getIntent().getStringExtra("prev");
        About();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_info, menu);
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

    private void About(){
        txtuser.setText("Username: " + user_name);
        //fname = fullname.split(" ")[0];
        //txtfirst.setText("First name: "+fname);
        //lname = fullname.substring(fullname.split(" ")[0].length()+1, fullname.length());
        //txtlast.setText("Last name: "+lname);
        txtid.setText("User ID: " + user_id);
    }

    private void goBack(View view) {
        Intent intent = new Intent(About.this, HomeScreen.class);
        startActivity(intent);
    }

}
