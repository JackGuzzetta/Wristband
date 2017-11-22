package com.wristband.yt_b_4.wristbandclient;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.login.LoginManager;
import com.wristband.yt_b_4.wristbandclient.app.AppController;
import com.wristband.yt_b_4.wristbandclient.utils.Const;
import com.wristband.yt_b_4.wristbandclient.utils.VolleyHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Add_User extends AppCompatActivity {
    final Context context = this;
    private Button btnDone;
    private CheckBox checkbox;
    private String prev_class, name1, date1, time1, loc1, relation;
    private AutoCompleteTextView autoView;
    private EditText phoneNumber;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private ArrayList<String> names;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> userIDs;
    private AutoCompleteTextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__user);
        btnDone = (Button) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back_Homescreen(view);
            }

        });
        checkbox = (CheckBox) findViewById(R.id.checkBox);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        Intent intent = getIntent();
        loc1 = intent.getStringExtra("loc");
        name1 = intent.getStringExtra("party_name");
        relation = intent.getStringExtra("relation");

        intent.putExtra("relation", relation);
        prev_class = intent.getStringExtra("prev");
        names = new ArrayList<>();

        userIDs = new ArrayList<>();
        getDataFromServer();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, names);
        textView = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView);
        textView.setAdapter(adapter);


        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String text = textView.getText().toString();
                    //username.setText(getUserID(text));
                    getUserID(text);
                }
            }
        });
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.SEND_SMS},1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},1);

    }

    private String getUserID(String fullName) {
        String cur;
        String ID;
        int pos = -1;
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

    public void buttonClickParty(View view) {
        String user_id;
        boolean isChecked;
        String coHost;
        String text = textView.getText().toString();
        if (text.isEmpty() == false) {
            user_id = getUserID(text);
            isChecked = checkbox.isChecked();
            if (isChecked == true) {
                //cohost = true
                coHost = "3";
            } else {
                coHost = "2";
            }
            Intent intent = getIntent();
            String party_id = intent.getStringExtra("party_id");
            VolleyHandler.inviteUser(party_id, user_id, coHost);
            Toast pass = Toast.makeText(getApplicationContext(), text + " added to party", Toast.LENGTH_LONG);
            pass.show();
            textView.setText("");
        } else {
            Toast fail = Toast.makeText(getApplicationContext(), "Please enter name", Toast.LENGTH_LONG);
            fail.show();
        }
        String number = phoneNumber.getText().toString();
        if (!number.isEmpty()) {
            VolleyHandler.invitebyNumber(number);
        }
        //PhoneController p = new PhoneController();
        //p.sendSMS(getApplicationContext(), phoneNumber.getText().toString(), "123");
    }

    private void back_Homescreen(View view) {
        Intent intent = new Intent(Add_User.this, HomeScreen.class);
        startActivity(intent);
    }

    private void goBack(View view) {
        if (prev_class.equals("party")) {
            Intent intent = new Intent(this, HostScreen.class);
            intent.putExtra("party_name", name1);
            intent.putExtra("relation", relation);
            finish();
            startActivity(intent);
        } else {
            Intent intent = new Intent(Add_User.this, HostScreen.class);
            prev_class = "add_user";
            intent.putExtra("party_name", name1);
            intent.putExtra("prev", prev_class);
            startActivity(intent);
        }
    }



}