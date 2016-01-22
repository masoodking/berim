package ir.ac.ut.fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import ir.ac.ut.adapter.ChatsListAdapter;
import ir.ac.ut.berim.BerimApplication;
import ir.ac.ut.berim.ProfileUtils;
import ir.ac.ut.berim.R;
import ir.ac.ut.berim.RoomActivity;
import ir.ac.ut.database.DatabaseHelper;
import ir.ac.ut.models.Room;
import ir.ac.ut.network.BerimNetworkException;
import ir.ac.ut.network.ChatNetworkListner;
import ir.ac.ut.network.MethodsName;
import ir.ac.ut.network.NetworkManager;
import ir.ac.ut.network.NetworkReceiver;

public class BerimListFragment extends BaseFragment {

    private static BerimListFragment mContext;

    private GridView PlacesGridView;

    private ChatsListAdapter mAdapter;

    public BerimListFragment() {
    }

    public static BerimListFragment newInstance() {
        BerimListFragment pf = new BerimListFragment();
        return pf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_berims_list, null);
        mContext = this;

        PlacesGridView = (GridView) rootView.findViewById(R.id.grid_view_places);

        getData();

        return rootView;
    }


    @Override
    public void getData() {
        if (getActivity() == null) {
            return;
        }
        ArrayList<Room> rooms = DatabaseHelper.getInstance(getActivity())
                .getRoom(DatabaseHelper.ID + "!='" + ProfileUtils.getUser(BerimApplication.getInstance()).getRoomId() + "'");
        mAdapter = new ChatsListAdapter(getActivity(), rooms);
        PlacesGridView.setAdapter(mAdapter);
        PlacesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RoomActivity.class);
                intent.putExtra("room", mAdapter.getItem(position));
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mChatNetworkListner.register();
        getData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mChatNetworkListner.unregister();
    }

    protected ChatNetworkListner mChatNetworkListner = new ChatNetworkListner() {
        @Override
        public void onMessageReceived(final JSONObject response) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    try {
//                        Message message = Message.createFromJson(response);
//                        DatabaseHelper.getInstance(getActivity()).InsertMessage(message);
                    getRoomData();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            });
        }

        @Override
        public void onMessageErrorReceived(BerimNetworkException error) {
            error.printStackTrace();
        }
    };

    public void getRoomData() {
        NetworkManager.sendRequest(MethodsName.GET_ROOMS, new JSONObject(),
                new NetworkReceiver<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.wtf("CHAT_ROOMS", response.toString());
                                List<Room> rooms = new ArrayList<Room>();
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        Room room = Room
                                                .createFromJson(response.getJSONObject(i));
                                        rooms.add(room);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                DatabaseHelper.getInstance(getActivity()).InsertRoom(rooms);
                                getData();
                            }
                        });
                    }

                    @Override
                    public void onErrorResponse(BerimNetworkException error) {
                        Log.wtf("CHAT_LIST", error.getMessage());
                    }
                });
    }
}
