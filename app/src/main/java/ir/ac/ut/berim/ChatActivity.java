package ir.ac.ut.berim;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.ac.ut.adapter.ChatAdapter;
import ir.ac.ut.models.Message;

import static android.graphics.Color.CYAN;


public class ChatActivity extends ActionBarActivity {
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mContext = this;
        final EditText et = (EditText)findViewById(R.id.chat_text);
        final ListView listview = (ListView) findViewById(R.id.listview);
        final ArrayList<Message> messages = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(this, messages.toArray(new Message[messages.size()]));
        listview.setAdapter(chatAdapter);
        final ImageButton button;
        button = (ImageButton) findViewById(R.id.send_button);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(et.getText())) {
                    button.setImageResource(R.drawable.attach);
                } else
                    button.setImageResource(R.drawable.send_icon);
            }
        });
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        setTitle("sudn");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo change Message constructor
                Message message = new Message(et.getText().toString());
                if (et.getText().toString().equals(""))
                    return;
                messages.add(message);
                chatAdapter.notifyDataSetChanged();
                et.setText("");
                listview.setSelection(chatAdapter.getCount() - 1);
            }
        });
    }
}
