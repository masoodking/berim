package ir.ac.ut.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ir.ac.ut.berim.R;
import ir.ac.ut.models.Chat;

/**
 * Created by saeed on 11/24/2015.
 */
public class ChatsListAdapter extends BaseAdapter {


    private Context mContext;

    private Chat[] mChats;

    public ChatsListAdapter(FragmentActivity context, Chat[] data) {
        mContext = context;
        mChats = data;
    }





    @Override
    public int getCount() {
        return mChats.length;
    }

    @Override
    public Object getItem(int position) {
        return mChats[position];
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

        viewHolder.name.setText(mChats[position].getName());
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
