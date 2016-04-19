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
import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasFile;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasQuery;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;

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
                            //    int itt = 0;
                            //     while(itt != 1) {
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
                            //        itt = 1;
                            //      }
                        } else {
                        }
                    }
                });
    }
    void UploadResults(List y){

        clearDB();

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());




       // String listString = "";

        String newString = y.toString();

            BaasDocument doc = new BaasDocument("Preferences");
            doc.putString("Date", currentDate)
                    .putString("Author",BaasUser.current().getName().toString())
                    .putString("Interests", newString);
            doc.save(new BaasHandler<BaasDocument>() {
                @Override
                public void handle(BaasResult<BaasDocument> res) {
                    if (res.isSuccess()) {
                        Log.d("LOG", "Saved: " + res.value());
                    } else {
                    }
                }
            });

    }
    void PresentResults(MyInt x) {
        final TextView txt1 = (TextView) findViewById(R.id.txtView);
        txt1.setText(Integer.toString(x.getValue()));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);

        String fileId = "46c653ea-5955-451b-bd9a-d1d9c3b8a3b5";
        final TextView txt = (TextView) findViewById(R.id.test);
        BaasFile.fetch(fileId, new BaasHandler<BaasFile>() {
            @Override
            public void handle(BaasResult<BaasFile> res) {
                if (res.isSuccess()) {
                    byte[] data = res.value().getData();
                    String s = new String(data);
                    Log.d("LOG", "File received");
                    txt.setText(s);


                } else {
                    Log.e("LOG", "Error while streaming", res.error());
                    txt.setText("nope");
                }
            }
        });

        //Do testow uzywam randomowych zdjec
     /*   final String[] urlString = {    "http://performancecomms.com/wp-content/uploads/2014/02/Putin-Happy.jpg",
                                        "http://ipolitics.ca/wp-content/uploads/2014/12/Screen-Shot-2014-12-11-at-1.43.57-PM-125x125.png",
                                        "http://ep.yimg.com/ca/I/todofut_2271_12093767",
                                        "http://www.coastalcapitalwealth.com/news/wp-content/uploads/2014/03/Russia-125x125.png",
                                        "http://performancecomms.com/wp-content/uploads/2014/02/Putin-Neutral.jpg",
                                        "http://performancecomms.com/wp-content/uploads/2014/02/Putin-Sad.jpg",
                                        "http://shirtoid.com/wp-content/uploads/2014/03/scare-bear-sm.jpg",
                                        "http://www.bedtimebear.com/forums/image.php?u=742&dateline=1222878059",
                                        "http://ep.yimg.com/ca/I/todofut_2271_12093767"};

*/    // final List links = new ArrayList();
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



       /* BaasQuery.Criteria filter = BaasQuery.builder().pagination(0, 20)
                .orderBy("_creation_date desc")
                .where("_type='" + "series" + "'")
                .criteria();
*/
     /*   BaasDocument.fetchAll("Data",
                new BaasHandler<List<BaasDocument>>() {
                    @Override
                    public void handle(BaasResult<List<BaasDocument>> res) {
                        if (res.isSuccess()) {

                            for (BaasDocument doc : res.value()) {
                                String currLink = doc.getString("Link");
                                links.add(currLink);
                                String currPref = doc.getString("Extra");
                                pref.add(currPref);


                                Log.d("LOG", "Doc: " + doc);
                                break;
                            }

                            //  BaasDocument doc = res.value(data);
                        } else {
                            Log.e("LOG", "Doc: Tutaj");
                        }
                    }
                });





*/

        final MyInt passes = new MyInt(0);
        //final MyString series = new MyString("");
        final int numberOfEntries = 6;

        int val = 0;
        int temp = passes.getValue();
        new DownloadImageTask((ImageButton) findViewById(R.id.opt1))
                .execute(links.get(temp));
        new DownloadImageTask((ImageButton) findViewById(R.id.opt2))
                .execute(links.get(temp + 1));


        final ImageButton imgButt1 = (ImageButton) findViewById(R.id.opt1);
        final ImageButton imgButt2 = (ImageButton) findViewById(R.id.opt2);
        final Button upload = (Button) findViewById(R.id.button);

        imgButt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgButt1.isPressed()) {
                    int temp = passes.getValue();
                    if (temp > numberOfEntries -2) {
                        imgButt1.setEnabled(false);
                        imgButt2.setEnabled(false);
                        upload.setEnabled(true);
                    } else {
                        //
                        result.add(preferences.get(temp).toString());
                        temp = temp +2;
                        passes.setValue(temp);
                        new DownloadImageTask((ImageButton) findViewById(R.id.opt1))
                                .execute(links.get(temp).toString());
                        new DownloadImageTask((ImageButton) findViewById(R.id.opt2))
                                .execute(links.get(temp+1).toString());
                       // temp = temp + 2;
                       // passes.setValue(temp);
                    }
                }
            }
        });
        imgButt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgButt2.isPressed()) {
                    int temp = passes.getValue();
                    if (temp > numberOfEntries -2) {
                        imgButt1.setEnabled(false);
                        imgButt2.setEnabled(false);
                        upload.setEnabled(true);
                    } else {
                        //
                        result.add(preferences.get(temp+1).toString());
                        temp = temp +2;
                        passes.setValue(temp);
                        new DownloadImageTask((ImageButton) findViewById(R.id.opt1))
                                .execute(links.get(temp).toString());
                        new DownloadImageTask((ImageButton) findViewById(R.id.opt2))
                                .execute(links.get(temp+1).toString());
                      //  temp = temp + 2;
                       // passes.setValue(temp);
                    }
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

}
