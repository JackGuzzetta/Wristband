package com.wristband.yt_b_4.wristbandclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.app.Dialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.login.LoginManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.wristband.yt_b_4.wristbandclient.app.AppController;
import com.wristband.yt_b_4.wristbandclient.utils.Const;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HomeScreen extends AppCompatActivity {

    Button NewPartyButton, publicparty, yes, no;
    ListView listView;
    List list = new ArrayList();
    List relationList = new ArrayList();
    Dialog DeleteDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<String> party_ids = new ArrayList();
    ArrayAdapter adapter;
    ProgressDialog pDialog;
    String user_id, user_name;
    String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen2);



        publicparty = (Button) findViewById(R.id.publicparties);
        NewPartyButton = (Button) findViewById(R.id.button3);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        initializeControls();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                getAllPartiesByUserId();
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_info, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.about:
                startActivity(new Intent(this, About.class));
                return true;
            case R.id.logout:
                LoginManager.getInstance().logOut();
                settings = getSharedPreferences("account", Context.MODE_PRIVATE);
                editor = settings.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(this, Login.class));
                return true;
            case R.id.delete:
                deletionDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeControls() {
        listView = (ListView) findViewById(R.id.list_view);
        SharedPreferences settings = getSharedPreferences("account", Context.MODE_PRIVATE);
        user_id = settings.getString("id", "default");
        user_name= settings.getString("username", "default");
        adapter = new ArrayAdapter(HomeScreen.this, android.R.layout.simple_list_item_1, list) {
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


        for (int i = 0; i < listView.getChildCount(); i++) {
            listView.getChildAt(i).setBackgroundColor(Color.CYAN);
        }

        //listView.setBackgroundColor(Color.CYAN);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                String party_name = (String) parent.getItemAtPosition(position);
                String relation = relationList.get(position).toString();
                String party_id = party_ids.get(position).toString();
                if (relation.equals("1")) {
                    //Host
                    hostScreen(1,party_name, relation, party_id);
                }
                else if (relation.equals("2")) {
                    hostScreen(2,party_name, relation, party_id);
                    //Guest
                }
                else if (relation.equals("3")) {
                    hostScreen(3,party_name, relation, party_id);
                    //CoHost
                }
                else {
                    //Error
                }

                // Display the selected item text on TextView

            }
        });
        publicparty = (Button) findViewById(R.id.publicparties);
        NewPartyButton = (Button) findViewById(R.id.button3);
        NewPartyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newParty();
            }
        });
        publicparty.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                publicparty();
            }
        });

        getAllPartiesByUserId();
    }

    public void hostScreen(int screen, String party_name, String relation, String party_id) {
        Intent intent;
        switch (screen) {
            case 1://host
                intent = new Intent(this, HostScreen.class);
                intent.putExtra("party_name", party_name);
                intent.putExtra("party_id", party_id);
                intent.putExtra("relation", relation);
                startActivity(intent);
                finish();
                break;
            case 2://guest
                intent = new Intent(this, GuestScreen.class);
                intent.putExtra("party_name", party_name);
                intent.putExtra("party_id", party_id);
                intent.putExtra("relation", relation);
                startActivity(intent);
                finish();
                break;
            case 3://cohost
                intent = new Intent(this, GuestScreen.class);
                intent.putExtra("party_name", party_name);
                intent.putExtra("party_id", party_id);
                intent.putExtra("relation", relation);
                startActivity(intent);
                finish();
                break;
            default:
        }

    }

    private void publicparty() {
        Intent intent = new Intent(HomeScreen.this, PublicParties.class);

        startActivity(intent);
    }

    private void deleteAccount(final String user_id) {
        new Thread(new Runnable() {
            public void run() {
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
                        Const.URL_USERS + "/" + user_id, null,
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

    public void newParty() {
        Intent intent = new Intent(this, Create_Party.class);
        finish();
        startActivity(intent);
    }


    public void logout(View view) {
        Intent intent = new Intent(this, Login.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
    public void User_Info(String User_Id){
        Intent intent = new Intent(HomeScreen.this, User_Info.class);
        intent.putExtra("user_id", User_Id);
        intent.putExtra("user_name", user_name);
        startActivity(intent);
    }

    public void deletionDialog() {
        final SharedPreferences[] settings = new SharedPreferences[1];
        final SharedPreferences.Editor[] editor = new SharedPreferences.Editor[1];
        DeleteDialog = new Dialog(HomeScreen.this);
        DeleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DeleteDialog.setContentView(R.layout.delete_dialog);
        DeleteDialog.setTitle("Are you sure?");

        yes = (Button)DeleteDialog.findViewById(R.id.yes);
        no = (Button)DeleteDialog.findViewById(R.id.no);

        yes.setEnabled(true);
        no.setEnabled(true);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount(user_id);
                LoginManager.getInstance().logOut();
                settings[0] = getSharedPreferences("account", Context.MODE_PRIVATE);
                editor[0] = settings[0].edit();
                editor[0].clear();
                editor[0].commit();
                startActivity(new Intent(HomeScreen.this, Login.class));
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDialog.cancel();
            }
        });

        DeleteDialog.show();
    }

    private void getAllPartiesByUserId() {
        list.clear();
        new Thread(new Runnable() {
            public void run() {
                JsonArrayRequest req = new JsonArrayRequest(Const.URL_JOIN_USER + user_id,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    for (int i = 0; i < response.length(); i++) {
                                        String id = response.getJSONObject(i).getString("party_id");
                                        String relation = response.getJSONObject(i).getString("party_user_relation");
                                        String name = response.getJSONObject(i).getString("party_name");
                                        list.add(name);
                                        party_ids.add(id);
                                        relationList.add(relation);
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
        }).start();
    }
}
