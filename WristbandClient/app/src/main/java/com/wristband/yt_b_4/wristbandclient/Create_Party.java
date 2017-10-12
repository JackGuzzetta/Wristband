package com.wristband.yt_b_4.wristbandclient;

/**
 * Created by Mike on 10/7/2017.
 */
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.login.LoginManager;
import com.wristband.yt_b_4.wristbandclient.app.AppController;
import com.wristband.yt_b_4.wristbandclient.models.Party;
import com.wristband.yt_b_4.wristbandclient.models.User;
import com.wristband.yt_b_4.wristbandclient.utils.Const;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.Switch;
import android.widget.CompoundButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jackguzzetta on 9/26/17.
 */

public class Create_Party extends AppCompatActivity {
    Button create, next, btnBack;
    ImageButton pic;
    String name;
    String location;
    String date;
    String time;
    String locate;
    Switch swit;
    String user_id;
    String party_id;
    boolean s;
    public static int RESULT_LOAD_IMAGE = 1;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private ProgressDialog pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);
        this.pic = (ImageButton) findViewById(R.id.pict);
        this.create = (Button) findViewById(R.id.create);
        this.next = (Button) findViewById(R.id.next);
        btnBack = (Button) findViewById(R.id.btnBack);
        EditText eventname = (EditText) findViewById(R.id.eventName);
        EditText Date = (EditText) findViewById(R.id.day);
        EditText Time = (EditText) findViewById(R.id.tyme);
        EditText loc = (EditText) findViewById(R.id.locat);
        next.setVisibility(View.INVISIBLE);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);


        SharedPreferences settings = getSharedPreferences("account", Context.MODE_PRIVATE);
        user_id = settings.getString("id", "default");

        swit = (Switch) findViewById(R.id.swittch);



        swit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                s = isChecked;
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack(view);
            }

        });
        Intent intent = getIntent();
        name = intent.getStringExtra("eventname");
        date = intent.getStringExtra("Date");
        time = intent.getStringExtra("Time");
        locate = intent.getStringExtra("loc");
        eventname.setText(name);
        Date.setText(date);
        Time.setText(time);
        loc.setText(locate);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText eventname = (EditText) findViewById(R.id.eventName);
                EditText Date = (EditText) findViewById(R.id.day);
                EditText Time = (EditText) findViewById(R.id.tyme);
                EditText loc = (EditText) findViewById(R.id.locat);
                name = eventname.getText().toString();
                date = Date.getText().toString();
                time = Time.getText().toString();
                location = loc.getText().toString();

                if (name.length() == 0 || name == null || date.length() == 0 ||
                        date == null || time.length() == 0 || time == null || location.length() == 0 || location == null) {
                    Toast blank = Toast.makeText(getApplicationContext(), "Invalid! One or more fields has been left blank", Toast.LENGTH_LONG);
                    blank.show();

                } else {
                    SharedPreferences settings = getSharedPreferences("account", Context.MODE_PRIVATE);
                    String host = settings.getString("username", "default");
                    Party p = new Party(name, date, time, 0, 200, 0, host, location);
                    if (s) {
                        p.makePartyPublic();
                        sendDataToServer(p);
                        getDataFromServer(p.getPartyName());
                        Toast blank = Toast.makeText(getApplicationContext(), "Public Party Created!", Toast.LENGTH_LONG);
                        blank.show();
                        create.setVisibility(View.INVISIBLE);
                        next.setVisibility(View.VISIBLE);
                    } else {
                        p.MakePartyPrivate();
                        sendDataToServer(p);
                        getDataFromServer(p.getPartyName());
                        Toast blank = Toast.makeText(getApplicationContext(), "Private Party Created", Toast.LENGTH_LONG);
                        blank.show();
                        create.setVisibility(View.INVISIBLE);
                        next.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText eventname = (EditText) findViewById(R.id.eventName);
                EditText Date = (EditText) findViewById(R.id.day);
                EditText Time = (EditText) findViewById(R.id.tyme);
                EditText loc = (EditText) findViewById(R.id.locat);
                Intent intent = new Intent(Create_Party.this, CoHost.class);
                intent.putExtra("eventname",eventname.getText().toString());
                intent.putExtra("Date",Date.getText().toString());
                intent.putExtra("Time",Time.getText().toString());
                intent.putExtra("loc",loc.getText().toString());
                startActivity(intent);
            }
        });

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent GaleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(GaleryIntent, RESULT_LOAD_IMAGE);
            }


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri SelectedImage = data.getData();
            String[] FilePathColumn = {
                    MediaStore.Images.Media.DATA
            };

            Cursor SelectedCursor = getContentResolver().query(SelectedImage, FilePathColumn, null, null, null);
            SelectedCursor.moveToFirst();

            int columnIndex = SelectedCursor.getColumnIndex(FilePathColumn[0]);
            String picturePath = SelectedCursor.getString(columnIndex);
            SelectedCursor.close();


            pic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            Toast.makeText(getApplicationContext(), picturePath, Toast.LENGTH_LONG).show();
        }
    }


    private void goBack(View view) {
        Intent intent = new Intent(Create_Party.this, HomeScreen.class);
        finish();
        startActivity(intent);
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
    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }
    //We need to get the party id from party name
    //we need the user id
    private void getDataFromServer(final String party_name) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000L); //wait for party to be created first
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                JsonArrayRequest req = new JsonArrayRequest(Const.URL_PARTY_BY_NAME + party_name,
                        new Response.Listener <JSONArray> () {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    party_id = response.getJSONObject(0).getString("id");
                                } catch (JSONException e) {}
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                });
                AppController.getInstance().addToRequestQueue(req,
                        tag_json_arry);
            }
        }).start();
    }

    private void sendDataToServer(final Party party) {
        new Thread(new Runnable() {
            public void run() {
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        Const.URL_PARTY, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    response.getString("users");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
                    /**
                     * Passing some request headers
                     */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        headers.put("party_name", party.getPartyName());
                        headers.put("date", party.getDate());
                        headers.put("time", party.getTime());
                        headers.put("privacy", Integer.toString(party.getPrivacy()));
                        headers.put("max_people", Integer.toString(party.getMaxPeople()));
                        headers.put("alerts", Integer.toString(party.getAlerts()));
                        headers.put("host", party.getHost());
                        headers.put("location", party.getLocation());
                        return headers;
                    }
                };
                AppController.getInstance().addToRequestQueue(jsonObjReq,
                        tag_json_obj);
                //showProgressDialog();
                try {
                    Thread.sleep(3000L); //wait for party to be created first
                    sendRelationToServer(user_id, party_id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void sendRelationToServer(final String user, final String party) {
        new Thread(new Runnable() {
            public void run() {
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        Const.URL_RELATION_BY_ID, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    response.getString("users");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
                    /**
                     * Passing some request headers
                     */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        headers.put("user_id", user);
                        headers.put("party_id", party);
                        return headers;
                    }
                };
                AppController.getInstance().addToRequestQueue(jsonObjReq,
                        tag_json_obj);
                //showProgressDialog();
            }

        }).start();
    }
}