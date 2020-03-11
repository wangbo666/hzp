package com.qgyyzs.globalcosmetics.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.RxActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;
import com.qgyyzs.globalcosmetics.base.ViewHolder;
import com.qgyyzs.globalcosmetics.bean.ProductClassBean;
import com.qgyyzs.globalcosmetics.mvp.iface.SortProductView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.SortProductBigPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.SortProductSmallPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/9 0006.
 */

public class SortProductPopWindow extends PopupWindow implements SortProductView {
    private Activity context;
    private SortProductBigPresenter bigPresenter;
    private SortProductSmallPresenter smallPresenter;
    private String proType1="全部", proType2="全部";
    private int typeId1,typeId2;
    private RecyclerView recyclerView1, recyclerView2;
    private List<ProductClassBean> mProductClassBeenList;
    private Sort1ProductAdapter sort1Adapter;
    private List<ProductClassBean> mSort2List;
    private Sort2ProductAdapter sort2Adapter;
    private int classId;
    private int sortposition1 = 0, sortposition2 = 0;
    private SettingSortListener mSListener = null;

    public SortProductPopWindow(final Activity context) {
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conentView = inflater.inflate(R.layout.dialog_area_single, null);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);

        recyclerView1 = conentView.findViewById(R.id.recyclerView1);
        recyclerView2 = conentView.findViewById(R.id.recyclerView2);
        recyclerView1.setLayoutManager(new LinearLayoutManager(context));
        recyclerView2.setLayoutManager(new LinearLayoutManager(context));

        mProductClassBeenList = new ArrayList<>();//分类1
        mSort2List = new ArrayList<>();//分类2

        bigPresenter = new SortProductBigPresenter(this, (RxActivity) context);
        bigPresenter.getBigSort();
        smallPresenter = new SortProductSmallPresenter(sortProductView2, (RxActivity) context);

        sort1Adapter = new Sort1ProductAdapter(context, mProductClassBeenList);//分类的adapter
        recyclerView1.setAdapter(sort1Adapter);
        sort2Adapter = new Sort2ProductAdapter(context, mSort2List);
        recyclerView2.setAdapter(sort2Adapter);
    }

    public void setOnSettingSortListener(SettingSortListener listener) {
        mSListener = listener;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void closeLoading() {

    }

    @Override
    public String getJsonString() {
        return null;
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showSortResult(List<ProductClassBean> list) {
        if (null == list || list.size() == 0) return;
        mProductClassBeenList.clear();
        mProductClassBeenList.add(new ProductClassBean(-1,"全部"));
        mProductClassBeenList.addAll(list);
        classId = mProductClassBeenList.get(sortposition1).getId();
        sort1Adapter.setSelectedPosition(sortposition1);
        sort1Adapter.notifyDataSetChanged();
        smallPresenter.getSmallSort();
    }

    private SortProductView sortProductView2=new SortProductView() {
        @Override
        public void showSortResult(List<ProductClassBean> list) {
            if (null == list) return;
            mSort2List.clear();
            mSort2List.add(new ProductClassBean(-1,"全部"));
            mSort2List.addAll(list);
            sort2Adapter.setSelectedPosition(sortposition2);
            sort2Adapter.notifyDataSetChanged();
        }

        @Override
        public void showLoading() {

        }

        @Override
        public void closeLoading() {

        }

        @Override
        public String getJsonString() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ClassID", "" + classId);
                LogUtils.e(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }

        @Override
        public void showToast(String msg) {

        }
    };

    public interface SettingSortListener {
        void onSettingSort(String proType1, String proType2,int typeId1,int typeId2);
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }

    private class Sort1ProductAdapter extends CommonAdapter<ProductClassBean> {

        private int selectedPosition = -1;// 选中的位置

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public Sort1ProductAdapter(Activity context, List<ProductClassBean> biduserBeanList) {
            super(context, R.layout.adapter_sortproduct, biduserBeanList);
        }

        @Override
        public void convert(com.qgyyzs.globalcosmetics.base.ViewHolder viewHolder, final ProductClassBean biduserBean, final int position) {
            TextView mItemProduct1Tv = viewHolder.getView(R.id.mTvSortproductAdapter);
            mItemProduct1Tv.setText(TextUtils.isEmpty(biduserBean.getType1()) ? "" : biduserBean.getType1());
            mItemProduct1Tv.setTextColor(selectedPosition == position ? context.getResources().getColor(R.color.main_color) : context.getResources().getColor(R.color.item_title));

            viewHolder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    classId = biduserBean.getId();
                    proType1 = biduserBean.getType1();
                    smallPresenter.getSmallSort();
                    sort1Adapter.setSelectedPosition(position);
                    sort1Adapter.notifyDataSetChanged();
                    typeId1=classId;
                }
            });
        }
    }

    private class Sort2ProductAdapter extends CommonAdapter<ProductClassBean> {

        private int selectedPosition = -1;// 选中的位置

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public Sort2ProductAdapter(Activity context, List<ProductClassBean> biduserBeanList) {
            super(context, R.layout.adapter_sortproduct2, biduserBeanList);
        }

        @Override
        public void convert(ViewHolder viewHolder, final ProductClassBean item, final int position) {
            TextView mItemProduct1Tv = viewHolder.getView(R.id.mTvSortproductAdapter);
            mItemProduct1Tv.setText(TextUtils.isEmpty(item.getType1()) ? "" : item.getType1());
            mItemProduct1Tv.setTextColor(selectedPosition == position ? context.getResources().getColor(R.color.title_color) : context.getResources().getColor(R.color.color_grey_666666));

            viewHolder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    proType2 = item.getType1();
                    typeId2=item.getId();
                    sort2Adapter.setSelectedPosition(position);
                    sort2Adapter.notifyDataSetChanged();
                    if (mSListener != null) {
                        mSListener.onSettingSort(proType1, proType2,typeId1,typeId2);
                    }
                    dismiss();
                }
            });
        }
    }
}
