package com.rajeshsaini.dmr.demo;

/**
 * Created by DMRSAINI on 3/16/2016.
 */
import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.*;
import com.google.android.gms.common.api.GoogleApiClient;

public class CurrentLocation implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    Context context;

    public CurrentLocation(Context context) {
        this.context = context;
    }



    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
