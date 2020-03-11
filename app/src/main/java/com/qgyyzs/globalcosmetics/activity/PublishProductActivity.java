package com.qgyyzs.globalcosmetics.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.ProductDetialBean;
import com.qgyyzs.globalcosmetics.customview.WaitDialog;
import com.qgyyzs.globalcosmetics.eventbus.AnyEventMyPdtList;
import com.qgyyzs.globalcosmetics.mvp.iface.ProductDetialView;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ProductAddPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ProductUpdatePresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.qgyyzs.globalcosmetics.application.MyApplication.USERSPINFO;

public class PublishProductActivity extends BaseActivity implements StringView{
    private ProductAddPresenter productAddPresenter;
    private ProductUpdatePresenter productUpdatePresenter=new ProductUpdatePresenter(this,this);
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.product_type_tv)
    TextView mProductTypeTv;
    @BindView(R.id.product_type_rl)
    RelativeLayout mProductTypeRl;
    @BindView(R.id.next_btn)
    Button mNextBtn;
    @BindView(R.id.name_edit)
    EditText mNameEdit;
    @BindView(R.id.ppname_edit)
    EditText mPPnameEdit;
    @BindView(R.id.spec_edit)
    EditText mGuigeEdit;
    @BindView(R.id.qudao_spinner)
    Spinner mQudaoSpinner;
    @BindView(R.id.jinkou_spinner)
    Spinner mSpinner;
    @BindView(R.id.area_edit)
    EditText mAreaEdit;
    @BindView(R.id.yongfa_edit)
    EditText mYongtuEdit;
    @BindView(R.id.linkman_edit)
    EditText mLinkManEdit;
    @BindView(R.id.danwei_edit)
    EditText mDanweiEdit;
    @BindView(R.id.phone_edit)
    EditText mPhoneEdit;
    @BindView(R.id.shuoming_edit)
    EditText mShuomingEdit;

    private WaitDialog waitDialog;

    private SharedPreferences mSharedPreferences,mSharedPreferences2;
    private String username,userid;//用户名
    private int proid = 0;
    private String subject;//产品名称
    private String modeId = "";//大类
    private String modeName="";
    private String set_type;

    private int jinkouId=-1;
    private String[] mItems;

    private String channelString;
    private String qudao="";
    private String[] mQudaoItems;

    private String company,linktel,linkman;

    @Override
    protected int getLayout() {
        return R.layout.activity_publish_product;
    }


    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                jinkouId=i-1;
                LogUtils.e("点击选中权限"+jinkouId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mQudaoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                qudao=mQudaoItems[i].toString();
                LogUtils.e("渠道="+qudao);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        waitDialog=new WaitDialog(this);
        mSharedPreferences = getSharedPreferences(USERSPINFO, MODE_PRIVATE);
        username=mSharedPreferences.getString("username","");
        userid=mSharedPreferences.getString("userid","");
        linktel=mSharedPreferences.getString("linkTel","");
        linkman=mSharedPreferences.getString("RealName","");
        company=mSharedPreferences.getString("Company","");

        Intent intent = getIntent();
        set_type = intent.getStringExtra("set_type");

        mPhoneEdit.setText(linktel);
        mDanweiEdit.setText(company);
        mLinkManEdit.setText(linkman);

        if (set_type.equals("update")) {
            toolbar.setTitle("设置产品");
            mNextBtn.setText("确认修改");
            proid = intent.getIntExtra("proid", 0);
            subject = intent.getStringExtra("subject");//产品名称
            if(!TextUtils.isEmpty(subject)) {
                mNameEdit.setText(subject);
                mNameEdit.setSelection(subject.length());
            }
            modeId=intent.getStringExtra("modeId");
            modeName = intent.getStringExtra("modeName");//大类
            mProductTypeTv.setText(modeName);
            mPPnameEdit.setText(TextUtils.isEmpty(intent.getStringExtra("ppai_name"))?"":intent.getStringExtra("ppai_name"));
            mGuigeEdit.setText(TextUtils.isEmpty(intent.getStringExtra("x_gg"))?"":intent.getStringExtra("x_gg"));
            qudao=TextUtils.isEmpty(intent.getStringExtra("Qudao"))?"":intent.getStringExtra("Qudao");
            mYongtuEdit.setText(TextUtils.isEmpty(intent.getStringExtra("YongTu"))?"": Html.fromHtml(intent.getStringExtra("YongTu")));
            mShuomingEdit.setText(TextUtils.isEmpty(intent.getStringExtra("Content"))?"":intent.getStringExtra("Content"));
            mLinkManEdit.setText(TextUtils.isEmpty(intent.getStringExtra("lianxiren"))?"":intent.getStringExtra("lianxiren"));
            mDanweiEdit.setText(TextUtils.isEmpty(intent.getStringExtra("danwei"))?"":intent.getStringExtra("danwei"));
            mPhoneEdit.setText(TextUtils.isEmpty(intent.getStringExtra("dianhua"))?"":intent.getStringExtra("dianhua"));
            mAreaEdit.setText(TextUtils.isEmpty(intent.getStringExtra("ProductArea"))?"":intent.getStringExtra("ProductArea"));
            jinkouId=intent.getIntExtra("jinkouId",-1);
            LogUtils.e("jinkouId="+jinkouId);

        }
        if (set_type.equals("add")) {
            toolbar.setTitle("发布产品");
        }
    }

    @Override
    public void initData() {
        productAddPresenter=new ProductAddPresenter(addView,this);

        mItems = getResources().getStringArray(R.array.jinkounArrays);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.view_spinner_text, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mSpinner.setSelection(jinkouId+1,true);

        mSharedPreferences2=getSharedPreferences(MyApplication.CONSTACTDATA,MODE_PRIVATE);
        channelString=mSharedPreferences2.getString("channel","");

        if (!TextUtils.isEmpty(channelString)) {
            mQudaoItems = channelString.split(",");
            ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this,R.layout.view_spinner_text, mQudaoItems);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mQudaoSpinner.setAdapter(adapter2);

            for(int i=0;i<mQudaoItems.length;i++){
                if(qudao.equals(mQudaoItems[i].toString())){
                    mQudaoSpinner.setSelection(i,true);
                }
            }
        }
    }

    @OnClick({R.id.product_type_rl, R.id.next_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.product_type_rl:
                Intent intent=new Intent(this,ProductTypeActivity.class);
                intent.putExtra("type",modeName);
                intent.putExtra("id",modeId);
                startActivityForResult(intent, 1);
                break;
            case R.id.next_btn:
                if (adjust()) {
                    if (mNextBtn.getText().toString().equals("确认修改")) {
                        productUpdatePresenter.Updateproduct();
                    } else {
                        productAddPresenter.Addproduct();
                    }

                }
                break;
        }
    }

    private boolean adjust() {

        boolean flog = false;
        if (!mProductTypeTv.getText().toString().equals("选择产品类型")) {
            if (!mNameEdit.getText().toString().trim().equals("")) {
                if (!mPPnameEdit.getText().toString().trim().equals("")) {
                    if (!mGuigeEdit.getText().toString().trim().equals("")) {
                        if (!TextUtils.isEmpty(qudao)) {
                            if (jinkouId!=-1) {
                                if(!mAreaEdit.getText().toString().trim().equals("")) {
                                    if(!mYongtuEdit.getText().toString().trim().equals("")) {
                                        if(!mLinkManEdit.getText().toString().trim().equals("")) {
                                            if(!mDanweiEdit.getText().toString().trim().equals("")) {
                                                if(!mPhoneEdit.getText().toString().trim().equals("")) {
                                                    flog = true;
                                                }else {
                                                    ToastUtil.showToast(this,"请输入电话",true);
                                                }
                                            }else {
                                                ToastUtil.showToast(this,"请输入单位名称",true);
                                            }
                                        }else {
                                            ToastUtil.showToast(this,"请输入联系人",true);
                                        }
                                    }else {
                                        ToastUtil.showToast(this,"请输入用途",true);
                                    }
                                }else {
                                    ToastUtil.showToast(this,"请选择产地",true);
                                }
                            } else {
                                ToastUtil.showToast(this, "请选择进口类型", true);
                            }
                        } else {
                            ToastUtil.showToast(this, "渠道不能为空", true);
                        }
                    } else {
                        ToastUtil.showToast(this, "规格不能为空", true);
                    }
                } else {
                    ToastUtil.showToast(this, "品牌不能为空", true);
                }
            } else {
                ToastUtil.showToast(this, "产品名称不能为空", true);
            }
        } else {
            ToastUtil.showToast(this, "请选择产品类型", true);
        }
        return flog;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode  == 1) {
            modeName=data.getStringExtra("type");
            modeId=data.getStringExtra("id");
            if(!TextUtils.isEmpty(modeName)) {
                mProductTypeTv.setText(modeName);
            }
        }
    }

    @Override
    public void showLoading() {
        waitDialog.show();
    }

    @Override
    public void closeLoading() {
        waitDialog.dismiss();
    }

    @Override
    public String getJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", proid);
            jsonObject.put("subject", mNameEdit.getText().toString());//产品名称
            jsonObject.put("classid", modeId.split(",")[0]);
            jsonObject.put("nclassid", modeId.split(",")[1]);
            jsonObject.put("ppai_name", mPPnameEdit.getText().toString().trim());
            jsonObject.put("x_gg", mGuigeEdit.getText().toString().trim());//生产厂家
            jsonObject.put("Qudao", qudao);
            jsonObject.put("YongTu", mYongtuEdit.getText().toString().trim());
            if(!TextUtils.isEmpty(mShuomingEdit.getText().toString().trim())) {
                jsonObject.put("Content", mShuomingEdit.getText().toString().trim());
            }
            jsonObject.put("lianxiren",mLinkManEdit.getText().toString().trim());
            jsonObject.put("danwei",mDanweiEdit.getText().toString().trim());
            jsonObject.put("dianhua",mPhoneEdit.getText().toString().trim());
            jsonObject.put("ProductArea",mAreaEdit.getText().toString().trim());
            jsonObject.put("jinkouId",jinkouId);
            LogUtils.e( jsonObject.toString());
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
    public void showStringResult(String Msg) {
        if(TextUtils.isEmpty(Msg))return;

        showToast("更新成功");
        EventBus.getDefault().post(new AnyEventMyPdtList());
        Intent intent =new Intent();
        intent.putExtra("subject",mNameEdit.getText().toString());
        intent.putExtra("modeName",mProductTypeTv.getText().toString());
        intent.putExtra("modeId", modeId);
        intent.putExtra("modeName",modeName);
        intent.putExtra("ppai_name", mPPnameEdit.getText().toString().trim());
        intent.putExtra("x_gg", mGuigeEdit.getText().toString().trim());//生产厂家
        intent.putExtra("Qudao", qudao);
        intent.putExtra("YongTu", mYongtuEdit.getText().toString().trim());
        intent.putExtra("Content", mShuomingEdit.getText().toString().trim());
        intent.putExtra("lianxiren",mLinkManEdit.getText().toString().trim());
        intent.putExtra("danwei",mDanweiEdit.getText().toString().trim());
        intent.putExtra("dianhua",mPhoneEdit.getText().toString().trim());
        intent.putExtra("ProductArea",mAreaEdit.getText().toString().trim());
        intent.putExtra("jinkouId",jinkouId);
        setResult(RESULT_OK,intent);
        finish();
    }

    private ProductDetialView addView=new ProductDetialView() {
        @Override
        public void showProductResult(ProductDetialBean bean) {
            if(bean==null)return;

            Intent intent = new Intent(PublishProductActivity.this, ProductSettingActivity.class);
            intent.putExtra("proid", bean.getJsonData().getId());
            intent.putExtra("type", "add");
            intent.putExtra("isShare",true);
            startActivity(intent);
            finish();
            EventBus.getDefault().post(new AnyEventMyPdtList());
            showToast("产品发布成功");
        }

        @Override
        public void showLoading() {
            waitDialog.show();
        }

        @Override
        public void closeLoading() {
            waitDialog.dismiss();
        }

        @Override
        public String getJsonString() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("userid",userid);
                jsonObject.put("pcusername",username);
                jsonObject.put("subject", mNameEdit.getText().toString().trim());
                jsonObject.put("classid", modeId.split(",")[0]);
                jsonObject.put("nclassid", modeId.split(",")[1]);
                jsonObject.put("ppai_name", mPPnameEdit.getText().toString().trim());
                jsonObject.put("x_gg", mGuigeEdit.getText().toString().trim());//生产厂家
                jsonObject.put("Qudao", qudao);
                jsonObject.put("YongTu", mYongtuEdit.getText().toString().trim());
                jsonObject.put("Content", mShuomingEdit.getText().toString().trim());
                jsonObject.put("lianxiren",mLinkManEdit.getText().toString().trim());
                jsonObject.put("danwei",mDanweiEdit.getText().toString().trim());
                jsonObject.put("dianhua",mPhoneEdit.getText().toString().trim());
                jsonObject.put("ProducingArea",mAreaEdit.getText().toString().trim());
                jsonObject.put("IsJinKou",jinkouId);
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
}
