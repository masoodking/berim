package ir.ac.ut.berim;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import ir.ac.ut.models.User;
import ir.ac.ut.network.BerimNetworkException;
import ir.ac.ut.network.MethodsName;
import ir.ac.ut.network.NetworkManager;
import ir.ac.ut.network.NetworkReceiver;
import ir.ac.ut.utils.EncryptionUtils;

public class RegisterActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    private LinearLayout mRegisterPan;

    private LinearLayout mActivatePan;

    private Button mRegisterButton;

    private Button mActivateButton;

    private EditText mActivateInput;

    private EditText mPhoneNumber;

    private String DEVICE_ID;

    private Context mContext;

    private String mActivationCode; //todo should be deleted

    private NetworkReceiver mNetworkReceiver = new NetworkReceiver<JSONObject>() {
        @Override
        public void onResponse(final JSONObject response) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.dismiss();
                    try {
                        if (response.has("activationCode")) {
                            Toast.makeText(mContext,
                                    "activation code is: " + response
                                            .getString("activationCode"),
                                    Toast.LENGTH_SHORT)
                                    .show();
                            mActivationCode = response
                                    .getString("activationCode");
                            mActivateInput.setText(mActivationCode);
                            showActivationPan();
                        } else {
                            User user = User.createFromJson(response);
                            ProfileUtils.loginUser(mContext, user);
                            Toast.makeText(mContext,
                                    getString(R.string.welcome),
                                    Toast.LENGTH_SHORT)
                                    .show();
                            mContext.startActivity(
                                    new Intent(mContext, MainActivity.class));
                            finish();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(mContext,
                                getString(R.string.an_error_occurred),
                                Toast.LENGTH_SHORT).show();
                        showRegisterPan();
                    }
                }
            });

        }

        @Override
        public void onErrorResponse(final BerimNetworkException error) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (error.getErrorCode() != BerimNetworkException.CONNECTION_LOST_ERROR_CODE) {
                        Toast.makeText(mContext, error.getMessage(),
                                Toast.LENGTH_SHORT)
                                .show();
                        mProgressDialog.dismiss();
                        showRegisterPan();
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        try {
            EncryptionUtils encryptionUtils = new EncryptionUtils(EncryptionUtils.MAIN_KEY.getBytes(),
                    EncryptionUtils.IV_KEY.getBytes());
            String secretMsg = "سلام من متن هستنم";
            byte[] enc = encryptionUtils.encrypt(secretMsg);
            Log.wtf("####encrypted: ", enc.toString());
            Object dec = encryptionUtils.decrypt(enc);
            Log.wtf("####decrypted: ", dec.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mContext = this;

        DEVICE_ID = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.please_wait));
        mProgressDialog.setMessage(getString(R.string.please_wait_more));
        mProgressDialog.setCancelable(true);

        mRegisterPan = (LinearLayout) findViewById(R.id.register_pan);
        mActivatePan = (LinearLayout) findViewById(R.id.activation_pan);

        mActivateInput = (EditText) findViewById(R.id.activation_input);
        mPhoneNumber = (EditText) findViewById(R.id.phone_number);

        mActivateButton = (Button) this.findViewById(R.id.button_activate);
        mRegisterButton = (Button) this.findViewById(R.id.button_register);

        if (ProfileUtils.isLogin(this)) {
            mProgressDialog.show();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("deviceId", DEVICE_ID);
                jsonObject.put("phoneNumber", ProfileUtils.getUser(this).getPhoneNumber());
            } catch (JSONException e) {
                return;
            }
            NetworkManager.sendRequest(MethodsName.LOGIN, jsonObject, mNetworkReceiver);
        }

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mPhoneNumber.getText().toString())) {
                    Toast.makeText(mContext, getString(R.string.fill_all_inputs),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                mProgressDialog.show();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("phoneNumber", mPhoneNumber.getText().toString());
                    jsonObject.put("deviceId", DEVICE_ID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                NetworkManager.sendRequest(MethodsName.LOGIN, jsonObject, mNetworkReceiver);

            }
        });

        mActivateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mActivateInput.getText().toString())) {
                    Toast.makeText(mContext, getString(R.string.fill_all_inputs),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("activationCode", mActivateInput.getText().toString());
                    jsonObject.put("phoneNumber", mPhoneNumber.getText());
                    jsonObject.put("deviceId", DEVICE_ID);
                } catch (JSONException e) {
                    return;
                }
                NetworkManager.sendRequest(MethodsName.ACTIVE_USER, jsonObject,
                        new NetworkReceiver<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                ((Activity) mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            User user = User.createFromJson(response);
                                            ProfileUtils.loginUser(mContext, user);
                                        } catch (JSONException e) {
                                        }
                                        Toast.makeText(mContext, getString(R.string.welcome),
                                                Toast.LENGTH_SHORT)
                                                .show();
                                        mContext.startActivity(
                                                new Intent(mContext, MainActivity.class));
                                        finish();
                                    }
                                });
                            }

                            @Override
                            public void onErrorResponse(final BerimNetworkException error) {
                                ((Activity) mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext, error.getMessage(),
                                                Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                });
                            }
                        });
            }
        });
    }

    public void showRegisterPan() {
        mRegisterPan.setVisibility(View.VISIBLE);
        mActivatePan.setVisibility(View.GONE);
    }

    public void showActivationPan() {
        mRegisterPan.setVisibility(View.GONE);
        mActivatePan.setVisibility(View.VISIBLE);
    }
}
