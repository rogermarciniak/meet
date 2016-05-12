package com.baasbox.ITC_Meett.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;



public class Scan extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String mLatitudeText;
    private String mLongitudeText;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        // Create an instance of GoogleAPIClient.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        final Button scanButton = (Button) findViewById(R.id.buttonbutton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View c) {
                if (scanButton.isPressed()) {
                    setMyLocation();
                    scanForMatches();
                }
            }

        });
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
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
        final ListView matchList = (ListView) findViewById(R.id.matchList);
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
        String whereString = "distance(Latitude,Longitude," + mLatitudeText + "," + mLongitudeText + ") < 0.5";
        final BaasQuery PREPARED_QUERY =
                BaasQuery.builder()
                        .collection("geo")
                        .where(whereString)
                        .build();

        PREPARED_QUERY.query(new BaasHandler<List<JsonObject>>() {
            @Override
            public void handle(BaasResult<List<JsonObject>> res) {
                    if (res.isSuccess()) {
                            for (JsonObject doc : res.value()) {

                                String userName = doc.getString("Author");
                                String lati = doc.get("Latitude");
                                String longi = doc.get("Longitude");

                                Log.d("Pass", userName);

                                String finalOutPut = userName + " " + lati + " " + longi;
                                arrayList.add(finalOutPut);
                                adapter.notifyDataSetChanged();
                                matchList.invalidateViews();

                            }
                    }
                    else{
                        arrayList.add("TEST TEST 2223");
                        adapter.notifyDataSetChanged();
                        matchList.invalidateViews();
                    }
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
    //Moje i chuj
        BaasQuery.Criteria filter = BaasQuery.builder().pagination(0, 20)
                .orderBy("_creation_date desc")
                .where("_author='" + BaasUser.current().getName() + "'")
                .criteria();


        BaasDocument.fetchAll("geo", filter,
                new BaasHandler<List<BaasDocument>>() {
                    @Override
                    public void handle(BaasResult<List<BaasDocument>> res) {
                        if (res.isSuccess()) {
                            for (BaasDocument doc : res.value()) {
                                Log.d("LOG", "Doc: " + doc);
                                doc.delete(new BaasHandler<Void>() {
                                    @Override
                                    public void handle(BaasResult<Void> res) {
                                        if (res.isSuccess()) {
                                            Log.d("LOG", "Document deleted");
                                        } else {
                                            Log.e("LOG", "error", res.error());
                                        }
                                    }
                                });
                                break;
                            }
                        } else {
                        }
                    }
                });

        BaasDocument doc = new BaasDocument("geo");
        doc.put("Author",BaasUser.current().getName())
           .put("Latitude", mLatitudeText)
                .put("Longitude", mLongitudeText);

        doc.save(new BaasHandler<BaasDocument>() {
            @Override
            public void handle(BaasResult<BaasDocument> res) {
                if (res.isSuccess()) {
                    Log.d("LOG", "Saved: " + res.value());
                } else {
                    Log.e("LOG", "Failed");
                }
            }
        });


    }


}

