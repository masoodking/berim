package ir.ac.ut.berim;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import ir.ac.ut.adapter.UserListAdapter;
import ir.ac.ut.models.User;
import ir.ac.ut.network.BerimNetworkException;
import ir.ac.ut.network.MethodsName;
import ir.ac.ut.network.NetworkManager;
import ir.ac.ut.network.NetworkReceiver;


public class SearchUserActivity extends Activity {

    private Context mContext;

    private EditText mSearchBox;

    private ListView mListView;

    private UserListAdapter mAdapter;

    private NetworkReceiver<JSONArray> mNetworkReceiver = new NetworkReceiver<JSONArray>() {
        @Override
        public void onResponse(final JSONArray response) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    User[] users = new User[response.length()];
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            users[i] = User.createFromJson(response.getJSONObject(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    updateUserList(users);
                }
            });
        }

        @Override
        public void onErrorResponse(final BerimNetworkException error) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        mContext = this;

        mSearchBox = (EditText) findViewById(R.id.user_search_box);
        mListView = (ListView) findViewById(R.id.user_search_list);

        mAdapter = new UserListAdapter(mContext, new User[0]);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("user", mAdapter.getItem(position));
                startActivity(intent);
                finish();
            }
        });

        mSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("query", s.toString());
                        NetworkManager
                                .sendRequest(MethodsName.SEARCH_USER, jsonObject, mNetworkReceiver);
                    } catch (JSONException e) {
                    }
                }
            }
        });
    }

    public void updateUserList(User[] users) {
        mAdapter.setData(users);
        mAdapter.notifyDataSetChanged();
    }
}
