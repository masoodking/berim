package ir.ac.ut.berim;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ir.ac.ut.adapter.ChatAdapter;
import ir.ac.ut.models.Message;
import ir.ac.ut.models.User;
import ir.ac.ut.network.BerimNetworkException;
import ir.ac.ut.network.ChatNetworkListner;
import ir.ac.ut.network.MethodsName;
import ir.ac.ut.network.NetworkManager;
import ir.ac.ut.network.NetworkReceiver;

public class ChatActivity extends ActionBarActivity {

    private Context mContext;

    private EditText mMessageInput;

    private ListView mListView;

    private ArrayList<Message> mMessages;

    private ChatAdapter mAdapter;

    private User mMe;

    private User mTalkee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mContext = this;

        mMe = ProfileUtils.getUser(mContext);

        mTalkee = (User) getIntent().getSerializableExtra("user");

        mMessageInput = (EditText) findViewById(R.id.chat_text);
        mListView = (ListView) findViewById(R.id.listview);

        mMessages = new ArrayList<>();
        mAdapter = new ChatAdapter(this, mMessages);

        mListView.setAdapter(mAdapter);
        mListView.setDivider(null);
        final ImageButton button = (ImageButton) findViewById(R.id.send_button);

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
                    button.setImageResource(R.drawable.ic_action_attach);
                } else {
                    button.setImageResource(R.drawable.send_icon);
                }
            }
        });

        setTitle(mTalkee.getNickName());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mMessageInput.getText().toString())) {
                    Message message = new Message();
                    message.setText(mMessageInput.getText().toString());
                    message.setRoomId(mTalkee.getRoomId());
                    message.setFrom(mMe.getId());
                    message.setStatus(Message.MessageStatus.SENT);
                    try {
                        sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void sendMessage(Message message) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("text", message.getText());
        json.put("roomId", message.getRoomId());
        NetworkManager.sendRequest(MethodsName.SEND_MESSAGE, json, new NetworkReceiver() {
            @Override
            public void onResponse(Object response) {
                //todo change message status to sent
            }

            @Override
            public void onErrorResponse(BerimNetworkException error) {
                //todo show error for message.
            }
        });
//        mAdapter.setSentBy(message.getFrom());
        mMessages.add(message);
        mAdapter.notifyDataSetChanged();
        mMessageInput.setText("");
        mListView.smoothScrollToPosition((mAdapter.getCount() - 1));
    }

    public void addMessage(Message message) {
        //add message to list
        Toast.makeText(mContext, message.getUsername() + ": " + message.getText(),
                Toast.LENGTH_SHORT).show();
        mMessages.add(message);
        mAdapter.notifyDataSetChanged();
        mMessageInput.setText("");
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
