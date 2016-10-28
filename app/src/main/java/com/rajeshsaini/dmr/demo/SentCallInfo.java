package com.rajeshsaini.dmr.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by DMRSAINI on 3/15/2016.
 */
public class SentCallInfo extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getApplicationContext(),"Calling Activity Now ",Toast.LENGTH_LONG).show();
    }

}
