package com.wristband.yt_b_4.wristbandclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.facebook.login.LoginManager;
import com.wristband.yt_b_4.wristbandclient.R;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wristband.yt_b_4.wristbandclient.app.AppController;
import com.wristband.yt_b_4.wristbandclient.models.User;
import com.wristband.yt_b_4.wristbandclient.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginProfile extends AppCompatActivity {
    private String TAG = Create_Profile.class.getSimpleName();
    private User user;
    private EditText user_name, user_password;
    private Button btnLogin, btnBack;
    private TextView msgStatus;
    private ProgressDialog pDialog;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_profile);
        initializeControls();
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
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
    private void initializeControls() {
        user_name = (EditText) findViewById(R.id.username);
        user_password = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnBack = (Button) findViewById(R.id.btnBack);
        msgStatus = (TextView) findViewById(R.id.msgResponse);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                createLogin(view);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                goBack(view);
            }

        });
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    private void goBack(View view) {
        Intent intent = new Intent(LoginProfile.this, Login.class);
        startActivity(intent);
    }

    private void createLogin(View view) {
        Toast fail;
        //text in username box
        String username = user_name.getText().toString();
        //text in password box
        String password = user_password.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            fail = Toast.makeText(getApplicationContext(), "Required information missing", Toast.LENGTH_LONG);
            fail.show();
        } else {
            sendDataToServer(username, password);
        }

    }
    private void sendDataToServer(final String username, final String password) {
        showProgressDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Const.URL_USERS + "/login", null,
                new Response.Listener < JSONObject > () {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast toast;
                        try {
                            String responseToken = response.getString("token");
                            String username = response.getString("username");
                            toast = Toast.makeText(getApplicationContext(), "Welcome: " + username, Toast.LENGTH_LONG);
                            toast.show();
                            //stores user and token into encrypted storage accessible across activities
                            SharedPreferences settings = getSharedPreferences("account", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("token", responseToken);
                            editor.putString("username", username);
                            editor.commit();

                            //to get the stored token and username
                            //SharedPreferences settings = getSharedPreferences("account", Context.MODE_PRIVATE);
                            //String myString = settings.getString("username", "default");

                            Intent intent = new Intent(LoginProfile.this, HomeScreen.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            toast = Toast.makeText(getApplicationContext(), "Invalid Login Credentials", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getApplicationContext(), "Invalid Login Credentials", Toast.LENGTH_LONG);
                toast.show();
                hideProgressDialog();
            }
        }) {
            /**
             * Passing some request headers
             * */
            @Override
            public Map < String, String > getHeaders() throws AuthFailureError {
                HashMap < String, String > headers = new HashMap < String, String > ();
                headers.put("Content-Type", "application/json");
                headers.put("username", username);
                headers.put("password", password);
                return headers;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                tag_json_obj);
        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }
}