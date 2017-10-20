package com.wristband.yt_b_4.wristbandclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.login.LoginManager;
import com.wristband.yt_b_4.wristbandclient.app.AppController;
import com.wristband.yt_b_4.wristbandclient.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Add_User extends AppCompatActivity {
    private Button btnBack, btnDone;
    String prev_class, name1, date1, time1, loc1;
    AutoCompleteTextView autoView;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private ArrayList<String> names;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> userIDs;
    private EditText foundID;
    private AutoCompleteTextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__user);
        btnBack = (Button) findViewById(R.id.next);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack(view);
            }

        });
        btnDone = (Button) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back_Homescreen(view);
            }

        });

        Intent intent = getIntent();
        name1 = intent.getStringExtra("eventname");
        date1 = intent.getStringExtra("Date");
        time1 = intent.getStringExtra("Time");
        loc1 = intent.getStringExtra("loc");
        prev_class = intent.getStringExtra("prev");
        names = new ArrayList<>();
        foundID = (EditText) findViewById(R.id.id);

        userIDs = new ArrayList<>();
        getDataFromServer();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, names);
        textView = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView);
        textView.setAdapter(adapter);



        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String text = textView.getText().toString();
                    //username.setText(getUserID(text));
                    getUserID(text);
                }

            }
        });

    }

    private String getUserID(String fullName) {
        String cur;
        String ID;
        int pos = -1;
        if (names.contains(fullName)) {

            int idx = names.indexOf(fullName);
            ID = userIDs.get(idx);
            foundID.setText(ID);
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
                                        String firstName;
                                        String lastName;
                                        String userId;
                                        String fullName;
                                        String nameWithID;

                                        firstName = response.getJSONObject(i).getString("f_name");
                                        lastName = response.getJSONObject(i).getString("l_name");
                                        userId = response.getJSONObject(i).getString("id");
                                        fullName = firstName + " " + lastName;
                                        names.add(fullName);
                                        userIDs.add(userId);
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

    public void buttonClickParty(View view) {

            String text = textView.getText().toString();
            getUserID(text);

//        //Intent intent = new Intent(this, DisplayMessageActivity.class);
//        EditText person_name = (EditText) findViewById(R.id.invitee);
//        EditText person_num = (EditText) findViewById(R.id.number);
//        //text in name box
//        String name = person_name.getText().toString();
//        //text in number box
//        String number = person_num.getText().toString();
//
//        /*This will check the blacklist, users, party, and party_list tables to verify and/or add
//        the person to the table.
//         */
//
//        //make sure party is not at capacity by checking max people value from party table
//
//        //search by number
//        if (name.isEmpty() && !number.isEmpty()) {
//            //find user from users table and add that user to the party_list table
//            //if no user is found from number, search by name
//            //check name to see if it is on blacklist, if it is, show toast that person is blacklisted
//            //check to see if user has a profile, otherwise add blank entry to party_list table
//            Toast pass = Toast.makeText(getApplicationContext(), "Added " + name + " to party", Toast.LENGTH_LONG);
//            pass.show();
//            //if no user is found from number, give a toast that user number is not found
//            //Toast fail = Toast.makeText(getApplicationContext(), "Failed to find person with that user number", Toast.LENGTH_LONG);
//            //fail.show();
//
//        } else if (!name.isEmpty()) {
//            //search for person by name, if not found, add person to both users and party_list tables
//            Toast pass = Toast.makeText(getApplicationContext(), "Added " + name + " to party", Toast.LENGTH_LONG);
//            pass.show();
//        } else {
//            Toast blank = Toast.makeText(getApplicationContext(), "Enter the name or user number of person to add", Toast.LENGTH_LONG);
//            blank.show();
//        }
    }

    private void back_Homescreen(View view) {
        Intent intent = new Intent(Add_User.this, HomeScreen.class);
        startActivity(intent);
    }

    private void goBack(View view) {
        if(prev_class.equals("party")) {
            Intent intent = new Intent(Add_User.this, CoHost.class);
            intent.putExtra("prev", "party");
            intent.putExtra("eventname", name1);
            intent.putExtra("Date", date1);
            intent.putExtra("Time", time1);
            intent.putExtra("loc", loc1);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(Add_User.this, GuestScreen.class);
            prev_class="add_user";
            intent.putExtra("prev", prev_class);
            startActivity(intent);
        }
    }
}