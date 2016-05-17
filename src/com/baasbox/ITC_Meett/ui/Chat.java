package com.baasbox.ITC_Meett.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.os.Handler;

import com.baasbox.ITC_Meett.R;
import com.baasbox.android.BaasACL;
import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasQuery;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;
import com.baasbox.android.Grant;
import com.baasbox.android.Role;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Chat extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final EditText txt = (EditText) findViewById(R.id.chatBox);
        final ListView chat = (ListView) findViewById(R.id.chatList);
        final Button send = (Button) findViewById(R.id.sentButton);

        final String pkgn = getIntent().getExtras().getString("userName");

        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(Chat.this, android.R.layout.simple_spinner_item, arrayList);

        final String matchedUser = pkgn;
        Log.d("UserName", matchedUser);

        chat.setAdapter(adapter);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                // arrayList.clear();
                BaasDocument.fetchAll("ChatLog", new BaasHandler<List<BaasDocument>>() {
                    @Override
                    public void handle(BaasResult<List<BaasDocument>> res) {
                        if (res.isSuccess()) {
                            for (BaasDocument doc : res.value()) {
                                String rec = doc.getString("Receiver");
                                String send = doc.getString("Sender");
                                String mess = doc.getString("Message");

                                Log.d("Panie", arrayList.toString());

                                // arrayList.add(doc.getString("Message"));
                                // adapter.notifyDataSetChanged();

                                if (BaasUser.current().getName().equals(doc.getString("Receiver"))) {


                                    String msg = doc.getString("Sender")+": " + mess;
                                    arrayList.add(msg);
                                    adapter.notifyDataSetChanged();


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

                                } else {
                                    Log.e("ERROR", "NIE WIEM");
                                }
                            }
                        }
                    }
                });

            }
        }, 0, 5, TimeUnit.SECONDS);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View c) {
                if (send.isPressed()) {

                    String currentDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());

                    String newString = txt.getText().toString();
                    arrayList.add(newString);


                    BaasDocument doc = new BaasDocument("ChatLog");
                    doc.put("Date", currentDate)
                            .put("Sender", BaasUser.current().getName().toString())     //NIE TRZEBA toStringa bo to oddaj stringa.
                            .put("Receiver", pkgn)
                            .put("Message", newString);
                    doc.save(BaasACL.grantRole(Role.REGISTERED, Grant.ALL), new BaasHandler<BaasDocument>() {
                        @Override
                        public void handle(BaasResult<BaasDocument> res) {
                            if (res.isSuccess()) {
                                Log.d("LOG", "Zapisany: " + res.value());
                                txt.setText("");
                                adapter.notifyDataSetChanged();
                                chat.invalidateViews();
                            } else {
                                Log.e("LOG", "Error");
                            }
                        }
                    });

                } else {

                }

            }
        });


    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this,MainScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}