package ir.ac.ut.berim;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.ArrayList;


public class ChatActivity extends ActionBarActivity {

    private Context mContext;

    private EditText mMessageText;

    private ListView mListView;

    private ArrayList<String> mMessages;

    private ArrayAdapter mAdapter;

    private Emitter.Listener onNewMessage;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://172.18.49.236:3000");
            loginUser("0936", "masood");
        } catch (URISyntaxException e) {
            Log.wtf("Socket", "connection faild.");
        } catch (JSONException e) {
            Log.wtf("Socket", "singin faild.");
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mContext = this;

        mSocket.connect();

        mMessageText = (EditText) findViewById(R.id.chat_text);
        mListView = (ListView) findViewById(R.id.listview);

        mMessages = new ArrayList<>();
        mAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, mMessages);

        mListView.setAdapter(mAdapter);

        final ImageButton button;
        button = (ImageButton) findViewById(R.id.send_button);

        mMessageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(mMessageText.getText())) {
                    button.setImageResource(R.drawable.attach);
                } else {
                    button.setImageResource(R.drawable.send_icon);
                }
            }
        });
        Intent intent = getIntent();
        setTitle("sudn");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mMessageText.getText().toString();
                if (TextUtils.isEmpty(message)) {
                    return;
                }
                try {
                    sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        mMessageText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    if (TextUtils.isEmpty(mMessageText.getText().toString())) {
                        return false;
                    }
                    sendMessage(v.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mMessageText.setText("");
                return true;
            }
        });

        onNewMessage = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.e("notif", args[0].toString());
                            JSONObject data = new JSONObject(args[0].toString());
                            String username;
                            String message;
                            username = "user";//data.getString("username");
                            message = data.getString("text");
                            addMessage(username, message);
                        } catch (JSONException e) {
                            return;
                        }

                    }
                });
            }
        };
        mSocket.on("broadcast", onNewMessage);
    }


    public void sendMessage(String message) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("text", message);
        mSocket.emit("broadcast", json.toString());
        mMessages.add(message);
        mAdapter.notifyDataSetChanged();
        mMessageText.setText("");
        mListView.smoothScrollToPosition((mAdapter.getCount() - 1));
    }

    public void addMessage(String username, String message) {
        //add message to list
        Toast.makeText(mContext, username + ": " + message, Toast.LENGTH_SHORT).show();

        mMessages.add(message);
        mAdapter.notifyDataSetChanged();
        mMessageText.setText("");
        mListView.smoothScrollToPosition((mAdapter.getCount() - 1));
    }

    public void loginUser(String phoneNumber, String password) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("phoneNumber", phoneNumber);
        json.put("password", password);
        mSocket.emit("signIn", json.toString()).on("signInResponse", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.e("notif", args[0].toString());
                            JSONObject data = new JSONObject(args[0].toString());
//                            String username;
//                            String message;
//                            username = "user";//data.getString("username");
//                            message = data.getString("text");
                            addMessage("login", args[0].toString());
                        } catch (JSONException e) {
                            return;
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off("broadcast", onNewMessage);
    }
}
