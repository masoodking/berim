package ir.ac.ut.berim;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;
import com.nononsenseapps.filepicker.FilePickerActivity;

import java.io.File;
import java.util.ArrayList;

import ir.ac.ut.models.User;
import ir.ac.ut.network.BerimNetworkException;
import ir.ac.ut.network.MethodsName;
import ir.ac.ut.network.NetworkManager;
import ir.ac.ut.network.NetworkReceiver;
import ir.ac.ut.utils.ImageLoader;

/**
 * Created by saeed on 1/17/2016.
 */
public class AddPlaceActivity extends BerimActivity {

    private Context mContext;

    private Button mSubmit;
    private Button mUpload;

    private TextView mName;

    private TextView mAddress;
    private ImageView mPhoto;

    private TextView mDescription;
    private ProgressDialog mProgressDialog;
    private String uploadedLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        mContext = this;
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.please_wait));
        mProgressDialog.setMessage(getString(R.string.please_wait_more));

        mPhoto = (ImageView) findViewById(R.id.imageView);

        mName = (TextView) findViewById(R.id.place_name);
        mAddress = (TextView) findViewById(R.id.place_address);
        mDescription = (TextView) findViewById(R.id.place_desc);

        mUpload = (Button) findViewById(R.id.buttonUpload);
        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachFile();
            }
        });
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
                    jsonObject.put("avatar", mDescription.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                NetworkManager.sendRequest(MethodsName.ADD_PLACE, jsonObject,
                        new NetworkReceiver<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                ((Activity) mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext, getString(R.string.successful),
                                                Toast.LENGTH_SHORT);
                                        ((Activity) mContext).finish();

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
                            uploadedLink = result.getResult().toString();
                            ImageLoader.getInstance()
                                    .display(uploadedLink, mPhoto, R.drawable.default_place);

                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBerimHeader.setTitle(getString(R.string.title_activity_add_place));
    }
}
