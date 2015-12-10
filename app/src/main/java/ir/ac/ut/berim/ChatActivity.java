package ir.ac.ut.berim;

import com.github.nkzawa.emitter.Emitter;

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

import java.util.ArrayList;

import ir.ac.ut.models.Message;
import ir.ac.ut.network.BerimNetworkException;
import ir.ac.ut.network.ChatNetworkListner;
import ir.ac.ut.network.NetworkManager;
import ir.ac.ut.network.NetworkReceiver;

public class ChatActivity extends ActionBarActivity{

    private Context mContext;

    private EditText mMessageText;

    private ListView mListView;

    private ArrayList<String> mMessages;

    private ArrayAdapter mAdapter;

    private Emitter.Listener onNewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mContext = this;

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
                Message message = new Message(mMessageText.getText().toString());
                if (TextUtils.isEmpty(mMessageText.getText().toString())) {
                    return;
                }
                try {
                    sendMessage(message.getText());
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
    }


    public void sendMessage(String message) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("text", message);
        NetworkManager.sendRequest("sendMessage", json, new NetworkReceiver() {
            @Override
            public void onResponse(Object response) {
                //todo change message status to sent
            }

            @Override
            public void onErrorResponse(BerimNetworkException error) {
                //todo show error for message.
            }
        });
        mMessages.add(message);
        mAdapter.notifyDataSetChanged();
        mMessageText.setText("");
        mListView.smoothScrollToPosition((mAdapter.getCount() - 1));
    }

    public void addMessage(Message message) {
        //add message to list
        Toast.makeText(mContext, message.getUsername() + ": " + message.getText(), Toast.LENGTH_SHORT).show();
        mMessages.add(message.getText());
        mAdapter.notifyDataSetChanged();
        mMessageText.setText("");
        mListView.smoothScrollToPosition((mAdapter.getCount() - 1));
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                        addMessage(message);
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
}
