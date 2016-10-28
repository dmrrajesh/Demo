package com.rajeshsaini.dmr.demo;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.rajeshsaini.dmr.demo.adapter.NavItemAdapter;
import com.rajeshsaini.dmr.demo.fragment.ViewDetail;
import com.rajeshsaini.dmr.demo.models.CallDetails;
import com.rajeshsaini.dmr.demo.models.GPSTracker;
import com.rajeshsaini.dmr.demo.models.NavItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ViewDetail.OnViewDetailListener {
    private List<NavItem> navItems;
    private ListView navItemView;
    private ImageView parent_imageView, addChild;
    private TextView parent_user_name, parent_user_mobile;
    public static Handler handler = new Handler();
    public static Handler handler1 = new Handler();
    public static GPSTracker gpsTracker;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gpsTracker = new GPSTracker(MainActivity.this);
        parent_imageView = (ImageView) findViewById(R.id.parent_imageView);
        parent_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Profile.class));
            }
        });
        parent_user_name = (TextView) findViewById(R.id.parent_user_name);
        parent_user_mobile = (TextView) findViewById(R.id.parent_mobile_number);

        if (MySharedPreferences.getSharedPreferences(this, Admin.MOBILE).length() > 0) {
            parent_user_mobile.setText(MySharedPreferences.getSharedPreferences(this, Admin.MOBILE));
        }

        addChild = (ImageView) findViewById(R.id.add_child);
        addChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChildAdd.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


/*
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navItemView=(ListView)navigationView.findViewById(R.id.drawer_menu_items);
        navItemView.setOnItemClickListener(new NavMenuItemClickListener());
*/
        /*
        navItems=new ArrayList<>();

        navItems.add(new NavItem("RAJESH", R.mipmap.ic_launcher));
        navItems.add(new NavItem("RINKU",R.mipmap.ic_launcher));
        navItems.add(new NavItem("MOHAN",R.mipmap.ic_launcher));
        navItems.add(new NavItem("VIPIN",R.mipmap.ic_launcher));
        navItems.add(new NavItem("RAVI", R.mipmap.ic_launcher));
        navItems.add(new NavItem("RENU", R.mipmap.ic_launcher));
        */
        //navItemView.setAdapter(new NavItemAdapter(getApplicationContext(), R.layout.drawer_menu_item, navItems, metrics));

        //navItemView.setAdapter(new NavItemAdapter(getApplicationContext(), R.layout.drawer_menu_item, navItems));

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Admin.USER_ID, MySharedPreferences.getSharedPreferences(this, Admin.USER_ID));
        new MyProfile().execute(params);
        new MyChilds().execute(params);

