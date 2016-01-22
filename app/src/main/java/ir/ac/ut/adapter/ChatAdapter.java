package ir.ac.ut.adapter;

import android.content.Context;
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
import ir.ac.ut.utils.ImageLoader;

/**
 * Created by saeed on 11/24/2015.
 */
public class ChatAdapter extends BaseAdapter {


    private static final int FROM_ME = 0;
    private static final int FROM_HER = 1;
    private Context mContext;

    private ArrayList<Message> mMessages;

    public ChatAdapter(Context context, ArrayList<Message> data) {
        mContext = context;
        mMessages = data;
    }


    @Override
    public int getCount() {
        return mMessages.size();
    }

    @Override
    public Message getItem(int position) {
        return mMessages.get(position);
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
        int listViewItemType = getItemViewType(position);
        Message chatMessage = mMessages.get(position);
        MessageViewHolder viewHolder = null;
        if (convertView == null) {
            if (listViewItemType == FROM_ME) {
                convertView = LayoutInflater.from(mContext)
                        .inflate(R.layout.item_chat_message, parent, false);
                viewHolder = new MessageViewHolder(convertView);
                convertView.setTag(viewHolder);

            } else {//IT'S A MESSEGE FROM OTHERS
                convertView = LayoutInflater.from(mContext)
                        .inflate(R.layout.item_chat_message_her, parent, false);
                viewHolder = new MessageViewHolder(convertView);
                viewHolder.sender.setText(chatMessage.getSender().getValidUserName());
                convertView.setTag(viewHolder);
                ImageLoader.getInstance()
                        .display(chatMessage.getSender().getAvatar(), viewHolder.icon,
                                R.drawable.default_avatar);
            }
        } else {
            viewHolder = (MessageViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(chatMessage.getText());
//        LinearLayout chatLayout = (LinearLayout) convertView
//                .findViewById(R.id.chat_background_linear_layout);
//        LinearLayout chatScrennLayout = (LinearLayout) convertView
//                .findViewById(R.id.chat_screen_linear_layout);

//        if (ProfileUtils.getUser(mContext).getId()
//                .equals(chatMessage.getSender().getId())) {
//            chatLayout.setBackgroundResource(R.drawable.bubble);
//            chatScrennLayout.setGravity(Gravity.RIGHT);
//        } else {
//            chatLayout.setBackgroundResource(R.drawable.theirbubble);
//            chatScrennLayout.setGravity(Gravity.LEFT);
//        }
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (ProfileUtils.getUser(mContext).getId()
                .equals(mMessages.get(position).getSender().getId())) {
            return FROM_ME;
        }
        return FROM_HER;
    }

    class MessageViewHolder {

        final TextView name;

        final TextView description;
        final TextView sender;

        final ImageView icon;

        MessageViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.message_name_text_view);
            description = (TextView) view.findViewById(R.id.message_description_text_view);
            sender = (TextView) view.findViewById(R.id.chat_sender);
            icon = (ImageView) view.findViewById(R.id.chat_sender_avatar);
        }
    }
}
