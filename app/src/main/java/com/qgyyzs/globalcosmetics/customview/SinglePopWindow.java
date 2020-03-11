package com.qgyyzs.globalcosmetics.customview;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/11/9 0009.
 */

public class SinglePopWindow extends PopupWindow{
    private Activity context;
    private List<String> DataList;
    private RecyclerView recyclerView;
    private SinglePopAdapter adapter;
    private SettingSingleListener mSListener = null;
    public SinglePopWindow(final Activity context,List<String> DataList) {
        this.context = context;
        this.DataList=DataList;
        initView();
    }
    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conentView = inflater.inflate(R.layout.view_recycleview, null);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        this.setContentView(conentView);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();

        recyclerView= conentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter=new SinglePopAdapter(context,DataList);
        recyclerView.setAdapter(adapter);

    }
    public void setOnSettingSingleListener(SettingSingleListener listener) {
        mSListener = listener;
    }

    public interface SettingSingleListener {
        void onSettingSingle(int position);
    }

    private class SinglePopAdapter extends CommonAdapter<String> {

        private int selectedPosition = 1;// 选中的位置
        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public SinglePopAdapter(Activity context,List<String> dataList) {
            super(context,R.layout.adapter_single,dataList);
        }

        @Override
        public void convert(com.qgyyzs.globalcosmetics.base.ViewHolder viewHolder, String item, final int position) {
            viewHolder.getView(R.id.mImageGou).setVisibility(selectedPosition==position?View.VISIBLE:View.INVISIBLE);
            viewHolder.setText(R.id.mTvTxt,item);

            viewHolder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.setSelectedPosition(position);
                    adapter.notifyDataSetChanged();
                    if(mSListener!=null){
                        mSListener.onSettingSingle(position);
                    }
                    dismiss();
                }
            });
        }
    }
}
