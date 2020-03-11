package com.qgyyzs.globalcosmetics.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/12/12 0012.
 */

public class NoScrollListview extends ListView{

    public NoScrollListview(Context context) {
        super(context);
    }
    public NoScrollListview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public NoScrollListview(Context context, AttributeSet attrs) {
        super(context, attrs);}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
