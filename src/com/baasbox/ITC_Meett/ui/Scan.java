package com.baasbox.ITC_Meett.ui;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.baasbox.ITC_Meett.R;
import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasFile;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasQuery;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;

import com.baasbox.android.json.JsonObject;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import java.util.List;


public class Scan extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String mLatitudeText;
    private String mLongitudeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        // Create an instance of GoogleAPIClient.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        final Button scanButton = (Button) findViewById(R.id.scan_button);

        if (scanButton == null) throw new AssertionError();

        scanButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(scanButton.isPressed()){
                    scanForMatches();
                }
            }
        });
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override //NOT NULL PLZ
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText = String.valueOf(mLastLocation.getLatitude());
            mLongitudeText = String.valueOf(mLastLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Connection failed", connectionResult.toString());
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void scanForMatches(){

        setMyLocation();
        String whereString = "distance(Latitude,Longitude," + mLatitudeText + "," + mLongitudeText + ") < 0.5";
        final BaasQuery PREPARED_QUERY =
                BaasQuery.builder()
                        .collection("geo")
                        .where(whereString)
                        .build();

        PREPARED_QUERY.query(new BaasHandler<List<JsonObject>>(){
            @Override
            public void handle(BaasResult<List<JsonObject>> res){
                // handle result or failure
            }
        });
    }

    public void setMyLocation(){

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText = String.valueOf(mLastLocation.getLatitude());
            mLongitudeText = String.valueOf(mLastLocation.getLongitude());
        }

        BaasDocument doc = new BaasDocument("geo");
        doc.put("Author",BaasUser.current().getName())
           .put("Latitude", mLatitudeText)
           .put("Longitude", mLongitudeText);

        doc.save(new BaasHandler<BaasDocument>() {
            @Override
            public void handle(BaasResult<BaasDocument> res) {
                if(res.isSuccess()) {
                    Log.d("LOG","Saved: "+res.value());
                } else {
                    Log.e("LOG","Failed");
                }
            }
        });

    }
}
