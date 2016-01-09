package ir.ac.ut.berim;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ir.ac.ut.adapter.PlaceReviewAdapter;
import ir.ac.ut.models.Review;
import ir.ac.ut.models.User;

public class TestScrollActivity extends AppCompatActivity {

    private View mToolbarView;

    private View mBackgroundImage;

    private ObservableListView listView;

    TextView mtextView;
    Context mContext;
    String temp;

    private View mStickyHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scroll);
        mContext = this;

        listView = (ObservableListView) findViewById(R.id.list);
        listView.setDivider(null);
        mStickyHeader = findViewById(R.id.placeHeaderMenuSticky);
        ArrayList<Review> items = new ArrayList<Review>();
        for (int i = 1; i <= 100; i++) {
            User u = new User("user"+i);
            items.add(new Review(u,"Review\nReview\nReview\nReview\n"+i));
        }
        PlaceReviewAdapter placeReviewAdapter = new PlaceReviewAdapter(mContext,items);
        listView.setAdapter(placeReviewAdapter);
        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup header = (ViewGroup) inflater.inflate(R.layout.place_header, listView,
                false);
        listView.addHeaderView(header, null, false);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount) {
                if (listView != null && listView.getChildAt(1) != null
                        && header.getBottom() <90) {//VERY STRANGE THINGS HAPPENING HERE, I DON'T WANT TO USE 90, BUT I WAS FORCED
                    mStickyHeader.setVisibility(View.VISIBLE);
                } else {
                    mStickyHeader.setVisibility(View.GONE);
                }
            }
        });
    }
}
