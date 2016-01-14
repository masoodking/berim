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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_places_list, null);
        mContext = this;

        PlacesGridView = (GridView) rootView.findViewById(R.id.grid_view_places);

        getChats();

        return rootView;
    }


    private void getChats() {
//        NetworkManager
//                .sendRequest(MethodsName.GET_ROOMS, new JSONObject(), mNetworkReceiver);
//        mChatAdapter = new ChatsListAdapter(getActivity(), chats);
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

//    private NetworkReceiver<JSONArray> mNetworkReceiver = new NetworkReceiver<JSONArray>() {
//        @Override
//        public void onResponse(final JSONArray response) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Room[] rooms = new Room[response.length()];
//                    for (int i = 0; i < response.length(); i++) {
//                        try {
//                            Room[i] = Room.createFromJson(response.getJSONObject(i));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    updateUserList(users);
//                }
//            });
//        }
//
//        @Override
//        public void onErrorResponse(final BerimNetworkException error) {
//            ((Activity) mContext).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(mContext, error.toString(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    };
}
