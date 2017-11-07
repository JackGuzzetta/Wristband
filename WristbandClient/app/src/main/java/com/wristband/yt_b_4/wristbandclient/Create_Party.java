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
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.common.api.Status;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.util.Log;

import java.util.Calendar;

import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import com.google.android.gms.maps.model.LatLng;
import android.telephony.SmsManager;


/**
 * Created by Jackguzzetta on 9/26/17.
 */

public class Create_Party extends AppCompatActivity {
    private static final String TAG = "Date";
    Button create, btnBack;
    private TextView mDisplayDate;
    private TextView mDisplayTime;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private PlaceAutocompleteFragment autocompleteFragment;
    ImageButton pic;
    String name;
    String location;
    String date;
    String time;
    String locate;
    String prev_class;
    Switch swit;
    String user_id;
    String party_id;
    String created;
    LatLng fromadd;
    Intent finalIntent;
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
        btnBack = (Button) findViewById(R.id.btnBack);
        EditText eventname = (EditText) findViewById(R.id.eventName);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                fromadd = place.getLatLng();
                double lat = fromadd.latitude;
                double lng = fromadd.longitude;
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(getBaseContext(),"failure",Toast.LENGTH_LONG).show();

            }
        });
        EditText loc = (EditText) findViewById(R.id.locat);
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
        loc.setText(locate);
        mDisplayDate = (TextView) findViewById(R.id.date);
        mDisplayTime = (TextView) findViewById(R.id.time);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog dialog = new DatePickerDialog(
                        Create_Party.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = year + "-" + month + "-" + day;
                mDisplayDate.setText(date);
            }
        };

        mDisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR);
                int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(
                        Create_Party.this,
                        mTimeSetListener,
                        hour, minute, true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                Log.d(TAG, "onDateSet: hh:mm: " + hour + ":" + minute);

                String date = hour + ":" + minute + ":00";
                mDisplayTime.setText(date);
            }
        };

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText eventname = (EditText) findViewById(R.id.eventName);
                TextView Date = (TextView) findViewById(R.id.date);
                TextView Time = (TextView) findViewById(R.id.time);
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
                    created = "1";
                    finalIntent = new Intent(Create_Party.this, HomeScreen.class);
                    finalIntent.putExtra("eventname", eventname.getText().toString());
                    finalIntent.putExtra("Date", Date.getText().toString());
                    finalIntent.putExtra("Time", Time.getText().toString());
                    finalIntent.putExtra("loc", loc.getText().toString());
                    finalIntent.putExtra("activity", created);
                    finalIntent.putExtra("prev", "party");
                    //  sendSms("5554", "Hi You got a message!");
                    if (s) {
                        p.makePartyPublic();
                        sendDataToServer(p);
                        //getDataFromServer(p.getPartyName());
                        Toast blank = Toast.makeText(getApplicationContext(), "Public Party Created!", Toast.LENGTH_LONG);
                        blank.show();
                        create.setVisibility(View.GONE);
                        //startActivity(finalIntent);
                    } else {
                        p.MakePartyPrivate();
                        sendDataToServer(p);
                        //getDataFromServer(p.getPartyName());
                        Toast blank = Toast.makeText(getApplicationContext(), "Private Party Created", Toast.LENGTH_LONG);
                        blank.show();
                        create.setVisibility(View.GONE);
                        //startActivity(finalIntent);
                    }
                }

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
        inflater.inflate(R.menu.regular, menu);
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

    //    private void sendSms(String phoneNumber, String message) {
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse( "sms:" + phoneNumber ) );
//        intent.putExtra( "sms_body", message );
//        startActivity(intent);
//    }
//    private void sendsms(String phoneNumber, String message) {
//        SmsManager sms = SmsManager.getDefault();
//        sms.sendTextMessage(phoneNumber, null, message, null, null);
//    }
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
                JsonArrayRequest req = new JsonArrayRequest(Const.URL_PARTY_BY_NAME + party_name,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    party_id = response.getJSONObject(0).getString("id");
                                    sendRelationToServer(user_id, party_id, "1");
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

    private void sendDataToServer(final Party party) {
        new Thread(new Runnable() {
            public void run() {
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        Const.URL_PARTY, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                getDataFromServer(party.getPartyName());
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
            }
        }).start();
    }

    private void sendRelationToServer(final String user, final String party_id, final String relation) {
        new Thread(new Runnable() {
            public void run() {
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        Const.URL_RELATION, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                startActivity(finalIntent);
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
                        headers.put("party_id", party_id);
                        headers.put("relation", relation);
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
