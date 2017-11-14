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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.login.LoginManager;
import com.wristband.yt_b_4.wristbandclient.app.AppController;
import com.wristband.yt_b_4.wristbandclient.app.QRGenerator;

import com.wristband.yt_b_4.wristbandclient.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User_Info extends AppCompatActivity {
    private Button btnBack, btnRemove;
    private TextView txtuser, txtfirst, txtlast, txtid;
    private String user_id, user_name, fname, lname, party_name, prev_class, relation, user_rel;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private ArrayList<String> names;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> userIDs;
    private ImageView code;

    String firstName;
    String lastName;
    String userId;
    String fullName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__info);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnRemove = (Button) findViewById(R.id.btnRemove);
        txtuser = (TextView) findViewById(R.id.usertxt);
        txtfirst = (TextView) findViewById(R.id.firsttxt);
        txtlast = (TextView) findViewById(R.id.lasttxt);
        txtid = (TextView) findViewById(R.id.idtxt);
        code= (ImageView) findViewById(R.id.qr);
        user_id = getIntent().getStringExtra("user_id");
        user_name = getIntent().getStringExtra("user_name");
        party_name = getIntent().getStringExtra("party_name");
        relation = getIntent().getStringExtra("relation");
        user_rel = getIntent().getStringExtra("user_rel");
        prev_class = getIntent().getStringExtra("prev");
        if(prev_class.equals("guest"))
            btnRemove.setVisibility(View.INVISIBLE);
        if(relation.equals("1"))
            btnRemove.setVisibility(View.INVISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                goBack(view);
            }

        });
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                goRemove(view);
            }

        });

        getDataFromServer();

       // user_id = getUserID(user_name);
        txtuser.setText("Full name: " + user_name);
        fname = user_name.split(" ")[0];
        txtfirst.setText("First name: "+fname);
        lname = user_name.substring(user_name.split(" ")[0].length()+1, user_name.length());
        txtlast.setText("Last name: "+lname);
        txtid.setText("User ID: " + user_id);
        //addqr(user_name);
    }



    private void addqr(String name){
        QRGenerator x = new QRGenerator(user_id);
        code.setImageBitmap(x.createQR());
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

    /*private void getDataFromServer() {
        new Thread(new Runnable() {
            public void run() {
                JsonArrayRequest req = new JsonArrayRequest(Const.URL_USERS,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    for (int i = 0; i < response.length(); i++) {
                                        String firstName;
                                        String lastName;
                                        String userId;
                                        String fullName;
                                        String username;

                                        firstName = response.getJSONObject(i).getString("f_name");
                                        lastName = response.getJSONObject(i).getString("l_name");
                                        userId = response.getJSONObject(i).getString("id");
                                        username = response.getJSONObject(i).getString("username");
                                        fullName = firstName + " " + lastName;
                                        if(fullName.equals(user_name)) {
                                            user_id = userId;
                                            user_name = username;
                                            break;
                                        }
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
    }*/

    private void getDataFromServer() {
        new Thread(new Runnable() {
            public void run() {
                JsonArrayRequest req = new JsonArrayRequest(Const.URL_USER_BY_NAME + user_name,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                        String firstName;
                                        String lastName;
                                        String userId;
                                        String username;

                                        firstName = response.getJSONObject(0).getString("f_name");
                                        lastName = response.getJSONObject(0).getString("l_name");
                                        userId = response.getJSONObject(0).getString("id");
                                        username = response.getJSONObject(0).getString("username");
                                        fullName = user_name;
                                        user_id = userId;
                                        user_name = username;


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
        Intent intent;
        if(prev_class.equals("host")) {
            intent = new Intent(User_Info.this, HostScreen.class);
        }
        else{
            intent = new Intent(User_Info.this, GuestScreen.class);
        }
        prev_class = "user_info";
        intent.putExtra("party_name", party_name);
        intent.putExtra("prev", prev_class);
        intent.putExtra("relation", user_rel);
        startActivity(intent);
    }

    private void goRemove(View view){
        new Thread(new Runnable() {
            public void run() {
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
                        Const.URL_RELATION, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //msgStatus.setText("Error creating account: " + error);
                    }
                }) {
                    /**
                     * Passing some request headers
                     * */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        headers.put("user_id", user_id);
                        headers.put("relation_id", relation);
                        return headers;
                    }
                };
                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(jsonObjReq,
                        tag_json_obj);
                // Cancelling request
                // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
            }
        }).start();
        goBack(view);
    }
}
