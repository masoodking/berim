package ir.ac.ut.berim;

import android.app.Activity;
import android.os.Bundle;

import ir.ac.ut.widget.BerimHeader;

/**
 * Created by Masood on 1/18/2016 AD.
 */
public class BerimActivity extends Activity{

    public BerimHeader mBerimHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBerimHeader = (BerimHeader) findViewById(R.id.berim_header);
    }
}
