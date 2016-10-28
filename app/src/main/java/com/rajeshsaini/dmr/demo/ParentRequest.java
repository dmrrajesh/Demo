package com.rajeshsaini.dmr.demo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rajeshsaini.dmr.demo.models.NavItemRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ParentRequest extends AppCompatActivity implements View.OnClickListener {
    private List<NavItemRequest> navItems;
    private ListView navItemView;
    private JSONObject jsonObject;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navItemView=(ListView)findViewById(R.id.request_list_view);

        /*
        navItems=new ArrayList<>();
        NavItemRequest request=new NavItemRequest();
        request.setImage("1.png");
        request.setId("30");
        request.setMobile("82233245461");
        request.setNavTitle("Rajesh");


        navItems.add(request);
        navItems.add(request);
        navItems.add(request);
        navItems.add(request);
        navItems.add(request);
        navItems.add(request);
    */
/*
        navItems.add(new NavItemRequest("RAJESH", R.mipmap.ic_launcher));
        navItems.add(new NavItemRequest("RINKU",R.mipmap.ic_launcher));
        navItems.add(new NavItemRequest("MOHAN",R.mipmap.ic_launcher));
        navItems.add(new NavItemRequest("VIPIN",R.mipmap.ic_launcher));
        navItems.add(new NavItemRequest("RAVI", R.mipmap.ic_launcher));
        navItems.add(new NavItemRequest("RENU", R.mipmap.ic_launcher));
*/
        //navItemView.setAdapter(new NavItemAdapter(getApplicationContext(), R.layout.drawer_menu_item, navItems, metrics));
    /*    if(navItemView!=null) {
            navItemView.setAdapter(new NavItemRequestAdapter(getApplicationContext(), R.layout.drawer_menu_item_request, navItems));
        }
*/
/*
        sent_request_mobile=(EditText)findViewById(R.id.sent_request_mobile);
        sent_request_button=(Button)findViewById(R.id.sent_request_button);
        sent_request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Admin.USER_ID, MySharedPreferences.getSharedPreferences(ParentRequest.this, Admin.USER_ID));
                params.put(Admin.MOBILE,sent_request_mobile.getText().toString());
                new RequestChild().execute(params);
            }
        });
        */

        HashMap<String,String> params = new HashMap<String, String>();
        params.put(Admin.USER_ID, MySharedPreferences.getSharedPreferences(ParentRequest.this, Admin.USER_ID));
        new RequestChild().execute(params);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.request_confirm:{
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Admin.USER_ID, MySharedPreferences.getSharedPreferences(this, Admin.USER_ID));
                params.put(Admin.SN,"1");
                params.put(Admin.ACTION,Admin.CONFIRM);
                new RequestAction().execute(params);
                Toast.makeText(this,"Accept",Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.request_reject: {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Admin.USER_ID, MySharedPreferences.getSharedPreferences(this, Admin.USER_ID));
                params.put(Admin.SN,"1");
                params.put(Admin.ACTION,Admin.REJECT);
                new RequestAction().execute(params);
                Toast.makeText(this, "Reject", Toast.LENGTH_SHORT).show();
                break;
            }
        }
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
                URL url = new URL(Admin.getParentRequest());
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

                    parseResult(response.toString());

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
        }
    }
    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            navItems = new ArrayList<>();
            if(response.has(Admin.SUCCESS)){
                int success=response.getInt(Admin.SUCCESS);
                if(success==1){
                    if(response.has(Admin.REQUEST)){
                        JSONArray jsonArray=response.getJSONArray(Admin.REQUEST);
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            NavItemRequest giftCard = new NavItemRequest();
                            if (jsonObject.has(Admin.USER_NAME)) {
                                giftCard.setNavTitle(jsonObject.getString(Admin.USER_NAME));
                            } else {
                                giftCard.setNavTitle("");
                            }
                            if (jsonObject.has(Admin.USER_ID)) {
                                giftCard.setId(jsonObject.getString(Admin.USER_ID));
                            } else {
                                giftCard.setId("");
                            }
                            if (jsonObject.has(Admin.MOBILE)) {
                                giftCard.setMobile(jsonObject.getString(Admin.MOBILE));
                            } else {
                                giftCard.setMobile("");
                            }
                            if (jsonObject.has(Admin.IMAGE)) {
                                giftCard.setImage(jsonObject.getString(Admin.IMAGE));
                            } else {
                                giftCard.setImage("");
                            }
                            if (jsonObject.has(Admin.SN)) {
                                giftCard.setSn(jsonObject.getString(Admin.SN));
                            } else {
                                giftCard.setImage("");
                            }
                            navItems.add(giftCard);
                        }
                    }
                }else if(success==2){

                }else{

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Ex", e.toString());
        }
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            if(navItemView!=null) {
                navItemView.setAdapter(new NavItemRequestAdapter(getApplicationContext(), R.layout.drawer_menu_item_request, navItems,ParentRequest.this));
            }
        }
    });
    }

   private  class NavItemRequestAdapter extends ArrayAdapter<NavItemRequest> {
        Context context;
        int resource;
        List<NavItemRequest> navItems;
        Activity activity;
        DisplayMetrics metrics;
        int navWidth;
        public NavItemRequestAdapter(Context context, int resource, List<NavItemRequest> navItems) {
            super(context, resource, navItems);
            this.context=context;
            this.resource=resource;
            this.navItems=navItems;
        }

        public NavItemRequestAdapter(Context context, int resource, List<NavItemRequest> navItems, Activity activity) {
            super(context, resource,navItems);
            this.context=context;
            this.resource=resource;
            this.navItems = navItems;
            this.activity = activity;
        }

        public NavItemRequestAdapter(Context context, int resource, List<NavItemRequest> navItems, int navWidth) {
            super(context, resource);
            this.context=context;
            this.resource=resource;
            this.navItems = navItems;
            this.navWidth =navWidth;
        }
        public NavItemRequestAdapter(Context context, int resource, List<NavItemRequest> navItems, DisplayMetrics metrics) {
            super(context, resource);
            this.context=context;
            this.resource=resource;
            this.navItems = navItems;
            this.metrics = metrics;
        }
        @Override
        public View getView(int position, final View convertView, ViewGroup parent) {
            View view=View.inflate(context, resource, null);
            final NavItemRequest navItem=navItems.get(position);
            ImageView navIcon=(ImageView)view.findViewById(R.id.drawer_menu_item_icon_1);
            TextView navTitle=(TextView)view.findViewById(R.id.drawer_menu_item_title_1);
            TextView navMobile=(TextView)view.findViewById(R.id.drawer_menu_item_mobile_1);
            Button request_confirm=(Button)view.findViewById(R.id.request_confirm);
            Button request_reject=(Button)view.findViewById(R.id.request_reject);

            String image=Admin.getImagePath(navItem.getImage()!=null?navItem.getImage():"");
            Picasso.with(context).load(image)
                    .error(R.mipmap.ic_account_box_white_48dp)
                    .placeholder(R.mipmap.ic_account_box_white_48dp)
                    .into(navIcon);
            navIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProfileChild.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            navTitle.setText(navItem.getNavTitle());
            navMobile.setText(navItem.getMobile());

           /*
                request_confirm.setOnClickListener(new BackGround(context, navItem.getId(),activity));
                 request_reject.setOnClickListener(new BackGround(context,navItem.getId(),activity));
            */

            //request_confirm.setOnClickListener(ParentRequest.this);
            //request_reject.setOnClickListener(ParentRequest.this);

        request_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: reject request code here
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Admin.USER_ID, MySharedPreferences.getSharedPreferences(context, Admin.USER_ID));
                params.put(Admin.SN,navItem.getSn());
                params.put(Admin.ACTION,Admin.CONFIRM);
                new RequestAction().execute(params);
            }
        });

        request_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //TODO: reject request code here
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Admin.USER_ID, MySharedPreferences.getSharedPreferences(context, Admin.USER_ID));
                params.put(Admin.SN,navItem.getSn());
                params.put(Admin.ACTION,Admin.REJECT);
                new RequestAction().execute(params);
            }
        });

            return view;
        }
    }

    private class RequestAction extends AsyncTask<HashMap<String,String>, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Integer doInBackground(HashMap<String, String>... params) {
            System.out.println("Calling Request");
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(Admin.getRequestAction());
                System.out.println("My URL :"+url);
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
                System.out.println("Response is :" +stringBuilder);
                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(stringBuilder.toString());
                wr.flush();
                wr.close();
                int statusCode = urlConnection.getResponseCode();
                // 200 represents HTTP OK
                System.out.println("Response Code is :" +statusCode);
                if (statusCode == 200) {
                    System.out.println("Response Code is :1" +statusCode);
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    System.out.println("Response Code is :2" +statusCode);
                    StringBuilder response = new StringBuilder();
                    System.out.println("Response Code is :3" +statusCode);
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println("Response Code is :4" +statusCode);
                    Log.d("Res Click", response.toString());
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
                System.out.print("FINISH");
            }else{
                System.out.print("UNFINISH");
            }
        }
    }
}
