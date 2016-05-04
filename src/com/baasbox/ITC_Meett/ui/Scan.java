package com.baasbox.ITC_Meett.ui;

import android.content.Intent;
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

import com.baasbox.ITC_Meett.R;
import com.google.android.gms.common.api.GoogleApiClient;

public class Scan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        final Button scanButton = (Button) findViewById(R.id.scan_button);

        scanButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(scanButton.isPressed()){
                    scanForMatches();
                }
            }
        });

    }

    public void scanForMatches(){

        setMyLocation();

    }

    public void setMyLocation(){

        BaasDocument doc = new BaasDocument("geo");
        doc.put("Author",BaasUser.current().getName())
           .put("Latitude","lat")
           .put("Longitude","long")
           .put("radius","rad");

        doc.save(new BaasHandler<BaasDocument>() {
            @Override
            public void handle(BaasResult<BaasDocument> res) {
                if(res.isSuccess()) {
                    Log.d("LOG","Saved: "+res.value());
                } else {

                }
            }
        });

    }
}
