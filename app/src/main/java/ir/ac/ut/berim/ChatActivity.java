package ir.ac.ut.berim;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;
import com.nononsenseapps.filepicker.FilePickerActivity;

import org.json.JSONArray;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import ir.ac.ut.adapter.ChatAdapter;
import ir.ac.ut.database.DatabaseHelper;
import ir.ac.ut.models.Message;
import ir.ac.ut.models.User;
import ir.ac.ut.network.BerimNetworkException;
import ir.ac.ut.network.ChatNetworkListner;
import ir.ac.ut.network.MethodsName;
import ir.ac.ut.network.NetworkManager;
import ir.ac.ut.network.NetworkReceiver;

public class ChatActivity extends BerimActivity {

    private Context mContext;

    private EditText mMessageInput;

    private ProgressDialog mProgressDialog;

    private ListView mListView;

    private ArrayList<Message> mMessages;

    private ChatAdapter mAdapter;

    private ImageButton mSendOrAttachButton;

    private User mMe;

    private User mTalkee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mContext = this;

        mMe = ProfileUtils.getUser(mContext);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.please_wait));
        mProgressDialog.setMessage(getString(R.string.please_wait_more));

        mTalkee = (User) getIntent().getSerializableExtra("user");
        mMessageInput = (EditText) findViewById(R.id.chat_text);
        mListView = (ListView) findViewById(R.id.listview);
        mMessages = new ArrayList<>();
        mAdapter = new ChatAdapter(this, mMessages);
        mListView.setAdapter(mAdapter);
        mListView.setDivider(null);
        mSendOrAttachButton = (ImageButton) findViewById(R.id.send_button);

        mMessageInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    mSendOrAttachButton.setImageResource(R.drawable.ic_action_attach);
                } else {
                    mSendOrAttachButton.setImageResource(R.drawable.send_icon);
                }
            }
        });

        mSendOrAttachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mMessageInput.getText().toString())) {
                    Message message = new Message();
                    message.setText(mMessageInput.getText().toString());
                    message.setRoomId(mTalkee.getRoomId());
                    message.setSender(mMe);
                    message.setStatus(Message.MessageStatus.SENT);
                    message.setDate(String.valueOf(System.currentTimeMillis()));
                    message.setId("not-set");
                    try {
                        sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    attachFile();
                }
            }
        });

        loadMessages();

    }

    public void loadMessages() {
        ArrayList<Message> messages = DatabaseHelper.getInstance(mContext).getMessage(true,
                DatabaseHelper.ROOM_ID + "='" + mTalkee.getRoomId() + "' or ("
                        + DatabaseHelper.SENDER_ROOM_ID
                        + "='" + mTalkee.getRoomId() + "' and " + DatabaseHelper.ROOM_ID + "='"
                        + mMe.getRoomId() + "')");
        mMessages.addAll(messages);
        mAdapter.notifyDataSetChanged();
        mMessageInput.setText("");
        try {
            seenMessagesOnServer(messages, true);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void seenMessagesOnServer(final ArrayList<Message> messages, final boolean retry)
            throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "seen");
        JSONArray messageIdArray = new JSONArray();
        for (Message message : messages) {
            if (message.getSender().getId() != mMe.getId()) {
                messageIdArray.put(message.getId());
            }
        }
        jsonObject.put("messages", messageIdArray);
        if (messageIdArray.length() == 0) {
            return;
        }
        NetworkManager.sendRequest(MethodsName.bULK_CHANGE_MESSAGE_STATUS_GOT, jsonObject,
                new NetworkReceiver() {
                    @Override
                    public void onResponse(Object response) {
                        //message seen in server side
                    }

                    @Override
                    public void onErrorResponse(BerimNetworkException error) {
                        if (retry) {
                            try {
                                seenMessagesOnServer(messages, false);
                            } catch (JSONException e) {

                            }
                        }
                    }
                });
    }

    public void sendMessage(Message message) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("text", message.getText());
        json.put("roomId", message.getRoomId());
        if (message.getFileAddress() != null && message.getFileAddress().length() > 0) {
            json.put("file", message.getFileAddress());
        } else {
            json.put("file", "");
        }
        NetworkManager.sendRequest(MethodsName.SEND_MESSAGE, json, new NetworkReceiver() {
            @Override
            public void onResponse(Object response) {
                //todo change message status to sent
                Log.wtf("SEND_MESSAGE", response.toString());
            }

            @Override
            public void onErrorResponse(BerimNetworkException error) {
                //todo show error for message.
                Log.wtf("SEND_MESSAGE", error.getMessage());
            }
        });
        addMessage(message);
    }

    public void addMessage(Message message) {
        //add message to list
        mMessages.add(message);
        mAdapter.notifyDataSetChanged();
        mMessageInput.setText("");
        if (message.getSender().getId().equals(mMe.getId())) {
            DatabaseHelper.getInstance(mContext).InsertMessageNoUpdate(message);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBerimHeader.setTitle(mTalkee.getValidUserName());
        mChatNetworkListner.register();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mChatNetworkListner.unregister();
    }

    protected ChatNetworkListner mChatNetworkListner = new ChatNetworkListner() {
        @Override
        public void onMessageReceived(final JSONObject response) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.e("notif", response.getString("text"));
                        Message message = Message.createFromJson(response);
                        if (message.getRoomId().equals(mMe.getRoomId()) &&
                                message.getSender().getId().equals(mTalkee.getId())) {
                            addMessage(message);
                            ArrayList<Message> messages = new ArrayList<>();
                            seenMessagesOnServer(messages, true);
                        }
                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }

        @Override
        public void onMessageErrorReceived(BerimNetworkException error) {

        }
    };


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
                                    Message message = new Message();
                                    message.setText("file");
                                    message.setRoomId(mTalkee.getRoomId());
                                    message.setFileAddress(
                                            jsonObject.getJSONObject("data").getString(
                                                    "fileAddress"));
                                    message.setSender(mMe);
                                    message.setStatus(Message.MessageStatus.SENT);
                                    message.setDate(String.valueOf(System.currentTimeMillis()));
                                    message.setId("not-set");
                                    sendMessage(message);
                                }
                            } catch (JSONException ex) {
                            }

                        }
                    }
                });
    }

}
