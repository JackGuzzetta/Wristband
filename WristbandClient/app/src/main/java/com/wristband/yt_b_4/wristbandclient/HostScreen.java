package com.wristband.yt_b_4.wristbandclient;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.login.LoginManager;
import com.google.zxing.qrcode.QRCodeReader;
import com.wristband.yt_b_4.wristbandclient.app.AppController;
import com.wristband.yt_b_4.wristbandclient.utils.Const;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.Result;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.graphics.drawable.ColorDrawable;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.util.Calendar;
import android.widget.TextView;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.content.DialogInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
//import me.dm7.barcodescanner.zxing.ZXingScannerView;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HostScreen extends AppCompatActivity {
    private static final String TAG = "Date";
    private Button btnCohost, btnLocation, btnPhotos, btnComments;
    private TextView dateText, partyText, locationTxt, timeTxt;
    private ArrayList<String> usernames = new ArrayList<String>();
    private ArrayList<String> unames = new ArrayList<String>();

    private ProgressDialog pDialog;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private String party_id, user_id, uname;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private static final int REQUEST_CAMERA = 1;
    // private ZXingScannerView mScannerView;


    String party_name, user_name, relation, prev_class, priv, dat, time, loc, maxp, alert, hosts;
    final Context context = this;
    ListView listView;
    List list = new ArrayList();
    List idList = new ArrayList();

    ArrayAdapter adapter;
    List relationList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_screen);
        btnLocation = (Button) findViewById(R.id.button7);
        btnPhotos = (Button) findViewById(R.id.button5);
        btnComments = (Button) findViewById(R.id.button6);
        partyText = (TextView) findViewById(R.id.partyTxt);
        timeTxt = (TextView) findViewById(R.id.time);
        locationTxt = (TextView) findViewById(R.id.location);
        dateText = (TextView) findViewById(R.id.dateTxt);

        //mScannerView = new ZXingScannerView(this);
        SharedPreferences settings = getSharedPreferences("account", Context.MODE_PRIVATE);
        user_id = settings.getString("id", "default");
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        party_name = getIntent().getStringExtra("party_name");
        relation = getIntent().getStringExtra("relation");
        //Toast.makeText(getApplicationContext(), unames.get(0), Toast.LENGTH_LONG).show();



