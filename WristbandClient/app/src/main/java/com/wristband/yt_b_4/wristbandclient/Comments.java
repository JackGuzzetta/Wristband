package com.wristband.yt_b_4.wristbandclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.login.LoginManager;
import com.wristband.yt_b_4.wristbandclient.app.AppController;
import com.wristband.yt_b_4.wristbandclient.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class Comments extends AppCompatActivity {
    Button btnBack, btnComment;
    ListView listView;
    List list = new ArrayList();
    ArrayList<String> comments = new ArrayList<String>();
    ArrayAdapter adapter;
    ProgressDialog pDialog;
    String user_id, party_name, relation, prev_class;
    String comment;
    String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        btnBack = (Button) findViewById(R.id.back);
        btnComment = (Button) findViewById(R.id.send);
        EditText cmt = (EditText) findViewById(R.id.editText);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                goBack(view);
            }

        });
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                EditText cmt = (EditText) findViewById(R.id.editText);
                comment = cmt.getText().toString();
                sendComment(view);
            }

        });
        Intent intent = getIntent();
        party_name = intent.getStringExtra("party_name");
        relation = intent.getStringExtra("relation");
        prev_class = intent.getStringExtra("prev");
        initializeControls();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_info, menu);
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

    private void initializeControls() {
        listView = (ListView) findViewById(R.id.list_view);
        SharedPreferences settings = getSharedPreferences("account", Context.MODE_PRIVATE);
        user_id = settings.getString("id", "default");
        adapter = new ArrayAdapter(Comments.this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                String party_name = (String) parent.getItemAtPosition(position);
                // Display the selected item text on TextView

            }
        });
        btnComment = (Button) findViewById(R.id.send);

    }

    private void sendComment(View view) {
        list.add("Hello World!");
        adapter.notifyDataSetChanged();
    }

    private void goBack(View view) {
        Intent intent;
        if(prev_class.equals("host")) {
            intent = new Intent(Comments.this, HostScreen.class);
        }
        else{
            intent = new Intent(Comments.this, GuestScreen.class);
        }
        intent.putExtra("party_name", party_name);
        intent.putExtra("relation", relation);
        intent.putExtra("prev", "comments");
        startActivity(intent);
    }
}
