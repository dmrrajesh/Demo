package com.rajeshsaini.dmr.demo;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

public class ProfileEdit extends AppCompatActivity {

    private Spinner update_gender;
    private EditText update_name,update_mobile,update_email,update_dob,update_address;
    private Button update_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        update_name=(EditText)findViewById(R.id.update_name);
        update_mobile=(EditText)findViewById(R.id.update_mobile);
        update_email=(EditText)findViewById(R.id.update_email);
        update_dob=(EditText)findViewById(R.id.update_dob);
        update_address=(EditText)findViewById(R.id.update_address);
        update_gender=(Spinner)findViewById(R.id.update_gender);
        update_button=(Button)findViewById(R.id.update_button);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Admin.USER_ID, MySharedPreferences.getSharedPreferences(ProfileEdit.this,Admin.USER_ID));

                params.put(Admin.MOBILE,update_mobile.getText().toString());
                params.put(Admin.USER_NAME,update_name.getText().toString());
                params.put(Admin.EMAIL,update_email.getText().toString());
                params.put(Admin.DOB,update_dob.getText().toString());
                params.put(Admin.ADDRESS,update_address.getText().toString());
                params.put(Admin.GENDER,update_gender.getSelectedItem().toString());
                new ProfileUpadate().execute(params);
            }
        });
    }
    private class ProfileUpadate extends AsyncTask<HashMap<String,String>, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Integer doInBackground(HashMap<String, String>... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(Admin.getProfileUpdate());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("User-Agent", Admin.USER_AGENT);
                urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                urlConnection.setDoOutput(true);
                StringBuilder stringBuilder=new StringBuilder();
                if(params[0].size()>0){
                    Iterator<String> iterator = (params[0].keySet()).iterator();
                    while (iterator.hasNext()){
                        String key=iterator.next();
                        stringBuilder.append(key);
                        stringBuilder.append("=");
                        stringBuilder.append(params[0].get(key));
                        stringBuilder.append("&");
                    }
                }
                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(stringBuilder.toString());
                wr.flush();
                wr.close();
                int statusCode = urlConnection.getResponseCode();
                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    Log.d("Res",response.toString());

                    try{
                        JSONObject object=new JSONObject(response.toString());
                        if(object.has(Admin.SUCCESS)){
                            int success=object.getInt(Admin.SUCCESS);
                            if(success==1){
                                return 1;
                            }else{
                                return 0;
                            }
                        }
                    }catch (Exception e){
                    }
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d("Ex", e.getLocalizedMessage());
                Log.d("Ex", e.toString());
            }
            return result; //"Failed to fetch data!";
        }
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer==1){
                Snackbar.make(update_button.getRootView(), "Profile Successfully update!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }else{
                Snackbar.make(update_button.getRootView(), "Profile updation failed!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

}
