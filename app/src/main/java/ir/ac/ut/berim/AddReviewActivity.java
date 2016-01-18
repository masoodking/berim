package ir.ac.ut.berim;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import ir.ac.ut.network.BerimNetworkException;
import ir.ac.ut.network.MethodsName;
import ir.ac.ut.network.NetworkManager;
import ir.ac.ut.network.NetworkReceiver;

public class AddReviewActivity extends BerimActivity {

    private Button mSendButton;

    private Context mContext;

    private TextView placeNameTextView;

    private EditText userReview;

    private String placeId;

    private String mPlaceName;

    private RatingBar mRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        mContext = this;
        placeId = getIntent().getStringExtra("id");
        mPlaceName = getIntent().getStringExtra("name");

        placeNameTextView = (TextView) findViewById(R.id.place_name);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        userReview = (EditText) findViewById(R.id.user_review);

        placeNameTextView.setText("ثبت نظر برای «" + mPlaceName + "«");

        mSendButton = (Button) findViewById(R.id.send_button);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("placeId", placeId);
                    jsonObject.put("rate", mRatingBar.getRating());
                    jsonObject.put("text", userReview.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                NetworkManager.sendRequest(MethodsName.ADD_REVIEW, jsonObject,
                        new NetworkReceiver<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                ((Activity) mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext, getString(R.string.successful),
                                                Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                });
                            }

                            @Override
                            public void onErrorResponse(final BerimNetworkException error) {
                                ((Activity) mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext,
                                                getString(R.string.unsuccessful) + ": " + error
                                                        .getMessage(),
                                                Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                });
                            }
                        });
            }
        });
    }
}
