package com.qgyyzs.globalcosmetics.customview;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.WebBaseActivity;
import com.qgyyzs.globalcosmetics.bean.UPMarqueeViewData;
import com.qgyyzs.globalcosmetics.http.retrofit.RetrofitUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/6/6.
 */

public class UPMarqueeView extends ViewFlipper {
    private Context mContext;
    /**
     * 是否开启动画
     */
    private boolean isSetAnimDuration = true;
    /**
     * 时间间隔
     */
    private int interval = 3000;
    /**
     * 动画时间
     */
    private int animDuration = 500;

    public UPMarqueeView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public UPMarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        setFlipInterval(interval);
        if (isSetAnimDuration) {
            Animation animIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_in);
            animIn.setDuration(animDuration);
            setInAnimation(animIn);
            Animation animOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_out);
            animOut.setDuration(animDuration);
            setOutAnimation(animOut);
        }
    }

    /**
     * 设置循环滚动的View数组
     *
     * @param// views
     */
    public void setViews(final List<UPMarqueeViewData> datas) {
        LinearLayout item;
        TextView mTvHeadLine;
        if (datas == null || datas.size() == 0) return;
        int size = datas.size();
        for (int i = 0; i < size; i += 1) {
            final int position = i;
            //根布局
            item = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_scroll, null);
            //控件赋值
            mTvHeadLine= (TextView) item.findViewById(R.id.tvHeadLine);
            mTvHeadLine.setText(datas.get(position).getValue());
            //设置监听
            mTvHeadLine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, WebBaseActivity.class);
                    intent.putExtra("title", datas.get(position).getValue());
                    intent.putExtra("type","medical");
                    intent.putExtra("toutiao","toutiao");
                    intent.putExtra("url", RetrofitUtils.BASE_API + "artcle/artcleinfo?Type=1&id=" + datas.get(position).getId());
                    mContext.startActivity(intent);
                }
            });
            addView(item);
        }
    }

    public boolean isSetAnimDuration() {
        return isSetAnimDuration;
    }

    public void setSetAnimDuration(boolean isSetAnimDuration) {
        this.isSetAnimDuration = isSetAnimDuration;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getAnimDuration() {
        return animDuration;
    }

    public void setAnimDuration(int animDuration) {
        this.animDuration = animDuration;
    }
}
