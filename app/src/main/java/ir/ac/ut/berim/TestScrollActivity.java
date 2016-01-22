package ir.ac.ut.berim;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ir.ac.ut.adapter.PlaceReviewAdapter;
import ir.ac.ut.models.Place;
import ir.ac.ut.models.Review;
import ir.ac.ut.models.Room;
import ir.ac.ut.network.BerimNetworkException;
import ir.ac.ut.network.MethodsName;
import ir.ac.ut.network.NetworkManager;
import ir.ac.ut.network.NetworkReceiver;
import ir.ac.ut.utils.DimensionUtils;
import ir.ac.ut.utils.ImageLoader;

public class TestScrollActivity extends AppCompatActivity {

    private ObservableListView mListView;

    public View.OnClickListener mMapClickListener;
    public View.OnClickListener shareClickListener;

    Context mContext;

    Place mPlace;

    TextView mPlaceDescription;

    TextView mPlaceName;

    TextView mPlaceAddress;
    TextView mPlaceRate;
    RatingBar mRatingBar;

    ImageButton mMap;
    ImageButton mShare;

    FloatingActionButton mBerimFAB;

    private View mStickyHeader;

    private ImageView background;
    private Button mAddReview;
    private TextView mPlaceNameSticky;
    private TextView mPlaceAddressSticky;
    private TextView mPlaceRateSticky;
    private RatingBar mRatingBarSticky;
    private ImageButton mShareSticky;
    private ImageButton mMapSticky;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scroll);
        mContext = this;
        mPlace = (Place) getIntent().getSerializableExtra("place");

        mBerimFAB = (FloatingActionButton) findViewById(R.id.berim_fab);

        mListView = (ObservableListView) findViewById(R.id.list);
        mListView.setDivider(null);
        mStickyHeader = findViewById(R.id.placeHeaderMenuSticky);

        final PlaceReviewAdapter placeReviewAdapter = new PlaceReviewAdapter(mContext,
                mPlace.getReviews(), mPlace.getDescription(), mPlace.getAddress());
        mListView.setAdapter(placeReviewAdapter);

        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup header = (ViewGroup) inflater.inflate(R.layout.place_header, mListView,
                false);

        mPlaceRate = (TextView) header.findViewById(R.id.placeRate);
        mPlaceRate.setText(""+mPlace.getRate());
        mRatingBar = (RatingBar) header.findViewById(R.id.placeRateStars);
        if(mPlace.getRate()>=0)
            mRatingBar.setRating(mPlace.getRate());
        LayerDrawable stars = (LayerDrawable) mRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        mPlaceRateSticky = (TextView) mStickyHeader.findViewById(R.id.placeRate);
        mPlaceRateSticky.setText("" + mPlace.getRate());
        mRatingBarSticky = (RatingBar) header.findViewById(R.id.placeRateStars);
        if(mPlace.getRate()>=0)
            mRatingBarSticky.setRating(mPlace.getRate());
        LayerDrawable starsSticky = (LayerDrawable) mRatingBar.getProgressDrawable();
        starsSticky.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        mPlaceName = (TextView) header.findViewById(R.id.PlaceName);
        mPlaceName.setText(mPlace.getName());
        mPlaceNameSticky = (TextView) mStickyHeader.findViewById(R.id.PlaceName);
        mPlaceNameSticky.setText(mPlace.getName());

        mPlaceAddress = (TextView) header.findViewById(R.id.PlaceLocation);
        mPlaceAddress.setText(mPlace.getAddress());
        mPlaceAddressSticky = (TextView) mStickyHeader.findViewById(R.id.PlaceLocation);
        mPlaceAddressSticky.setText(mPlace.getAddress());

        mMap = (ImageButton) header.findViewById(R.id.image_map);
        mShare = (ImageButton) header.findViewById(R.id.image_share);
        mMapSticky = (ImageButton) mStickyHeader.findViewById(R.id.image_map);
        mShareSticky = (ImageButton) mStickyHeader.findViewById(R.id.image_share);

        background = (ImageView) header.findViewById(R.id.place_background);

        ImageLoader.getInstance()
                .display(mPlace.getAvatar(),background,
                        R.drawable.no_photo);

        shareClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = mPlace.getDescription();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "بریم "+mPlace.getName());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        };
        mMapClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeUri = "google.navigation:q=" + mPlace.getName();
                Uri gmmIntentUri = Uri.parse(placeUri);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                startActivity(mapIntent);
            }
        };

        mMap.setOnClickListener(mMapClickListener);
        mShare.setOnClickListener(shareClickListener);
        mMapSticky.setOnClickListener(mMapClickListener);
        mShareSticky.setOnClickListener(shareClickListener);
        mListView.addHeaderView(header, null, false);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount) {
                if (mListView != null
                        && header.getBottom() < DimensionUtils.convertDpToPx(mContext, 60)) {
                    mStickyHeader.setVisibility(View.VISIBLE);
                } else {
                    mStickyHeader.setVisibility(View.GONE);
                }
            }
        });

        mBerimFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewBerim();
            }
        });

        mAddReview = (Button) findViewById(R.id.add_review);
        mAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddReviewActivity.class);
                intent.putExtra("id", mPlace.getId());
                intent.putExtra("name", mPlace.getName());
                mContext.startActivity(intent);
            }
        });
    }

    public void createNewBerim() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "بریم " + mPlace.getName());
            jsonObject.put("placeId", mPlace.getId());
            jsonObject.put("maxUserCount", 50);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkManager.sendRequest(MethodsName.ADD_ROOM, jsonObject,
                new NetworkReceiver<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Room room = Room.createFromJson(response);
                            Intent intent = new Intent(mContext, RoomActivity.class);
                            intent.putExtra("room", room);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onErrorResponse(BerimNetworkException error) {

                    }
                });
    }
}
