package com.wristband.yt_b_4.wristbandclient;

/**
 * Created by Mike on 9/23/2017.
 */
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.params.Face;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import static android.R.attr.data;

public class FacebookLogin extends AppCompatActivity {
    TextView txtStatus;
    LoginButton login_button;
    CallbackManager callbackManager;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        FacebookSdk.sdkInitialize(getApplicationContext());
        initializeControls();
        loginWithFB();
    }
    private void initializeControls() {
        callbackManager = CallbackManager.Factory.create();
        txtStatus = (TextView) findViewById(R.id.txtstatus);
        txtStatus.setText("Please Log In");
        login_button = (LoginButton) findViewById(R.id.login_button);
    }

    private void loginWithFB() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback < LoginResult > () {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(FacebookLogin.this, JsonRequestActivity.class);
                startActivity(intent);
                txtStatus.setText("Login Successful\n" + "Facebook ID: " +
                        loginResult.getAccessToken().getUserId() + "\n" + loginResult.getAccessToken().describeContents());
            }

            @Override
            public void onCancel() {
                txtStatus.setText("Login Cancelled\n");
            }

            @Override
            public void onError(FacebookException error) {
                txtStatus.setText("Error: " + error.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}