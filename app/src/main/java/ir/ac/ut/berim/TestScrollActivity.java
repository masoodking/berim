package ir.ac.ut.berim;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;

import ir.ut.ac.widget.ViewPager;

public class TestScrollActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    private View mToolbarView;
    private View mBackgroundImage;
    private ObservableListView listView;
    TextView mtextView;
    String temp;
    private View mStickyHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scroll);

        listView = (ObservableListView) findViewById(R.id.list);
        mStickyHeader = findViewById(R.id.placeHeaderMenuSticky);
        listView.setScrollViewCallbacks(this);
        ArrayList<String> items = new ArrayList<String>();
        for (int i = 1; i <= 100; i++) {
            items.add("Item " + i);
        }
        listView.setAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, items));
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.place_header, listView,
                false);
        listView.addHeaderView(header, null, false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if(listView.getChildAt(1).getTop()<85)
            mStickyHeader.setVisibility(View.VISIBLE);
        else
            mStickyHeader.setVisibility(View.GONE);

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
