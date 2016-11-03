package co.cristiangarcia.hdtransmileniopro;
import android.os.Build.VERSION;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by cristiangarcia on 30/10/16.
 */


public class Browser extends BaseMovilixaBrowser {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (VERSION.SDK_INT >= 21) {
            getWindow().addFlags(LinearLayoutManager.INVALID_OFFSET);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }
}
