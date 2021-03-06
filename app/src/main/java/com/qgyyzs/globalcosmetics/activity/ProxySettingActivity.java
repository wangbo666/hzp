package com.qgyyzs.globalcosmetics.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.GridviewFliterAdapter;
import com.qgyyzs.globalcosmetics.adapter.MyFliterAdapter;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.ProductClassBean;
import com.qgyyzs.globalcosmetics.bean.ProxySetInfoBean;
import com.qgyyzs.globalcosmetics.customview.MyGridView;
import com.qgyyzs.globalcosmetics.mvp.iface.ProxySetInfoView;
import com.qgyyzs.globalcosmetics.mvp.iface.SortProductView;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.MyProxyInfoPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.SortProductBigPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.UpdateProxySetPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

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

public class ProxySettingActivity extends BaseActivity implements ProxySetInfoView,SortProductView{
    private MyProxyInfoPresenter myProxyInfoPresenter=new MyProxyInfoPresenter(this,this);
    private UpdateProxySetPresenter updateProxySetPresenter;
    @BindView(R.id.keep_tv)
    Button mKeepTv;
    @BindView(R.id.proxy_type_gridview)
    MyGridView mProxyTypeGridview;
    @BindView(R.id.channel_gridview)
    MyGridView mChannelGridview;
    @BindView(R.id.my_place_tv)
    TextView mMyPlaceTv;
    @BindView(R.id.my_place_rl)
    RelativeLayout mMyPlaceRl;
    @BindView(R.id.depart_tv)
    TextView mDepartTv;
    @BindView(R.id.depart_rl)
    RelativeLayout mDepartRl;
    @BindView(R.id.daili_checkbox)
    CheckBox mDailiCheckbox;
    @BindView(R.id.changjia_checkbox)
    CheckBox mChangjiaCheckbox;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private SortProductBigPresenter bigPresenter=new SortProductBigPresenter(this,this);

    private List<ProductClassBean> stringList1;
    private List<String> stringList2;
    private MyFliterAdapter mGridview1Adapter;
    private GridviewFliterAdapter mGridview2Adapter;

    private String  channelString;
    private String[] channelstrs;
    private String resplace;
    private String daili_type, daili_sort = "";
    private String qudao;
    private String keshiString="";

    private SharedPreferences mSharedPreferences,mSharedPreferences2;
    private SharedPreferences.Editor mEditor;

    private static final int REQUEST_KESHI_CODE=1001;
    /**
     * 用了存放点击的gridview的item的pos和这个pos的选中状态
     */
    private Map<Integer, Boolean> typeChooseMap = new HashMap<Integer, Boolean>();
    private Map<Integer, Boolean> channelChooseMap = new HashMap<Integer, Boolean>();

