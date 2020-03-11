package com.qgyyzs.globalcosmetics.activity;

import android.content.Intent;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.JobTypeBean;
import com.qgyyzs.globalcosmetics.mvp.iface.JobTypeView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.JobTypePresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SelectJobtypeActivity extends BaseActivity implements JobTypeView{
    private JobTypePresenter jobTypePresenter=new JobTypePresenter(this,this);
    @BindView(R.id.mListView)
    ListView mListView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private List<String> mStringList;
    private ArrayAdapter<String> mArrayAdapter;
    private List<String> keyList;
    private String type;

    @Override
    protected int getLayout() {
        return R.layout.activity_listview_select;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        mStringList = new ArrayList<>();
        keyList = new ArrayList<>();

        mArrayAdapter = new ArrayAdapter<String>(SelectJobtypeActivity.this, android.R.layout.simple_list_item_1, mStringList);
        mListView.setAdapter(mArrayAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.autoRefresh();
            }
        }).start();
    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if(mStringList.size()==0){
                    jobTypePresenter.getJobType();
                }else {
                    refreshlayout.finishRefresh(500);
                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("value", mStringList.get(position));
                intent.putExtra("key", keyList.get(position));
                //记录下要返回给那个父亲，可能还有什么继父什么的
                LogUtils.e( mStringList.get(position).toString());
                setResult(001, intent);
                finish();
            }
        });
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        toolbar.setTitle("选择类型");
        refreshLayout.setRefreshHeader(new MaterialHeader(this)).setEnableHeaderTranslationContent(false);
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
            jsonObject.put("sign", type);
            jsonObject.toString();
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
    public void showJobTypeResult(JobTypeBean bean) {
        if(null!=refreshLayout)refreshLayout.finishRefresh();
        if(null==bean)return;
        keyList.clear();
        mStringList.clear();
        for(int i=0;i<bean.getJsonData().size();i++){
            keyList.add(bean.getJsonData().get(i).getKey()+"");
            mStringList.add(bean.getJsonData().get(i).getValue());
        }
        mArrayAdapter.notifyDataSetChanged();
    }
}
