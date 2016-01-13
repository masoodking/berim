package ir.ac.ut.utils;

import android.content.Context;

public class DimensionUtils {

    public static int convertDpToPx(Context context, int dp) {
        int px = Math.round(dp * context.getResources().getDisplayMetrics().density);
        return px;
    }

    public static int convertPxToDp(Context context, int px) {
        int dp = Math.round(px / context.getResources().getDisplayMetrics().density);
        return dp;
    }

}
