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

        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);

        final String matchedUser = "test1";
        BaasDocument.fetchAll("ChatLog", new BaasHandler<List<BaasDocument>>() {
            @Override
            public void handle(BaasResult<List<BaasDocument>> res) {
                if (res.isSuccess()) {
                    for (BaasDocument doc : res.value()) {
                        String rec = doc.getString("Receiver");
                        String send = doc.getString("Sender");
                        String mess = doc.getString("Message");
                        Log.d("STRINGS", send);
                        Log.d("MATCHED", matchedUser);
                        Log.d("2", rec);
                        Log.d("3", mess);
                        Log.d("user", BaasUser.current().getName());

                        arrayList.add(doc.getString("Receiver"));
                        arrayList.add(doc.getString("Message"));
                        adapter.notifyDataSetChanged();
                        chat.invalidateViews();

                        if (BaasUser.current().getName() == doc.getString("Receiver")) {

                            arrayList.add(doc.getString("Message"));
                            adapter.notifyDataSetChanged();
                            chat.invalidateViews();
                        } else {
                            Log.e("ERROR", "NIE WIEM");
                        }
                          /*     if (doc.getString("Receiver") == BaasUser.current().getName()){
                                    Log.d("Receiver PASS", rec);
                                    Log.d("Sender PASS", send);
                                    arrayList.add(doc.getString("Message"));
                                    adapter.notifyDataSetChanged();
                                    chat.invalidateViews();
                                        Log.d("LOG", "Doc: " + doc);
                                    break;
                                }
                                else{
                                    arrayList.add("Cos sie zjebalo");
                                    adapter.notifyDataSetChanged();
                                    chat.invalidateViews();
                                }
*/

                    }
                }
            }
        });

        chat.setAdapter(adapter);

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
                            .put("Receiver", "test1")
                            .put("Message", newString);
                    doc.save(BaasACL.grantRole(Role.REGISTERED, Grant.READ), new BaasHandler<BaasDocument>() {
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
