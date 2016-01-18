package ir.ac.ut.berim;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ir.ac.ut.database.DatabaseHelper;
import ir.ac.ut.fragment.BaseFragment;
import ir.ac.ut.fragment.BerimListFragment;
import ir.ac.ut.fragment.ChatsListFragment;
import ir.ac.ut.fragment.PlaceListFragment;
import ir.ac.ut.models.Message;
import ir.ac.ut.models.Room;
import ir.ac.ut.network.BerimNetworkException;
import ir.ac.ut.network.ChatNetworkListner;
import ir.ac.ut.network.MethodsName;
import ir.ac.ut.network.NetworkManager;
import ir.ac.ut.network.NetworkReceiver;
import ir.ac.ut.widget.BerimHeader;
import ir.ac.ut.widget.SlidingTabBar;
import ir.ac.ut.widget.ViewPager;

public class MainActivity extends FragmentActivity {

    private Context mContext;

    private SlidingTabBar mTabBar;

    private BerimHeader mBerimHeader;

    private AppPagerAdapter mFragmentPagerAdapter;

    public final static int PLACE_TAB = 0;

    public final static int CHAT_TAB = 1;

    public final static int BERIM_TAB = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mBerimHeader = (BerimHeader) findViewById(R.id.berim_header);
        mBerimHeader.showRightAction(R.drawable.ic_action_user_default, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ProfileActivity.class));
            }
        });
//        mBerimHeader.showRightAction(R.drawable.ic_action_new_user, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(mContext, SearchUserActivity.class));
//            }
//        });

        mTabBar = (SlidingTabBar) findViewById(R.id.tab_bar);
        initTabBar();
        getData();
    }

    private void getData() {
        try {
            JSONObject json = new JSONObject();
            json.put("messageId", "");
            NetworkManager.sendRequest(MethodsName.GET_CHAT_LIST, new JSONObject(),
                    new NetworkReceiver<JSONArray>() {
                        @Override
                        public void onResponse(final JSONArray response) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.wtf("CHAT_LIST", response.toString());
                                    List<Message> messages = new ArrayList<Message>();
                                    for (int i = 0; i < response.length(); i++) {
                                        try {
                                            Message message = Message
                                                    .createFromJson(response.getJSONObject(i));
                                            messages.add(message);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    DatabaseHelper.getInstance(mContext).InsertMessage(messages);
                                    ((BaseFragment) mFragmentPagerAdapter.getItem(CHAT_TAB)).getData();
                                }
                            });
                        }

                        @Override
                        public void onErrorResponse(BerimNetworkException error) {
                            Log.wtf("CHAT_LIST", error.getMessage());
                        }
                    });
        } catch (JSONException e) {
        }

        NetworkManager.sendRequest(MethodsName.GET_ROOMS, new JSONObject(),
                new NetworkReceiver<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        ((Activity) mContext).runOnUiThread(new Runnable() {
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
                                DatabaseHelper.getInstance(mContext).InsertRoom(rooms);
                                ((BaseFragment) mFragmentPagerAdapter.getItem(BERIM_TAB)).getData();
                            }
                        });
                    }

                    @Override
                    public void onErrorResponse(BerimNetworkException error) {
                        Log.wtf("CHAT_LIST", error.getMessage());
                    }
                });
    }

    private void initTabBar() {
        String[] titles = {getString(R.string.places),
                getString(R.string.chats), getString(R.string.berims)};
        TabPagerAdapter mTabAdapter = new TabPagerAdapter(titles);

        mFragmentPagerAdapter = new AppPagerAdapter(
                getSupportFragmentManager());

        final ViewPager fragmentPager = (ViewPager) findViewById(R.id.list_pager);
        fragmentPager.setOffscreenPageLimit(3);
//        fragmentPager.setAnimationCacheEnabled(true);
        mTabBar.setOnTabChangeListener(new SlidingTabBar.OnTabChangeListener() {
            @Override
            public void onTabChange(int position) {
                fragmentPager.setCurrentItem(position, true);
            }
        });
        mTabBar.setAdapter(mTabAdapter, true);
        mTabBar.setListPager(fragmentPager);

        fragmentPager.setAdapter(mFragmentPagerAdapter);
    }

    public class TabPagerAdapter extends SlidingTabBar.TabAdapter {

        private final TabInfo[] mAppLists;

        public TabPagerAdapter(String[] titles) {
            mAppLists = new TabInfo[titles.length];
            for (int i = 0; i < titles.length; i++) {
                mAppLists[i] = new TabInfo(titles[i]);
            }
        }

        @Override
        public TabInfo[] getItems() {
            return mAppLists;
        }
    }

    public class TabInfo extends SlidingTabBar.TabItem {

        private final String mTitle;

        public TabInfo(String listTitle) {
            mTitle = listTitle;
        }

        @Override
        public String getTitle() {
            return mTitle;
        }
    }

    public class AppPagerAdapter extends FragmentStatePagerAdapter {

        public AppPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case PLACE_TAB:
                    return PlaceListFragment.newInstance();
                case CHAT_TAB:
                    return ChatsListFragment.newInstance();
                case BERIM_TAB:
                    return BerimListFragment.newInstance();
                default:
                    return PlaceListFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mChatNetworkListner.register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChatNetworkListner.unregister();
    }

    protected ChatNetworkListner mChatNetworkListner = new ChatNetworkListner() {
        @Override
        public void onMessageReceived(final JSONObject response) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.wtf("notif",
                                response.getString("text") + "-" + response.getString("id"));
                        Message message = Message.createFromJson(response);
                        DatabaseHelper.getInstance(mContext).InsertMessage(message);
                        ((BaseFragment) mFragmentPagerAdapter.getItem(CHAT_TAB)).getData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onMessageErrorReceived(BerimNetworkException error) {
            error.printStackTrace();
        }
    };
}
