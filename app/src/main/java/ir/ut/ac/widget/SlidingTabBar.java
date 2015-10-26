/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ir.ut.ac.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import ir.ac.ut.berim.R;

public class SlidingTabBar extends HorizontalScrollView {

    private int mTextColor;

    private int mTextColorSelected;

    public static abstract class TabAdapter {

        public abstract TabItem[] getItems();

    }

    public static abstract class TabItem {

        public abstract String getTitle();

    }

    public interface IconTabProvider {

        public int getPageIconResId(int position);

    }

    // @formatter:off
    private static final int[] ATTRS = new int[]{
            android.R.attr.textSize,
            android.R.attr.textColor
    };

    private final LinearLayout.LayoutParams defaultTabLayoutParams;

    private final LinearLayout.LayoutParams expandedTabLayoutParams;

    private OnTabChangeListener mOnTabChangeListener;

    private OnVisibilityChangedListener mOnVisibilityChangedListener;

    private final PageListener pageListener = new PageListener();

    public ViewPager.OnPageChangeListener delegatePageListener;

    private final LinearLayout tabsContainer;

    private ViewPager mListPager;

    private int tabCount;

    private int currentPosition = -1;

    private float currentPositionOffset = 0f;

    private final Paint rectPaint;

    private final Paint dividerPaint;

    private boolean checkedTabWidths = false;

    private int indicatorColor = getResources().getColor(android.R.color.white);

    private int underlineColor = 0xFFE5E5E5;

    private int dividerColor = 0x55ffffff;

    private boolean shouldExpand = true;

    private boolean textAllCaps = true;

    private int scrollOffset = 52;

    private int indicatorHeight = 3;

    private int underlineHeight = 0;

    private int dividerPadding = 12;

    private int tabPadding = 24;

    private int dividerWidth = 0;

    private int tabTextSize = 14;

    private int tabTextColor = getResources().getColor(android.R.color.white);

    private int lastScrollX = 0;

    private int tabBackgroundResId = R.drawable.selector;

    private Locale locale;

    private int mMinTabCountToSnap;

    private int mMaxTabCountToSnap;

    private boolean mScrollSettled = false;

    public SlidingTabBar(Context context) {
        this(context, null);
        init();
    }

    public SlidingTabBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public SlidingTabBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();

        setFillViewport(true);
        setWillNotDraw(false);

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        tabsContainer.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        addView(tabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        scrollOffset = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        dividerPadding = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
        tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

        // get system attrs (android:textSize and android:textColor)

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
        tabTextColor = a.getColor(1, tabTextColor);

        a.recycle();

        // get custom attrs

        a = context.obtainStyledAttributes(attrs, R.styleable.SlidingTabBar);

        indicatorColor = a.getColor(R.styleable.SlidingTabBar_indicatorColor, indicatorColor);
        underlineColor = a.getColor(R.styleable.SlidingTabBar_underlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.SlidingTabBar_dividerColor, dividerColor);
        indicatorHeight = a
                .getDimensionPixelSize(R.styleable.SlidingTabBar_indicatorHeight, indicatorHeight);
        underlineHeight = a
                .getDimensionPixelSize(R.styleable.SlidingTabBar_underlineHeight, underlineHeight);
        dividerPadding = a
                .getDimensionPixelSize(R.styleable.SlidingTabBar_dividerPaddings, dividerPadding);
        tabPadding = a
                .getDimensionPixelSize(R.styleable.SlidingTabBar_tabPaddingLeftRight, tabPadding);
        tabBackgroundResId = a
                .getResourceId(R.styleable.SlidingTabBar_tabBackground, tabBackgroundResId);
        shouldExpand = a.getBoolean(R.styleable.SlidingTabBar_shouldExpand, shouldExpand);
        scrollOffset = a
                .getDimensionPixelSize(R.styleable.SlidingTabBar_scrollOffset, scrollOffset);
        textAllCaps = false;

        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }

