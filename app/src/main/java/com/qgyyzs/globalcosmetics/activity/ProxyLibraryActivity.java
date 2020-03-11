package com.qgyyzs.globalcosmetics.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.GridviewFliterAdapter;
import com.qgyyzs.globalcosmetics.adapter.MyFliterAdapter;
import com.qgyyzs.globalcosmetics.adapter.ProxyLibraryAdapter;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.ProductClassBean;
import com.qgyyzs.globalcosmetics.bean.ProxyLibraryBean;
import com.qgyyzs.globalcosmetics.customview.AreaPopWindow;
import com.qgyyzs.globalcosmetics.customview.EmptyRecyclerView;
import com.qgyyzs.globalcosmetics.customview.MyGridView;
import com.qgyyzs.globalcosmetics.mvp.iface.ProxyLibraryView;
import com.qgyyzs.globalcosmetics.mvp.iface.SortProductView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ProxyLibraryPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.SortProductBigPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.ScreenUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

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

import static com.qgyyzs.globalcosmetics.application.MyApplication.CONSTACTDATA;

public class ProxyLibraryActivity extends BaseActivity implements ProxyLibraryView,AreaPopWindow.SettingAreaListener,SortProductView{
    private ProxyLibraryPresenter libraryPresenter=new ProxyLibraryPresenter(this,this);
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.filter_tv)
    TextView mFilterTv;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.empty_view)
    View emptyView;
    @BindView(R.id.province_tv)
    TextView mProvinceTv;
    @BindView(R.id.activity_proxy_library)
    LinearLayout mActivityProxyLibrary;
    @BindView(R.id.LinearProvince)
    LinearLayout LinearProvince;
    @BindView(R.id.LinearFilter)
    LinearLayout LinearFilter;

    private SortProductBigPresenter bigPresenter=new SortProductBigPresenter(this,this);
    private List<ProxyLibraryBean.JsonData> mProxyBeanList=new ArrayList<>();
    private ProxyLibraryAdapter mProxyListAdapter;

    private SharedPreferences mSharedPreferences;
    private Dialog dialog;
    private String channelString;
    private MyGridView mGridView3,mGridView4;
    private GridviewFliterAdapter mGridviewFliterAdapter3;
    private MyFliterAdapter mGridviewFliterAdapter4;
    private List<String> stringList3=new ArrayList<>();
    private List<ProductClassBean> stringList4=new ArrayList<>();
    private Button mButton, mResetButton;
    private int cur = 1;//显示列表的页码号
    private String[] channelstrs;
    private String province = "", city = "";
    private AreaPopWindow areaWindow;

    private Map<Integer, Boolean> typeChooseMap = new HashMap<Integer, Boolean>();
    private Map<Integer, Boolean> channelChooseMap = new HashMap<Integer, Boolean>();

    @Override
    protected int getLayout() {
        return R.layout.activity_proxy_library;
    }

    @Override
    protected void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        mSharedPreferences=getSharedPreferences(CONSTACTDATA,MODE_PRIVATE);
        channelString = mSharedPreferences.getString("channel","");

        bigPresenter.getBigSort();
//        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        initdailog();
    }

    @Override
    protected void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mGridView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isSelect1(position)) {
                    view.setActivated(false);
                    typeChooseMap.put(position, false);
                    mGridviewFliterAdapter3.changeState(position);
                } else {
                    if(getMapValue().split(",").length>=5){
                        ToastUtil.showToast(ProxyLibraryActivity.this,"最多只可选择5个渠道",true);
                        return;
                    }
                    view.setActivated(true);
                    typeChooseMap.put(position, true);
                    mGridviewFliterAdapter3.changeState(position);
                }
//                if (Aposition3 == position) {
//                    strc = "";
//                    Aposition3 = -1;
//                    mGridviewFliterAdapter3 = new GridviewFliterAdapter(ProxyLibraryActivity.this,stringList3,  false);
//                    mGridView3.setAdapter(mGridviewFliterAdapter3);
//                    mGridviewFliterAdapter3.notifyDataSetChanged();
//                } else {
//                    mGridviewFliterAdapter3.changeState(position);
//                    strc = stringList3.get(position);
//                    Aposition3 = position;
//                }
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
                        ToastUtil.showToast(ProxyLibraryActivity.this,"最多只可选择5个分类",true);
                        return;
                    }
                    view.setActivated(true);
                    channelChooseMap.put(position, true);
                    mGridviewFliterAdapter4.changeState(position);
                }