//      navigationView.setNavigationItemSelectedListener(this);
        handler.postDelayed(runnable, 1000);
        handler1.postDelayed(runnable1, 1000);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    public  Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (gpsTracker.canGetLocation()) {
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(Admin.USER_ID, MySharedPreferences.getSharedPreferences(MainActivity.this, Admin.USER_ID));
                params.put(Admin.LATITUDE,String.valueOf(latitude));
                params.put(Admin.LONGITUDE, String.valueOf(longitude));
                String latii    =    MySharedPreferences.getSharedPreferences(getApplicationContext(), Admin.LATITUDE);
                String lano     =    MySharedPreferences.getSharedPreferences(getApplicationContext(),Admin.LONGITUDE);
                if(!lano.equalsIgnoreCase(String.valueOf(longitude)) || ! latii.equalsIgnoreCase(String.valueOf(latitude)) ){
                    new LocationUpdate().execute(params);
                    MySharedPreferences.setSharedPreferences(MainActivity.this, Admin.LATITUDE, latii);
                    MySharedPreferences.setSharedPreferences(MainActivity.this,Admin.LONGITUDE,lano);
                }
            }
            handler.postDelayed(runnable, 1000*60*5);
        }
    };
    private Runnable runnable1=new Runnable() {
        @Override
        public void run() {
            CallDetails details=getLastCallDetails();
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(Admin.USER_ID, MySharedPreferences.getSharedPreferences(MainActivity.this, Admin.USER_ID));
            params.put(Admin.DATE,details.getDate());
            params.put(Admin.TIME,details.getTime());
            params.put(Admin.MOBILE,details.getMobile());
            params.put(Admin.DURATION,details.getDuration());
            params.put(Admin.TYPE, details.getType());
            if(!MySharedPreferences.getSharedPreferences(MainActivity.this,Admin.OLD_CALL_UPDATE).equalsIgnoreCase(details.getOldUpdate())) {
                new CallUpdate().execute(params);
                MySharedPreferences.setSharedPreferences(MainActivity.this,Admin.OLD_CALL_UPDATE,details.getOldUpdate());
            }
            handler.postDelayed(runnable1, 1000*60);
        }
    };
    private CallDetails getLastCallDetails(){
        CallDetails details=new CallDetails();
        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
        Cursor managedCursor =managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, strOrder);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        while (managedCursor.moveToNext()) {
            String phNum = managedCursor.getString(number);
            details.setMobile(phNum);
            String callTypeCode = managedCursor.getString(type);
            String strcallDate = managedCursor.getString(date);
            details.setOldUpdate(strcallDate);
            Date callDate = new Date(Long.valueOf(strcallDate));
            java.sql.Date date1=new java.sql.Date(callDate.getTime());
            java.sql.Time time=new java.sql.Time(callDate.getTime());
            details.setDate(date1.toString());
            details.setTime(time.toString());
            String callDuration = managedCursor.getString(duration);
            details.setDuration(callDuration);
            String callType = null;
            int callcode = Integer.parseInt(callTypeCode);
            switch (callcode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    details.setType(Admin.CALL_OUTGOING);
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    details.setType(Admin.CALL_INCOMING);
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    details.setType(Admin.CALL_MISS);
                    break;
            }
            break;
        }
        managedCursor.close();
        return details;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            MySharedPreferences.logOut(this);
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        if (id == R.id.action_requests) {
            startActivity(new Intent(MainActivity.this, ParentRequest.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, ViewDetail.newInstance(), ViewDetail.TAG).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Admin.USER_ID, MySharedPreferences.getSharedPreferences(this, Admin.USER_ID));
        new MyProfile().execute(params);
        new MyChilds().execute(params);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Admin.USER_ID, MySharedPreferences.getSharedPreferences(this, Admin.USER_ID));
        new MyProfile().execute(params);
        new MyChilds().execute(params);
    }

    @Override
    public void onViewDetail(Uri uri) {

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.rajeshsaini.dmr.demo/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.rajeshsaini.dmr.demo/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class NavMenuItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            navigateTo(position);
        }
    }
    private void navigateTo(int position) {
       MySharedPreferences.setSharedPreferences(MainActivity.this,Admin.CHILD_ID,navItems.get(position).getId());
       getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, ViewDetail.newInstance(navItems.get(position).getId()), ViewDetail.TAG).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    private class MyChilds extends AsyncTask<HashMap<String, String>, Void, Integer> {
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
            Log.d("RESULT",result);
            JSONObject response = new JSONObject(result);
            navItems = new ArrayList<>();
            if (response.has(Schema.SUCCESS)) {
                int success = response.getInt(Schema.SUCCESS);
                if (success == 1) {
                    if (response.has(Schema.CHILD)) {
                        JSONArray jsonArray = response.getJSONArray(Schema.CHILD);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            NavItem giftCard = new NavItem();
                            if (jsonObject.has(Admin.USER_ID)) {
                                giftCard.setId(jsonObject.getString(Admin.USER_ID));
                            } else {
                                giftCard.setId("");
                            }
                            if (jsonObject.has(Schema.NAME)) {
                                giftCard.setNavTitle(jsonObject.getString(Schema.NAME));
                            } else {
                                giftCard.setNavTitle("");
                            }
                            if (jsonObject.has(Schema.MOBILE)) {
                                giftCard.setMobile(jsonObject.getString(Schema.MOBILE));
                            } else {
                                giftCard.setMobile("");
                            }
                            if (jsonObject.has(Schema.IMAGE)) {
                                giftCard.setImage(jsonObject.getString(Schema.IMAGE));
                            } else {
                                giftCard.setImage("");
                            }
                            navItems.add(giftCard);
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navItemView = (ListView) navigationView.findViewById(R.id.drawer_menu_items);
                navItemView.setOnItemClickListener(new NavMenuItemClickListener());
                if (navItems != null) {
                    navItemView.setAdapter(new NavItemAdapter(getApplicationContext(), R.layout.drawer_menu_item, navItems));
                }
                navigationView.setNavigationItemSelectedListener(MainActivity.this);
            }
        });
    }

    private class LocationUpdate extends AsyncTask<HashMap<String, String>, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(HashMap<String, String>... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(Admin.getLocationUpdate());
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
                Log.d("ResponseCode", statusCode + "");
                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    final JSONObject object = new JSONObject(response.toString());
                    if (object.has(Admin.SUCCESS)) {
                        int success = object.getInt(Admin.SUCCESS);
                        if (success == 1) {
                            if (object.has(Admin.USER_NAME)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            parent_user_name.setText(object.getString(Admin.USER_NAME));
                                        } catch (Exception ee) {

                                        }
                                    }
                                });
                            }
                            if (object.has(Admin.IMAGE)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String image = Admin.getImagePath(object.getString(Admin.IMAGE));
                                            Picasso.with(MainActivity.this).load(image)
                                                    .error(R.mipmap.ic_account_box_white_48dp)
                                                    .placeholder(R.mipmap.ic_account_box_white_48dp)
                                                    .into(parent_imageView);
                                        } catch (JSONException ee) {
                                        }
                                    }
                                });
                            }
                            if (object.has(Admin.MOBILE)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            parent_user_mobile.setText(object.getString(Admin.MOBILE).length() == 10 ? object.getString(Admin.MOBILE) : MySharedPreferences.getSharedPreferences(MainActivity.this, Admin.MOBILE));
                                        } catch (Exception ee) {
                                        }
                                    }
                                });
                            }
                            return 1;
                        } else {
                            return 0;
                        }
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
    private class CallUpdate extends AsyncTask<HashMap<String, String>, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(HashMap<String, String>... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(Admin.getCallUpdate());
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
                Log.d("ResponseCode", statusCode + "");
                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    final JSONObject object = new JSONObject(response.toString());
                    if (object.has(Admin.SUCCESS)) {
                        int success = object.getInt(Admin.SUCCESS);
                        if (success == 1) {
                            return 1;
                        } else {
                            return 0;
                        }
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
    private class MyProfile extends AsyncTask<HashMap<String, String>, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(HashMap<String, String>... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(Admin.getMyProfile());
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
                Log.d("ResponseCode", statusCode + "");
                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    final JSONObject object = new JSONObject(response.toString());
                    if (object.has(Admin.SUCCESS)) {
                        int success = object.getInt(Admin.SUCCESS);

                        if (success == 1) {
                            if (object.has(Admin.USER_NAME)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            parent_user_name.setText(object.getString(Admin.USER_NAME));
                                        } catch (Exception ee) {

                                        }
                                    }
                                });
                            }
                            if (object.has(Admin.IMAGE)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String image = Admin.getImagePath(object.getString(Admin.IMAGE));
                                            Picasso.with(MainActivity.this).load(image)
                                                    .error(R.mipmap.ic_account_box_white_48dp)
                                                    .placeholder(R.mipmap.ic_account_box_white_48dp)
                                                    .into(parent_imageView);
                                        } catch (JSONException ee) {
                                        }
                                    }
                                });
                            }
                            if (object.has(Admin.MOBILE)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            parent_user_mobile.setText(object.getString(Admin.MOBILE).length() == 10 ? object.getString(Admin.MOBILE) : MySharedPreferences.getSharedPreferences(MainActivity.this, Admin.MOBILE));
                                        } catch (Exception ee) {
                                        }
                                    }
                                });
                            }
                            return 1;
                        } else {
                            return 0;
                        }
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
}
