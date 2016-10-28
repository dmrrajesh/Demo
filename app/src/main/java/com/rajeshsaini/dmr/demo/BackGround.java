package com.rajeshsaini.dmr.demo;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by rajesh on 3/14/2016.
 */

public class BackGround implements View.OnClickListener{
    private Context context;
    private String userId;
    private Activity activity;
    public BackGround(Context context ) {
        this.context = context;
    }

    public BackGround(Context context, String userId) {
        this.context = context;
        this.userId = userId;
    }

    public BackGround(Context context, String userId, Activity activity) {
        this.context = context;
        this.userId = userId;
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.request_reject:
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String, String> params1 = new HashMap<String, String>();
                        params1.put(Admin.USER_ID, MySharedPreferences.getSharedPreferences(context, Admin.USER_ID));
                        params1.put(Admin.SN, userId);
                        params1.put(Admin.ACTION, Admin.REJECT);
                        new BackGroundRunn().execute(params1);
                    }
                });
                break;
            case R.id.request_confirm:
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String,String> params1 = new HashMap<String, String>();
                        params1.put(Admin.USER_ID, MySharedPreferences.getSharedPreferences(context, Admin.USER_ID));
                        params1.put(Admin.SN,userId);
                        params1.put(Admin.ACTION, Admin.CONFIRM);
                        new BackGroundRunn().execute(params1);
                    }
                });
                break;
        }
    }
    private class BackGroundRunn  extends AsyncTask<HashMap<String,String>, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Integer doInBackground(HashMap<String, String>... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try{
                System.out.print("Working Hit 1"+Admin.getRequestAction());
                URL url = new URL(Admin.getRequestAction());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("User-Agent", Admin.USER_AGENT);
                urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                urlConnection.setDoOutput(true);
                StringBuilder stringBuilder=new StringBuilder();
                System.out.print("Working Hit 2");
                if(params[0].size()>0){
                    System.out.print("Working Hit 3");

                    Iterator<String> iterator = (params[0].keySet()).iterator();
                    while (iterator.hasNext()){
                        String key=iterator.next();
                        stringBuilder.append(key);
                        stringBuilder.append("=");
                        stringBuilder.append(params[0].get(key));
                        stringBuilder.append("&");
                    }
                }
                System.out.print("Working Hit 4 "+stringBuilder.toString());
                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(stringBuilder.toString());
                wr.flush();
                wr.close();
                int statusCode = urlConnection.getResponseCode();
                // 200 represents HTTP OK
                System.out.print("Working Hit 5: "+statusCode);
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    Log.d("Res Click", response.toString());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Ex Rajesh ", e.getLocalizedMessage());
                Log.d("Ex Rajesh", e.toString());
            }
            return result; //"Failed to fetch data!";
        }
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer==1){
                System.out.print("FINISH");
            }else{
                System.out.print("UNFINISH");
            }
        }
    }
}



