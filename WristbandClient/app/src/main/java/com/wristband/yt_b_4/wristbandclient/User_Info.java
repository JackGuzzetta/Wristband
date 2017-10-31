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

import java.util.ArrayList;

public class User_Info extends AppCompatActivity {
    private Button btnBack;
    private TextView txtuser, txtfirst, txtlast, txtid;
    private String user_id, user_name, lname;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private ArrayList<String> names;
    private ArrayList<String> userIDs;
    String firstName;
    String lastName;
    String userId;
    String fullName;
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
        names = new ArrayList<>();
        userIDs = new ArrayList<>();
        getDataFromServer();
        user_id = getUserID(user_name);
        txtuser.setText("Username: " + user_name);
        txtfirst.setText("First name: " + user_name.split(" ")[0]);
        lname = user_name.substring(user_name.split(" ")[0].length()+1, user_name.length());
        txtlast.setText("Last name: " + lname);
        txtid.setText("User ID: " + user_id);
    }

    private String getUserID(String fullName) {
        String ID;
        if (names.contains(fullName)) {
            int idx = names.indexOf(fullName);
            ID = userIDs.get(idx);
            return ID;
        }
        return null;
    }


    private void getDataFromServer() {
        new Thread(new Runnable() {
            public void run() {
                JsonArrayRequest req = new JsonArrayRequest(Const.URL_USERS,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    for (int i = 0; i < response.length(); i++) {
                                        firstName = response.getJSONObject(i).getString("f_name");
                                        lastName = response.getJSONObject(i).getString("l_name");
                                        userId = response.getJSONObject(i).getString("id");
                                        fullName = firstName + " " + lastName;
                                        names.add(fullName);
                                        userIDs.add(userId);
                                    }
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
            }
        }).start();
    }


    private void goBack(View view) {
        Intent intent = new Intent(this, HostScreen.class);
        startActivity(intent);
    }
}
