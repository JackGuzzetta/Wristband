package com.wristband.yt_b_4.wristbandclient;

/**
 * Created by Mike on 9/23/2017.
 */
import com.wristband.yt_b_4.wristbandclient.models.User;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wristband.yt_b_4.wristbandclient.app.AppController;
import com.wristband.yt_b_4.wristbandclient.utils.Const;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import java.util.HashMap;
import java.util.Map;

public class JsonRequestActivity extends AppCompatActivity implements OnClickListener {
    private String TAG = JsonRequestActivity.class.getSimpleName();
    private Button btnJsonArray;
    private TextView msgResponse;
    private ProgressDialog pDialog;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);
        btnJsonArray = (Button) findViewById(R.id.btnJsonArray);
        msgResponse = (TextView) findViewById(R.id.msgResponse);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        btnJsonArray.setOnClickListener(this);
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    private void sendDataToServer() {
        user = new User("Michael", "test", "tester", "123", "as@as.com");
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
                headers.put("f_name", user.getFirstName());
                headers.put("l_name", user.getLastName());
                headers.put("username", user.getUsername());
                headers.put("password", user.getPassword());
                headers.put("email", user.getEmail());
                return headers;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                tag_json_obj);
        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }
    private void getDataFromServer() {
        showProgressDialog();
        JsonArrayRequest req = new JsonArrayRequest(Const.URL_USERS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            String newString = "User extracted from Json\n";
                            newString += response.getJSONObject(0).getString("id");
                            newString += "\n";
                            newString += response.getJSONObject(0).getString("f_name");
                            newString += "\n";
                            newString += response.getJSONObject(0).getString("l_name");
                            newString += "\n";
                            newString += response.getJSONObject(0).getString("username");
                            newString += "\n";
                            newString += response.getJSONObject(0).getString("password");
                            newString += "\n";
                            newString += response.getJSONObject(0).getString("email");
                            msgResponse.setText(newString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                         hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                msgResponse.setText("error: " + error.getMessage());
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideProgressDialog();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req,
                tag_json_arry);
        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_arry);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnJsonArray:
                sendDataToServer();
                break;
        }
    }
}