    @Override
    protected int getLayout() {
        return R.layout.activity_proxy_setting;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private boolean setCbCheck(String value){
        if(!TextUtils.isEmpty(daili_sort)) {
            if (daili_sort.indexOf(value) != -1) {
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mProxyTypeGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mGridview1Adapter.changeState(position);
                if (isSelect1(position)) {
                    view.setActivated(false);
                    typeChooseMap.put(position, false);
                } else {
                    view.setActivated(true);
                    typeChooseMap.put(position, true);
                }
                Log.e("代理类型", "结果" + typeChooseMap.toString() + "字符串" + getMapValue());

            }
        });
        mChannelGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mGridview2Adapter.changeState(position);
                if (isSelect2(position)) {
                    view.setActivated(false);
                    channelChooseMap.put(position, false);
                } else {
                    view.setActivated(true);
                    channelChooseMap.put(position, true);

                }
            }
        });
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
                str += stringList1.get(key).getId() + ",";
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
                str += stringList2.get(key) + ",";
            }
        }
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        stringList1 = new ArrayList<>();
        stringList2 = new ArrayList<>();

        mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, MODE_PRIVATE);
        mSharedPreferences2=getSharedPreferences(MyApplication.CONSTACTDATA,MODE_PRIVATE);

        channelString=mSharedPreferences2.getString("channel","");
        mEditor = mSharedPreferences.edit();


        if (!TextUtils.isEmpty(channelString)) {
            channelstrs = channelString.split(",");
            for (int j = 0; j < channelstrs.length; j++) {
                stringList2.add(channelstrs[j]);
            }
        }

        mGridview2Adapter = new GridviewFliterAdapter(ProxySettingActivity.this,stringList2,  true);
        mChannelGridview.setAdapter(mGridview2Adapter);

        updateProxySetPresenter=new UpdateProxySetPresenter(updateView,this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                bigPresenter.getBigSort();
                myProxyInfoPresenter.getMyProxySet();
            }
        }).start();
    }



    @Override
    public void initData() {
        resplace = mSharedPreferences.getString("dailiArea", "");

        mMyPlaceTv.setText(TextUtils.isEmpty(resplace)?"未设置":resplace);
    }

    @OnClick({R.id.keep_tv, R.id.my_place_rl, R.id.depart_rl, R.id.daili_checkbox})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.keep_tv:
                if(!TextUtils.isEmpty(getdailisort())) {
                    if(!TextUtils.isEmpty(getMapValue())) {
                        if(!TextUtils.isEmpty(getChannelMapValue())) {
                            if(!TextUtils.isEmpty(resplace)) {
                                updateProxySetPresenter.updateProxySet();
                            }else{
                                ToastUtil.showToast(this,"常驻地不能为空",true);
                            }
                        }else{
                            ToastUtil.showToast(this,"渠道不能为空",true);
                        }
                    }else{
                        ToastUtil.showToast(this,"产品分类不能为空",true);
                    }
                }else{
                    ToastUtil.showToast(this,"身份类型不能为空",true);
                }
                break;
            case R.id.my_place_rl:
                intent = new Intent(ProxySettingActivity.this, ProvinceSelectActivity.class);
                String area=mMyPlaceTv.getText().toString();
                intent.putExtra("proxy", "1");
                intent.putExtra("proxyset","set");
                if (area.equals("未设置")) {
                    intent.putExtra("province", "");
                    intent.putExtra("city", "");
                } else {
                    String[] str = area.split(",");
                    if (str.length == 1) {
                        intent.putExtra("province", str[0]);
                        intent.putExtra("city", "");
                    } else {
                        intent.putExtra("province", str[0]);
                        intent.putExtra("city", area.substring(str[0].length()+1,area.length()));
                    }
                }
                startActivity(intent);
                break;
            case R.id.depart_rl:
                intent = new Intent(ProxySettingActivity.this, KeshiSelectActivity.class);
                intent.putExtra("keshi", keshiString);
                startActivityForResult(intent,REQUEST_KESHI_CODE);
                break;
            case R.id.daili_checkbox:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&data!=null){
            if(requestCode==REQUEST_KESHI_CODE){
                keshiString=data.getStringExtra("keshi");
                mDepartTv.setText(TextUtils.isEmpty(keshiString)?"未设置":keshiString);
            }
        }
    }

    public String getdailisort(){
        String daili_sort;
        if(mDailiCheckbox.isChecked()&&mChangjiaCheckbox.isChecked()){
            daili_sort = mDailiCheckbox.getText().toString() + "," + mChangjiaCheckbox.getText().toString();
        }else if(mDailiCheckbox.isChecked()&&!mChangjiaCheckbox.isChecked()){
            daili_sort = mDailiCheckbox.getText().toString();
        }else if(mChangjiaCheckbox.isChecked()&&!mDailiCheckbox.isChecked()){
            daili_sort =  mChangjiaCheckbox.getText().toString();
        }else{
            daili_sort="";
        }
        return daili_sort;
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

    @Override
    public void showProxyResult(ProxySetInfoBean proxySetInfoBean) {
        if(proxySetInfoBean!=null) {
            if (proxySetInfoBean.getJsonData() != null) {
                resplace = proxySetInfoBean.getJsonData().getCzd();
                daili_sort = proxySetInfoBean.getJsonData().getDaili_sort();
                daili_type = proxySetInfoBean.getJsonData().getDaili_type();
                qudao = proxySetInfoBean.getJsonData().getQudao();
                keshiString = proxySetInfoBean.getJsonData().getKeshi();

                mDepartTv.setText(TextUtils.isEmpty(keshiString)?"未设置":keshiString);
                mDailiCheckbox.setChecked(setCbCheck("代理商"));
                mChangjiaCheckbox.setChecked(setCbCheck("厂商"));
                if (!TextUtils.isEmpty(daili_type)) {
                    String typeStrs[] = daili_type.split(",");
                    for (int j = 0; j < stringList1.size(); j++) {
                        stringList1.get(j);
                        for (int k = 0; k < typeStrs.length; k++) {
                            if (typeStrs[k].equals(stringList1.get(j).getId()+"")) {
                                mGridview1Adapter.changeState(j);
                                typeChooseMap.put(j, true);
                            }
                        }
                    }
                }

                if (!TextUtils.isEmpty(qudao)) {
                    String channelStrs[] = qudao.split(",");
                    for (int j = 0; j < stringList2.size(); j++) {
                        stringList2.get(j);
                        for (int k = 0; k < channelStrs.length; k++) {
                            if (channelStrs[k].equals(stringList2.get(j))) {
                                mGridview2Adapter.changeState(j);
                                channelChooseMap.put(j, true);
                            }
                        }
                    }
                    mEditor.putString("qudao", qudao);
                    mEditor.putString("daili_type", daili_type);
                    mEditor.putString("daili_sort",daili_sort);
                    mEditor.commit();
                }


                if (TextUtils.isEmpty(resplace) || resplace.equals("null")) {
                    mMyPlaceTv.setText("未设置");
                } else {
                    mMyPlaceTv.setText(resplace);
                    mEditor.putString("dailiArea",resplace);
                    mEditor.commit();
                }
            }
        }
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
            jsonObject.put("userid", MyApplication.userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToast(this,msg,true);
    }

    private StringView updateView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if(TextUtils.isEmpty(result))return;

            mEditor.putString("qudao",getChannelMapValue());
            mEditor.putString("daili_type",getMapValue());
            mEditor.putString("daili_sort",daili_sort);
            mEditor.putInt("isdaili",1);
            mEditor.commit();
            finish();
            ToastUtil.showToast(ProxySettingActivity.this,result,true);
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
                jsonObject.put("userid", MyApplication.userId);
                jsonObject.put("daili_sort", getdailisort());
                jsonObject.put("daili_type", getMapValue());
                jsonObject.put("qudao", getChannelMapValue());
                jsonObject.put("czd", resplace);
//                jsonObject.put("keshi", keshiString);
                LogUtils.e( jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }

        @Override
        public void showToast(String msg) {

        }
    };

    @Override
    public void showSortResult(List<ProductClassBean> list) {
        if(list==null||list.size()==0)return;
        stringList1.clear();
        stringList1.addAll(list);
        mGridview1Adapter = new MyFliterAdapter(ProxySettingActivity.this,stringList1,  true);
        mProxyTypeGridview.setAdapter(mGridview1Adapter);
    }
}
