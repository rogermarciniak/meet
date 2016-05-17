package com.baasbox.ITC_Meet.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.baasbox.ITC_Meet.R;
import com.baasbox.android.BaasACL;
import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasHandler;
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

/**
 * @author:
 * Bartosz Zurawski(c00165634)
 */

public class Chat extends Activity {
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;


    public class MyInt {
        private int value;
        public MyInt(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
        public void setValue(int value) {
            this.value = value;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getIntent().setAction("Already created");
        final EditText txt = (EditText) findViewById(R.id.chatBox);
        final ListView chat = (ListView) findViewById(R.id.chatList);
        final Button send = (Button) findViewById(R.id.sentButton);

        final String pkgn = getIntent().getExtras().getString("userName");

        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(Chat.this, android.R.layout.simple_spinner_item, arrayList);

        final String matchedUser = pkgn;
        Log.d("UserName", matchedUser);

        chat.setAdapter(adapter);

        final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
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


                                    String msg = doc.getString("Sender") + ": " + mess;
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
            public void stop(){
                scheduler.shutdown();
            }
        }, 0, 5, TimeUnit.SECONDS);
        final MyInt count = new MyInt(0);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View c) {
                if (send.isPressed()) {

                    if (count.getValue() < 6) {
                        String currentDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());

                        String newString = BaasUser.current().getName() + ": " + txt.getText().toString();
                        String myMsg = txt.getText().toString();
                        arrayList.add(newString);


                        BaasDocument doc = new BaasDocument("ChatLog");
                        doc.put("Date", currentDate)
                                .put("Sender", BaasUser.current().getName())     //NIE TRZEBA toStringa bo to oddaj stringa.
                                .put("Receiver", pkgn)
                                .put("Message", myMsg);
                        doc.save(BaasACL.grantRole(Role.REGISTERED, Grant.ALL), new BaasHandler<BaasDocument>() {
                            @Override
                            public void handle(BaasResult<BaasDocument> res) {
                                if (res.isSuccess()) {
                                    Log.d("LOG", "Zapisany: " + res.value());
                                    txt.setText("");
                                    adapter.notifyDataSetChanged();
                                    chat.invalidateViews();
                                    count.setValue(count.getValue() + 1);
                                } else {
                                    Log.e("LOG", "Error");
                                }
                            }
                        });

                    } else {
                        send.setEnabled(false);
                        txt.setEnabled(false);
                        txt.setText(" ");
                        String info = "No more messages can be sent. Limit reached(6)";

                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, info, duration);
                        toast.show();

                    }
                }
                else{

                }
            }
        });


        }

        @Override
    public void onBackPressed() {

        /*   Intent intent = new Intent(this,MainScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();

*/         Intent myInent = new Intent(this, Chat.class);
            this.stopService(myInent);
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
    }
    @Override
    protected void onResume() {
        Log.v("Example", "onResume");

        String action = getIntent().getAction();
        // Prevent endless loop by adding a unique action, don't restart if action is present
        if(action == null || !action.equals("Already created")) {
            Log.v("Example", "Force restart");
            Intent intent = new Intent(this, Chat.class);
            startActivity(intent);
            finish();
        }
        // Remove the unique action so the next time onResume is called it will restart
        else
            getIntent().setAction(null);

        super.onResume();
    }
}
