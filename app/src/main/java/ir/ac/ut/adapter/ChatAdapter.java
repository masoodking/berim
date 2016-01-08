package ir.ac.ut.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ir.ac.ut.berim.ProfileUtils;
import ir.ac.ut.berim.R;
import ir.ac.ut.models.Message;

/**
 * Created by saeed on 11/24/2015.
 */
public class ChatAdapter extends BaseAdapter {


    private Context mContext;

    private ArrayList<Message> mMessages;

    public ChatAdapter(FragmentActivity context, ArrayList<Message> data) {
        mContext = context;
        mMessages = data;
    }





    @Override
    public int getCount() {
        return mMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  0;
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

        Message chatMessage = (Message) getItem(position);
        MessageViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_chat_message, parent, false);
            viewHolder = new MessageViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MessageViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(mMessages.get(position).getText());
        LinearLayout chatLayout = (LinearLayout) convertView.findViewById(R.id.chat_background_linear_layout);
        LinearLayout chatScrennLayout = (LinearLayout) convertView.findViewById(R.id.chat_screen_linear_layout);

        if(position%2==0) {//todo change this if
            chatLayout.setBackgroundResource(R.drawable.bubble);
            chatScrennLayout.setGravity(Gravity.RIGHT);
        }
        else {
            chatLayout.setBackgroundResource(R.drawable.theirbubble);
            chatScrennLayout.setGravity(Gravity.LEFT);
        }
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
