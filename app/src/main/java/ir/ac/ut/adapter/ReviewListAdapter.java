package ir.ac.ut.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.ac.ut.berim.PlaceActivity;
import ir.ac.ut.berim.R;
import ir.ac.ut.models.Review;

/**
 * Created by Saeed on 10/1/15.
 */
public class ReviewListAdapter extends BaseAdapter {

    private Context mContext;

    private Review[] mReviews;

    public ReviewListAdapter(Context context, Review[] data) {
        mContext = context;
        mReviews = data;
    }

    public ReviewListAdapter(PlaceActivity context, ArrayList<Review> data) {
        mContext = context;
        mReviews = (Review[]) data.toArray();
    }


    @Override
    public int getCount() {
        return mReviews.length;
    }

    @Override
    public Object getItem(int position) {
        return mReviews[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
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

        ReviewViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_place_row, parent, false);
            viewHolder = new ReviewViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ReviewViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(mReviews[position].getUser().getName());
        viewHolder.description.setText(mReviews[position].getDescription());
        viewHolder.icon.setImageResource(R.drawable.ic_launcher);

        return convertView;
    }

    //todo: for now we use the settings used for main activity's lists. it can be customized later
    class ReviewViewHolder {

        final TextView name;
        final TextView description;
        final ImageView icon;


        ReviewViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.place_name_text_view);
            description = (TextView) view.findViewById(R.id.place_description_text_view);
            icon = (ImageView) view.findViewById(R.id.list_icon_image);
        }
    }
}



