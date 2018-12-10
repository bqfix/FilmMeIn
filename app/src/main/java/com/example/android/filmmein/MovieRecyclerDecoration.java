package com.example.android.filmmein;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class MovieRecyclerDecoration extends RecyclerView.ItemDecoration {

    private int mItemWidth;
    private Context mContext;
    private int mPosterWidth;
    private float mScreenDensity;
    private int mItemColumns;

    /**
     * @param context         of the Activity
     * @param numberOfColumns in the RecyclerView, determined and passed in in the onCreate of MainActivity
     */
    public MovieRecyclerDecoration(Context context, int numberOfColumns) {
        mContext = context;

        mScreenDensity = mContext.getResources().getDisplayMetrics().density;

        //The number of item columns
        mItemColumns = numberOfColumns;

        //Find the overall item width by dividing total screen width in pixels by the number of columns in the RecyclerView
        int screenWidth = (int) ((mContext.getResources().getDisplayMetrics().widthPixels) / mScreenDensity);
        mItemWidth = screenWidth / mItemColumns;

        //Block to get the width of the actual poster imageview from dimens file and divide by screen density to get int
        float posterWidthDp = mContext.getResources().getDimension(R.dimen.poster_width);
        mPosterWidth = (int) (posterWidthDp / mScreenDensity);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        //Determine the total whitespace width and posterspace width
        int totalPosterSpace = mPosterWidth * mItemColumns;
        int totalWhiteSpace = (mItemWidth * mItemColumns) - totalPosterSpace;

        //Each dividing column should be the same width.  Multiplied by screen density to get dp.
        int whiteSpacePerColumn = (int) ((totalWhiteSpace / mItemColumns) * mScreenDensity);

        //Causes left and right edge to be half width, dividing columns become full width
        outRect.left = whiteSpacePerColumn / 2;
        outRect.right = whiteSpacePerColumn / 2;

        //To give the bottom edge of each item a dynamic size
        outRect.bottom = whiteSpacePerColumn / 3;
    }
}
