package com.wristband.yt_b_4.wristbandclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.login.LoginManager;
import java.util.ArrayList;
import java.util.List;
import com.wristband.yt_b_4.wristbandclient.app.AppController;
import com.wristband.yt_b_4.wristbandclient.models.Party;
import com.wristband.yt_b_4.wristbandclient.utils.Const;
import org.json.JSONArray;
import org.json.JSONException;


public class HomeScreen extends AppCompatActivity {

    Button GuestButton;
    Button NewPartyButton;
    ListView listView;
    List list = new ArrayList();
    ArrayAdapter adapter;
    ProgressDialog pDialog;
    String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen2);
        GuestButton = (Button) findViewById(R.id.button2);
        NewPartyButton = (Button) findViewById(R.id.button3);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        initializeControls();
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

    private void initializeControls()  {
        listView = (ListView) findViewById(R.id.list_view);
        SharedPreferences settings = getSharedPreferences("account", Context.MODE_PRIVATE);
        String myString = settings.getString("username", "default");
        adapter = new ArrayAdapter(HomeScreen.this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        GuestButton = (Button) findViewById(R.id.button2);
        GuestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                guestScreen();
            }
        });
        NewPartyButton = (Button) findViewById(R.id.button3);
        NewPartyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newParty();
            }
        });

        getDataFromServer();
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
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }
    private void getDataFromServer() {
        new Thread(new Runnable() {
            public void run() {
                //showProgressDialog();
                JsonArrayRequest req = new JsonArrayRequest(Const.URL_PARTY,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    for (int i=0; i < response.length(); i++) {
                                        String name = response.getJSONObject(i).getString("party_name");
                                        list.add(name);
                                        adapter.notifyDataSetChanged();
                                    }
                                } catch (JSONException e) {
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(req,
                        tag_json_arry);
                // Cancelling request
                // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_arry);

            }
        }).start();
    }
}
