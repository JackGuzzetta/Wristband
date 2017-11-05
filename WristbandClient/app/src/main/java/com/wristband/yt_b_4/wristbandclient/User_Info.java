package com.wristband.yt_b_4.wristbandclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.wristband.yt_b_4.wristbandclient.app.AppController;
import com.wristband.yt_b_4.wristbandclient.app.QRGenerator;

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
    private ProgressDialog pDialog;
    private ImageView code;

    String firstName;
    String lastName;
    String userId;
    String fullName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__info);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        code= (ImageView) findViewById(R.id.qr);
        btnBack = (Button) findViewById(R.id.btnBack);
        txtuser = (TextView) findViewById(R.id.usertxt);
        txtfirst = (TextView) findViewById(R.id.firsttxt);
        txtlast = (TextView) findViewById(R.id.lasttxt);
        txtid = (TextView) findViewById(R.id.idtxt);
        user_id = getIntent().getStringExtra("user_id");
        user_name = getIntent().getStringExtra("user_name");
        addqr(user_id);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                goBack(view);
            }

        });
        getDataFromServer();


        txtuser.setText("Username: " + user_name);
        txtfirst.setText("First name: " + firstName);
//        lname = user_name.substring(user_name.split(" ")[0].length()+1, user_name.length());
        txtlast.setText("Last name: " + lname);
        txtid.setText("User ID: " + user_id);
    }
    private void addqr(String name){
        QRGenerator x = new QRGenerator(user_id);
        code.setImageBitmap(x.createQR());
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

//    private void getDataFromServer(final int id) {
//        showProgressDialog();
//        JsonArrayRequest req = new JsonArrayRequest(Const.URL_USERS + "/" + id,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        try {
//                            newString = "User extracted from Json\n";
//                            newString += response.getJSONObject(0).getString("id");
//                            newString += "\n";
//                            newString += response.getJSONObject(0).getString("f_name");
//                            newString += "\n";
//                            newString += response.getJSONObject(0).getString("l_name");
//                            newString += "\n";
//                            newString += response.getJSONObject(0).getString("username");
//                            newString += "\n";
//                            newString += response.getJSONObject(0).getString("password");
//                            newString += "\n";
//                            newString += response.getJSONObject(0).getString("email");
//                            getUserStatus.setText(newString);
//                        } catch (JSONException e) {
//                            getUserStatus.setText("Error: " + e);
//                        }
//
//                        hideProgressDialog();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                getUserStatus.setText("error: " + error.getMessage());
//                hideProgressDialog();
//            }
//        });
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(req,
//                tag_json_arry);
//        // Cancelling request
//        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_arry);
//    }

    private void getDataFromServer() {
        new Thread(new Runnable() {
            public void run() {
                JsonArrayRequest req = new JsonArrayRequest(Const.URL_USERS + user_id,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {


                                        firstName = response.getJSONObject(0).getString("f_name");
                                        lastName = response.getJSONObject(0).getString("l_name");
                                        userId = response.getJSONObject(0).getString("id");
                                        fullName = firstName + " " + lastName;
                                        names.add(fullName);
                                        userIDs.add(userId);

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
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }
    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }
}
