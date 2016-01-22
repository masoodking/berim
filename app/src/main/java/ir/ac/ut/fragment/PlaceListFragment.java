package ir.ac.ut.fragment;

import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import ir.ac.ut.adapter.PlacesListAdapter;
import ir.ac.ut.berim.AddPlaceActivity;
import ir.ac.ut.berim.R;
import ir.ac.ut.berim.TestScrollActivity;
import ir.ac.ut.models.Place;
import ir.ac.ut.network.BerimNetworkException;
import ir.ac.ut.network.MethodsName;
import ir.ac.ut.network.NetworkManager;
import ir.ac.ut.network.NetworkReceiver;

public class PlaceListFragment extends BaseFragment {

    private static PlaceListFragment mContext;

    private GridView placesGridView;

    private PlacesListAdapter mAdapter;
    private TextView mEditText;

    public PlaceListFragment() {
    }

    public static PlaceListFragment newInstance() {
        PlaceListFragment pf = new PlaceListFragment();
        return pf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_places_list, null);
        mContext = this;
        placesGridView = (GridView) rootView.findViewById(R.id.grid_view_places);
        mEditText= (TextView) rootView.findViewById(R.id.place_search_box);

        getData();
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.places_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddPlaceActivity.class);
                getActivity().startActivity(intent);
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return rootView;
    }

    @Override
    public void getData() {
        NetworkManager.sendRequest(MethodsName.GET_PLACES, new JSONObject(),
                mNetworkReceiver);
    }

    private NetworkReceiver<JSONArray> mNetworkReceiver = new NetworkReceiver<JSONArray>() {
        @Override
        public void onResponse(final JSONArray response) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final Place[] places = new Place[response.length()];
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            places[i] = Place.createFromJson(response.getJSONObject(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mAdapter = new PlacesListAdapter(getActivity(), places);
                        placesGridView.setAdapter(mAdapter);
                        placesGridView
                                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,
                                                            int position, long id) {
                                        Intent intent = new Intent(getActivity(),
                                                TestScrollActivity.class);//0544250
                                        intent.putExtra("place", places[position]);
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