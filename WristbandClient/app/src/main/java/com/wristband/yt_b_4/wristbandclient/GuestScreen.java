package com.wristband.yt_b_4.wristbandclient;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.login.LoginManager;
import com.wristband.yt_b_4.wristbandclient.app.AppController;
import com.wristband.yt_b_4.wristbandclient.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuestScreen extends AppCompatActivity {
    private Button btnCohost, btnBack, btnLocation, btnPhotos, btnComments;
    private TextView dateText, partyText, locationTxt, timeTxt;
    private ProgressDialog pDialog;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private String party_id, user_id;
    private String party_name, user_name, relation, prev_class, loc;
    final Context context = this;
    ListView listView;
    List list = new ArrayList();
    ArrayAdapter adapter;
    List relationList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_screen);
        btnLocation = (Button) findViewById(R.id.button7);
        btnPhotos = (Button) findViewById(R.id.button5);
        btnComments = (Button) findViewById(R.id.button6);
        partyText = (TextView) findViewById(R.id.partyTxt);
        timeTxt = (TextView) findViewById(R.id.time);
        locationTxt = (TextView) findViewById(R.id.location);
        dateText = (TextView) findViewById(R.id.dateTxt);
        SharedPreferences settings = getSharedPreferences("account", Context.MODE_PRIVATE);
        user_id = settings.getString("id", "default");
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        party_name = getIntent().getStringExtra("party_name");
        relation = getIntent().getStringExtra("relation");
        party_id = getIntent().getStringExtra("party_id");

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                goLocation(loc);
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


        listView = (ListView) findViewById(R.id.list_view);
        adapter = new ArrayAdapter(GuestScreen.this, android.R.layout.simple_list_item_1, list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the current item from ListView
                View view = super.getView(position, convertView, parent);
                if (relationList.get(position).equals("1")) {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.parseColor("#19c482"));
                } else if (relationList.get(position).equals("2")) {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.parseColor("#a6abae"));
                } else if (relationList.get(position).equals("3")) {
                    view.setBackgroundColor(Color.parseColor("#326f93"));
                } else {
                    view.setBackgroundColor(Color.RED);
                }
                return view;
            }
        };
        listView.setAdapter(adapter);
        ;
        getDataFromServer();
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l){
                Intent intent = new Intent(GuestScreen.this, User_Info.class);
                user_name = (listView.getItemAtPosition(i)).toString();
                intent.putExtra("user_rel", relation);
                relation = relationList.get(i).toString();
                intent.putExtra("prev", "guest");
                intent.putExtra("user_id", user_id);
                intent.putExtra("user_name", user_name);
                intent.putExtra("party_name", party_name);
                intent.putExtra("party_id", party_id);
                intent.putExtra("relation", relation);
                startActivity(intent);
            }
        });

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
            case R.id.delete:
                deleteUser();
                startActivity(new Intent(GuestScreen.this, HomeScreen.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GuestScreen.this, HomeScreen.class);
        startActivity(intent);
    }
    private void deleteUser() {
        if (relation.equals("1")) {
            deleteParty(party_id);
        } else if (relation.equals("2") || relation.equals("3")) {
            deleteRelation(user_id, relation);
        } else {
            //error
        }
    }

    private void deleteRelation(final String user_id, final String party_id) {
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
                        headers.put("party_id", party_id);
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
    }

    private void deleteParty(final String party_id) {
        new Thread(new Runnable() {
            public void run() {
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
                        Const.URL_PARTY + party_id, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
                    /**
                     * Passing some request headers
                     * */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        //headers.put("party_id", party_id);
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
    }

    private void getDataFromServer() {
        new Thread(new Runnable() {
            public void run() {
                JsonArrayRequest req = new JsonArrayRequest(Const.URL_PARTY_BY_NAME + party_name,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    String name = response.getJSONObject(0).getString("party_name");
                                    String date = response.getJSONObject(0).getString("date");
                                    String host = response.getJSONObject(0).getString("host");
                                    String time = response.getJSONObject(0).getString("time");
                                    String location = response.getJSONObject(0).getString("location");
                                    //party_id = response.getJSONObject(0).getString("id");
                                    loc = location;
                                    partyText.setText("Party name: " + name);
                                    dateText.setText("Date: " + date);
                                    locationTxt.setText("Location: " + location);
                                    timeTxt.setText("Time: " + time);
                                    getAllUsers();
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


    private void goLocation(String party_name) {
        Intent intent = new Intent(GuestScreen.this, MapsActivity.class);
        intent.putExtra("party_location", party_name);
        intent.putExtra("prev", "guest");
        intent.putExtra("party_name", party_id);
        intent.putExtra("relation", relation);
        finish();
        startActivity(intent);
    }

    private void goPhotos(View view) {
        Intent intent = new Intent(GuestScreen.this, Photos.class);
        intent.putExtra("party_name", party_id);
        intent.putExtra("relation", relation);
        intent.putExtra("prev", "guest");
        startActivity(intent);
    }

    private void goComments(View view) {

        Intent intent = new Intent(GuestScreen.this, Comments.class);
        intent.putExtra("party_name", party_id);
        intent.putExtra("relation", relation);
        intent.putExtra("prev", "guest");
        startActivity(intent);
    }



    private void getAllUsers() {
        JsonArrayRequest req = new JsonArrayRequest(Const.URL_JOIN_Party + party_id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                String Relation = response.getJSONObject(i).getString("party_user_relation");
                                relationList.add(Relation);
                                String name = response.getJSONObject(i).getString("f_name");
                                name += " ";
                                name += response.getJSONObject(i).getString("l_name");
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
        AppController.getInstance().addToRequestQueue(req,
                tag_json_arry);
    }
}