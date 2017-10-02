package com.wristband.yt_b_4.wristbandclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.wristband.yt_b_4.wristbandclient.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;

public class Create_Profile extends AppCompatActivity {
    private String TAG = JsonRequestActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__profile);
    }

    public void createProfile(View view){
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

            Toast pass = Toast.makeText(getApplicationContext(), "Profile created", Toast.LENGTH_LONG);
            pass.show();
        }

    }
}
