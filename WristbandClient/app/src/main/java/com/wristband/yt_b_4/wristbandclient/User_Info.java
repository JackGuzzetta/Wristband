package com.wristband.yt_b_4.wristbandclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.wristband.yt_b_4.wristbandclient.app.AppController;
import com.wristband.yt_b_4.wristbandclient.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;

public class User_Info extends AppCompatActivity {
    private Button btnBack;
    private TextView txtuser, txtfirst, txtlast, txtid;
    private String user_id, user_name;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__info);
        btnBack = (Button) findViewById(R.id.btnBack);
        txtuser = (TextView) findViewById(R.id.usertxt);
        txtfirst = (TextView) findViewById(R.id.firsttxt);
        txtlast = (TextView) findViewById(R.id.lasttxt);
        txtid = (TextView) findViewById(R.id.idtxt);
        user_id = getIntent().getStringExtra("user_id");
        user_name = getIntent().getStringExtra("user_name");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                goBack(view);
            }

        });
        txtuser.setText("Username: " + user_name);
        txtid.setText("User ID: " + user_id);
        getDataFromServer();
    }


    private void getDataFromServer() {
        new Thread(new Runnable() {
            public void run() {
                JsonArrayRequest req = new JsonArrayRequest(Const.URL_USER_BY_NAME + user_id,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    String name = response.getJSONObject(0).getString("party_name");
                                    String fname = response.getJSONObject(0).getString("date");
                                    String lname = response.getJSONObject(0).getString("location");
                                    txtuser.setText("Username: " + user_name);
                                    txtfirst.setText("First name: " + fname);
                                    txtlast.setText("Last name: " + lname);
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
        Intent intent = new Intent(this, GuestScreen.class);
        startActivity(intent);
    }
}
