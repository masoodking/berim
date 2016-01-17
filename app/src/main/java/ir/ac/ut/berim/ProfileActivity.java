package ir.ac.ut.berim;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ir.ac.ut.network.BerimNetworkException;
import ir.ac.ut.network.MethodsName;
import ir.ac.ut.network.NetworkManager;
import ir.ac.ut.network.NetworkReceiver;

public class ProfileActivity extends AppCompatActivity {

    private Button logoutButton;

    private Context mContext;

    private ProgressDialog mProgressDialog;
    private TextView mMobile;
    private TextView mNickName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_profile);
        logoutButton = (Button) findViewById(R.id.logout_button);
        mMobile = (TextView) findViewById(R.id.phone_textView);
        mNickName = (TextView) findViewById(R.id.nickname_textView);
        mMobile.setText(ProfileUtils.getUser(mContext).getPhoneNumber());

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.please_wait));
        mProgressDialog.setMessage(getString(R.string.please_wait_more));

        if(ProfileUtils.getUser(mContext).getNickName().equals("null")|| TextUtils.isEmpty(ProfileUtils.getUser(mContext).getNickName()))
            mNickName.setText(R.string.no_nickname);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    logout();
                } catch (JSONException e) {
                    mProgressDialog.dismiss();
                    Toast.makeText(mContext, getString(R.string.cant_logout), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        mNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle(R.string.change_nickname);
                final EditText input = new EditText (mContext);
                alert.setView(input);

                alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mNickName.setText(input.getText().toString());

                    }
                });

                alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });
                alert.show();
            }
        });
    }


    public void logout() throws JSONException {
        mProgressDialog.show();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phoneNumber", ProfileUtils.getUser(mContext).getPhoneNumber());
        jsonObject.put("deviceId", Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID));
        NetworkManager.sendRequest(MethodsName.LOGOUT, jsonObject, new NetworkReceiver() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onResponse(Object response) {
                mProgressDialog.dismiss();
                ProfileUtils.logoutUser(mContext);//todo do we need this?
                Intent intent = new Intent(mContext, RegisterActivity.class);
                if (Build.VERSION.SDK_INT > 10) {
                    intent.addFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }
                mContext.startActivity(intent);
                finish();
            }

            @Override
            public void onErrorResponse(BerimNetworkException error) {
                mProgressDialog.dismiss();
                Toast.makeText(mContext, getString(R.string.cant_logout), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
    public void editProfile(String nickname, String avatar){
        JSONObject jsonObject = new JSONObject();
        try {
            if(!TextUtils.isEmpty(nickname))
                jsonObject.put("nickName",nickname);
            if(!TextUtils.isEmpty(avatar))
                jsonObject.put("avatar",avatar);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkManager.sendRequest(MethodsName.EDIT_PROFILE, jsonObject, new NetworkReceiver<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(mContext, getString(R.string.profile_updated), Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onErrorResponse(BerimNetworkException error) {
                Toast.makeText(mContext, getString(R.string.unsuccessful), Toast.LENGTH_SHORT)
                        .show();
            }
        });

    }
}
