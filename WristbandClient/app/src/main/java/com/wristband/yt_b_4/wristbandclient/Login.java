package com.wristband.yt_b_4.wristbandclient;

/**
 * Created by Mike on 9/23/2017.
 */
import com.wristband.yt_b_4.wristbandclient.examples.exampleActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.graphics.Color;


public class Login extends AppCompatActivity {
    TextView txtStatus;
    LoginButton FacebookLoginButton;
    Button LoginButton;
    Button RegisterButton;


    CallbackManager callbackManager;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        context = this;
        FacebookSdk.sdkInitialize(getApplicationContext());
        initializeControls();
        findViewById(R.id.screen).setBackgroundColor(Color.rgb(0, 0, 100));

        if (isLoggedIn()) {
            Intent intent = new Intent(Login.this, HomeScreen.class);
            startActivity(intent);
        }
        loginWithFB();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.loginmenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                //startActivity(new Intent(this, About.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void initializeControls() {
        callbackManager = CallbackManager.Factory.create();
        txtStatus = (TextView) findViewById(R.id.txtstatus);
        txtStatus.setText("Please Log In");
        FacebookLoginButton = (LoginButton) findViewById(R.id.fb_login_button);
        LoginButton = (Button) findViewById(R.id.login_button);
        RegisterButton = (Button) findViewById(R.id.register_button);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                loginProfile();
            }
        });
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                createProfile();
            }
        });
    }

    private void loginWithFB() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback < LoginResult > () {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(Login.this, HomeScreen.class);
                startActivity(intent);
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
    public boolean isLoggedIn() {
        SharedPreferences settings = getSharedPreferences("account", Context.MODE_PRIVATE);
        String username = settings.getString("username", null);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return ((accessToken != null) || (username != null));
    }

    private void createProfile() {
        Intent intent = new Intent(Login.this, Create_Profile.class);
        startActivity(intent);

    }

    private void loginProfile() {
        Intent intent = new Intent(Login.this, LoginProfile.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //Used to get hash key for facebook API (only needed if creating a new application or releasing publicly)
    private void PrintFBHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.wristband.yt_b_4.wristbandclient", PackageManager.GET_SIGNATURES);
            for (Signature signature: info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                //msgResponse.setText("KeyHash:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}