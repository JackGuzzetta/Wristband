package com.wristband.yt_b_4.wristbandclient;

import com.facebook.login.LoginManager;
import com.wristband.yt_b_4.wristbandclient.app.AppController;
import com.wristband.yt_b_4.wristbandclient.models.Party;
import com.wristband.yt_b_4.wristbandclient.utils.Const;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

public class GuestScreen extends AppCompatActivity {
    private Button btnCohost, btnBack, btnGuest, btnBlacklist, btnPhotos, btnComments;
    private TextView dateText, partyText, responseTxt, locationTxt, timeTxt;
    private ProgressDialog pDialog;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private Party party;
    private String party_name, prev_class;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_screen2);
        btnCohost = (Button) findViewById(R.id.btnCohost);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnGuest = (Button) findViewById(R.id.btnGuest);
        btnBlacklist = (Button) findViewById(R.id.btnBlacklist);
        btnPhotos = (Button) findViewById(R.id.button5);
        btnComments = (Button) findViewById(R.id.button6);
        partyText = (TextView) findViewById(R.id.partyTxt);
        responseTxt = (TextView) findViewById(R.id.msgResponse);
        timeTxt = (TextView) findViewById(R.id.time);
        locationTxt = (TextView) findViewById(R.id.location);
        dateText = (TextView) findViewById(R.id.dateTxt);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        party_name = getIntent().getStringExtra("party_name");
        getDataFromServer();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                goBack(view);
            }

        });
        btnCohost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                goCohost(view);
            }
        });
        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                goGuest(view);
            }
        });
        btnBlacklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                goBlacklist(view);
            }
        });
        btnPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                goPhotos(view);
            }
        });
        btnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                goComments(view);
            }
        });
        Intent intent = getIntent();
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
                JsonArrayRequest req = new JsonArrayRequest(Const.URL_PARTY_BY_NAME + party_name,
                        new Response.Listener < JSONArray > () {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    String name = response.getJSONObject(0).getString("party_name");
                                    String date = response.getJSONObject(0).getString("date");
                                    String host = response.getJSONObject(0).getString("host");
                                    String time = response.getJSONObject(0).getString("time");
                                    String location = response.getJSONObject(0).getString("location");
                                    partyText.setText("Party name: " + name);
                                    dateText.setText("Date: " + date);
                                    locationTxt.setText("Location: " + location);
                                    responseTxt.setText("Data retrieved from server");
                                    timeTxt.setText("Time: " + time);
                                } catch (JSONException e) {
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                AppController.getInstance().addToRequestQueue(req,
                        tag_json_arry);
                // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_arry);

            }
        }).start();
    }
    private void goBack(View view) {
        Intent intent = new Intent(GuestScreen.this, HomeScreen.class);
        startActivity(intent);
    }

    private void goCohost(View view) {
        Intent intent = new Intent(GuestScreen.this, CoHost.class);
        intent.putExtra("prev", "guest");
        startActivity(intent);
    }

    private void goGuest(View view) {
        Intent intent = new Intent(GuestScreen.this, Add_User.class);
        startActivity(intent);
    }
    private void goBlacklist(View view) {
        Intent intent = new Intent(GuestScreen.this, Blacklist.class);
        startActivity(intent);
    }
    private void goPhotos(View view) {
        Intent intent = new Intent(GuestScreen.this, Photos.class);
        startActivity(intent);
    }
    private void goComments(View view) {
        Intent intent = new Intent(GuestScreen.this, Comments.class);
        startActivity(intent);
    }
}