        mMinTabCountToSnap = getContext().getResources()
                .getInteger(R.integer.min_tab_count_to_snap);
        mMaxTabCountToSnap = getContext().getResources()
                .getInteger(R.integer.max_tab_count_to_snap);

    }

    public void alwaysSnap() {
        mMinTabCountToSnap = 0;
        mMaxTabCountToSnap = Integer.MAX_VALUE;
    }

    private void init() {
        mTextColorSelected = getContext().getResources().getColor(android.R.color.white);
        mTextColor = getContext().getResources().getColor(R.color.text_tab);
    }

    public void setListPager(ViewPager pager) {
        this.mListPager = pager;
        pager.setOnPageChangeListener(pageListener);
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    private void updateTabStyles() {

        setBackgroundColor(getResources().getColor(R.color.tab_bar_bg));

        shouldExpand = tabCount <= mMaxTabCountToSnap && tabCount >= mMinTabCountToSnap;

        for (int i = 0; i < tabCount; i++) {
            View v = tabsContainer.getChildAt(i);

            v.setLayoutParams(defaultTabLayoutParams);
            v.setBackgroundResource(tabBackgroundResId);
            if (shouldExpand) {
                v.setPadding(0, 0, 0, 0);
            } else {
                v.setPadding(tabPadding, 0, tabPadding, 0);
            }
        }

        updateTabItemTextStyles();
    }

    private void updateTabItemTextStyles() {
        for (int i = 0; i < tabCount; i++) {
            View v = tabsContainer.getChildAt(i);

            if (v instanceof TextView) {

                TextView tab = (TextView) v;
                tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                if (i == currentPosition) {
                    tab.setTextAppearance(getContext(), R.style.TextAppearance_Small_Bold);
                    tab.setTextColor(mTextColorSelected);
                } else {
                    tab.setTextAppearance(getContext(), R.style.TextAppearance_Small);
                    tab.setTextColor(mTextColor);
                }

                // setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
                // pre-ICS-build
//                if (textAllCaps) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//                        tab.setAllCaps(true);
//                    } else {
//                        tab.setText(tab.getText().toString().toUpperCase(locale));
//                    }
//                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!shouldExpand || MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            return;
        }

        int myWidth = getMeasuredWidth();
        int childWidth = 0;
        for (int i = 0; i < tabCount; i++) {
            childWidth += tabsContainer.getChildAt(i).getMeasuredWidth();
        }

        if (!checkedTabWidths && childWidth > 0 && myWidth > 0) {

            if (childWidth <= myWidth) {
                for (int i = 0; i < tabCount; i++) {
                    tabsContainer.getChildAt(i).setLayoutParams(expandedTabLayoutParams);
                }
            }

            checkedTabWidths = true;
        }
    }

    public void scrollToChild(int position, int offset) {
        scrollToChild(position, offset, false);
    }

    private void scrollToChild(int position, int offset, boolean smooth) {

        if (tabCount == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;

            if (smooth && mScrollSettled) {
                final int finalNewScrollX = newScrollX;
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        smoothScrollTo(finalNewScrollX, 0);
                    }
                }, 600);
            } else {
                scrollTo(newScrollX, 0);
            }
            mScrollSettled = true;
        } else {
            scrollTo(newScrollX, 0);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || tabCount == 0) {
            return;
        }

        final int height = getHeight();

        // draw indicator line

        rectPaint.setColor(indicatorColor);

        // default: line below current tab
        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

        // if there is an offset, start interpolating left and right coordinates between current and next tab
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            lineLeft = (currentPositionOffset * nextTabLeft
                    + (1f - currentPositionOffset) * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight
                    + (1f - currentPositionOffset) * lineRight);
        }

        canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height, rectPaint);

        // draw underline

        rectPaint.setColor(underlineColor);
        canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);

        // draw divider

