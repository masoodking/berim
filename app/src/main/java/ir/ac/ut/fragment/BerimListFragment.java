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
import ir.ac.ut.models.Place;
import ir.ac.ut.models.Room;

public class BerimListFragment extends BaseFragment {

    private static BerimListFragment mContext;

    private GridView BerimsGridView;

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

        BerimsGridView = (GridView) rootView.findViewById(R.id.gridView_berims);

        getBerims();

        return rootView;
    }

    private void getBerims() {
        // todo get from server
        int count = 20;
        final Room[] rooms = new Room[count];
        for (int i = 0; i < count; i++) {
            rooms[i] = new Room(String.valueOf(i),  "this is berim " + i);
        }
        mAdapter = new ChatsListAdapter(getActivity(), rooms);
        BerimsGridView.setAdapter(mAdapter);
        BerimsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("Berimid", rooms[position].getRoomId());
                getActivity().startActivity(intent);
            }
        });
    }
}
