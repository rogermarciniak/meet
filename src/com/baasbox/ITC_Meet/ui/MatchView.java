package com.baasbox.ITC_Meet.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.baasbox.ITC_Meet.R;
import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasFile;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasQuery;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;

import java.io.InputStream;
import java.util.List;

/**
 * @author:
 * Bartosz Zurawski(c00165634)
 */

public class MatchView extends Activity {

    final MyInt req = new MyInt(0);

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
                resizedBitmap = Bitmap.createScaledBitmap(mIcon11, 350, 350, false);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return resizedBitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    void clearDB(){
        BaasQuery.Criteria filter = BaasQuery.builder().pagination(0, 20)
                .orderBy("_creation_date desc")
                .where("_author='" + BaasUser.current().getName() + "'")
                .criteria();


        BaasFile.fetchAll(filter, new BaasHandler<List<BaasFile>>() {
            @Override
            public void handle(BaasResult<List<BaasFile>> res) {
                if (res.isSuccess()) {
                    for (BaasFile doc : res.value()) {
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
    void newActiv(){
        Intent intent = new Intent(this,Minigame.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    void onChat(String userName){
        Intent intent = new Intent(this,Chat.class);
        intent.putExtra("userName", userName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_view);
        final String pkgn = getIntent().getExtras().getString("userName");

        BaasQuery.Criteria filter = BaasQuery.builder().pagination(0, 20)
                .orderBy("_creation_date desc")
                .where("_author='" + pkgn + "'")
                .criteria();

        BaasFile.fetchAll(filter, new BaasHandler<List<BaasFile>>() {
            @Override
            public void handle(BaasResult<List<BaasFile>> res) {
                if (res.isSuccess()) {
                    for (BaasFile doc : res.value()) {
                        Log.d("Pass", doc.getStreamUri().toString());
                        new DownloadImageTask((ImageButton) findViewById(R.id.profPic))
                                .execute(doc.getStreamUri().toString());

                        break;
                    }
                } else {
                    Log.e("LOG", "Error", res.error());
                }
            }
        });



        final TextView txt1 = (TextView) findViewById(R.id.userName);
        txt1.setText(pkgn);
        txt1.setTextColor(Color.parseColor("#CDDC39"));

        final TextView inte1 = (TextView) findViewById(R.id.textView5);
        final TextView inte2 = (TextView) findViewById(R.id.textView6);
        final TextView inte3 = (TextView) findViewById(R.id.textView7);

        BaasQuery.Criteria filter2 = BaasQuery.builder().pagination(0, 20)
                .orderBy("_creation_date desc")
                .where("_author='" + BaasUser.current().getName() + "'")
                .criteria();


        BaasDocument.fetchAll("Preferences", filter2,
                new BaasHandler<List<BaasDocument>>() {
                    @Override
                    public void handle(BaasResult<List<BaasDocument>> res) {
                        if (res.isSuccess()) {

                            for (BaasDocument doc : res.value()) {
                                String pref = doc.getString("Interests");
                                pref = pref.substring(1, pref.length() - 1);
                                String[] parts = pref.split(",", 4);
                                inte1.setText(parts[0]);
                                inte2.setText(parts[1]);
                                inte3.setText(parts[2]);
                            }


                        } else {
                            newActiv();
                        }
                    }
                });
        final ImageButton pic = (ImageButton) findViewById(R.id.imageButton);
        final Button chatBtn = (Button) findViewById(R.id.sendMessageButton);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChat(pkgn);
            }

        });
     /*   pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int PICK_IMAGE_REQUEST = 1;
                req.setValue(1);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }

        });
    }
    @Override
    protected void onActivityResult(final int requestCode,final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Main Activity", "On Activity Result");



        try {
            Uri selectedImage=null;
            if (requestCode == req.getValue() && resultCode == RESULT_OK && null != data) {
                Log.d("Main Activity", "Gallery");
                selectedImage = data.getData();
            }

            if(selectedImage==null)
            {
                Log.d("Main Activity", "Back");
                return;
            }

            Log.d("Main Activity","Out");
            String[] filePathColumn = {MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            Log.d("Main Activity",picturePath);
            cursor.close();
            Bitmap bmp=BitmapFactory.decodeFile(picturePath);
            Bitmap resized = Bitmap.createScaledBitmap(bmp, 250, 250, true);
            final ImageButton pic = (ImageButton) findViewById(R.id.imageButton);
            pic.setImageBitmap(resized);

            clearDB();
            //raz raz

            File imgToUpload = new File(picturePath);
            BaasFile baasFile = new BaasFile();
            baasFile.upload(BaasACL.grantRole(Role.REGISTERED, Grant.READ), imgToUpload, new BaasHandler<BaasFile>() {
                @Override
                public void handle(BaasResult<BaasFile> baasResult) {
                    if (baasResult.isSuccess()) {
                        Log.d("Main","file successfully uploaded");
                        //...
                    } else {
                        Log.e("Main", "error in uploading file: " + baasResult.error());
                    }
                }
            });

        } catch (Exception e) {
            Log.d("Main Activity", "Exception");
            //req.setValue(0);
        }

*/
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


}