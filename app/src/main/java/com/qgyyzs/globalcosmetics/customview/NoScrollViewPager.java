package com.qgyyzs.globalcosmetics.customview;

/**
 * Created by ZFG on 2017/03/08.
 * 描述:
 */

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 重写ViewPager，覆盖 onTouchEvent 和 onInterceptTouchEvent 方法，使其返回false，
 * 这样就等于禁止了ViewPager上的滑动事件。
 * 注意：禁止滑动的同时不能禁止 setCurrentItem 方法。
 */

public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean isCanScroll = true;
    /*   public void setScanScroll(boolean isCanScroll){
        this.isCanScroll = isCanScroll;
    }
    @Override
    public void scrollTo(int x, int y){
        if (isCanScroll){
            super.scrollTo(x, y);
        }
    }*/


    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        //onTouchEvent() 用于处理事件，返回值决定当前控件是否消费（consume）了这个事件
        //ACTION_MOVE或者ACTION_UP发生的前提是一定曾经发生了ACTION_DOWN，
        // 如果你没有消费ACTION_DOWN，那么系统会认为ACTION_DOWN没有发生过，
        // 所以ACTION_MOVE或者ACTION_UP就不能被捕获。
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //onInterceptTouchEvent()用于处理事件并改变事件的传递方向。
        //返回值为false时事件会传递给子控件的onInterceptTouchEvent()
        //返回值为true时事件会传递给当前控件的onTouchEvent()，而不在传递给子控件，这就是所谓的Intercept(截断)。
        return false;

    }
}
