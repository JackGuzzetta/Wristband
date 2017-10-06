package com.wristband.yt_b_4.wristbandclient;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wristband.yt_b_4.wristbandclient.app.AppController;
import com.wristband.yt_b_4.wristbandclient.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Create_Profile extends AppCompatActivity implements View.OnClickListener {
    private String TAG = Create_Profile.class.getSimpleName();
    private Button btnCreate;
    private TextView msgResponse;
    private ProgressDialog pDialog;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__profile);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        msgResponse = (TextView) findViewById(R.id.msgResponse);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        btnCreate.setOnClickListener(this);
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    private void createProfile(){
        //Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText first_name = (EditText) findViewById(R.id.fname);
        EditText last_name = (EditText) findViewById(R.id.lname);
        EditText user_email = (EditText) findViewById(R.id.email);
        EditText user_name = (EditText) findViewById(R.id.username);
        EditText user_password = (EditText) findViewById(R.id.password);
        EditText user_reenter = (EditText) findViewById(R.id.reenter);

        //text in first name box
        String f_name = first_name.getText().toString();
        //text in last name box
        String l_name = last_name.getText().toString();
        //text in email box
        String email = user_email.getText().toString();
        //text in username box
        String username = user_name.getText().toString();
        //text in password box
        String password = user_password.getText().toString();
        //text in reenter box
        String reenter = user_reenter.getText().toString();

        /*check to see that the user has entered text in all boxes,
        give toast error message if text is missing
        if all boxes contain valid answers, add the user to the Users table and take user to
        the home screen (perhaps add toast "profile created" on home screen
         */
        //check for missing answers
        if(f_name.isEmpty() || l_name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || reenter.isEmpty()){
            Toast fail = Toast.makeText(getApplicationContext(), "Required information missing", Toast.LENGTH_LONG);
            fail.show();
        }
        else{

            showProgressDialog();
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Const.URL_USERS, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            msgResponse.setText(response.toString());
                            hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    hideProgressDialog();
                }
            }) {
                /**
                 * Passing some request headers
                 * */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("f_name", "f_name");
                    headers.put("l_name", "tester");
                    headers.put("username", "asasdasdasdd");
                    headers.put("password", "ads");
                    headers.put("email", "asd@adasd.com");
                    return headers;
                }
            };
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq,
                    tag_json_obj);
            // Cancelling request
            // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);

            Toast pass = Toast.makeText(getApplicationContext(), "Profile created", Toast.LENGTH_LONG);
            pass.show();
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreate:
                createProfile();
                break;
        }
    }
}
