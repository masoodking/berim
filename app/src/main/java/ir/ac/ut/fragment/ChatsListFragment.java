package ir.ac.ut.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import ir.ac.ut.adapter.ChatsListAdapter;
import ir.ac.ut.adapter.PlacesListAdapter;
import ir.ac.ut.berim.ChatActivity;
import ir.ac.ut.berim.R;
import ir.ac.ut.models.Chat;

public class ChatsListFragment extends BaseFragment {

    private static ChatsListFragment mContext;

    private GridView PlacesGridView;

    private PlacesListAdapter mAdapter;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_places_list, null);
        mContext = this;

        PlacesGridView = (GridView) rootView.findViewById(R.id.gridView_places);

        getChats();

        return rootView;
    }


    private void getChats() {
        // todo get from server
        int count = 20;
        final Chat[] chats = new Chat[count];
        for (int i = 0; i < count; i++) {
            chats[i] = new Chat(String.valueOf(i), "Chat " + i);
        }
        mChatAdapter = new ChatsListAdapter(getActivity(), chats);
        PlacesGridView.setAdapter(mChatAdapter);
        PlacesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
//                intent.putExtra("placeId", chats[position].getId());
                getActivity().startActivity(intent);
            }
        });
    }
}
