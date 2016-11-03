package co.cristiangarcia.hdtransmileniopro;

import android.os.Bundle;

import org.kxml2.wap.Wbxml;

/**
 * Created by cristiangarcia on 26/10/16.
 */

public class SplashScreen extends BaseSplash {
    protected void onCreate(Bundle savedInstanceState) {
        set_appID(getResources().getInteger(R.integer.appID));
        setMainClass(MainActivity.class);
       // setRegistrationIntentService(RegistrationIntentService.class);
        //setClouds(R.mipmap.ic_launcher);
      //  setBuildings(R.drawable.buildings);
      //  setBackgroundFloorSplash(R.color.backgroundFloorSplash);
       // setRoad(R.drawable.road);
        setSplashLogo(R.drawable.splash_logo);
        setBackgroundSplash(R.color.backgroundSplash);
        set_iVersionApp(Wbxml.LITERAL_AC);
        super.onCreate(savedInstanceState);
    }
}
