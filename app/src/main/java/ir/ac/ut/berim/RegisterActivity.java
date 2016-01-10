package ir.ac.ut.berim;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ir.ac.ut.models.User;
import ir.ac.ut.network.BerimNetworkException;
import ir.ac.ut.network.MethodsName;
import ir.ac.ut.network.NetworkManager;
import ir.ac.ut.network.NetworkReceiver;

public class RegisterActivity extends AppCompatActivity {

    private Button mRegisterButton;

    private EditText mNickName;

    private EditText mPhoneNumber;

    private EditText mPassword;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = this;

        if(ProfileUtils.isLogin(this)){
            Toast.makeText(mContext, "you are already logged in :|", Toast.LENGTH_SHORT)
                    .show();
            finish();
        }
        mNickName = (EditText) findViewById(R.id.nickname);
        mPhoneNumber = (EditText) findViewById(R.id.phone_number);
        mPassword = (EditText) findViewById(R.id.password);

        mRegisterButton = (Button) this.findViewById(R.id.button_register);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mNickName.getText().toString()) ||
                        TextUtils.isEmpty(mPassword.getText().toString()) ||
                        TextUtils.isEmpty(mPhoneNumber.getText().toString())) {
                    Toast.makeText(mContext, "fill all inputs", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("nickName", mNickName.getText().toString());
                    jsonObject.put("password", mPassword.getText().toString());
                    jsonObject.put("phoneNumber", mPhoneNumber.getText().toString());
                } catch (JSONException e) {
                    return;
                }
                NetworkManager.sendRequest(MethodsName.SIGN_UP, jsonObject, new NetworkReceiver() {
                    @Override
                    public void onResponse(final Object response) {
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    User user = User.createFromJson((JSONObject) response);
                                    ProfileUtils.loginUser(mContext, user);
                                }catch (JSONException e){

                                }
                                Toast.makeText(mContext, "welcome ;)", Toast.LENGTH_SHORT)
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
}
