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

import ir.ac.ut.adapter.ChatsListAdapter;
import ir.ac.ut.adapter.PlacesListAdapter;
import ir.ac.ut.berim.LoginActivity;
import ir.ac.ut.berim.ProfileUtils;
import ir.ac.ut.berim.R;
import ir.ac.ut.berim.TestScrollActivity;
import ir.ac.ut.models.Place;

public class PlaceListFragment extends BaseFragment {

    private static PlaceListFragment mContext;

    private GridView PlacesGridView;

    private PlacesListAdapter mAdapter;
    private ChatsListAdapter mChatAdapter;

    public PlaceListFragment() {
    }

    public static PlaceListFragment newInstance() {
        PlaceListFragment pf = new PlaceListFragment();
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

        getPlaces();

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.places_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileUtils.logoutUser(getActivity());
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

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
                Intent intent = new Intent(getActivity(), TestScrollActivity.class);//0544250
//                intent.putExtra("placeId", places[position].getId());
                getActivity().startActivity(intent);
            }
        });
    }
}
