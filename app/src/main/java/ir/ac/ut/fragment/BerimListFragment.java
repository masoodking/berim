package ir.ac.ut.fragment;

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
import ir.ac.ut.database.DatabaseHelper;
import ir.ac.ut.models.Room;

public class BerimListFragment extends BaseFragment {

    private static BerimListFragment mContext;

    private GridView PlacesGridView;

    private ChatsListAdapter mAdapter;

    public BerimListFragment() {
    }

    public static BerimListFragment newInstance() {
        BerimListFragment pf = new BerimListFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt("type", type);
//        pf.setArguments(bundle);
        return pf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_berims_list, null);
        mContext = this;

        PlacesGridView = (GridView) rootView.findViewById(R.id.grid_view_places);

        getBerims();

        return rootView;
    }


    private void getBerims() {
        ArrayList<Room> rooms = DatabaseHelper.getInstance(getActivity())
                .getRoom(DatabaseHelper.MAX_USER_COUNT + "!='1'");
        mAdapter = new ChatsListAdapter(getActivity(), rooms);
        PlacesGridView.setAdapter(mAdapter);
        PlacesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter.getItem(position).getMaxUserCount() == 1) {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("user",
                            mAdapter.getItem(position).getLastMessage().getSender());
                    getActivity().startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "group chat", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
