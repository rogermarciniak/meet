package com.baasbox.ITC_Meett.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baasbox.ITC_Meett.R;
import com.baasbox.android.BaasACL;
import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasFile;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasQuery;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;
import com.baasbox.android.Grant;
import com.baasbox.android.Role;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Filter;

public class Series extends ActionBarActivity {

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageButton bmImage;

        public DownloadImageTask(ImageButton bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

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
    public class MyString {
        private String value;
        public MyString(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
    }
    void loadURL(){

    }
    void clearDB(){
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
    }
    void UploadResults(List y){

        clearDB();

        String currentDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());

        String newString = y.toString();

            BaasDocument doc = new BaasDocument("Preferences");
            doc.put("Date", currentDate)
                    .put("Author",BaasUser.current().getName().toString())
                    .put("Interests", newString);
            doc.save(BaasACL.grantRole(Role.REGISTERED, Grant.READ),new BaasHandler<BaasDocument>() {
                @Override
                public void handle(BaasResult<BaasDocument> res) {
                    if (res.isSuccess()) {
                        Log.d("LOG", "Zapisany: " + res.value());
                    } else {
                        Log.e("LOG", "Error");
                    }
                }
            });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);

        final List pref = new ArrayList();

        final ArrayList<String> links = new ArrayList<String>();
       links.add("http://www.klydewarrenpark.org/media/images/Activities/reading.jpg");
        links.add("http://www.roadtogrammar.com/movies/fimls.jpg");
        links.add("http://www.ballingerathleticperformance.com/wp-content/uploads/2012/01/crowie.jpg");
        links.add("http://www.natural-homeremedies.com/fitness/wp-content/uploads/2010/10/Health-Benefits-Of-Swimming.jpg");
        links.add("http://goodlogo.com/images/logos/ea_sports_logo_3377.gif");
        links.add("http://i-cdn.phonearena.com/images/article/73702-image/The-5-best-smartphones-for-mobile-gaming.jpg");
        links.add("http://s.hswstatic.com/gif/10-best-family-dog-breeds-6.jpg");
        links.add("http://static1.squarespace.com/static/5575eb95e4b08f79780bfb17/5575f0c9e4b04dfb97b3994d/5575f0e9e4b04dfb97b39cf2/1433792832487/tabby-cat-licking-its-lips.png");
        //links.add("http://performancecomms.com/wp-content/uploads/2014/02/Putin-Happy.jpg");
        final ArrayList<String> preferences = new ArrayList<String>();
        preferences.add("Books");
        preferences.add("Movies");
        preferences.add("Running");
        preferences.add("Swimming");
        preferences.add("Sports");
        preferences.add("Gaming");
        preferences.add("Dogs");
        preferences.add("Cats");
        final ArrayList<String> result = new ArrayList<String>();
       // links.add("http://performancecomms.com/wp-content/uploads/2014/02/Putin-Happy.jpg");





        final MyInt passes = new MyInt(0);
        final MyInt passes2 = new MyInt(0);
        final int numberOfEntries = 3;

        int temp = passes.getValue();
        new DownloadImageTask((ImageButton) findViewById(R.id.opt1))
                .execute(links.get(temp));
        new DownloadImageTask((ImageButton) findViewById(R.id.opt2))
                .execute(links.get(temp + 1));


        final ImageButton imgButt1 = (ImageButton) findViewById(R.id.opt1);
        final ImageButton imgButt2 = (ImageButton) findViewById(R.id.opt2);
        final Button upload = (Button) findViewById(R.id.button);

        imgButt1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View c) {
                if(imgButt1.isPressed()){
                    int var = passes2 .getValue();
                    if(var <= numberOfEntries -1){
                        int temp = passes.getValue();
                        result.add(preferences.get(temp).toString());
                        temp = temp +2;
                        new DownloadImageTask((ImageButton) findViewById(R.id.opt1))
                                .execute(links.get(temp).toString());
                        new DownloadImageTask((ImageButton) findViewById(R.id.opt2))
                                .execute(links.get(temp+1).toString());
                        passes.setValue(temp);
                    }
                    else{
                        UploadResults(result);
                        Intent intent = new Intent(Series.this, MainScreen.class);
                        startActivity(intent);
                        finish();
                    }

                    var = var +1;
                    passes2.setValue(var);
                }
            }

        });
        imgButt2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View c) {
                if(imgButt2.isPressed()){
                    int var = passes2 .getValue();
                    if(var <= numberOfEntries -1){
                        int temp = passes.getValue();
                        result.add(preferences.get(temp+1).toString());
                        temp = temp +2;
                        new DownloadImageTask((ImageButton) findViewById(R.id.opt1))
                                .execute(links.get(temp).toString());
                        new DownloadImageTask((ImageButton) findViewById(R.id.opt2))
                                .execute(links.get(temp+1).toString());
                        passes.setValue(temp);
                    }
                    else{
                        UploadResults(result);
                        Intent intent = new Intent(Series.this, MainScreen.class);
                        startActivity(intent);
                        finish();
                    }

                    var = var +1;
                    passes2.setValue(var);
                }
            }

        });
        upload.setEnabled(false);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadResults(result);
                upload.setEnabled(false);
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
