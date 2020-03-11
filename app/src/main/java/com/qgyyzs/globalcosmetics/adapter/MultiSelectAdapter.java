package com.qgyyzs.globalcosmetics.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/4/28.
 */

public class MultiSelectAdapter extends CommonAdapter<String> {
    private static HashMap<Integer, Boolean> isSelected;
    private boolean isSingleselect;

    public MultiSelectAdapter(Activity context, List<String> stringList, boolean isSingleselect) {
        super(context,R.layout.multiselect_listview_item,stringList);
        this.isSingleselect = isSingleselect;
        isSelected = new HashMap<Integer, Boolean>();
    }

    public void addData(List<String> stringList){
        for (int i = 0; i < stringList.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    public HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public void convert(com.qgyyzs.globalcosmetics.base.ViewHolder viewHolder, String item, final int position) {
        CheckBox cb=viewHolder.getView(R.id.checkBox1);
        TextView tvName=viewHolder.getView(R.id.tv_device_name);
        cb.setChecked(getIsSelected().get(position)); // 根据isSelected来设置checkbox的选中状况
        tvName.setText(TextUtils.isEmpty(item)?"":item);
        cb.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (isSingleselect) {
                    if (position == 0) {
                        for (int i = 1; i < mDatas.size(); i++) {
                            getIsSelected().put(i, false);
                        }
                        notifyDataSetChanged();
                    } else {
                        getIsSelected().put(0, false);
                        notifyDataSetChanged();
                    }
                }
                if (isSelected.get(position)) {
                    isSelected.put(position, false);
                    setIsSelected(isSelected);
                } else {
                    isSelected.put(position, true);
                    setIsSelected(isSelected);
                }
            }
        });
    }
}
