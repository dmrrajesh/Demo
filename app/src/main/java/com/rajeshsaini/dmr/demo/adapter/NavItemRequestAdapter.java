package com.rajeshsaini.dmr.demo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rajeshsaini.dmr.demo.Admin;
import com.rajeshsaini.dmr.demo.BackGround;
import com.rajeshsaini.dmr.demo.MySharedPreferences;
import com.rajeshsaini.dmr.demo.ProfileChild;
import com.rajeshsaini.dmr.demo.R;
import com.rajeshsaini.dmr.demo.models.NavItem;
import com.rajeshsaini.dmr.demo.models.NavItemRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by rajesh on 3/9/2016.
 */



public class NavItemRequestAdapter extends ArrayAdapter<NavItemRequest>{
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
        request_confirm.setOnClickListener(new BackGround(context, navItem.getId(),activity));
        request_reject.setOnClickListener(new BackGround(context,navItem.getId(),activity));


/*
        request_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: reject request code here
                HashMap<String,String> params = new HashMap<String, String>();
                params.put(Admin.USER_ID, MySharedPreferences.getSharedPreferences(context, Admin.USER_ID));
                params.put(Admin.SN,navItem.getSn());
                params.put(Admin.ACTION,Admin.CONFIRM);
                new BackGround(context).execute(params);
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
                new BackGround(context).execute(params);
            }
        });
        */
        return view;
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
