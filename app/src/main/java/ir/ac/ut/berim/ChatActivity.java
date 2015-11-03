package ir.ac.ut.berim;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

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
        final ArrayList<String> messages = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,messages);
        listview.setAdapter(adapter);
        final ImageButton button;
        button = (ImageButton) findViewById(R.id.send_button);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        setTitle((String) bd.get("placeId"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                String message = et.getText().toString();
                if(et.getText().toString().equals(""))
                    return;
                messages.add(message);
                adapter.notifyDataSetChanged();
                et.setText("");
                listview.setSelection(adapter.getCount() - 1);
            }
        });
    }
}
