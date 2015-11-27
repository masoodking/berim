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
import ir.ac.ut.models.Message;

/**
 * Created by saeed on 11/24/2015.
 */
public class ChatAdapter extends BaseAdapter {


    private Context mContext;

    private Message[] mMessages;

    public ChatAdapter(FragmentActivity context, Message[] data) {
        mContext = context;
        mMessages = data;
    }





    @Override
    public int getCount() {
        return mMessages.length;
    }

    @Override
    public Object getItem(int position) {
        return mMessages[position];
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

        MessageViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_chat_message, parent, false);
            viewHolder = new MessageViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MessageViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(mMessages[position].getText());
        viewHolder.icon.setImageResource(R.drawable.ic_launcher);

        return convertView;
    }

    class MessageViewHolder {

        final TextView name;
        final TextView description;
        final ImageView icon;


        MessageViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.message_name_text_view);
            description = (TextView) view.findViewById(R.id.message_description_text_view);
            icon = (ImageView) view.findViewById(R.id.list_icon_image);
        }
    }
}
