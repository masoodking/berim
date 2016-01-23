package ir.ac.ut.fragment;

import com.melnykov.fab.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import ir.ac.ut.adapter.ChatsListAdapter;
import ir.ac.ut.berim.ChatActivity;
import ir.ac.ut.berim.R;
import ir.ac.ut.berim.SearchUserActivity;
import ir.ac.ut.database.DatabaseHelper;
import ir.ac.ut.models.Message;
import ir.ac.ut.models.Room;
import ir.ac.ut.network.BerimNetworkException;
import ir.ac.ut.network.ChatNetworkListner;
import ir.ac.ut.utils.NotificationUtils;

public class ChatsListFragment extends BaseFragment {

    private static ChatsListFragment mContext;

    private GridView ChatsGridView;

    private ChatsListAdapter mChatAdapter;

    public ChatsListFragment() {
    }

    public static ChatsListFragment newInstance() {
        ChatsListFragment pf = new ChatsListFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt("type", type);
//        pf.setArguments(bundle);
        return pf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mContext = this;
        View rootView = inflater.inflate(R.layout.fragment_chats_list, null);

        ChatsGridView = (GridView) rootView.findViewById(R.id.grid_view_chats);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.chats_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchUserActivity.class);
                getActivity().startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void getData() {
        Log.wtf("CHAT ACTIVIT: ", "getData called");
//        ArrayList<Room> rooms = DatabaseHelper.getInstance(getActivity())
//                .getRoom(DatabaseHelper.MAX_USER_COUNT + "='1'");
        if (getActivity() == null) {
            return;
        }
        ArrayList<Room> rooms = DatabaseHelper.getInstance(getActivity()).getChatList();
        mChatAdapter = new ChatsListAdapter(getActivity(), rooms);
        ChatsGridView.setAdapter(mChatAdapter);
        ChatsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mChatAdapter.getItem(position).getMaxUserCount() == 1) {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("user",
                            mChatAdapter.getItem(position).getLastMessage().getSender());
                    getActivity().startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "group chat", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mChatNetworkListner.register();
        getData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mChatNetworkListner.unregister();
    }

    protected ChatNetworkListner mChatNetworkListner = new ChatNetworkListner() {
        @Override
        public void onMessageReceived(final JSONObject response) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.wtf("CHAT MESSAGE RECIEVED",
                                response.getString("text") + "-" + response.getString("id"));
                        Message message = Message.createFromJson(response);
                        DatabaseHelper.getInstance(getActivity()).InsertMessage(message);
                        getData();

                        NotificationUtils
                                .sendMessageNotif(getActivity(), getString(R.string.new_message),
                                        getString(R.string.you_have_new_message));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onMessageErrorReceived(BerimNetworkException error) {
            error.printStackTrace();
        }
    };
}
