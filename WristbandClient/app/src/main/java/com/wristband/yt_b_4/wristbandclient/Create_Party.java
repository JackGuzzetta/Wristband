package com.wristband.yt_b_4.wristbandclient;

/**
 * Created by Mike on 10/7/2017.
 */
import com.facebook.login.LoginManager;
import com.wristband.yt_b_4.wristbandclient.models.Party;
import android.content.Intent;
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
    Switch swit;
    boolean s;
    public static int RESULT_LOAD_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);
        this.pic = (ImageButton) findViewById(R.id.pict);
        this.create = (Button) findViewById(R.id.create);
        this.next = (Button) findViewById(R.id.next);
        btnBack = (Button) findViewById(R.id.btnBack);
        next.setVisibility(View.INVISIBLE);
        swit = (Switch) findViewById(R.id.swittch);
        swit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                s = isChecked;
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a new user with values from the EditTexts
                goBack(view);
            }

        });
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
                    //Party p = new Party(name, date, time, location);
                    Party p = new Party(name, date, time, 0, 200, 0, "mvanbosc", location);
                    if (s) {
                        p.makePartyPublic();
                        Toast blank = Toast.makeText(getApplicationContext(), "Public Party Created!", Toast.LENGTH_LONG);
                        blank.show();
                        create.setVisibility(View.INVISIBLE);
                        next.setVisibility(View.VISIBLE);
                    } else {
                        p.MakePartyPrivate();
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
                setContentView(R.layout.activity_add_cohost);
            }
        });

//        pic.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//    }

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent GaleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(GaleryIntent, RESULT_LOAD_IMAGE);
//                Intent i = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//                final int ACTIVITY_SELECT_IMAGE = 1234;
//                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);


            }


        });
//
//
//    }
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch(requestCode) {
//            case 1:
//                if(resultCode == RESULT_OK){
//                    Uri selectedImage = data.getData();
//                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//
//                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    String filePath = cursor.getString(columnIndex);
//                    cursor.close();
//
//
//                    Bitmap photo = BitmapFactory.decodeFile(filePath);
//                    BitmapDrawable d = new BitmapDrawable(getResources(), photo);
//                    pic.setImageBitmap(photo);
//                }
//        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri SelectedImage = data.getData();
            String[] FilePathColumn = {MediaStore.Images.Media.DATA};

            Cursor SelectedCursor = getContentResolver().query(SelectedImage, FilePathColumn, null, null, null);
            SelectedCursor.moveToFirst();

            int columnIndex = SelectedCursor.getColumnIndex(FilePathColumn[0]);
            String picturePath = SelectedCursor.getString(columnIndex);
            SelectedCursor.close();


            pic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            Toast.makeText(getApplicationContext(), picturePath, Toast.LENGTH_LONG).show();

        }

    }


    private void goBack(View view){
        Intent intent = new Intent (Create_Party.this, HomeScreen.class);
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
                startActivity(new Intent(this, Login.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

