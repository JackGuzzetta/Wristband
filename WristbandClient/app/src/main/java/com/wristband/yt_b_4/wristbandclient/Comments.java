package com.wristband.yt_b_4.wristbandclient;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.login.LoginManager;
import com.wristband.yt_b_4.wristbandclient.app.AppController;
import com.wristband.yt_b_4.wristbandclient.models.Party;
import com.wristband.yt_b_4.wristbandclient.utils.Const;
import com.wristband.yt_b_4.wristbandclient.models.Comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Comments extends AppCompatActivity {
    Button btnComment, viewBtn, deleteBtn, cancelBtn, yes, no;
    ListView listView;
    List list = new ArrayList();
    ArrayList<String> comments = new ArrayList<String>();
    ArrayAdapter adapter;
    ProgressDialog pDialog;
    String user_id, party_id, username, relation, party_name, user_rel;
    int screen;
    String comment;
    String f_name = "test";
    String l_name = "test";
    Dialog CommentDialog;
    Dialog DeleteDialog;
    String name = "test test: ";

    String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        btnComment = (Button) findViewById(R.id.send);

        cancelBtn = (Button) findViewById(R.id.cancel);
        EditText cmt = (EditText) findViewById(R.id.editText);
        party_id = getIntent().getStringExtra("party_id");
        relation = getIntent().getStringExtra("relation");
        party_name = getIntent().getStringExtra("party_name");
        user_rel = getIntent().getStringExtra("user_rel");
        screen = Integer.parseInt(relation);


        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                EditText cmt = (EditText) findViewById(R.id.editText);
                comment = cmt.getText().toString();
                sendComment(view, comment);
            }

        });
        initializeControls();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.regular, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
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
    @Override
    public void onBackPressed() {
        Intent intent;
        switch (screen) {
            case 1://host
                intent = new Intent(this, HostScreen.class);
                intent.putExtra("party_name", party_name);
                intent.putExtra("relation", relation);
                startActivity(intent);
                finish();
                break;
            case 2://guest
                intent = new Intent(this, GuestScreen.class);
                intent.putExtra("party_name", party_name);
                intent.putExtra("relation", relation);
                startActivity(intent);
                finish();
                break;
            case 3://cohost
                intent = new Intent(this, GuestScreen.class);
                intent.putExtra("party_name", party_name);
                intent.putExtra("relation", relation);
                startActivity(intent);
                finish();
                break;
            default:
        }
    }

    private void initializeControls() {
        SharedPreferences settings = getSharedPreferences("account", Context.MODE_PRIVATE);
        username = settings.getString("username", "default");
        listView = (ListView) findViewById(R.id.list_view);
        //SharedPreferences settings = getSharedPreferences("account", Context.MODE_PRIVATE);
        //user_id = settings.getString("id", "default");
        //user_name= settings.getString("username", "default");
        adapter = new ArrayAdapter(Comments.this, android.R.layout.simple_list_item_1, list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the current item from ListView
                View view = super.getView(position, convertView, parent);

                return view;
            }
        };

        listView.setAdapter(adapter);
        list.add("Afro man: " + party_id);
        list.add("austin austin: Hello");
        adapter.notifyDataSetChanged();


        //listView.setBackgroundColor(Color.CYAN);
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l){
                commentingDialog(v, i);
            }
        });

        getAllCommentsByPartyId(party_id);
    }



    private void sendComment(View view, String comment) {
        SharedPreferences settings = getSharedPreferences("account", Context.MODE_PRIVATE);
        username = settings.getString("username", "default");
        list.add(f_name + " " + l_name + ": " + comment);
        adapter.notifyDataSetChanged();
        Comment c = new Comment(party_id, username, comment);
        sendDataToServer(c);
    }



    private void getAllCommentsByPartyId(String party_id) {
        JsonArrayRequest req = new JsonArrayRequest(Const.URL_GET_COMMENTS + party_id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                //String id = response.getJSONObject(i).getString("party_id");
                                //String relation = response.getJSONObject(i).getString("party_user_relation");
                                String text = response.getJSONObject(i).getString("cmt");
                                list.add(text);
                                //party_ids.add(id);
                                //relationList.add(relation);
                                adapter.notifyDataSetChanged();
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

    private void sendDataToServer(final Comment comment) {
        new Thread(new Runnable() {
            public void run() {
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        Const.URL_COMMENTS, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Toast pass = Toast.makeText(getApplicationContext(), "Commented", Toast.LENGTH_LONG);
                                pass.show();


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
                        headers.put("party_id", comment.getPartyId());
                        headers.put("username", comment.getUsername());
                        headers.put("cmt", comment.getText());

                        return headers;
                    }
                };
                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(jsonObjReq,
                        tag_json_obj);
                // Cancelling request
                // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
            }
        }).start();
    }

    private void goBack(View view) {
        Intent intent = new Intent(Comments.this, GuestScreen.class);
        startActivity(intent);
    }

    public void commentingDialog(View view, int i) {
        final int selection = i;
        final SharedPreferences[] settings = new SharedPreferences[1];
        final SharedPreferences.Editor[] editor = new SharedPreferences.Editor[1];
        CommentDialog = new Dialog(Comments.this);
        CommentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        CommentDialog.setContentView(R.layout.comment_dialog);
        CommentDialog.setTitle("What would you like to do?");

        viewBtn = CommentDialog.findViewById(R.id.view_user);
        deleteBtn = CommentDialog.findViewById(R.id.delete_comment);
        cancelBtn = CommentDialog.findViewById(R.id.cancel);

        deleteBtn.setEnabled(true);
        if (relation.equals("2")){
            deleteBtn.setEnabled(false);
        }
        deleteBtn.setEnabled(true);

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        String checker = list.get(selection).toString();
                        String[] seperated = checker.split(":");
                        username = seperated[0];
                System.out.println(username);
                        if (username.equals("Afro man")){
                            user_id = "97";
                        }
                        if (username.equals("austin austin")){
                            user_id = "96";
                        }
                        if(username.equals("test test")){
                            user_id = "94";
                        }
                        Intent intent = new Intent(Comments.this, User_Info.class);


                        intent.putExtra("user_rel", relation);
                        intent.putExtra("prev", "host");
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("party_name", party_name);
                        intent.putExtra("user_name", username);
                        intent.putExtra("relation", relation);
                        startActivity(intent);
                    }
                });


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletionDialog(selection);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentDialog.cancel();
            }
        });

        CommentDialog.show();
    }

    private void deleteComment(final String username) {
        new Thread(new Runnable() {
            public void run() {
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
                        Const.URL_COMMENTS + "/" + username, null,
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
                        return headers;
                    }
                };
                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(jsonObjReq,
                        tag_json_obj);
                // Cancelling request
                // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
            }
        }).start();
    }

    public void deletionDialog(final int i) {
        final SharedPreferences[] settings = new SharedPreferences[1];
        final SharedPreferences.Editor[] editor = new SharedPreferences.Editor[1];
        DeleteDialog = new Dialog(Comments.this);
        DeleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DeleteDialog.setContentView(R.layout.delete_dialog);
        DeleteDialog.setTitle("Are you sure?");

        yes = (Button)DeleteDialog.findViewById(R.id.yes);
        no = (Button)DeleteDialog.findViewById(R.id.no);

        yes.setEnabled(true);
        no.setEnabled(true);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(i);
                adapter.notifyDataSetChanged();
                deleteComment(username);
                CommentDialog.cancel();
                DeleteDialog.cancel();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDialog.cancel();
            }
        });

        DeleteDialog.show();
    }
}
