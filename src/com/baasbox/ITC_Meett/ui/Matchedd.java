package com.baasbox.ITC_Meett.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baasbox.ITC_Meett.R;
import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasFile;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasQuery;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;

import java.io.InputStream;
import java.util.List;

public class Matchedd extends AppCompatActivity {

    String[] myInterests = {"0","1","2"};
    String[] myMatches = {"0","1","2"};
    int matchCount = 0;

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageButton bmImage;

        public DownloadImageTask(ImageButton bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            Bitmap resizedBitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                resizedBitmap = Bitmap.createScaledBitmap(mIcon11, 250, 250, false);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return resizedBitmap;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchedd);


        final ImageButton inte1 = (ImageButton) findViewById(R.id.match1);
        final ImageButton inte2 = (ImageButton) findViewById(R.id.match2);
        final ImageButton inte3 = (ImageButton) findViewById(R.id.match3);

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
                                pref = pref.substring(1, pref.length() - 1);
                                myInterests = pref.split(",", 4);

                            }
                        }
                    }
                });


        BaasDocument.fetchAll("Preferences",
                new BaasHandler<List<BaasDocument>>() {
                    @Override
                    public void handle(BaasResult<List<BaasDocument>> res) {
                        if (res.isSuccess()) {
                            for (BaasDocument doc : res.value()) {
                                String pref = doc.getString("Interests");
                                pref = pref.substring(1, pref.length() - 1);
                                String[] currInterests = pref.split(",", 4);
                                int matchPoints = 0;
                                int count = 0;
                                Log.d("myINTS", myInterests[2]);
                                Log.d("thINTS", currInterests[2]);
                                while(count < 3){
                                    if( currInterests[count] == myInterests[count]){
                                        matchPoints = matchPoints +1;
                                        count++;
                                    }
                                }
                                if ( matchPoints > 2){
                                    if(matchCount <= 3) {
                                        myMatches[matchCount] = doc.getAuthor();
                                    }
                                }
                            }
                        } else {
                        }
                    }
                });

        final MyInt passes = new MyInt(0);
        while (passes.getValue() < 3){

            BaasFile.fetchAll(filter, new BaasHandler<List<BaasFile>>() {
                @Override
                public void handle(BaasResult<List<BaasFile>> res) {
                    if (res.isSuccess()) {
                        for (BaasFile doc : res.value()) {

                            if(doc.getAuthor() == myMatches[passes.getValue()]){
                                Log.d("Pass", doc.getStreamUri().toString());
                                new DownloadImageTask((ImageButton) findViewById(R.id.imageButton))
                                        .execute(doc.getStreamUri().toString());
                            }
                        }
                    } else {
                        Log.e("LOG", "Error", res.error());
                    }
                }
            });

            passes.setValue(passes.getValue()+1);
        }





    }
}
