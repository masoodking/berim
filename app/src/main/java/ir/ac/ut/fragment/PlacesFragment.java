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
import ir.ac.ut.berim.R;
import ir.ac.ut.models.Place;

public class PlacesFragment extends Fragment {

    private GridView PlacesGridView;

    private PlacesListAdapter mAdapter;

    public static PlacesFragment newInstance() {
        return new PlacesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_places_list, null);

        PlacesGridView = (GridView) rootView.findViewById(R.id.gridView_places);

        getPlaces();

        return rootView;
    }


    public void getPlaces() {
        // todo get from server

        int count = 20;
        final Place[] places = new Place[count];
        for (int i = 0; i < count; i++) {
            places[i] = new Place(String.valueOf(i), "Place " + i, "this is place " + i);
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
