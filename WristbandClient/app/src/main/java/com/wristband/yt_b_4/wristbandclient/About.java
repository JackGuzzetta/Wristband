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
    private TextView txtuser, txtid, txtfull;
    private String user_id, user_name, fname, lname, prev_class;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private ImageView code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        txtuser = (TextView) findViewById(R.id.usertxt);
        code = (ImageView) findViewById(R.id.qr);
        txtfull = (TextView) findViewById(R.id.fullname);
        txtid = (TextView) findViewById(R.id.idtxt);
        SharedPreferences settings = getSharedPreferences("account", Context.MODE_PRIVATE);
        user_id = settings.getString("id", "default");
        user_name = settings.getString("username", "default");
        fname = settings.getString("f_name", "default");
        lname = settings.getString("l_name", "default");
        prev_class = getIntent().getStringExtra("prev");
        txtuser.setText("Username: " + user_name);
        txtfull.setText(fname+" "+ lname);
        txtid.setText("User ID: " + user_id);
        addqr(fname,lname, user_id);
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
    private void addqr(String f_name, String l_name, String user_id){
        QRGenerator x = new QRGenerator(f_name + "-" + l_name + "_" + user_id);
        code.setImageBitmap(x.createQR());
    }
}
