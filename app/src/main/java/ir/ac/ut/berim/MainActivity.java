package ir.ac.ut.berim;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ir.ac.ut.fragment.PlacesFragment;
import ir.ut.ac.widget.SlidingTabBar;
import ir.ut.ac.widget.ViewPager;

public class MainActivity extends FragmentActivity {

    private SlidingTabBar mTabBar;

    public static final int PLACE=1,BERIM=3,CHAT=2,ERROR=4;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabBar = (SlidingTabBar) findViewById(R.id.tab_bar);

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
                    return PlacesFragment.newInstance(1);
                case 1:
                    return PlacesFragment.newInstance(2);
                case 2:
                    return PlacesFragment.newInstance(3);
                default:
                    return PlacesFragment.newInstance(4);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

    }
}