//        if (tabCount < 3) {
//            dividerPaint.setColor(dividerColor);
//            for (int i = 0; i < tabCount - 1; i++) {
//                View tab = tabsContainer.getChildAt(i);
//                canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(),
//                        height - dividerPadding,
//                        dividerPaint);
//            }
//        }
    }

    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            if (tabCount > 0) {
                scrollToChild(position,
                        (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()),
                        positionOffset == 0 && currentPositionOffset == 0);
                invalidate();
            }

            currentPosition = position;
            currentPositionOffset = positionOffset;

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(mListPager.getCurrentItem(), 0);
                updateTabItemTextStyles();
            }

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
        }

    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineColorResource(int resId) {
        this.underlineColor = getResources().getColor(resId);
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public void setDividerColorResource(int resId) {
        this.dividerColor = getResources().getColor(resId);
        invalidate();
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setUnderlineHeight(int underlineHeightPx) {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }

    public int getUnderlineHeight() {
        return underlineHeight;
    }

    public void setDividerPadding(int dividerPaddingPx) {
        this.dividerPadding = dividerPaddingPx;
        invalidate();
    }

    public int getDividerPadding() {
        return dividerPadding;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        requestLayout();
    }

    public boolean getShouldExpand() {
        return shouldExpand;
    }

    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }

    public void setTextSize(int textSizePx) {
        this.tabTextSize = textSizePx;
        updateTabStyles();
    }

    public int getTextSize() {
        return tabTextSize;
    }

    public void setTextColor(int textColor) {
        this.tabTextColor = textColor;
        updateTabStyles();
    }

    public void setTextColorResource(int resId) {
        this.tabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }

    public int getTextColor() {
        return tabTextColor;
    }

    public void setTabBackground(int resId) {
        this.tabBackgroundResId = resId;
    }

    public int getTabBackground() {
        return tabBackgroundResId;
    }

    public void setTabPaddingLeftRight(int paddingPx) {
        this.tabPadding = paddingPx;
        updateTabStyles();
    }

    public int getTabPaddingLeftRight() {
        return tabPadding;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {

        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public void setAdapter(TabAdapter adapter, boolean animateItems) {

        tabsContainer.removeAllViews();

        if (adapter.getItems() == null || adapter.getItems().length < 2) { // no tab, or one tab
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }

        tabCount = adapter.getItems().length;
        if (currentPosition == -1) { // not specified yet
            currentPosition = tabCount - 1; // select first tab
        }

        for (int i = 0; i < tabCount; i++) {
            TabItem tabInfo = adapter.getItems()[i];
            TextView tab = new TextView(getContext());
            tab.setText(tabInfo.getTitle().toUpperCase(locale));
            tab.setFocusable(true);
            tab.setGravity(Gravity.CENTER);
            tab.setSingleLine();
            final int position = i;
            tab.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mOnTabChangeListener != null) {
                        mOnTabChangeListener.onTabChange(position);
                        updateTabItemTextStyles();
                    }
                }
            });
            if (animateItems && tabCount > 2) {
                int level;
                    level = tabCount - i - 1;
                scheduleFadeAnimationFromStart(getContext(), tab, level);
            }
            tabsContainer.addView(tab);
        }

        updateTabStyles();

        checkedTabWidths = false;

        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                scrollToChild(currentPosition, 0);
            }
        });

    }

    public void setCurrentTab(int position) {
        currentPosition = position;
    }

    public void setOnTabChangeListener(OnTabChangeListener onTabChangeListener) {
        mOnTabChangeListener = onTabChangeListener;
    }

    public interface OnTabChangeListener {

        public void onTabChange(int position);
    }

    public interface OnVisibilityChangedListener {

        public void onVisibilityChanged(int visibility);
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public static void scheduleFadeAnimationFromStart(Context context, View view, float level) {
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        animation.setStartOffset((long) (100 * level));
        view.setAnimation(animation);
    }

}