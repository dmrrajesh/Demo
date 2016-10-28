package com.rajeshsaini.dmr.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by rajesh on 3/12/2016.
 */

public class  CallReceiver extends BroadcastReceiver {
    String numString;
    @Override
    public void onReceive(Context context, Intent intent) {
        String state=intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if(state==null){
            String number=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        }else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
            String number=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
        }
        context.getApplicationContext().startActivity(new Intent(context.getApplicationContext(),SentCallInfo.class));
        /*
        if(state==null){
            String number=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.i("TAG","Outgoing Number : "+number);
            numString=number;
            Toast.makeText(context.getApplicationContext(),"Outgoing Number"+number,Toast.LENGTH_LONG).show();
        }else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
            String number=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            numString=number;
            Log.i("TAG","Incoming Number : "+number);
            Toast.makeText(context.getApplicationContext(),"Incoming Number"+number,Toast.LENGTH_LONG).show();
        } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            String number=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.i("TAG","Incoming Number : "+number);
            Toast.makeText(context.getApplicationContext(),"Call Disconnect"+numString,Toast.LENGTH_LONG).show();
        }else if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
            String number=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.i("TAG","Incoming Number : "+number);
            Toast.makeText(context.getApplicationContext(),"Call Disconnect"+numString,Toast.LENGTH_LONG).show();
        }
        if(state.equals(TelephonyManager.CALL_STATE_IDLE)){
            String number=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.i("TAG","Incoming Number : "+number);
            Toast.makeText(context.getApplicationContext(),"Call IDLE"+number,Toast.LENGTH_LONG).show();
        }else if(state.equals(TelephonyManager.CALL_STATE_OFFHOOK)){
            String number=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.i("TAG","Incoming Number : "+number);
            Toast.makeText(context.getApplicationContext(),"CALL OFFHOOK"+number,Toast.LENGTH_LONG).show();
        }else if(state.equals(TelephonyManager.CALL_STATE_RINGING)){
            String number=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.i("TAG","Incoming Number : "+number);
            Toast.makeText(context.getApplicationContext(),"CALL RINGING"+number,Toast.LENGTH_LONG).show();
        }
        */
    }
    /*
    private void getLastCallDetails(Intent context){
        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
        Cursor managedCursor = context.managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, strOrder);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        while (managedCursor.moveToNext()) {
            String phNum = managedCursor.getString(number);
            String callTypeCode = managedCursor.getString(type);
            String strcallDate = managedCursor.getString(date);
            Date callDate = new Date(Long.valueOf(strcallDate));
            java.sql.Date date1=new java.sql.Date(callDate.getTime());
            java.sql.Time time=new java.sql.Time(callDate.getTime());
            String callDuration = managedCursor.getString(duration);
            String callType = null;
            int callcode = Integer.parseInt(callTypeCode);
            switch (callcode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    break;
            }
            break;
        }
    }
   */
    private class SendCallDetails extends AsyncTask<HashMap<String, String>, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(HashMap<String, String>... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(Admin.getAllChild());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("User-Agent", Admin.USER_AGENT);
                urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                urlConnection.setDoOutput(true);
                StringBuilder stringBuilder = new StringBuilder();
                if (params[0].size() > 0) {
                    Iterator<String> iterator = (params[0].keySet()).iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
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
                    //parseResult(response.toString());
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
            if (integer == 1) {

            } else {

            }
        }
    }
}
