package ir.ac.ut.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ir.ac.ut.berim.R;

/**
 * Created by Masood on 1/10/2016 AD.
 */
public class BerimHeader extends RelativeLayout {

    private Context mContext;

    private View mRootView;

    private ImageView mNewUserIcon;

    private ImageView mLeftActionIcon;

    private TextView mTitle;

    public BerimHeader(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public BerimHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public BerimHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }


    private void init() {
        mRootView = LayoutInflater.from(getContext())
                .inflate(R.layout.berim_header, this, true);

        mNewUserIcon = (ImageView) mRootView.findViewById(R.id.header_new_user);
        mLeftActionIcon = (ImageView) mRootView.findViewById(R.id.header_left_action);
        mTitle = (TextView) findViewById(R.id.header_title);
    }

    public void showRightAction(int iconDrawable, OnClickListener onClickListener){
        mNewUserIcon.setImageResource(iconDrawable);
        mNewUserIcon.setOnClickListener(onClickListener);
        mNewUserIcon.setVisibility(VISIBLE);
    }

    public void showLeftAction(int iconDrawable, OnClickListener onClickListener){
        mLeftActionIcon.setImageResource(iconDrawable);
        mLeftActionIcon.setOnClickListener(onClickListener);
        mLeftActionIcon.setVisibility(VISIBLE);
    }

    public void hideRightAction(){
        mNewUserIcon.setVisibility(GONE);
    }

    public void setTitle(String title){
        mTitle.setText(title);
        mTitle.setVisibility(VISIBLE);
    }

    public void hideTitle(){
        mTitle.setVisibility(GONE);
    }
}
