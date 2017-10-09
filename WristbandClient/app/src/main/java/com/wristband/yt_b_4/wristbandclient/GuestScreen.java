package com.wristband.yt_b_4.wristbandclient;

import com.facebook.login.LoginManager;
import com.wristband.yt_b_4.wristbandclient.app.AppController;
import com.wristband.yt_b_4.wristbandclient.utils.Const;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

public class GuestScreen extends AppCompatActivity {
    private Button getPartyBtn, btnBack;
    private TextView dateText, partyText, responseTxt;
    private ProgressDialog pDialog;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_screen2);
        getPartyBtn = (Button) findViewById(R.id.btnJsonArray);
        btnBack = (Button) findViewById(R.id.btnBack);
        partyText = (TextView) findViewById(R.id.partyTxt);
        responseTxt = (TextView) findViewById(R.id.msgResponse);
        dateText = (TextView) findViewById(R.id.dateTxt);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                goBack(view);
            }

        });
        getPartyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                int id;
                try {
                    id = 1;
                    getDataFromServer(id);
                } catch (NumberFormatException e) {
                    responseTxt.setText("Invalid ID, not a string");
                }
            }
        });
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
    private void getDataFromServer(final int id) {
        showProgressDialog();
        JsonArrayRequest req = new JsonArrayRequest(Const.URL_PARTY + "/" + id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String newString = "Party extracted from Json\n";
                            newString += response.getJSONObject(0).getString("party_name");
                            newString += "\n";
                            newString += response.getJSONObject(0).getString("date");

                            responseTxt.setText(newString);
                        } catch (JSONException e) {
                            responseTxt.setText("Error: " + e);
                        }

                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                responseTxt.setText("error: " + error.getMessage());
                hideProgressDialog();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req,
                tag_json_arry);
        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_arry);
    }

    private void goBack(View view){
        Intent intent = new Intent (GuestScreen.this, HomeScreen.class);
        startActivity(intent);
    }

}

