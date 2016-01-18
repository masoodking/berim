package ir.ac.ut.berim;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ir.ac.ut.models.User;
import ir.ac.ut.network.BerimNetworkException;
import ir.ac.ut.network.MethodsName;
import ir.ac.ut.network.NetworkManager;
import ir.ac.ut.network.NetworkReceiver;

/**
 * Created by saeed on 1/17/2016.
 */
public class AddPlaceActivity extends BerimActivity {

    private Context mContext;

    private Button mSubmit;

    private TextView mName;

    private TextView mAddress;

    private TextView mDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        mContext = this;

        mName = (TextView) findViewById(R.id.place_name);
        mAddress = (TextView) findViewById(R.id.place_address);
        mDescription = (TextView) findViewById(R.id.place_desc);

        mSubmit = (Button) findViewById(R.id.button);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    if (!TextUtils.isEmpty(mName.getText().toString())) {
                        jsonObject.put("name", mName.getText().toString());
                    }
                    if (!TextUtils.isEmpty(mAddress.getText().toString())) {
                        jsonObject.put("address", mAddress.getText().toString());
                    }
                    if (!TextUtils.isEmpty(mDescription.getText().toString())) {
                        jsonObject.put("description", mDescription.getText().toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                NetworkManager.sendRequest(MethodsName.EDIT_PROFILE, jsonObject,
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

    @Override
    protected void onResume() {
        super.onResume();
        mBerimHeader.setTitle(getString(R.string.title_activity_add_place));
    }
}
