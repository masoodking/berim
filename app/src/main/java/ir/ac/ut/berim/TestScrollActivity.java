package ir.ac.ut.berim;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
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

public class TestScrollActivity extends AppCompatActivity {

    private ObservableListView mListView;

    public View.OnClickListener mMapClickListener;

    Context mContext;

    Place mPlace;

    TextView mPlaceDescription;

    TextView mPlaceName;

    TextView mPlaceAddress;

    ImageButton mMap;

    FloatingActionButton mBerimFAB;

    private View mStickyHeader;

    private Button mAddReview;

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
                mPlace.getReviews(), mPlace.getDescription());
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

        mMap = (ImageButton) header.findViewById(R.id.image_map);

        mMapClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeUri = "google.navigation:q=" + mPlace.getLatitude() + "," + mPlace
                        .getLongitude();
                Uri gmmIntentUri = Uri.parse(placeUri);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                startActivity(mapIntent);
            }
        };

        mMap.setOnClickListener(mMapClickListener);
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
