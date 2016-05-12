package com.baasbox.ITC_Meett.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baasbox.ITC_Meett.R;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;
import com.baasbox.android.RequestToken;

public class MainScreen extends AppCompatActivity {



    private void onProfile(){
        Intent intent = new Intent(this,UserProfile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    private void onSeries(){
        Intent intent = new Intent(this,Series.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    private void onLogout(){
        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    private void onScan(){
        Intent intent = new Intent(this,Scan.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    private void onChat(){
        Intent intent = new Intent(this,Chat.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    /*private void onScan2(){
        Intent intent = new Intent(this,Matchedd.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }*/

    private RequestToken logoutToken;
    private final BaasHandler<Void> logoutHandler =
            new BaasHandler<Void>() {
                @Override
                public void handle(BaasResult<Void> voidBaasResult) {
                    logoutToken=null;
                    onLogout();
                }
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen) ;

        final TextView UserName = (TextView) findViewById(R.id.userN);
        UserName.setText(BaasUser.current().getName());

        final Button profile = (Button) findViewById(R.id.button2);
        final Button series = (Button) findViewById(R.id.button3);
        final Button logout = (Button) findViewById(R.id.button4);
        final Button scan = (Button) findViewById(R.id.button5);
        final Button chat = (Button) findViewById(R.id.button6);
        final Button scan2 = (Button) findViewById(R.id.scand);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfile();
            }
        });
        series.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSeries();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaasUser.current().logout(logoutHandler);
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScan();
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChat();
            }
        });
        /*scan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScan2();
            }
        });*/

    }
}
