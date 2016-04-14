package com.baasbox.ITC_Meett.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    void UploadResults(MyInt x){
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());





        BaasDocument doc = new BaasDocument("Preferences");
        doc.putString("Date",currentDate)
                .putString("Author", BaasUser.current().getName().toString())
                .putString("Interests", Integer.toString(x.getValue()));
        doc.save(new BaasHandler<BaasDocument>() {
            @Override
            public void handle(BaasResult<BaasDocument> res) {
                if (res.isSuccess()) {
                    Log.d("LOG", "Saved: " + res.value());
                } else { }
            }
        });
    }

    void presentResults(MyInt x) {
        final TextView txt1 = (TextView) findViewById(R.id.txtView);
        txt1.setText(Integer.toString(x.getValue()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);

        //Do testow uzywam randomowych zdjec
        final String[] urlString = {    "http://performancecomms.com/wp-content/uploads/2014/02/Putin-Happy.jpg",
                                        "http://vps.yatsu.eu:9000/file/f93b39e6-b1fd-4116-9e78-26e84ccc25ef?X-BB-SESSION=f6300162-6f1c-420b-8686-8d9f03da1058&X-BAASBOX-APPCODE=1234567890",
                                        "http://ep.yimg.com/ca/I/todofut_2271_12093767",
                                        "http://www.coastalcapitalwealth.com/news/wp-content/uploads/2014/03/Russia-125x125.png",
                                        "http://performancecomms.com/wp-content/uploads/2014/02/Putin-Neutral.jpg",
                                        "http://performancecomms.com/wp-content/uploads/2014/02/Putin-Sad.jpg",
                                        "http://shirtoid.com/wp-content/uploads/2014/03/scare-bear-sm.jpg",
                                        "http://www.bedtimebear.com/forums/image.php?u=742&dateline=1222878059",
                                        "http://ep.yimg.com/ca/I/todofut_2271_12093767"};

        final MyInt passes = new MyInt(2);
        final MyInt seriesResult = new MyInt(0);
        final int numberOfEntries = 6;

        new DownloadImageTask((ImageButton) findViewById(R.id.opt1))
                .execute(urlString[0]);
        new DownloadImageTask((ImageButton) findViewById(R.id.opt2))
                .execute(urlString[1]);

        final ImageButton imgButt1 = (ImageButton) findViewById(R.id.opt1);
        final ImageButton imgButt2 = (ImageButton) findViewById(R.id.opt2);
        final Button upload = (Button) findViewById(R.id.button);
        final Button ret = (Button) findViewById(R.id.button2);
        imgButt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgButt1.isPressed()) {
                    int temp = passes.getValue();
                    int seriesRes = seriesResult.getValue();
                    if (temp > numberOfEntries) {
                        presentResults(seriesResult);
                        imgButt1.setEnabled(false);
                        imgButt2.setEnabled(false);
                        upload.setEnabled(true);
                    } else {
                        if (seriesRes == 0) {
                            seriesRes = seriesRes + 1;
                            seriesResult.setValue(seriesRes);
                        } else {
                            seriesRes = (seriesRes * 10) + 1;
                            seriesResult.setValue(seriesRes);
                        }
                        new DownloadImageTask((ImageButton) findViewById(R.id.opt1))
                                .execute(urlString[temp]);
                        new DownloadImageTask((ImageButton) findViewById(R.id.opt2))
                                .execute(urlString[temp + 1]);
                        temp = temp + 2;
                        passes.setValue(temp);
                    }
                }
            }
        });
        imgButt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgButt2.isPressed()) {
                    int temp = passes.getValue();
                    int seriesRes = seriesResult.getValue();
                    if (temp > numberOfEntries) {
                        presentResults(seriesResult);
                        imgButt1.setEnabled(false);
                        imgButt2.setEnabled(false);
                        upload.setEnabled(true);
                    } else {
                        if (seriesRes == 0) {
                            seriesRes = seriesRes + 2;
                            seriesResult.setValue(seriesRes);
                        } else {
                            seriesRes = (seriesRes * 10) + 2;
                            seriesResult.setValue(seriesRes);
                        }
                        new DownloadImageTask((ImageButton) findViewById(R.id.opt1))
                                .execute(urlString[temp]);
                        new DownloadImageTask((ImageButton) findViewById(R.id.opt2))
                                .execute(urlString[temp + 1]);
                        temp = temp + 2;
                        passes.setValue(temp);
                    }
                }
            }
        });
        upload.setEnabled(false);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadResults(seriesResult);
            }
        });
        ret.setEnabled(false);
        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
