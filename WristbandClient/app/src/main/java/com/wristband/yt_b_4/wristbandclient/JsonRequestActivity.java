package com.wristband.yt_b_4.wristbandclient;

/**
 * Created by Mike on 9/23/2017.
 */

import com.wristband.yt_b_4.wristbandclient.app.AppController;
import com.wristband.yt_b_4.wristbandclient.utils.Const;
import org.json.JSONArray;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JsonRequestActivity extends AppCompatActivity implements OnClickListener {
    private String TAG = JsonRequestActivity.class.getSimpleName();
    private Button btnJsonArray;
    private TextView msgResponse;
    private ProgressDialog pDialog;
    private String tag_json_arry = "jarray_req";
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


    //Used to get hash key for facebook API
    private void PrintFBHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.wristband.yt_b_4.wristbandclient", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                msgResponse.setText("KeyHash:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }
    private void makeJsonArryReq() {
        showProgressDialog();
        JsonArrayRequest req = new JsonArrayRequest(Const.URL_JSON_ARRAY,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        msgResponse.setText(response.toString());
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
                makeJsonArryReq();
                break;
        }
    }
}