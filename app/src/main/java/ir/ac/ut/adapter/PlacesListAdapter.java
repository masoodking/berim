package ir.ac.ut.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ir.ac.ut.berim.R;
import ir.ac.ut.models.Place;

/**
 * Created by masood on 10/1/15.
 */
public class PlacesListAdapter extends BaseAdapter {

    private Context mContext;

    private Place[] mPlaces;

    public PlacesListAdapter(Context context, Place[] data) {
        mContext = context;
        mPlaces = data;
    }




    @Override
    public int getCount() {
        return mPlaces.length;
    }

    @Override
    public Object getItem(int position) {
        return mPlaces[position];
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

        PlaceViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_place_row, parent, false);
            viewHolder = new PlaceViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PlaceViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(mPlaces[position].getName());
        viewHolder.description.setText(mPlaces[position].getAddress());
        viewHolder.icon.setImageResource(R.drawable.ic_launcher);

        return convertView;
    }

    class PlaceViewHolder {

        final TextView name;
        final TextView description;
        final ImageView icon;

        PlaceViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.place_name_text_view);
            description = (TextView) view.findViewById(R.id.place_description_text_view);
            icon = (ImageView) view.findViewById(R.id.list_icon_image);
        }
    }
}



