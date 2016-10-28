package com.rajeshsaini.dmr.demo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        runSplesh();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        runSplesh();
    }
    @Override
    protected void onResume() {
        super.onResume();
        runSplesh();
    }
    private void runSplesh(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(Splash.this,LoginActivity.class);
                Splash.this.startActivity(intent);
            }
        },1000);

    }
}
