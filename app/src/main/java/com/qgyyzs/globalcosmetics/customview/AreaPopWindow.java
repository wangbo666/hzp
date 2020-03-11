package com.qgyyzs.globalcosmetics.customview;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;
import com.qgyyzs.globalcosmetics.base.ViewHolder;
import com.qgyyzs.globalcosmetics.mvp.iface.ListStringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.CityPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/6 0006.
 */

public class AreaPopWindow extends PopupWindow implements ListStringView {
    private Activity context;
    private SharedPreferences mSharedPreferences;
    private String provinceStr="",cityStr="";
    private CityPresenter presenter;
    private String provinceString;
    private List<String> mProvinceList = new ArrayList<>();
    private List<String> mCityList = new ArrayList<>();
    private RecyclerView recyclerView1,recyclerView2;
    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;
    private SettingAreaListener mSListener = null;
    public AreaPopWindow(final Activity context) {
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conentView = inflater.inflate(R.layout.dialog_area_single, null);
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

        presenter=new CityPresenter(this, (Activity) context);
        mSharedPreferences=context.getSharedPreferences(MyApplication.CONSTACTDATA,Context.MODE_PRIVATE);
        provinceString= mSharedPreferences.getString("province","");
        recyclerView1=  conentView.findViewById(R.id.recyclerView1);
        recyclerView2=  conentView.findViewById(R.id.recyclerView2);
        recyclerView1.setLayoutManager(new LinearLayoutManager(context));
        recyclerView2.setLayoutManager(new LinearLayoutManager(context));

        provinceString = "全国," + provinceString;
        String str[]=provinceString.split(",");
        for(int i=0;i<str.length;i++){
            mProvinceList.add(str[i]);
        }
        provinceAdapter=new ProvinceAdapter(context,mProvinceList);
        recyclerView1.setAdapter(provinceAdapter);
        provinceAdapter.setSelectedPosition(0);
        provinceAdapter.notifyDataSetChanged();
        cityAdapter=new CityAdapter(context,mCityList);
        recyclerView2.setAdapter(cityAdapter);
    }

    public void setOnSettingAreaListener(SettingAreaListener listener) {
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
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("province", provinceStr);
            LogUtils.e( jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showStringListResult(List<String> list) {
        mCityList.clear();
        if(list==null||list.size()==0) {
            if (mSListener != null) {
                mSListener.onSettingArea(provinceStr,"");
            }
            dismiss();
            return;
        }
        mCityList.add("全省");
        mCityList.addAll(list);
        cityAdapter.notifyDataSetChanged();
    }

    public interface SettingAreaListener {
        void onSettingArea(String province,String city);
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }

    private class ProvinceAdapter extends CommonAdapter<String> {

        private int selectedPosition = -1;// 选中的位置

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public ProvinceAdapter(Activity context, List<String> biduserBeanList) {
            super(context, R.layout.adapter_sortproduct2, biduserBeanList);
        }

        @Override
        public void convert(ViewHolder viewHolder, final String item, final int position) {
            TextView mItemProduct1Tv = viewHolder.getView(R.id.mTvSortproductAdapter);
            mItemProduct1Tv.setText(TextUtils.isEmpty(item) ? "" : item);
            mItemProduct1Tv.setTextColor(selectedPosition == position ? context.getResources().getColor(R.color.title_color) : context.getResources().getColor(R.color.color_grey_666666));

            viewHolder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    provinceStr=item;
                    provinceAdapter.setSelectedPosition(position);
                    provinceAdapter.notifyDataSetChanged();
                    cityAdapter.setSelectedPosition(0);
                    cityAdapter.notifyDataSetChanged();
                    presenter.getCity();
                }
            });
        }
    }

    private class CityAdapter extends CommonAdapter<String> {

        private int selectedPosition = -1;// 选中的位置

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public CityAdapter(Activity context, List<String> biduserBeanList) {
            super(context, R.layout.adapter_sortproduct2, biduserBeanList);
        }

        @Override
        public void convert(ViewHolder viewHolder, final String item, final int position) {
            TextView mItemProduct1Tv = viewHolder.getView(R.id.mTvSortproductAdapter);
            mItemProduct1Tv.setText(TextUtils.isEmpty(item) ? "" : item);
            mItemProduct1Tv.setTextColor(selectedPosition == position ? context.getResources().getColor(R.color.title_color) : context.getResources().getColor(R.color.color_grey_666666));

            viewHolder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cityStr = item;
                    cityAdapter.setSelectedPosition(position);
                    cityAdapter.notifyDataSetChanged();
                    if (mSListener != null) {
                        mSListener.onSettingArea(provinceStr, cityStr);
                    }
                    dismiss();
                }
            });
        }
    }
}
