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
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.wristband.yt_b_4.wristbandclient.app.QRGenerator;

public class About extends AppCompatActivity {
    private TextView txtuser, txtid, txtfirst, txtlast;
    private String user_id, user_name, fname, lname, fullname, prev_class;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private ImageView code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        txtuser = (TextView) findViewById(R.id.usertxt);
        code= (ImageView) findViewById(R.id.qr);
        //txtfirst = (TextView) findViewById(R.id.firsttxt);
        //txtlast = (TextView) findViewById(R.id.lasttxt);
        txtid = (TextView) findViewById(R.id.idtxt);
        //fullname = getIntent().getStringExtra("user_name");
        SharedPreferences settings = getSharedPreferences("account", Context.MODE_PRIVATE);
        user_id = settings.getString("id", "default");
        user_name = settings.getString("username", "default");

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
            case android.R.id.home:
                onBackPressed();
                return true;
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
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(About.this, HomeScreen.class);
        startActivity(intent);
    }
    private void addqr(String name){
        QRGenerator x = new QRGenerator(user_name);
        code.setImageBitmap(x.createQR());
    }

    private void About(){
        txtuser.setText("Username: " + user_name);
        //fname = fullname.split(" ")[0];
        //txtfirst.setText("First name: "+fname);
        //lname = fullname.substring(fullname.split(" ")[0].length()+1, fullname.length());
        //txtlast.setText("Last name: "+lname);
        txtid.setText("User ID: " + user_id);
        addqr(user_name);

    }


}
