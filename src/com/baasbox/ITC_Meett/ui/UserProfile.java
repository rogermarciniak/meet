package com.baasbox.ITC_Meett.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baasbox.ITC_Meett.R;
import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasQuery;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;

import org.w3c.dom.Text;

import java.util.List;

public class UserProfile extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


    final TextView txt1 = (TextView) findViewById(R.id.userName);
        txt1.setText(BaasUser.current().getName().toString());

        final TextView inte1 = (TextView) findViewById(R.id.textView5);
        final TextView inte2 = (TextView) findViewById(R.id.textView6);
        final TextView inte3 = (TextView) findViewById(R.id.textView7);

        BaasQuery.Criteria filter = BaasQuery.builder().pagination(0, 20)
                .orderBy("_creation_date desc")
                .where("_author='" + BaasUser.current().getName() + "'")
                .criteria();


        BaasDocument.fetchAll("Preferences", filter,
                new BaasHandler<List<BaasDocument>>() {
                    @Override
                    public void handle(BaasResult<List<BaasDocument>> res) {
                        if (res.isSuccess()) {

                            for (BaasDocument doc : res.value()) {
                                String pref = doc.getString("Interests");
                                pref = pref.substring(1, pref.length()-1);
                                String[] parts = pref.split(",", 4);
                                inte1.setText(parts[0]);
                                inte2.setText(parts[1]);
                                inte3.setText(parts[2]);
                            }


                        } else {
                        }
                    }
                });

}
}
