package ir.ac.ut.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.ac.ut.berim.R;
import ir.ac.ut.models.Room;

/**
 * Created by saeed on 11/24/2015.
 */
public class ChatsListAdapter extends BaseAdapter {


    private Context mContext;

    private ArrayList<Room> mRooms;

    private LayoutInflater mLayoutInflater;

    public ChatsListAdapter(FragmentActivity context, ArrayList<Room> data) {
        mContext = context;
        mRooms = data;
        mLayoutInflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mRooms.size();
    }

    @Override
    public Room getItem(int position) {
        return mRooms.get(position);
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
            convertView = mLayoutInflater.inflate(R.layout.item_place_row, parent, false);
            viewHolder = new PlaceViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PlaceViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(mRooms.get(position).getValidName());
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
            icon = (ImageView) view.findViewById(R.id.user_avatar);
        }
    }
}
