package ir.ac.ut.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ir.ac.ut.berim.R;
import ir.ac.ut.models.User;

/**
 * Created by masood on 10/1/15.
 */
public class UserListAdapter extends BaseAdapter {

    private Context mContext;

    private User[] mUsers;

    public UserListAdapter(Context context, User[] data) {
        mContext = context;
        mUsers = data;
    }

    public void setData(User[] data){
        mUsers = data;
    }
    @Override
    public int getCount() {
        return mUsers.length;
    }

    @Override
    public User getItem(int position) {
        return mUsers[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PlaceViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_user_row, parent, false);
            viewHolder = new PlaceViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PlaceViewHolder) convertView.getTag();
        }
        User user = mUsers[position];
        viewHolder.name.setText(user.getNickName());
        viewHolder.description.setText(user.getPhoneNumber());
        viewHolder.icon.setImageResource(R.drawable.ic_action_user_default);
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



