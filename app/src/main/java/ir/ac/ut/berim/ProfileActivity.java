package ir.ac.ut.berim;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ir.ac.ut.network.BerimNetworkException;
import ir.ac.ut.network.MethodsName;
import ir.ac.ut.network.NetworkManager;
import ir.ac.ut.network.NetworkReceiver;

public class ProfileActivity extends AppCompatActivity {

    private Button logoutButton;

    private Context mContext;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_profile);
        logoutButton = (Button) findViewById(R.id.logout_button);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.please_wait));
        mProgressDialog.setMessage(getString(R.string.please_wait_more));

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

}
