package ir.ac.ut.berim;

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

import ir.ac.ut.fragment.BerimListFragment;
import ir.ac.ut.fragment.ChatsListFragment;
import ir.ac.ut.fragment.PlaceListFragment;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mBerimHeader = (BerimHeader) findViewById(R.id.berim_header);
        mBerimHeader.showNewUserIcon(R.drawable.ic_action_new_user, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SearchUserActivity.class));
            }
        });

        mTabBar = (SlidingTabBar) findViewById(R.id.tab_bar);
        initTabBar();


        try {
            JSONObject json = new JSONObject();
            json.put("messageId", "");
            NetworkManager.sendRequest(MethodsName.GET_CHAT_LIST, new JSONObject(), new NetworkReceiver() {
                @Override
                public void onResponse(Object response) {
                    Log.wtf("CHAT_LIST", response.toString());
                }

                @Override
                public void onErrorResponse(BerimNetworkException error) {
                    Log.wtf("CHAT_LIST", error.getMessage());
                }
            });
        }catch (JSONException e){
        }
    }

    private void initTabBar(){
        String[] titles = {getString(R.string.places),
                getString(R.string.chats), getString(R.string.berims)};
        TabPagerAdapter mTabAdapter = new TabPagerAdapter(titles);

        final AppPagerAdapter mFragmentPagerAdapter = new AppPagerAdapter(getSupportFragmentManager());

        final ViewPager fragmentPager = (ViewPager) findViewById(R.id.list_pager);
//        fragmentPager.setAnimationCacheEnabled(true);
        mTabBar.setOnTabChangeListener(new SlidingTabBar.OnTabChangeListener() {
            @Override
            public void onTabChange(int position) {
                fragmentPager.setCurrentItem(position,true);
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
                case 0:
                    return PlaceListFragment.newInstance();
                case 1:
                    return BerimListFragment.newInstance();
                case 2:
                    return ChatsListFragment.newInstance();
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
