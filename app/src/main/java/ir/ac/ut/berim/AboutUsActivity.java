package ir.ac.ut.berim;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import ir.ac.ut.adapter.PlacesListAdapter;
import ir.ac.ut.models.Place;

public class AboutUsActivity extends AppCompatActivity {

    Context mContext;
    ListView mListView;
    Place[] we;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

    }
}
