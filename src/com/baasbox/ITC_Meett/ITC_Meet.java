package com.baasbox.ITC_Meett;

import android.app.Application;

import com.baasbox.android.*;

/**
 * @authors:
 * Roger Marciniak (c00169733)
 * Bartosz Zurawski(c00165634)
 */
public class ITC_Meet extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        BaasBox.builder(this).setAuthentication(BaasBox.Config.AuthType.SESSION_TOKEN)
                .setApiDomain("vps.yatsu.eu")
                .setPort(9000)
                .setAppCode("1234567890")
                .init();
    }

}
