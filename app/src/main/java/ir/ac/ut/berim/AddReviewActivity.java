package ir.ac.ut.berim;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import ir.ac.ut.models.User;
import ir.ac.ut.network.BerimNetworkException;
import ir.ac.ut.network.MethodsName;
import ir.ac.ut.network.NetworkManager;
import ir.ac.ut.network.NetworkReceiver;

public class AddReviewActivity extends AppCompatActivity {

    private Button mSendButton;
    private Context mContext;
    private TextView placeName;
    private EditText userReview;
    private int placeId;
    private RatingBar mRatingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        mContext = this;
        placeId = getIntent().getIntExtra("id", -1);

        placeName = (TextView) findViewById(R.id.place_name);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        userReview = (EditText) findViewById(R.id.user_review);

        mSendButton = (Button) findViewById(R.id.send_button);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id",placeId);
                    jsonObject.put("rate", mRatingBar.getNumStars());
                    if (!TextUtils.isEmpty(userReview.getText().toString())) {
                        jsonObject.put("text",userReview.getText().toString());
                    }


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
                                        try {
                                            User user = User.createFromJson(response);
//                                            mNickName.setText(user.getNickName());
//                                            ImageLoader.getInstance()
//                                                    .display(user.getAvatar(), mAvatar, R.drawable.default_avatar);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onErrorResponse(BerimNetworkException error) {
                                ((Activity) mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext, getString(R.string.unsuccessful),
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
