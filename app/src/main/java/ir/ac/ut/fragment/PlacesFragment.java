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
import ir.ac.ut.berim.PlaceActivity;
import ir.ac.ut.berim.R;
import ir.ac.ut.models.Place;

public class PlacesFragment extends Fragment {

    private Place[] mPlaces;

    private GridView PlacesGridView;

    private PlacesListAdapter mAdapter;

    private View mRootView;

    public static PlacesFragment newInstance() {
        return new PlacesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mRootView = inflater.inflate(R.layout.fragment_places_list, null);

        PlacesGridView = (GridView) mRootView.findViewById(R.id.gridView_places);

        getPlaces();

        return mRootView;
    }


    public void getPlaces() {
        // todo get from server

        int count = 20;
        mPlaces = new Place[count];
        for (int i = 0; i < count; i++) {
            mPlaces[i] = new Place(String.valueOf(i), "Place " + i, "this is place " + i);
        }
        mAdapter = new PlacesListAdapter(getActivity(), mPlaces);
        PlacesGridView.setAdapter(mAdapter);
        PlacesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PlaceActivity.class);
                intent.putExtra("placeId", mPlaces[position].getId());
                getActivity().startActivity(intent);
            }
        });
    }

}
