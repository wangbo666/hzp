package com.qgyyzs.globalcosmetics.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.adapter.MyFliterAdapter;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.bean.ProductClassBean;
import com.qgyyzs.globalcosmetics.mvp.iface.SortProductView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.SortProductBigPresenter2;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.MyProxyActivity;
import com.qgyyzs.globalcosmetics.activity.SearchActivity;
import com.qgyyzs.globalcosmetics.adapter.GridviewFliterAdapter;
import com.qgyyzs.globalcosmetics.adapter.ProxyInfoAdapter;
import com.qgyyzs.globalcosmetics.base.BaseFragment;
import com.qgyyzs.globalcosmetics.bean.ProxyInfoBean;
import com.qgyyzs.globalcosmetics.customview.AreaPopWindow;
import com.qgyyzs.globalcosmetics.customview.EmptyRecyclerView;
import com.qgyyzs.globalcosmetics.customview.MyGridView;
import com.qgyyzs.globalcosmetics.mvp.iface.ProxyInfoView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ProxyInfoPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.ScreenUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

public class ProxyInfoFragment extends BaseFragment implements ProxyInfoView,AreaPopWindow.SettingAreaListener,SortProductView{
    private ProxyInfoPresenter proxyInfoPresenter;
    private SharedPreferences mSharedPreferences;
    @BindView(R.id.filter_tv)
    TextView mFilterTv;
    @BindView(R.id.province_tv)
    TextView mProvinceTv;
    @BindView(R.id.left_tv)
    TextView mLeftTextView;
    @BindView(R.id.LinearProvince)
    LinearLayout LinearProvince;
    @BindView(R.id.LinearFilter)
    LinearLayout LinearFilter;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.mTitleBarTv)
    LinearLayout titleBar;
    @BindView(R.id.title_text)
    TextView mTitleTv;
    @BindView(R.id.empty_view)
    View emptyView;
    @BindView(R.id.LinearSearch)
    LinearLayout mLinearSearch;
    @BindView(R.id.tv_search)
    TextView mSearchTv;

    private String searchText="";

    private List<ProxyInfoBean.JsonData> mProxyBeanList;
    private ProxyInfoAdapter mProxyListAdapter;
    private Dialog dialog;
    private MyGridView mGridView3,mGridView4;
    private TextView yaopinTypeTv;
    private GridviewFliterAdapter mGridviewFliterAdapter3;
    private MyFliterAdapter mGridviewFliterAdapter4;
    private List<String> stringList3;
    private List<ProductClassBean> stringList4;
    private Button mButton, mResetButton;

    private int cur = 1;//显示列表的页码号
    private String[] channelstrs;
    private String ChannelString;
    private AreaPopWindow areaWindow;
    private String province = "", city = "";

    private SortProductBigPresenter2 bigPresenter=new SortProductBigPresenter2(this, (RxFragmentActivity) getActivity());

    private Map<Integer, Boolean> typeChooseMap = new HashMap<Integer, Boolean>();
    private Map<Integer, Boolean> channelChooseMap = new HashMap<Integer, Boolean>();

    @Override
    protected int getLayout() {
        return R.layout.fragment_proxyinfo;
    }

    @Override
    protected void initView(View view) {
        mSharedPreferences = getActivity().getSharedPreferences(MyApplication.CONSTACTDATA, Context.MODE_PRIVATE);
        ChannelString=mSharedPreferences.getString("channel","");

        mTitleTv.setText("代理信息");
        try {
            titleBar.setVisibility(TextUtils.isEmpty(getArguments().getString("hide"))?View.VISIBLE:View.GONE);
        }catch (Exception e){

        }

//        refreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));


        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_fliter);
        Window dialogWindow = dialog.getWindow();
        mGridView3 = (MyGridView) dialogWindow.findViewById(R.id.fliter3_gridview);
        mGridView4 = (MyGridView) dialogWindow.findViewById(R.id.fliter4_gridview);
        mResetButton = (Button) dialogWindow.findViewById(R.id.fliter_reset_btn);
        yaopinTypeTv=(TextView)dialogWindow.findViewById(R.id.top4);
        mButton = (Button) dialogWindow.findViewById(R.id.fliter_ok_btn);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        lp.width = ScreenUtils.getScreenWidth(getActivity()); // 宽度
        lp.alpha = 1f; // 透明度
        dialogWindow.setAttributes(lp);
    }

    @Override
    protected void initData() {
        stringList3 = new ArrayList<>();
        stringList4 = new ArrayList<>();

        mProxyBeanList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mProxyListAdapter = new ProxyInfoAdapter(getActivity(),mProxyBeanList);
        recyclerView.setAdapter(mProxyListAdapter);

        proxyInfoPresenter=new ProxyInfoPresenter(this, (RxFragmentActivity) getActivity());
        bigPresenter.getBigSort();

        if (!TextUtils.isEmpty(ChannelString)) {
            channelstrs = ChannelString.split(",");
            for (int j = 0; j < channelstrs.length; j++) {
                stringList3.add(channelstrs[j]);
            }
            mGridviewFliterAdapter3 = new GridviewFliterAdapter(getActivity(),stringList3, true);
            mGridView3.setAdapter(mGridviewFliterAdapter3);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.autoRefresh();
            }
        }).start();
    }

    @Override
    protected void initListener() {
        mGridView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isSelect1(position)) {
                    view.setActivated(false);
                    typeChooseMap.put(position, false);
                    mGridviewFliterAdapter3.changeState(position);
                } else {
                    if(getMapValue().split(",").length>=5){
                        ToastUtil.showToast(mContext,"最多只可选择5个渠道",true);
                        return;
                    }
                    view.setActivated(true);
                    typeChooseMap.put(position, true);
                    mGridviewFliterAdapter3.changeState(position);
                }
            }
        });
        mGridView4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isSelect2(position)) {
                    view.setActivated(false);
                    channelChooseMap.put(position, false);
                    mGridviewFliterAdapter4.changeState(position);
                } else {
                    if(getChannelMapValue().split(",").length>=5){
                        ToastUtil.showToast(mContext,"最多只可选择5个分类",true);
                        return;
                    }
                    view.setActivated(true);
                    channelChooseMap.put(position, true);
                    mGridviewFliterAdapter4.changeState(position);
                }
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur = 1;
                refreshLayout.autoRefresh();
                dialog.dismiss();
            }
        });

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGridviewFliterAdapter3 = new GridviewFliterAdapter(getActivity(),stringList3, true);
                mGridView3.setAdapter(mGridviewFliterAdapter3);
                mGridviewFliterAdapter3.notifyDataSetChanged();

                mGridviewFliterAdapter4 = new MyFliterAdapter(getActivity(),stringList4, true);
                mGridView4.setAdapter(mGridviewFliterAdapter4);
                mGridviewFliterAdapter4.notifyDataSetChanged();
                typeChooseMap.clear();
                channelChooseMap.clear();
            }
        });
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                cur++;
                proxyInfoPresenter.getProxyList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                cur = 1;
                proxyInfoPresenter.getProxyList();
            }
        });
    }

    @OnClick({ R.id.LinearFilter, R.id.LinearProvince,R.id.left_tv,R.id.LinearSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.LinearSearch:
                Intent intent=new Intent(getActivity(),SearchActivity.class);
                intent.putExtra("searchType","proxyinfo_history");
                intent.putExtra("searchContent",searchText);
                startActivityForResult(intent,001);
                break;
            case R.id.left_tv:
                startActivity(new Intent(getActivity(), MyProxyActivity.class));
                break;
            case R.id.LinearFilter:
                mGridView4.setVisibility(View.VISIBLE);
                yaopinTypeTv.setVisibility(View.VISIBLE);
                dialog.show();
                break;
            case R.id.LinearProvince:
                if(areaWindow==null) {
                    areaWindow = new AreaPopWindow(getActivity());
                    areaWindow.setOnSettingAreaListener(this);
                    areaWindow.showAsDropDown(LinearProvince);
                }else{
                    areaWindow.setOnSettingAreaListener(this);
                    areaWindow.showAsDropDown(LinearProvince);
                }
                break;
        }
    }

    @Override
    public void onSettingArea(String province, String city) {
        this.province = province;
        this.city = city;
        if (city.equals("全省")||city.equals("")) {
            mProvinceTv.setText(province);
        }else{
            mProvinceTv.setText(city);
        }
        cur = 1;
        refreshLayout.autoRefresh();
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
            jsonObject.put("curpage", cur);
            jsonObject.put("pagesize", 10);
            jsonObject.put("keyword",searchText);
            if(!TextUtils.isEmpty(getMapValue()))
                jsonObject.put("channels", getMapValue());
            if(!TextUtils.isEmpty(getChannelMapValue()))
                jsonObject.put("classid", getChannelMapValue());
            jsonObject.put("province", province.equals("全国")?"":province);//省份
            jsonObject.put("city", city.equals("全省")?"":city);//城市
            LogUtils.e(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showProxyInfoResult(ProxyInfoBean bean) {
        if(null!=refreshLayout&&refreshLayout.isRefreshing()) refreshLayout.finishRefresh();
        if(null!=refreshLayout&&refreshLayout.isLoading())refreshLayout.finishLoadmore();
        if(null!=recyclerView)recyclerView.setEmptyView(emptyView);
        if(bean==null||bean.getJsonData()==null)  return;
        if(cur==1)mProxyBeanList.clear();
        mProxyBeanList.addAll(bean.getJsonData());
        mProxyListAdapter.notifyDataSetChanged();
        if(TextUtils.isEmpty(bean.getMsg()))return;
        refreshLayout.setLoadmoreFinished(cur >= Integer.parseInt(bean.getMsg())?true:false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            searchText=data.getStringExtra("searchStr");
            cur=1;
            refreshLayout.autoRefresh();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            if(null!=refreshLayout&&refreshLayout.isRefreshing()) refreshLayout.finishRefresh();
            if(null!=refreshLayout&&refreshLayout.isLoading())refreshLayout.finishLoadmore();
        }
    }

    @Override
    public void showSortResult(List<ProductClassBean> list) {
        if(list==null||list.size()==0)return;
        stringList4.clear();
        stringList4.addAll(list);
        mGridviewFliterAdapter4 = new MyFliterAdapter(getActivity(),stringList4, true);
        mGridView4.setAdapter(mGridviewFliterAdapter4);
    }

    private boolean isSelect1(int position) {
        Set set = typeChooseMap.keySet();//得到所有map里面key的集合
        Boolean value = false;
        for (Iterator iter = set.iterator(); iter.hasNext(); )//遍历
        {
            Integer key = (Integer) iter.next();
            if (key == position) {
                value = (Boolean) typeChooseMap.get(key);
            }
        }
        return value;
    }

    private boolean isSelect2(int position) {
        Set set = channelChooseMap.keySet();//得到所有map里面key的集合
        Boolean value = false;
        for (Iterator iter = set.iterator(); iter.hasNext(); )//遍历
        {
            Integer key = (Integer) iter.next();
            if (key == position) {
                value = (Boolean) channelChooseMap.get(key);
            }
        }
        return value;
    }

    private String getMapValue() {
        String str = "";
        Set set = typeChooseMap.keySet();//得到所有map里面key的集合
        Boolean value = false;
        for (Iterator iter = set.iterator(); iter.hasNext(); )//遍历
        {
            Integer key = (Integer) iter.next();
            value = (Boolean) typeChooseMap.get(key);
            if (value) {
                str += stringList3.get(key) + ",";
            }
        }
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private String getChannelMapValue() {
        String str = "";
        Set set = channelChooseMap.keySet();//得到所有map里面key的集合
        Boolean value = false;
        for (Iterator iter = set.iterator(); iter.hasNext(); )//遍历
        {
            Integer key = (Integer) iter.next();
            value = (Boolean) channelChooseMap.get(key);
            if (value) {
                str += stringList4.get(key).getId() + ",";
            }
        }
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}

