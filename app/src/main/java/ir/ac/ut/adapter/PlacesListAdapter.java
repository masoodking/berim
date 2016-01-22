package ir.ac.ut.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

import ir.ac.ut.berim.R;
import ir.ac.ut.models.Place;
import ir.ac.ut.utils.ImageLoader;

/**
 * Created by masood on 10/1/15.
 */
public class PlacesListAdapter extends BaseAdapter{ //implements Filterable


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

        ImageLoader.getInstance()
                .display(mPlaces[position].getAvatar(), viewHolder.icon,R.drawable.no_photo);

        return convertView;
    }

//    @Override
//    public android.widget.Filter getFilter() {
//
//        Filter filter = new Filter() {
//
//            @Override
//            public boolean isLoggable(LogRecord record) {
//                return false;
//            }
//
//            @SuppressWarnings("unchecked")
//            @Override
//            protected void publishResults(CharSequence constraint, android.widget.Filter.FilterResults results) {
//
//                arrayListNames = (List<String>) results.values;
//                notifyDataSetChanged();
//            }
//
//            @Override
//            protected android.widget.Filter.FilterResults performFiltering(CharSequence constraint) {
//
//                android.widget.Filter.FilterResults results = new android.widget.Filter.FilterResults();
//                ArrayList<String> FilteredArrayNames = new ArrayList<String>();
//
//                // perform your search here using the searchConstraint String.
//
//                constraint = constraint.toString().toLowerCase();
//                for (int i = 0; i < mDatabaseOfNames.size(); i++) {
//                    String dataNames = mDatabaseOfNames.get(i);
//                    if (dataNames.toLowerCase().startsWith(constraint.toString()))  {
//                        FilteredArrayNames.add(dataNames);
//                    }
//                }
//
//                results.count = FilteredArrayNames.size();
//                results.values = FilteredArrayNames;
//                Log.e("VALUES", results.values.toString());
//
//                return results;
//            }
//        };
//
//        return filter;
//    }

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



