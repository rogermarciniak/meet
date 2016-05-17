package com.baasbox.ITC_Meett.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

//import com.baasbox.ITC_Meett.Serie;
import com.baasbox.android.*;
import com.baasbox.ITC_Meett.R;

import java.util.List;

/**
 * @authors:
 * Roger Marciniak (c00169733)
 * Bartosz Zurawski(c00165634)
 */
public class NoteListActivity extends Activity{
    private final static String REFRESH_TOKEN_KEY = "refresh";
    private final static String SAVING_TOKEN_KEY = "saving";
    private final static String LOGOUT_TOKEN_KEY = "logout";

    private final static int EDIT_CODE = 1;

    private boolean mUseTwoPane;
    private RequestToken mRefresh;
    private RequestToken mSaving;

    private ProgressDialog mDialog;
    private boolean mDoRefresh=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //todo 2
        if (BaasUser.current() == null){
            startLoginScreen();
            return;
        }
        else{
            Intent intent = new Intent(this,MainScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }


    }


    private void startLoginScreen(){
        mDoRefresh = false;
        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }





}
