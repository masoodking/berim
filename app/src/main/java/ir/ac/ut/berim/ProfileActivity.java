package ir.ac.ut.berim;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;
import com.nononsenseapps.filepicker.FilePickerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import ir.ac.ut.models.User;
import ir.ac.ut.network.BerimNetworkException;
import ir.ac.ut.network.MethodsName;
import ir.ac.ut.network.NetworkManager;
import ir.ac.ut.network.NetworkReceiver;
import ir.ac.ut.utils.ImageLoader;

public class ProfileActivity extends AppCompatActivity {

    private Context mContext;

    private ProgressDialog mProgressDialog;

    private TextView mMobile;

    private TextView mNickName;

    private ImageView mAvatar;

    private Button logoutButton;

    private Button mBuyVIPButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_profile);
        logoutButton = (Button) findViewById(R.id.logout_button);
        mMobile = (TextView) findViewById(R.id.phone_textView);
        mNickName = (TextView) findViewById(R.id.nickname_textView);
        mAvatar = (ImageView) findViewById(R.id.user_avatar);
        mMobile.setText(ProfileUtils.getUser(mContext).getPhoneNumber());
        mBuyVIPButton = (Button) findViewById(R.id.buy_vip_button);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.please_wait));
        mProgressDialog.setMessage(getString(R.string.please_wait_more));

        mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachFile();
            }
        });

        ImageLoader.getInstance()
                .display(ProfileUtils.getUser(mContext).getAvatar(), mAvatar, R.drawable.default_avatar);

        if (ProfileUtils.getUser(mContext).getNickName().equals("null") || TextUtils
                .isEmpty(ProfileUtils.getUser(mContext).getNickName())) {
            mNickName.setText(R.string.no_nickname);
        }
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
                final EditText input = new EditText(mContext);
                alert.setView(input);

                alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        editProfile(input.getText().toString(), null);
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

        mBuyVIPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PaymentActivity.class));
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

    public void editProfile(String nickname, String avatar) {
        mProgressDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            if (!TextUtils.isEmpty(nickname)) {
                jsonObject.put("nickName", nickname);
            }
            if (!TextUtils.isEmpty(avatar)) {
                jsonObject.put("avatar", avatar);
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
                                mProgressDialog.dismiss();
                                try {
                                    User user = User.createFromJson(response);
                                    mNickName.setText(user.getNickName());
                                    ImageLoader.getInstance()
                                            .display(user.getAvatar(), mAvatar, R.drawable.default_avatar);
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
                                mProgressDialog.dismiss();
                                Toast.makeText(mContext, getString(R.string.unsuccessful),
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                    }
                });
    }


    public void attachFile() {
        Intent i = new Intent(mContext, FilePickerActivity.class);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
        i.putExtra(FilePickerActivity.EXTRA_START_PATH,
                Environment.getExternalStorageDirectory().getPath());
        startActivityForResult(i, FILE_CODE);
    }

    private final int FILE_CODE = 1;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {
            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
                // For JellyBean and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipData clip = data.getClipData();

                    if (clip != null) {
                        for (int i = 0; i < clip.getItemCount(); i++) {
                            Uri uri = clip.getItemAt(i).getUri();
                            uploadFile(uri.getPath());
                        }
                    }
                } else {
                    ArrayList<String> paths = data.getStringArrayListExtra
                            (FilePickerActivity.EXTRA_PATHS);

                    if (paths != null) {
                        for (String path : paths) {
                            Uri uri = Uri.parse(path);
                            uploadFile(uri.getPath());
                        }
                    }
                }

            } else {
                Uri uri = data.getData();
                uploadFile(uri.getPath());
            }
        }
    }

    public void uploadFile(String filePath) {
        if (filePath == null) {
            Toast.makeText(mContext,
                    getString(R.string.an_error_occurred_try_again),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressDialog.show();
        NetworkManager
                .uploadFile(mContext, new File(filePath), new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        mProgressDialog.dismiss();
                        if (result == null) {
                            Toast.makeText(mContext,
                                    getString(R.string.an_error_occurred_try_again),
                                    Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        } else {
                            Log.wtf("FILE UPLOADED", result.getResult().toString());
                            try {
                                JSONObject jsonObject = new JSONObject(result.getResult());
                                if (jsonObject.getBoolean("error")) {
                                    Toast.makeText(mContext,
                                            getString(R.string.an_error_occurred_try_again) + ": "
                                                    + jsonObject.getString("errorMessage"),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    editProfile(null, jsonObject.getJSONObject("data").getString(
                                            "fileAddress"));
                                }
                            } catch (JSONException ex) {
                            }

                        }
                    }
                });
    }
}
