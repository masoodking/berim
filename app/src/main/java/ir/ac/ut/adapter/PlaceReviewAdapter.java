package ir.ac.ut.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import ir.ac.ut.berim.R;
import ir.ac.ut.models.Review;
import ir.ac.ut.utils.ImageLoader;

/**
 * Created by masood on 10/1/15.
 */
public class PlaceReviewAdapter extends BaseAdapter {

    public static final int PLACE_DESCRIPTION = 0;

    public static final int PLACE_REVIEW = 1;

    private Context mContext;

    private String description;

    private ArrayList<Review> reviews;

    public PlaceReviewAdapter(Context context, ArrayList<Review> reviews, String description) {
        mContext = context;
        this.description = description;
        this.reviews = reviews;
    }


    @Override
    public int getCount() {
        return reviews.size() + 1;
    }

    @Override
    public Review getItem(int position) {
        return reviews.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position -1;
    }

//    @Override
//    public int getViewTypeCount() {
//        return super.getViewTypeCount();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int listViewItemType = getItemViewType(position);
        PlaceReviewViewHolder reviewViewHolder;
        if (convertView == null) {
            if (listViewItemType == PLACE_DESCRIPTION) {
                convertView = LayoutInflater.from(mContext)
                        .inflate(R.layout.item_place_description, parent, false);
                reviewViewHolder = new PlaceReviewViewHolder(convertView);
                convertView.setTag(reviewViewHolder);
                reviewViewHolder.description.setText(description);
            } else {
                convertView = LayoutInflater.from(mContext)
                        .inflate(R.layout.item_place_review, parent, false);

                Review review = getItem(position);
                reviewViewHolder = new PlaceReviewViewHolder(convertView);
                convertView.setTag(reviewViewHolder);
                reviewViewHolder.name.setText(review.getUser().getValidUserName());
                reviewViewHolder.description.setText(review.getText());
                reviewViewHolder.stars.setRating(review.getRate());
                LayerDrawable stars = (LayerDrawable) reviewViewHolder.stars.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                ImageLoader.getInstance()
                        .display(review.getUser().getAvatar(), reviewViewHolder.icon,
                                R.drawable.default_avatar);
            }
        } else {
            reviewViewHolder = (PlaceReviewViewHolder) convertView.getTag();
        }
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return PLACE_DESCRIPTION;
        }
        return PLACE_REVIEW;
    }

    class PlaceReviewViewHolder {

        final TextView name;

        final TextView description;

        final ImageView icon;

        final RatingBar stars;

        PlaceReviewViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.reviewer_name_text_view);
            description = (TextView) view.findViewById(R.id.review_description_text_view);
            icon = (ImageView) view.findViewById(R.id.review_icon_image);
            stars = (RatingBar) view.findViewById(R.id.ratingBar2);
        }
    }
}



