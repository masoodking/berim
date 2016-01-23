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
import ir.ac.ut.utils.ImageLoader;

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
            convertView = mLayoutInflater.inflate(R.layout.item_chat_row, parent, false);
            viewHolder = new PlaceViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PlaceViewHolder) convertView.getTag();
        }

        Room theRoom = mRooms.get(position);

        viewHolder.name.setText(theRoom.getValidName());
        if (theRoom.getLastMessage() != null
                && theRoom.getLastMessage().getSender() != null) {
            ImageLoader.getInstance()
                    .display(theRoom.getLastMessage().getSender().getAvatar(),
                            viewHolder.icon, R.drawable.ic_launcher);
            viewHolder.lastMessage.setText(theRoom.getLastMessage().getText());
        }
        if (theRoom.getUnreadMessageCount() > 0) {
            viewHolder.badge.setText(String.valueOf(theRoom.getUnreadMessageCount()));
            viewHolder.badge.setVisibility(View.VISIBLE);
        } else {
            viewHolder.badge.setVisibility(View.GONE);
        }
        return convertView;
    }

    class PlaceViewHolder {

        final TextView name;

        final TextView lastMessage;

        final ImageView icon;

        final TextView badge;

        PlaceViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.chat_list_name);
            lastMessage = (TextView) view.findViewById(R.id.chat_last_message);
            icon = (ImageView) view.findViewById(R.id.chat_list_icon);
            badge = (TextView) view.findViewById(R.id.chat_badge);
        }
    }
}
