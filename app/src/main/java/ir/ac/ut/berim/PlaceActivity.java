package ir.ac.ut.berim;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import java.util.ArrayList;

import ir.ac.ut.adapter.ReviewListAdapter;
import ir.ac.ut.models.Review;
import ir.ac.ut.models.User;

/**
 * This class creates the view of "Place" page.
 */
public class PlaceActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        //todo here we should receive a Place object from main aactivity containing informatin requried

        //managing reviews:
        User user1 = new User("Saeed1");
        User user2 = new User("Saeed2");
        User user3 = new User("Saeed3");
        User user4 = new User("Saeed4");
        User user5 = new User("Saeed5");
        User[] users = {user1,user2,user3,user4,user5};
        ArrayList<Review> reviews = new ArrayList<Review>();
        Review[] reviews1 = new Review[5];
        for (int i = 0; i < 5; i++) {
            reviews.add(new Review(users[i],"description, description, description, description, description, description, description, description, description, "));
            reviews1[i] = new Review(users[i],"description, description, description, description, description, description, description, description, description, ");
        }
        final ListView listview = (ListView) findViewById(R.id.PlaceReviewListView);
        ReviewListAdapter mAdapter = new ReviewListAdapter(this, reviews1);
        listview.setAdapter(mAdapter);
        //end managing review
    }
}
