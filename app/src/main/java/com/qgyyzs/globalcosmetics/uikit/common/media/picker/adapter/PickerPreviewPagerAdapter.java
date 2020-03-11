package com.qgyyzs.globalcosmetics.uikit.common.media.picker.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.uikit.common.activity.UI;
import com.qgyyzs.globalcosmetics.uikit.common.media.picker.activity.PickerAlbumPreviewActivity;
import com.qgyyzs.globalcosmetics.uikit.common.media.picker.model.PhotoInfo;
import com.qgyyzs.globalcosmetics.uikit.common.ui.imageview.BaseZoomableImageView;
import com.qgyyzs.globalcosmetics.uikit.common.util.sys.ScreenUtil;

import java.util.List;

public class PickerPreviewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<PhotoInfo> mList;
    private LayoutInflater mInflater;
    private int viewPagerWidth, viewPagerHeight;
    private UI mActivity;

    public PickerPreviewPagerAdapter(Context cx, List<PhotoInfo> list, LayoutInflater inflater, int width, int height, UI activity) {
        mContext = cx;
        mList = list;
        mInflater = inflater;
        viewPagerHeight = height;
        viewPagerWidth = width;
        mActivity = activity;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View layout = (View) object;
        BaseZoomableImageView iv = (BaseZoomableImageView) layout.findViewById(R.id.imageView);
        iv.clear();
        container.removeView(layout);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View layout = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            layout = mInflater.inflate(R.layout.nim_preview_image_layout_multi_touch, null);
        } else {
            layout = mInflater.inflate(R.layout.nim_preview_image_layout_zoom_control, null);
        }

        container.addView(layout);
        layout.setTag(position);
        viewPagerWidth = ScreenUtil.screenWidth;
        viewPagerHeight = ScreenUtil.screenHeight;

        return layout;
    }

    private void finishUpdate() {

    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void setPrimaryItem(View container, int position, Object object) {
        ((PickerAlbumPreviewActivity) mActivity).updateCurrentImageView(position);
    }
}
