package ir.ac.ut.berim;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

public class RegisterActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    private LinearLayout mRegisterPan;

    private LinearLayout mActivatePan;

    private Button mRegisterButton;

    private Button mActivateButton;

    private EditText mActivateInput;

    private EditText mNickName;

    private EditText mPhoneNumber;

    private EditText mPassword;

    private Context mContext;

    private String mActivationCode, mUserId; //todo should be deleted

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = this;

        if (ProfileUtils.isLogin(this)) {
            Toast.makeText(mContext, "you are already logged in :|", Toast.LENGTH_SHORT)
                    .show();
            finish();
        }

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.please_wait));
        mProgressDialog.setMessage(getString(R.string.please_wait_more));

        mRegisterPan = (LinearLayout) findViewById(R.id.register_pan);
        mActivatePan = (LinearLayout) findViewById(R.id.activation_pan);

        mActivateInput = (EditText) findViewById(R.id.activation_input);
        mNickName = (EditText) findViewById(R.id.nickname);
        mPhoneNumber = (EditText) findViewById(R.id.phone_number);
        mPassword = (EditText) findViewById(R.id.password);

        mActivateButton = (Button) this.findViewById(R.id.button_activate);
        mRegisterButton = (Button) this.findViewById(R.id.button_register);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mNickName.getText().toString()) ||
                        TextUtils.isEmpty(mPassword.getText().toString()) ||
                        TextUtils.isEmpty(mPhoneNumber.getText().toString())) {
                    Toast.makeText(mContext, getString(R.string.fill_all_inputs), Toast.LENGTH_SHORT).show();
                    return;
                }
                mProgressDialog.show();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("nickName", mNickName.getText().toString());
                    jsonObject.put("password", mPassword.getText().toString());
                    jsonObject.put("phoneNumber", mPhoneNumber.getText().toString());
                } catch (JSONException e) {
                    return;
                }
                NetworkManager.sendRequest(MethodsName.SIGN_UP, jsonObject, new NetworkReceiver<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgressDialog.dismiss();
                                try {
                                    Toast.makeText(mContext, "activation code is: " + response
                                            .getString("activationCode"), Toast.LENGTH_SHORT).show();
                                    mActivationCode = response.getString("activationCode");
                                    mUserId = response.getString("id");
                                    mActivateInput.setText(mActivationCode);
                                    showActivationPan();
                                }catch (JSONException e){
                                    Toast.makeText(mContext, getString(R.string.an_error_occurred), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT)
                                        .show();
                                mProgressDialog.dismiss();
                                showRegisterPan();
                            }
                        });
                    }
                });

            }
        });

        mActivateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mActivateInput.getText().toString())) {
                    Toast.makeText(mContext, getString(R.string.fill_all_inputs), Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("activationCode", mActivateInput.getText().toString());
                    jsonObject.put("userId", mUserId);
                } catch (JSONException e) {
                    return;
                }
                NetworkManager.sendRequest(MethodsName.ACTIVE_USER, jsonObject, new NetworkReceiver<JSONObject>() {
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
                                Toast.makeText(mContext, getString(R.string.welcome), Toast.LENGTH_SHORT)
                                        .show();
                                mContext.startActivity(new Intent(mContext, MainActivity.class));
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onErrorResponse(final BerimNetworkException error) {
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void showRegisterPan(){
        mRegisterPan.setVisibility(View.VISIBLE);
        mActivatePan.setVisibility(View.GONE);
    }

    public void showActivationPan(){
        mRegisterPan.setVisibility(View.GONE);
        mActivatePan.setVisibility(View.VISIBLE);
    }
}
