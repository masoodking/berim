package ir.ac.ut.berim;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TestScrollActivity extends AppCompatActivity {

    private View mToolbarView;

    private View mBackgroundImage;

    private ObservableListView listView;

    TextView mtextView;

    String temp;

    private View mStickyHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scroll);

        listView = (ObservableListView) findViewById(R.id.list);
        mStickyHeader = findViewById(R.id.placeHeaderMenuSticky);
        ArrayList<String> items = new ArrayList<String>();
        for (int i = 1; i <= 100; i++) {
            items.add("Item " + i);
        }
        listView.setAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, items));
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.place_header, listView,
                false);
        listView.addHeaderView(header, null, false);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (listView != null && listView.getChildAt(1) != null
                        && listView.getChildAt(1).getTop() < 85) {
                    mStickyHeader.setVisibility(View.VISIBLE);
                } else {
                    mStickyHeader.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount) {

            }
        });
    }
}
