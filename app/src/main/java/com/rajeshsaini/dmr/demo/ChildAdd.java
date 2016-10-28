package com.rajeshsaini.dmr.demo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

public class ChildAdd extends AppCompatActivity {
    private EditText sent_request_mobile;
    private Button sent_request_button;
    private JSONObject jsonObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sent_request_mobile=(EditText)findViewById(R.id.sent_request_mobile);
        sent_request_button=(Button)findViewById(R.id.sent_request_button);
        sent_request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Admin.USER_ID, MySharedPreferences.getSharedPreferences(ChildAdd.this, Admin.USER_ID));
                params.put(Admin.MOBILE,sent_request_mobile.getText().toString());
                new RequestChild().execute(params);
            }
        });
    }
    private class RequestChild extends AsyncTask<HashMap<String,String>, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Integer doInBackground(HashMap<String, String>... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(Admin.getChildRequest());
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
                        jsonObject=new JSONObject(response.toString());
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
                try{
                    if(jsonObject.has(Admin.SUCCESS)){
                        int success=jsonObject.getInt(Admin.SUCCESS);
                        if(success==1){
                            if(jsonObject.has(Admin.MESSAGE)) {
                                sent_request_mobile.setText("");
                                Snackbar.make(sent_request_button.getRootView(),jsonObject.getString(Admin.MESSAGE), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }else{
                            if(jsonObject.has(Admin.MESSAGE)) {
                                Snackbar.make(sent_request_button.getRootView(),jsonObject.getString(Admin.MESSAGE), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                    }
                }catch (Exception e){

                }
            }else{
                Snackbar.make(sent_request_button.getRootView(), "Oops! Failed to fetch data!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

}
