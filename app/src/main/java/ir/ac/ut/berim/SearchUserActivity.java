package ir.ac.ut.berim;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import ir.ac.ut.network.BerimNetworkException;
import ir.ac.ut.network.MethodsName;
import ir.ac.ut.network.NetworkManager;
import ir.ac.ut.network.NetworkReceiver;


public class SearchUserActivity extends Activity {

    private Context mContext;

    private EditText mSearchBox;

    private ListView mListView;

    private NetworkReceiver mNetworkReceiver = new NetworkReceiver() {
        @Override
        public void onResponse(final Object response) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, response.toString(), Toast.LENGTH_SHORT).show();
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

        mSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s.toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("query", s.toString());
                        NetworkManager
                                .sendRequest(MethodsName.SEARCH_USER, jsonObject, mNetworkReceiver);
                    }catch (JSONException e){
                    }
                }
            }
        });

    }
}
