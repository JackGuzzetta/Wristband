package com.wristband.yt_b_4.wristbandclient;

/**
 * Created by Jackguzzetta on 10/23/17.
 */

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.List;

import com.wristband.yt_b_4.wristbandclient.app.AppController;
import com.wristband.yt_b_4.wristbandclient.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;


public class PublicParties extends AppCompatActivity {
    ListView listView;
    List list = new ArrayList();
    ArrayList<String> party_ids = new ArrayList<String>();
    ArrayList<String> pubparty_id = new ArrayList();

    ArrayAdapter adapter;
    ProgressDialog pDialog;
    String user_id, pid, user_name;
    String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicparty);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        initializeControls();
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeControls() {
//        for (int i=0; i< list.size();i++){
//            for (int j =i+1; j<list.size();j++){
//                if(list.get(j).equals(list.get(i))){
//                    list.remove(j);
//                }
//            }
//        }
        listView = (ListView) findViewById(R.id.list_view2);
        SharedPreferences settings = getSharedPreferences("account", Context.MODE_PRIVATE);
        user_id = settings.getString("id", "default");

        adapter = new ArrayAdapter(PublicParties.this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                pid= pubparty_id.get(position);
                //Toast.makeText(getApplicationContext(), pid , Toast.LENGTH_LONG).show();
                String party_name = (String) parent.getItemAtPosition(position);
                guestScreen(party_name);
                // Display the selected item text on TextView

            }
        });



        getAllPartiesBypub();

    }

    public void guestScreen(String party_name) {
        Intent intent = new Intent(this, GuestScreen.class);
        intent.putExtra("party_name", party_name);
        intent.putExtra("username", user_name);
        intent.putExtra("party_id", pid);
        intent.putExtra("relation", 2);
        startActivity(intent);
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
        Intent intent = new Intent(PublicParties.this, HomeScreen.class);
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



    private void getAllPartiesBypub() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(500L); //wait for party to be created first
                    JsonArrayRequest req = new JsonArrayRequest(Const.URL_PARTY,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
                                        for (int i = 0; i < response.length(); i++) {
                                            String id = response.getJSONObject(i).getString("id");
                                            party_ids.add(id);
                                            getDataFromServer(id);
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getDataFromServer(final String id) {
        new Thread(new Runnable() {
            public void run() {
                //showProgressDialog();
                JsonArrayRequest req = new JsonArrayRequest(Const.URL_PARTY+id,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    String privacy;
                                    int priv;
                                    String name = response.getJSONObject(0).getString("party_name");
                                    String x = response.getJSONObject(0).getString("id");
                                    privacy = response.getJSONObject(0).getString("privacy");
                                    if(privacy.equals("1")) {
                                          priv = Integer.parseInt(privacy);
                                         }
                                         else{priv=0;}


                                    if (priv == 1) {
                                        list.add(name);
                                        pubparty_id.add(x);
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
