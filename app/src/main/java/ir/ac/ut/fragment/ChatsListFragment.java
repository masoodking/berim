package ir.ac.ut.fragment;

import com.melnykov.fab.FloatingActionButton;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import ir.ac.ut.models.Room;

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
    public void onResume() {
        super.onResume();
        getChats();
    }

    private void getChats() {
//        ArrayList<Room> rooms = DatabaseHelper.getInstance(getActivity())
//                .getRoom(DatabaseHelper.MAX_USER_COUNT + "='1'");
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

//    private NetworkReceiver<JSONArray> mNetworkReceiver = new NetworkReceiver<JSONArray>() {
//        @Override
//        public void onResponse(final JSONArray response) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Room[] rooms = new Room[response.length()];
//                    for (int i = 0; i < response.length(); i++) {
//                        try {
//                            Room[i] = Room.createFromJson(response.getJSONObject(i));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    updateUserList(users);
//                }
//            });
//        }
//
//        @Override
//        public void onErrorResponse(final BerimNetworkException error) {
//            ((Activity) mContext).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(mContext, error.toString(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    };
}
