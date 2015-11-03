package ir.ac.ut.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import ir.ac.ut.adapter.PlacesListAdapter;
import ir.ac.ut.berim.ChatActivity;
import ir.ac.ut.berim.MainActivity;
import ir.ac.ut.berim.R;
import ir.ac.ut.models.Chat;
import ir.ac.ut.models.Place;

public class PlacesFragment extends Fragment {

    private static PlacesFragment mContext;
    private GridView PlacesGridView;

    private PlacesListAdapter mAdapter;

    public  PlacesFragment(){
        mContext = this;
    }

    public static PlacesFragment newInstance(int type) {
        PlacesFragment pf = new PlacesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        pf.setArguments(bundle);
        return pf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_places_list, null);

        PlacesGridView = (GridView) rootView.findViewById(R.id.gridView_places);

        if(mContext.getArguments().getInt("type")== MainActivity.PLACE)
            getPlaces();
        if(mContext.getArguments().getInt("type")== MainActivity.PLACE)
            getBerims();
        if(mContext.getArguments().getInt("type")== MainActivity.PLACE)
            getChats();

        return rootView;
    }

    private void getChats() {
        // todo get from server
        int count = 20;
        final Chat[] chats= new Chat[count];
        for (int i = 0; i < count; i++) {
            chats[i] = new Chat(String.valueOf(i), "Chat " + i, "this is chat " +i);
        }
        mAdapter = new PlacesListAdapter(getActivity(), chats);
        PlacesGridView.setAdapter(mAdapter);
        PlacesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("placeId", chats[position].getId());
                getActivity().startActivity(intent);
            }
        });
    }

    private void getBerims() {
        // todo get from server
        int count = 20;
        final Place[] places = new Place[count];
        for (int i = 0; i < count; i++) {
            places[i] = new Place(String.valueOf(i), "Berim" + i, "this is berim " +i);
        }
        mAdapter = new PlacesListAdapter(getActivity(), places);
        PlacesGridView.setAdapter(mAdapter);
        PlacesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("placeId", places[position].getId());
                getActivity().startActivity(intent);
            }
        });
    }

    public void getPlaces() {
        // todo get from server
        int count = 20;
        final Place[] places = new Place[count];
        for (int i = 0; i < count; i++) {
            places[i] = new Place(String.valueOf(i), "Place " + i, "this is place " +i);
        }
        mAdapter = new PlacesListAdapter(getActivity(), places);
        PlacesGridView.setAdapter(mAdapter);
        PlacesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("placeId", places[position].getId());
                getActivity().startActivity(intent);
            }
        });
    }
}
