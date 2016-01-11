package ir.ac.ut.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ir.ac.ut.berim.R;

/**
 * Created by Masood on 1/10/2016 AD.
 */
public class BerimHeader extends RelativeLayout {

    private Context mContext;

    private View mRootView;

    private ImageView mNewUserIcon;

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
    }

    public void showNewUserIcon(int iconDrawable, OnClickListener onClickListener){
        mNewUserIcon.setImageResource(iconDrawable);
        mNewUserIcon.setOnClickListener(onClickListener);
        mNewUserIcon.setVisibility(VISIBLE);
    }

    public void hideNewUserIcon(){
        mNewUserIcon.setVisibility(GONE);
    }
}