//                if (Aposition4 == position) {
//                    strd = 0;
//                    Aposition4 = -1;
//                    mGridviewFliterAdapter4 = new MyFliterAdapter(ProxyLibraryActivity.this,stringList4,  false);
//                    mGridView4.setAdapter(mGridviewFliterAdapter4);
//                    mGridviewFliterAdapter4.notifyDataSetChanged();
//                } else {
//                    mGridviewFliterAdapter4.changeState(position);
//                    strd = stringList4.get(position).getId();
//                    Aposition4 = position;
//                }
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur=1;
                refreshLayout.autoRefresh();
                dialog.dismiss();
            }
        });
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mGridviewFliterAdapter3 = new GridviewFliterAdapter(ProxyLibraryActivity.this,stringList3, true);
                mGridView3.setAdapter(mGridviewFliterAdapter3);
                mGridviewFliterAdapter3.notifyDataSetChanged();

                mGridviewFliterAdapter4 = new MyFliterAdapter(ProxyLibraryActivity.this,stringList4 , true);
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
                libraryPresenter.getProxyList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                cur = 1;
                libraryPresenter.getProxyList();
            }
        });
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

    @Override
    protected void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProxyListAdapter = new ProxyLibraryAdapter(this,mProxyBeanList);
        recyclerView.setAdapter(mProxyListAdapter);

        libraryPresenter=new ProxyLibraryPresenter(this,this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.autoRefresh();
            }
        }).start();
    }

    @OnClick({R.id.LinearFilter, R.id.LinearProvince})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.LinearFilter:
                dialog.show();
                break;
            case R.id.LinearProvince:
                if(areaWindow==null) {
                    areaWindow = new AreaPopWindow(this);
                    areaWindow.setOnSettingAreaListener(this);
                    areaWindow.showAsDropDown(LinearProvince);
                }else{
                    areaWindow.setOnSettingAreaListener(this);
                    areaWindow.showAsDropDown(LinearProvince);
                }
                break;
        }
    }

    private void initdailog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_fliter);
        Window dialogWindow = dialog.getWindow();
        TextView tvKeshi=(TextView) dialogWindow.findViewById(R.id.top2);
        mGridView3 = (MyGridView) dialogWindow.findViewById(R.id.fliter3_gridview);
        mGridView4 = (MyGridView) dialogWindow.findViewById(R.id.fliter4_gridview);
        mResetButton = (Button) dialogWindow.findViewById(R.id.fliter_reset_btn);
        mButton = (Button) dialogWindow.findViewById(R.id.fliter_ok_btn);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        //解决dialog圆角之后背景是白色的问题
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        lp.width = ScreenUtils.getScreenWidth(this); // 宽度
        lp.alpha = 1f; // 透明度
        dialogWindow.setAttributes(lp);

        if (!TextUtils.isEmpty(channelString)) {
            channelstrs = channelString.split(",");
            for (int j = 0; j < channelstrs.length; j++) {
                stringList3.add(channelstrs[j]);
            }
            mGridviewFliterAdapter3 = new GridviewFliterAdapter(ProxyLibraryActivity.this, stringList3, true);
            mGridView3.setAdapter(mGridviewFliterAdapter3);
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
        cur=1;
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
            jsonObject.put("province", province.equals("全国")?"":province);
            jsonObject.put("city", city.equals("全省")?"":city);
            if(!TextUtils.isEmpty(getMapValue()))
                jsonObject.put("qudao", getMapValue());//渠道
            if(!TextUtils.isEmpty(getChannelMapValue()))
                jsonObject.put("daili_type", getChannelMapValue());
            LogUtils.e( "开始请求" + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToast(this,msg,true);
    }

    @Override
    public void showProxyLibraryResult(ProxyLibraryBean bean) {
        if(null!=refreshLayout&&refreshLayout.isRefreshing())refreshLayout.finishRefresh();
        if(null!=refreshLayout&&refreshLayout.isLoading())refreshLayout.finishLoadmore();
        if(null!=recyclerView)recyclerView.setEmptyView(emptyView);
        if(bean==null||bean.getJsonData()==null)return;
        if(cur==1)mProxyBeanList.clear();
        mProxyBeanList.addAll(bean.getJsonData());
        mProxyListAdapter.notifyDataSetChanged();
        List<String> list=new ArrayList<>();
        for(int i=0;i<bean.getJsonData().size();i++){
            if(!TextUtils.isEmpty(bean.getJsonData().get(i).getNimID())) {
                list.add(bean.getJsonData().get(i).getNimID());
            }
        }
        if(list.size()>0) {
            NIMClient.getService(UserService.class).fetchUserInfo(list);
        }
        if(TextUtils.isEmpty(bean.getMsg()))return;
        refreshLayout.setLoadmoreFinished(cur >= Integer.parseInt(bean.getMsg())?true:false);
    }

    @Override
    public void showSortResult(List<ProductClassBean> list) {
        if(list==null||list.size()==0)return;
        stringList4.clear();
        stringList4.addAll(list);
        mGridviewFliterAdapter4 = new MyFliterAdapter(ProxyLibraryActivity.this,stringList4,  true);
        mGridView4.setAdapter(mGridviewFliterAdapter4);
    }
}
