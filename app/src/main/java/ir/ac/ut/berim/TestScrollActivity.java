package ir.ac.ut.berim;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.ac.ut.adapter.PlaceReviewAdapter;
import ir.ac.ut.models.Place;
import ir.ac.ut.models.Review;
import ir.ac.ut.utils.DimensionUtils;

public class TestScrollActivity extends AppCompatActivity {

    private View mToolbarView;

    private View mBackgroundImage;

    private ObservableListView mListView;

    TextView mtextView;

    Context mContext;

    Place mPlace;

    TextView mPlaceDescription;
    TextView mPlaceName;
    TextView mPlaceAddress;

    private View mStickyHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scroll);
        mContext = this;
        mPlace = (Place) getIntent().getSerializableExtra("place");

        mListView = (ObservableListView) findViewById(R.id.list);
        mListView.setDivider(null);
        mStickyHeader = findViewById(R.id.placeHeaderMenuSticky);
//todo clean this shit:
        ArrayList<Review> reviews = new ArrayList<>();
        Review r = new Review();
        r.setUser(ProfileUtils.getUser(mContext));
        r.setDescription("این را سعید نهاد");
        reviews.add(r);
        mPlace.setReviews(reviews);
//until here

        PlaceReviewAdapter placeReviewAdapter = new PlaceReviewAdapter(mContext,
                mPlace.getReviews());
        mListView.setAdapter(placeReviewAdapter);

        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup header = (ViewGroup) inflater.inflate(R.layout.place_header, mListView,
                false);

        mPlaceDescription = (TextView) header.findViewById(R.id.placeDescription);
        mPlaceDescription.setText(mPlace.getDescription());

        mPlaceName = (TextView) header.findViewById(R.id.PlaceName);
        mPlaceName.setText(mPlace.getName());

        mPlaceAddress = (TextView) header.findViewById(R.id.PlaceLocation);
        mPlaceAddress.setText(mPlace.getAddress());



        mListView.addHeaderView(header, null, false);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount) {
                if (mListView != null && mListView.getChildAt(1) != null
                        && header.getBottom() < DimensionUtils.convertDpToPx(mContext, 60)) {
                    mStickyHeader.setVisibility(View.VISIBLE);
                } else {
                    mStickyHeader.setVisibility(View.GONE);
                }
            }
        });
    }
}
