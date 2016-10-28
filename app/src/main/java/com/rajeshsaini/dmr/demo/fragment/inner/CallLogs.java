package com.rajeshsaini.dmr.demo.fragment.inner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rajeshsaini.dmr.demo.Admin;
import com.rajeshsaini.dmr.demo.MySharedPreferences;
import com.rajeshsaini.dmr.demo.R;
import com.rajeshsaini.dmr.demo.Schema;
import com.rajeshsaini.dmr.demo.adapter.CallDetailsAdapter;
import com.rajeshsaini.dmr.demo.adapter.NavItemAdapter;
import com.rajeshsaini.dmr.demo.models.CallDetails;
import com.rajeshsaini.dmr.demo.models.NavItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by DMRSAINI on 3/9/2016.
 */

public class CallLogs extends Fragment {
    List<CallDetails> callDetailsList;
    ListView listView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_call_log,container,false);
        TelephonyManager telephonyManager=(TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new TeleListener(),PhoneStateListener.LISTEN_CALL_STATE);
        //TextView textView=(TextView)view.findViewById(R.id.call_details);
        listView=(ListView)view.findViewById(R.id.call_detatils_list);
        callDetailsList=new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
        Cursor managedCursor = getActivity().managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, strOrder);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        while (managedCursor.moveToNext()) {
            CallDetails details=new CallDetails();
            String phNum = managedCursor.getString(number);
            details.setMobile(phNum);
            String callTypeCode = managedCursor.getString(type);
            String strcallDate = managedCursor.getString(date);
            Date callDate = new Date(Long.valueOf(strcallDate));
            java.sql.Date date1=new java.sql.Date(callDate.getTime());
            java.sql.Time time=new java.sql.Time(callDate.getTime());
            details.setDate(date1.toString());
            details.setTime(time.toString());
            String callDuration = managedCursor.getString(duration);
            details.setDuration(callDuration+" Sec");
            String callType = null;
            int callcode = Integer.parseInt(callTypeCode);
            switch (callcode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    callType = "Outgoing";
                    details.setType(Admin.CALL_OUTGOING);
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    callType = "Incoming";
                    details.setType(Admin.CALL_INCOMING);
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    callType = "Missed";
                    details.setType(Admin.CALL_MISS);
                    break;
            }
            callDetailsList.add(details);
        }
        managedCursor.close();
        //listView.setAdapter(new CallDetailsAdapter(getActivity().getApplicationContext(), R.layout.call_log_layout, callDetailsList));
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Admin.USER_ID, MySharedPreferences.getSharedPreferences(getActivity(), Admin.CHILD_ID));
        new GetCallLogs().execute(params);
        return view;
    }
    private class GetCallLogs extends AsyncTask<HashMap<String, String>, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Integer doInBackground(HashMap<String, String>... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(Admin.getCallLogs());
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
                    parseResult(response.toString());
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
    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            callDetailsList = new ArrayList<>();
            if (response.has(Schema.SUCCESS)) {
                int success = response.getInt(Schema.SUCCESS);
                if (success == 1) {
                    if (response.has(Admin.LOG)) {
                        JSONArray jsonArray = response.getJSONArray(Admin.LOG);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            CallDetails giftCard = new CallDetails();
                            if (jsonObject.has(Admin.DATE)) {
                                giftCard.setDate(jsonObject.getString(Admin.DATE));
                            } else {
                                giftCard.setDate("");
                            }
                            if (jsonObject.has(Admin.MOBILE)) {
                                giftCard.setMobile(jsonObject.getString(Admin.MOBILE));
                            } else {
                                giftCard.setMobile("");
                            }
                            if (jsonObject.has(Admin.TIME)) {
                                giftCard.setTime(jsonObject.getString(Admin.TIME));
                            } else {
                                giftCard.setTime("");
                            }
                            if (jsonObject.has(Admin.DURATION)) {
                                giftCard.setDuration(jsonObject.getString(Admin.DURATION));
                            } else {
                                giftCard.setDuration("");
                            }
                            if (jsonObject.has(Admin.TYPE)) {
                                giftCard.setType(jsonObject.getString(Admin.TYPE));
                            } else {
                                giftCard.setType("");
                            }
                            callDetailsList.add(giftCard);
                        }
                    }
                } else if (success == 2) {

                } else {

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Ex", e.toString());
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(new CallDetailsAdapter(getActivity().getApplicationContext(), R.layout.call_log_layout, callDetailsList));
            }
        });
    }
    class TeleListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:
                break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    break;
            }
        }
    }
    public  class CallRece extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context.getApplicationContext(),"Working Inner Calling",Toast.LENGTH_LONG).show();
        }
    }
}
