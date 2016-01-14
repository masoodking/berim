package ir.ac.ut.fragment;

import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import ir.ac.ut.adapter.ChatsListAdapter;
import ir.ac.ut.adapter.PlacesListAdapter;
import ir.ac.ut.berim.LoginActivity;
import ir.ac.ut.berim.ProfileUtils;
import ir.ac.ut.berim.R;
import ir.ac.ut.berim.TestScrollActivity;
import ir.ac.ut.models.Place;
import ir.ac.ut.network.BerimNetworkException;
import ir.ac.ut.network.MethodsName;
import ir.ac.ut.network.NetworkManager;
import ir.ac.ut.network.NetworkReceiver;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_places_list, null);
        mContext = this;

        PlacesGridView = (GridView) rootView.findViewById(R.id.gridView_places);

        getPlaces();

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.places_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileUtils.logoutUser(getActivity());//todo do we need this?
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

        return rootView;
    }


    public void getPlaces() {
        NetworkManager
                .sendRequest(MethodsName.GET_ROOMS, new JSONObject(), mNetworkReceiver);//todo change this to get_places
    }

    private NetworkReceiver<JSONArray> mNetworkReceiver = new NetworkReceiver<JSONArray>() {
        @Override
        public void onResponse(final JSONArray response) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Place[] places = new Place[response.length()];
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            places[i] = new Place(response.getJSONObject(i).getString("id"),
                                    response.getJSONObject(i).getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mAdapter = new PlacesListAdapter(getActivity(), places);
                        PlacesGridView.setAdapter(mAdapter);
                        PlacesGridView
                                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                                        Intent intent = new Intent(getActivity(),
                                                TestScrollActivity.class);//0544250
//                                        intent.putExtra("placeId", places[position].getId());
                                        getActivity().startActivity(intent);
                                    }
                                });
                    }
                }
            });
        }

        @Override
        public void onErrorResponse(final BerimNetworkException error) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
}