//                QRGenerator(party_id,user_id);




        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                goLocation(view);
            }

        });

        btnPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                goPhotos(view);
            }
        });

        btnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goComments(view);
                //create a new user with values from the EditTexts
            }
        });
        Intent intent = getIntent();
        prev_class = intent.getStringExtra("prev");


        listView = (ListView) findViewById(R.id.list_view);
        getDataFromServer();
        adapter = new ArrayAdapter(HostScreen.this, android.R.layout.simple_list_item_1, list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the current item from ListView
                View view = super.getView(position, convertView, parent);
                if (relationList.get(position).equals("1")) {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.parseColor("#19c482"));
                } else if (relationList.get(position).equals("2")) {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.parseColor("#a6abae"));
                } else if (relationList.get(position).equals("3")) {
                    view.setBackgroundColor(Color.parseColor("#326f93"));
                } else {
                    view.setBackgroundColor(Color.RED);
                }
                return view;
            }
        };
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                Intent intent = new Intent(HostScreen.this, User_Info.class);
                user_name = (listView.getItemAtPosition(i)).toString();
                intent.putExtra("user_rel", relation);
                relation = relationList.get(i).toString();
                //user_id = findID(user_name);
                //uname = findunam(user_name);
                intent.putExtra("prev", "host");
                intent.putExtra("user_id", idList.get(i).toString());
                intent.putExtra("party_id", party_id);
                intent.putExtra("party_name", party_name);
                intent.putExtra("user_name", user_name);
                intent.putExtra("username", uname);

                intent.putExtra("relation", relation);
                startActivity(intent);
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = year + "-" + month + "-" + day;
                dat= date;
            }
        };

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                Log.d(TAG, "onDateSet: hh:mm: " + hour + ":" + minute);

                String t = hour + ":" + minute + ":00";
                time = t;
            }
        };

    }

    public void scanNow(View view) {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            Toast toast = Toast.makeText(getApplicationContext(),
            usernames.get(0), Toast.LENGTH_SHORT);
            toast.show();

        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.guestmenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.edit:
                //TODO
                return true;
            case R.id.newname:
                editname();
                getDataFromServer();
                return true;
            case R.id.invite:
                intent = new Intent(HostScreen.this, Add_User.class);
                intent.putExtra("prev", "guest");
                intent.putExtra("party_id", party_id);
                intent.putExtra("party_name", party_name);
                intent.putExtra("relation", relation);
                startActivity(intent);
                return true;
            case R.id.newdate:
                editdate();
                getDataFromServer();
                return true;
            case R.id.newtime:
                edittime();
                getDataFromServer();
                return true;
            case R.id.newlocation:
                editlocation();
                getDataFromServer();
                return true;

            case R.id.blacklist:
                intent = new Intent(HostScreen.this, Blacklist.class);
                intent.putExtra("party_name", party_id);
                intent.putExtra("relation", relation);
                startActivity(intent);
                return true;
            case R.id.delete:
                deleteUser();
                intent = new Intent(HostScreen.this, HomeScreen.class);
                startActivity(intent);
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
    public void onBackPressed() {
        Intent intent = new Intent(HostScreen.this, HomeScreen.class);
        startActivity(intent);
    }

    private void deleteUser() {
        if (relation.equals("1")) {
            deleteParty(party_id);
        } else if (relation.equals("2") || relation.equals("3")) {
            deleteRelation(user_id, relation);
        } else {
            //error
        }
    }

    private void deleteRelation(final String user_id, final String relation) {
        new Thread(new Runnable() {
            public void run() {
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
                        Const.URL_RELATION, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //msgStatus.setText("Error creating account: " + error);
                    }
                }) {
                    /**
                     * Passing some request headers
                     * */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        headers.put("user_id", user_id);
                        headers.put("relation_id", relation);
                        return headers;
                    }
                };
                AppController.getInstance().addToRequestQueue(jsonObjReq,
                        tag_json_obj);
            }
        }).start();
    }

    private void deleteParty(final String party_id) {
        new Thread(new Runnable() {
            public void run() {
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
                        Const.URL_PARTY + party_id, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
                    /**
                     * Passing some request headers
                     * */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        //headers.put("party_id", party_id);
                        return headers;
                    }
                };
                AppController.getInstance().addToRequestQueue(jsonObjReq,
                        tag_json_obj);
            }
        }).start();
    }

    private void editParty(final String party_id) {

        new Thread(new Runnable() {
            public void run() {
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                        Const.URL_PARTY, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                getDataFromServer();
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
                        headers.put("id", party_id);
                        headers.put("party_name", party_name);
                        headers.put("date", dat);
                        headers.put("time", time);
                        headers.put("privacy", priv);
                        headers.put("max_people", maxp);
                        headers.put("alerts", alert);
                        headers.put("host", hosts);
                        headers.put("location", loc);
                        return headers;
                    }
                };
                AppController.getInstance().addToRequestQueue(jsonObjReq,
                        tag_json_obj);
            }
        }).start();
    }

    private void getDataFromServer() {
        new Thread(new Runnable() {
            public void run() {
                JsonArrayRequest req = new JsonArrayRequest(Const.URL_PARTY_BY_NAME + party_name,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {

                                    String name = response.getJSONObject(0).getString("party_name");
                                    dat = response.getJSONObject(0).getString("date");
                                    String host = response.getJSONObject(0).getString("host");
                                    time = response.getJSONObject(0).getString("time");
                                    String location = response.getJSONObject(0).getString("location");
                                    party_id = response.getJSONObject(0).getString("id");
                                    priv = response.getJSONObject(0).getString("privacy");
                                    loc = location;
                                    maxp = response.getJSONObject(0).getString("max_people");
                                    alert = response.getJSONObject(0).getString("alerts");
                                    hosts = host;
                                    partyText.setText("Party name: " + name);
                                    dateText.setText("Date: " + dat);
                                    locationTxt.setText("Location: " + location);
                                    timeTxt.setText("Time: " + time);
                                    getAllUsers();
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
                // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_arry);

            }
        }).start();
    }

    private void goBack(View view) {
        Intent intent = new Intent(HostScreen.this, HomeScreen.class);
        startActivity(intent);
    }

    private String findID(String user_name) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(user_name)) {
                return usernames.get(i);
            }
        }
        return "ID not found";
    }
    private String findunam(String user_name) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(user_name)) {
                return unames.get(i);
            }
        }
        return "ID not found";
    }

    private void goLocation(View view) {
        Intent intent = new Intent(HostScreen.this, MapsActivity.class);
        intent.putExtra("party_location", loc);
        intent.putExtra("party_id", party_id);
        intent.putExtra("username", user_name);
        intent.putExtra("relation", relation);
        intent.putExtra("party_name", party_name);
        intent.putExtra("prev", "host");
        finish();
        startActivity(intent);
    }

    private void goPhotos(View view) {
        Intent intent = new Intent(HostScreen.this, Photos.class);
        intent.putExtra("party_name", party_id);
        intent.putExtra("relation", relation);
        intent.putExtra("user_id", user_id);
        intent.putExtra("party_name", party_name);
        intent.putExtra("prev", "host");

        startActivity(intent);
    }

    private void goComments(View view) {

        Intent intent = new Intent(HostScreen.this, Comments.class);
        intent.putExtra("party_id", party_id);
        intent.putExtra("username", user_name);
        intent.putExtra("relation", relation);
        intent.putExtra("party_name", party_name);
        intent.putExtra("prev", "host");
        startActivity(intent);
    }

    private void editname() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("New Party Name");
        alert.setMessage("Message");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                party_name = input.getText().toString();

                editParty(party_id);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    private void editdate() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog dialog = new DatePickerDialog(
                HostScreen.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


    }




    private void edittime(){
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(
                HostScreen.this,
                mTimeSetListener,
                hour, minute, true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        String t = hour + ":" + minute + ":00";
        time = t;

    }



    private void editlocation(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("New Party Location");
        alert.setMessage("Message");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                loc= input.getText().toString();

                editParty(party_id);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }


    private void getAllUsers() {
        list.clear();
        JsonArrayRequest req = new JsonArrayRequest(Const.URL_JOIN_Party + party_id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                String Relation = response.getJSONObject(i).getString("party_user_relation");
                                relationList.add(Relation);
                                String name = response.getJSONObject(i).getString("f_name");
                                name += " ";
                                name += response.getJSONObject(i).getString("l_name");
                                list.add(name);
                                adapter.notifyDataSetChanged();
                                usernames.add(response.getJSONObject(i).getString("user_id"));
                                idList.add(response.getJSONObject(i).getString("id"));
                                //unames.add(response.getJSONObject(i).getString("username"));

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
}
