package com.baasbox.ITC_Meett.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baasbox.ITC_Meett.R;
import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;
import com.baasbox.android.RequestToken;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainScreen extends AppCompatActivity {



    void startChat(String userName){
        Intent intent = new Intent(this,Chat.class);
        intent.putExtra("userName", userName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
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


    private RequestToken logoutToken;
    private final BaasHandler<Void> logoutHandler =
            new BaasHandler<Void>() {
                @Override
                public void handle(BaasResult<Void> voidBaasResult) {
                    logoutToken=null;
                    onLogout();
                }
            };

    String rec = "";

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
        final ImageButton notif = (ImageButton) findViewById(R.id.messageTracker);
        notif.setBackgroundResource(R.drawable.yes);
        notif.setEnabled(false);

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
        final int code = 1;
        Intent notificationIntent = new Intent(this, MainScreen.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                code, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                BaasDocument.fetchAll("ChatLog", new BaasHandler<List<BaasDocument>>() {
                    @Override
                    public void handle(BaasResult<List<BaasDocument>> res) {
                        if (res.isSuccess()) {
                            for (BaasDocument doc : res.value()) {
                                rec = doc.getString("Receiver");
                                String send = doc.getString("Sender");
                                String mess = doc.getString("Message");

                                if (BaasUser.current().getName().equals(doc.getString("Receiver"))) {

                                    notif.setBackgroundResource(R.drawable.no);
                                    notif.setEnabled(true);

                                } else {
                                    Log.e("ERROR", "NIE WIEM");
                                }
                            }
                        }
                    }
                });


            }

        }, 0, 5, TimeUnit.SECONDS);


        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChat(rec);
            }

        });


    }

}

