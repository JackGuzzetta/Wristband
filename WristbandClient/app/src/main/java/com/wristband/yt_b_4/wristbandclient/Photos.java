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

import com.facebook.login.LoginManager;

public class Photos extends AppCompatActivity {
    private String party_name, relation, user_id, prev_class;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);


        Intent intent = getIntent();
        party_name = intent.getStringExtra("party_name");
        relation = intent.getStringExtra("relation");
        user_id = intent.getStringExtra("user_id");
        prev_class = getIntent().getStringExtra("prev");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_info, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                startActivity(new Intent(this, About.class));
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

    private void goBack(View view) {
        Intent intent;
        if(prev_class.equals("host")) {
            intent = new Intent(Photos.this, HostScreen.class);
        }
        else{
            intent = new Intent(Photos.this, GuestScreen.class);
        }
        intent.putExtra("party_name", party_name);
        intent.putExtra("relation", relation);
        intent.putExtra("prev", "photos");
        startActivity(intent);
    }
}
