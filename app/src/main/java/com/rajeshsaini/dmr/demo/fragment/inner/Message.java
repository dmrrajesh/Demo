package com.rajeshsaini.dmr.demo.fragment.inner;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.rajeshsaini.dmr.demo.R;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DMRSAINI on 3/9/2016.
 */

public class Message extends Fragment implements View.OnClickListener{

    Button btnSent, btnInbox, btnDraft;
    TextView lblMsg, lblNo;
    ListView lvMsg;
    // Cursor Adapter
    SimpleCursorAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_message,container,false);

        // Init GUI Widget
        btnInbox = (Button)view.findViewById(R.id.btnInbox);
        btnInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,getInboxMessage());
                lvMsg.setAdapter(adapter1);
            }
        });

        btnSent = (Button)view.findViewById(R.id.btnSentBox);
        btnSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,getSentMessage());
                lvMsg.setAdapter(adapter1);
            }
        });

        btnDraft = (Button)view.findViewById(R.id.btnDraft);
        btnDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,getDraftMessage());
                lvMsg.setAdapter(adapter1);
            }
        });

        lvMsg = (ListView)view.findViewById(R.id.lvMsg);
        return view;
    }

    @Override
    public void onClick(View v) {

        if (v == btnInbox) {

            // Create Inbox box URI
            Uri inboxURI = Uri.parse("content://sms/inbox");

            // List required columns
            String[] reqCols = new String[] { "_id", "address", "body" };

            // Get Content Resolver object, which will deal with Content Provider
            ContentResolver cr = getActivity().getContentResolver();

            // Fetch Inbox SMS Message from Built-in Content Provider
            Cursor c = cr.query(inboxURI, reqCols, null, null, null);
            // Attached Cursor with adapter and display in listview

            List<String> list;
        /*
            adapter = new SimpleCursorAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item, c,
                    new String[] { "body", "address" }, new int[] {
                    R.id.lblMsg, R.id.lblNumber });
*/
            ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,getInboxMessage());
            lvMsg.setAdapter(adapter1);

        }
/*
        if(v==btnSent)
        {
            // Create Sent box URI
            Uri sentURI = Uri.parse("content://sms/sent");

            // List required columns
            String[] reqCols = new String[] { "_id", "address", "body" };

            // Get Content Resolver object, which will deal with Content Provider
            ContentResolver cr = getActivity().getContentResolver();

            // Fetch Sent SMS Message from Built-in Content Provider
            Cursor c = cr.query(sentURI, reqCols, null, null, null);

            // Attached Cursor with adapter and display in listview
            adapter = new SimpleCursorAdapter(getActivity(), R.layout.row, c,
                    new String[] { "body", "address" }, new int[] {
                    R.id.lblMsg, R.id.lblNumber });
            lvMsg.setAdapter(adapter);

        }

        if(v==btnDraft)
        {
            // Create Draft box URI
            Uri draftURI = Uri.parse("content://sms/draft");

            // List required columns
            String[] reqCols = new String[] { "_id", "address", "body" };

            // Get Content Resolver object, which will deal with Content Provider
            ContentResolver cr = getActivity().getContentResolver();

            // Fetch Sent SMS Message from Built-in Content Provider
            Cursor c = cr.query(draftURI, reqCols, null, null, null);

            // Attached Cursor with adapter and display in listview
            adapter = new SimpleCursorAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, c,
                    new String[] { "body", "address" }, new int[] {
                    R.id.lblMsg, R.id.lblNumber });
            lvMsg.setAdapter(adapter);
        }
        */
    }

    public List<String> getInboxMessage(){
        List<String> list=new ArrayList<>();
        Uri uri=Uri.parse("content://sms/inbox");
        Cursor cursor=getActivity().getContentResolver().query(uri,null,null,null,null);
        while (cursor.moveToNext()){
            String address=cursor.getString(cursor.getColumnIndex("address"));
            String body=cursor.getString(cursor.getColumnIndexOrThrow("body"));
            list.add(address+"\n"+body);
        }
        return list;
    }
    public List<String> getSentMessage(){
        List<String> list=new ArrayList<>();
        Uri uri=Uri.parse("content://sms/sent");
        Cursor cursor=getActivity().getContentResolver().query(uri,null,null,null,null);
        while (cursor.moveToNext()){
            String address=cursor.getString(cursor.getColumnIndex("address"));
            String body=cursor.getString(cursor.getColumnIndexOrThrow("body"));
            list.add(address+"\n"+body);
        }
        return list;
    }
    public List<String> getDraftMessage(){
        List<String> list=new ArrayList<>();
        Uri uri=Uri.parse("content://sms/draft");
        Cursor cursor=getActivity().getContentResolver().query(uri,null,null,null,null);
        while (cursor.moveToNext()){
            String address=cursor.getString(cursor.getColumnIndex("address"));
            String body=cursor.getString(cursor.getColumnIndexOrThrow("body"));
            list.add(address+"\n"+body);
        }
        return list;
    }
    public static void getMessageDetails(Activity activity, String ph_number) {
        String forDelete_thread_id = "";
        String forDelete_id = "";
        String address = ph_number;
        Uri allMsg = Uri.parse("content://sms/");
        Cursor managedCursor = activity.getContentResolver().query(
                allMsg,
                new String[] { "_id", "thread_id", "address", "date", "body",
                        "type" }, "address=?", new String[] { address }, null);
        while (managedCursor.moveToNext()) {
            String msg_id = managedCursor.getString(0);
            String thread_id = managedCursor.getString(1);
            String address_b = managedCursor.getString(2);
            String date = managedCursor.getString(3);
            String msg_body = managedCursor.getString(4);
            String type = managedCursor.getString(5);
        }
    }
}
