package com.qgyyzs.globalcosmetics.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.adapter.MyFliterAdapter;
import com.qgyyzs.globalcosmetics.bean.ProductClassBean;
import com.qgyyzs.globalcosmetics.customview.WaitDialog;
import com.qgyyzs.globalcosmetics.mvp.iface.SortProductView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.SortProductBigPresenter;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.GridviewFliterAdapter;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.ProxySetInfoBean;
import com.qgyyzs.globalcosmetics.bean.PublishProxyBean;
import com.qgyyzs.globalcosmetics.customview.MyGridView;
import com.qgyyzs.globalcosmetics.eventbus.AnyEventMyProxyList;
import com.qgyyzs.globalcosmetics.mvp.iface.ProxySetInfoView;
import com.qgyyzs.globalcosmetics.mvp.iface.PublishProxyView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.MyProxyInfoPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.PublishProxyPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.ShareUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
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
import static com.qgyyzs.globalcosmetics.application.MyApplication.USERSPINFO;

/**
 * 发布需求 招标求购
 */
public class PublishProxyActivity extends BaseActivity implements PublishProxyView ,SortProductView{
    private PublishProxyPresenter proxyPresenter=new PublishProxyPresenter(this,this);
    private MyProxyInfoPresenter myProxyInfoPresenter;
    private SortProductBigPresenter bigPresenter=new SortProductBigPresenter(this,this);
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_right)
    TextView mRightTv;
    @BindView(R.id.content_edit)
    EditText mContentEdit;
    @BindView(R.id.text)
    TextView mTextnumTv;
    @BindView(R.id.place_tv)
    TextView mPlaceTv;
    @BindView(R.id.place_rl)
    RelativeLayout mPlaceRl;
    @BindView(R.id.product_edit)
    EditText mPro_Edit;
    @BindView(R.id.channel_gridview)
    MyGridView mChannelGridview;
    @BindView(R.id.name_edit)
    EditText mName_Edit;
    @BindView(R.id.phone_edit)
    EditText mPhone_Edit;
    @BindView(R.id.proxy_type_gridview)
    MyGridView mProxyTypeGridview;
    @BindView(R.id.next_btn)
    Button mBtnOk;

    private int flag;
    private String userid,username;//用户名
    private String content;
    private SharedPreferences mSharedPreferences,mSharedPreferences1;
    private SharedPreferences.Editor mEditor;
    private CharSequence temp;
    private String channelString;
    private String[] channelstrs;
    private List<String> stringList=new ArrayList<>();
    private List<ProductClassBean> stringList1=new ArrayList<>();
    private GridviewFliterAdapter mGridviewAdapter;
    private MyFliterAdapter mGridview1Adapter;
    private Map<Integer, Boolean> channelChooseMap = new HashMap<Integer, Boolean>();
    private Map<Integer, Boolean> typeChooseMap = new HashMap<Integer, Boolean>();
    private String qudao,daili_type,resplace;
    private ShareUtils shareUtils;

    private int classid;

    private WaitDialog waitDialog;

    @Override
    protected int getLayout() {
        return R.layout.activity_publish_require;
    }

    @Override
    public void initData() {
        waitDialog=new WaitDialog(this);
        if (!TextUtils.isEmpty(channelString)) {
            channelstrs = channelString.split(",");
            for (int j = 0; j < channelstrs.length; j++) {
                stringList.add(channelstrs[j]);
            }
        }
        mGridviewAdapter = new GridviewFliterAdapter(PublishProxyActivity.this,stringList,  true);
        mChannelGridview.setAdapter(mGridviewAdapter);
        if (!TextUtils.isEmpty(qudao)) {
            String channelStrs[] = qudao.split(",");
            for (int j = 0; j < stringList.size(); j++) {
                stringList.get(j);
                for (int k = 0; k < channelStrs.length; k++) {
                    if (channelStrs[k].equals(stringList.get(j))) {
                        mGridviewAdapter.changeState(j);
                        channelChooseMap.put(j, true);
                    }
                }
            }
        }
        bigPresenter.getBigSort();
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
                str += stringList.get(key) + ",";
            }
        }
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
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

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mContentEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});  //其中100最大输入字数
        mContentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                mTextnumTv.setText(200 - temp.length()+"/200");
                if (temp.length() >= 200) {
                    ToastUtil.showToast(PublishProxyActivity.this, "你输入的字数已经超过了限制！", true);
                }
            }
        });
        mProxyTypeGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                classid=stringList1.get(position).getId();
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
                mGridviewAdapter.changeState(position);
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

    @Override
    protected void onResume() {
        super.onResume();
        if(shareUtils!=null) {
            finish();
        }

        resplace = mSharedPreferences.getString("dailiArea", "");
        if (TextUtils.isEmpty(resplace)||resplace.equals("null")) {
            mPlaceTv.setText("未设置");
        } else {
            mPlaceTv.setText(resplace);
        }
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        mSharedPreferences = getSharedPreferences(USERSPINFO, MODE_PRIVATE);
        flag=mSharedPreferences.getInt("flag",5);
        userid=mSharedPreferences.getString("userid","");
        username=mSharedPreferences.getString("username","");
        qudao=mSharedPreferences.getString("qudao","");
        daili_type=mSharedPreferences.getString("daili_type","");
        mSharedPreferences1 =getSharedPreferences(CONSTACTDATA,MODE_PRIVATE);
        channelString=mSharedPreferences1.getString("channel","");

        if(TextUtils.isEmpty(qudao)){
            myProxyInfoPresenter=new MyProxyInfoPresenter(proxyView,this);
            myProxyInfoPresenter.getMyProxySet();
        }

        Intent intent = getIntent();
        content = intent.getStringExtra("content");
        String Name = mSharedPreferences.getString("RealName", "");
        String Tel = mSharedPreferences.getString("linkTel", "");
        mContentEdit.setText(content);

        if (TextUtils.isEmpty(Name) || Name.equals("null")) {
            mName_Edit.setText("");
        } else {
            mName_Edit.setText(Name);
        }
        if (TextUtils.isEmpty(Tel) || Tel.equals("null")) {
            mPhone_Edit.setText("");
            mPhone_Edit.setFocusable(true);
            mPhone_Edit.setFocusableInTouchMode(true);
        } else {
            mPhone_Edit.setText(Tel);
            mPhone_Edit.setFocusable(false);
            mPhone_Edit.setFocusableInTouchMode(false);
        }
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

    @OnClick({R.id.place_rl, R.id.tv_right,R.id.next_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.place_rl:
                Intent intent = new Intent(this, ProvinceSelectActivity.class);
                intent.putExtra("proxy", "1");
                String area=mPlaceTv.getText().toString();
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
            case R.id.next_btn:
            case R.id.tv_right:
                if (!TextUtils.isEmpty(mPro_Edit.getText().toString().trim())) {
                    if(!TextUtils.isEmpty(mContentEdit.getText().toString().trim())) {
                        if (!TextUtils.isEmpty(mPlaceTv.getText().toString())&&!mPlaceTv.getText().toString().equals("未设置")&&!mPlaceTv.getText().toString().equals("null")) {
                            if(!TextUtils.isEmpty(mName_Edit.getText().toString())) {
                                if(!TextUtils.isEmpty(mPhone_Edit.getText().toString())) {
                                    if(!TextUtils.isEmpty(getMapValue())) {
                                        if (!TextUtils.isEmpty(getChannelMapValue())) {
                                            proxyPresenter.PublishProxy();
                                        } else {
                                            ToastUtil.showToast(this, "请选择代理渠道", true);
                                        }
                                    }else{
                                        ToastUtil.showToast(this, "请选择产品类别", true);
                                    }
                                }else{
                                    ToastUtil.showToast(this, "请输入联系人电话", true);
                                }
                            }else{
                                ToastUtil.showToast(this, "请输入联系人姓名", true);
                            }
                        } else {
                            ToastUtil.showToast(this, "请选择代理区域", true);
                        }
                    }else{
                        ToastUtil.showToast(this, "请输入代理内容", true);
                    }
                } else {
                    ToastUtil.showToast(this, "请输入产品名称", true);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 001 && resultCode == 002) {
            mPlaceTv.setText(data.getStringExtra("province"));
        }
    }

    @Override
    public void showPublishResult(final PublishProxyBean bean) {
        if (bean!=null) {
            if(bean.getResult()==1){
                EventBus.getDefault().post(new AnyEventMyProxyList());
//                if(flag==1) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mContentEdit.getWindowToken(), 0);
                    ToastUtil.showToast(this,"发布成功",true);
                    finish();
//                    new AlertDialog.Builder(this)
//                            .setMessage("发布成功")
//                            .setPositiveButton("分享一下", new DialogInterface.OnClickListener() {
//
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    UMImage image = new UMImage(PublishProxyActivity.this, R.mipmap.ic_logo);
//                                    UMWeb web;
//                                    web = new UMWeb(RetrofitUtils.BASE_API + "Caigou/DaiLiInfo?OpenFromApp=1&dlid=" + bean.getJsonData().getId());
//                                    web.setTitle("[代理信息]" + bean.getJsonData().getLinkMan());//标题
//                                    web.setThumb(image);  //缩略图
//                                    web.setDescription(" ");//描述
//                                    shareUtils = new ShareUtils(PublishProxyActivity.this, web, mRightTv);
//                                }
//                            })
//                            .setNegativeButton("下次吧", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    //取消按钮事件
//                                    finish();
//                                }
//                            })
//                            .show();
//                }else{
//                    if (!TextUtils.isEmpty(bean.getMsg())) {
//                        ToastUtil.showToast(this, bean.getMsg(), true);
//                    }
//                }
            }else {
                if (!TextUtils.isEmpty(bean.getMsg())) {
                    ToastUtil.showToast(this, bean.getMsg(), true);
                }
            }
        }
    }

    @Override
    public void showLoading() {
        if(waitDialog!=null&&!waitDialog.isShowing())
            waitDialog.show();
    }

    @Override
    public void closeLoading() {
        if(waitDialog!=null&&waitDialog.isShowing())
            waitDialog.dismiss();
    }

    @Override
    public String getJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", userid);
            jsonObject.put("PcUsername",username);
            jsonObject.put("Content", mContentEdit.getText().toString().trim());
            jsonObject.put("DailiArea", mPlaceTv.getText().toString().trim());
            jsonObject.put("ProName",mPro_Edit.getText().toString().trim());
            jsonObject.put("Qudao",getChannelMapValue());
            jsonObject.put("classid",classid);
            jsonObject.put("LinkMan",mName_Edit.getText().toString().trim());
            jsonObject.put("linktel",mPhone_Edit.getText().toString().trim());
            jsonObject.put("ShenFen","个人");
            LogUtils.e(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {

    }

    private ProxySetInfoView proxyView=new ProxySetInfoView() {
        @Override
        public void showProxyResult(ProxySetInfoBean proxySetInfoBean) {
            if(proxySetInfoBean!=null) {
                if (proxySetInfoBean.getJsonData() != null) {
                    resplace = proxySetInfoBean.getJsonData().getCzd();
                    daili_type = proxySetInfoBean.getJsonData().getDaili_type();
                    qudao = proxySetInfoBean.getJsonData().getQudao();

                    if (!TextUtils.isEmpty(qudao)) {
                        String channelStrs[] = qudao.split(",");
                        for (int j = 0; j < stringList.size(); j++) {
                            stringList.get(j);
                            for (int k = 0; k < channelStrs.length; k++) {
                                if (channelStrs[k].equals(stringList.get(j))) {
                                    mGridviewAdapter.changeState(j);
                                    channelChooseMap.put(j, true);
                                }
                            }
                            mGridviewAdapter.notifyDataSetChanged();
                        }
                    }
//                    if (!TextUtils.isEmpty(daili_type)) {
//                        String typeStrs[] = daili_type.split(",");
//                        for (int j = 0; j < stringList1.size(); j++) {
//                            stringList1.get(j);
//                            for (int k = 0; k < typeStrs.length; k++) {
//                                if (typeStrs[k].equals(stringList1.get(j))) {
//                                    mGridview1Adapter.changeState(j);
//                                    typeChooseMap.put(j, true);
//                                }
//                            }
//                            mGridview1Adapter.notifyDataSetChanged();
//                        }
//                    }

                    if (TextUtils.isEmpty(resplace)||resplace.equals("null")) {
                        mPlaceTv.setText("未设置");
                    } else {
                        mPlaceTv.setText(resplace);
                    }
                    mEditor = mSharedPreferences.edit();
                    mEditor.putString("qudao", qudao);
                    mEditor.putString("daili_type", daili_type);
                    mEditor.putString("dailiArea",resplace);
                    mEditor.commit();
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
                jsonObject.put("userid", userid);
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
        mGridview1Adapter = new MyFliterAdapter(this,stringList1,  false);
        mProxyTypeGridview.setAdapter(mGridview1Adapter);
//        if (!TextUtils.isEmpty(daili_type)) {
//            String typeStrs[] = daili_type.split(",");
//            for (int j = 0; j < stringList1.size(); j++) {
//                stringList1.get(j);
//                for (int k = 0; k < typeStrs.length; k++) {
//                    if (typeStrs[k].equals(stringList1.get(j).getId()+"")) {
//                        mGridview1Adapter.changeState(j);
//                        typeChooseMap.put(j, true);
//                    }
//                }
//            }
//        }
    }
}
