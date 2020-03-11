package com.qgyyzs.globalcosmetics.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.ProductDetialBean;
import com.qgyyzs.globalcosmetics.http.retrofit.RetrofitUtils;
import com.qgyyzs.globalcosmetics.mvp.iface.ProductDetialView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ProductDetialPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.ShareUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class ProductSettingActivity extends BaseActivity implements ProductDetialView{
    private ProductDetialPresenter productDetialPresenter=new ProductDetialPresenter(this,this);
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.product_zc_tv)
    TextView mProductZcTv;
    @BindView(R.id.product_zc_rl)
    RelativeLayout mProductZcRl;
    @BindView(R.id.product_quyu_tv)
    TextView mProductQuyuTv;
    @BindView(R.id.product_quyu_rl)
    RelativeLayout mProductQuyuRl;
    @BindView(R.id.preview_tv)
    TextView mPreviewTv;
    @BindView(R.id.product_photo_tv)
    TextView mProductPhotoTv;
    @BindView(R.id.product_photo_rl)
    RelativeLayout mProductPhotoRl;
    @BindView(R.id.product_set_rl)
    RelativeLayout mProductSetRl;

    private String danwei;//企业名称
    private String subject;//产品名称
    private String modeId;
    private String modeName;
    private int jinkouId;

    private String image;//产品图片
    private String x_gg;//规格
    private String zc = null;//招商政策
    private String ppai_name,Qudao,YongTu,Content,ProdutArea,lianxiren,dianhua;
    private String muser;

    private SharedPreferences mSharedPreferences;

    private int proid;
    private String province;
    private boolean isShare=false;
    private UMWeb web;

    private int flag;

    private static final int REQUEST_ZC_CODE = 1000;
    private static final int REQUEST_IMAGE_CODE = 1001;
    private static final int REQUEST_DETIAL_CODE = 1002;
    private static final int REQUEST_AREA_CODE = 1003;


    @Override
    protected int getLayout() {
        return R.layout.activity_product_setting;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        Intent intent = getIntent();
        proid = intent.getIntExtra("proid", 0);
        isShare=intent.getBooleanExtra("isShare",false);
    }

    @OnClick({R.id.product_zc_rl, R.id.product_quyu_rl, R.id.preview_tv, R.id.product_photo_rl, R.id.product_set_rl})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.product_zc_rl:
                intent = new Intent(this, ProductPolicyActivity.class);
                intent.putExtra("proid", proid);
                intent.putExtra("zc", TextUtils.isEmpty(zc)?"":zc);
                startActivityForResult(intent,REQUEST_ZC_CODE);
                break;
            case R.id.product_quyu_rl:
                intent = new Intent(this, ProductAreaActivity.class);
                intent.putExtra("province", province);
                intent.putExtra("proid", proid);
                startActivityForResult(intent,REQUEST_AREA_CODE);
                break;
            case R.id.preview_tv:
                intent = new Intent(ProductSettingActivity.this, MedicineDetailActivity.class);
                intent.putExtra("proid", proid);
                intent.putExtra("name", subject);
                if(!TextUtils.isEmpty(image)) {
                    String str[] = image.split("\\|");
                    intent.putExtra("image", str[0]);
                }
                intent.putExtra("muser",muser);
                intent.putExtra("company",danwei);
                startActivity(intent);
                break;
            case R.id.product_photo_rl:
                intent = new Intent(this, ProductImgsActivity.class);
                intent.putExtra("proid", proid);
                intent.putExtra("image", image);
                startActivityForResult(intent,REQUEST_IMAGE_CODE);
                break;
            case R.id.product_set_rl:
                intent = new Intent(this, PublishProductActivity.class);
                intent.putExtra("proid", proid);
                intent.putExtra("set_type", "update");
                intent.putExtra("subject", subject);
                intent.putExtra("modeId", modeId);
                intent.putExtra("x_gg", x_gg);
                intent.putExtra("danwei",danwei);
                intent.putExtra("lianxiren",lianxiren);
                intent.putExtra("dianhua",dianhua);
                intent.putExtra("ProductArea",ProdutArea);
                intent.putExtra("ppai_name",ppai_name);
                intent.putExtra("Qudao",Qudao);
                intent.putExtra("YongTu",YongTu);
                intent.putExtra("Content",Content);
                intent.putExtra("modeName",modeName);
                intent.putExtra("jinkouId",jinkouId);
                startActivityForResult(intent,REQUEST_DETIAL_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode){
                case REQUEST_ZC_CODE:
                    zc=data.getStringExtra("zc");
                    mProductZcTv.setText(zc);
                    break;
                case REQUEST_IMAGE_CODE:
                    image=data.getStringExtra("images");
                    LogUtils.e("image"+image);
                    mProductPhotoTv.setText(TextUtils.isEmpty(image)?"未设置":"已设置");
                    break;
                case REQUEST_DETIAL_CODE:
                    subject=data.getStringExtra("subject");
                    danwei=data.getStringExtra("danwei");
                    modeId=data.getStringExtra("modeId");
                    x_gg=data.getStringExtra("x_gg");
                    lianxiren=data.getStringExtra("lianxiren");
                    dianhua=data.getStringExtra("dianhua");
                    ProdutArea=data.getStringExtra("ProductArea");
                    ppai_name=data.getStringExtra("ppai_name");
                    Qudao=data.getStringExtra("Qudao");
                    YongTu=data.getStringExtra("YongTu");
                    modeName=data.getStringExtra("modeName");
                    Content=data.getStringExtra("Content");
                    jinkouId=data.getIntExtra("jinkouId",-1);
                    break;
                case REQUEST_AREA_CODE:
                    province=data.getStringExtra("area");
                    province=province.length() > 2?province.substring(1,province.length()-1):province;
                    mProductQuyuTv.setText(province);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void initData() {
        mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
        flag=mSharedPreferences.getInt("flag",5);

        productDetialPresenter=new ProductDetialPresenter(this,this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                productDetialPresenter.getProductDetial();
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
    }

    @Override
    public String getJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("proid", proid);
            LogUtils.e(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void showProductResult(ProductDetialBean bean) {
        if(bean==null)return;
        danwei = bean.getJsonData().getDanwei();//企业名称
        subject = bean.getJsonData().getSubject();//产品名称
        modeId = bean.getJsonData().getClassID()+""+bean.getJsonData().getNClassID();
        modeName=bean.getJsonData().getClassName()+(TextUtils.isEmpty(bean.getJsonData().getNClassName())?"":","+bean.getJsonData().getNClassName());
        image = bean.getJsonData().getImage();
        x_gg = bean.getJsonData().getX_gg();
        ppai_name=bean.getJsonData().getPpai_name();
        Qudao=bean.getJsonData().getQudao();
        YongTu=bean.getJsonData().getYongTu();
        Content=bean.getJsonData().getContent();
        ProdutArea=bean.getJsonData().getProducingArea();
        lianxiren=bean.getJsonData().getLianxiren();
        dianhua=bean.getJsonData().getDianhua();
        jinkouId=bean.getJsonData().getIsJinKou();
        zc = bean.getJsonData().getZc();//政策
        muser=bean.getJsonData().getPcUsername();
        province=bean.getJsonData().getZsArea();
        mProductZcTv.setText(TextUtils.isEmpty(zc)?"未设置":zc);
        mProductPhotoTv.setText(TextUtils.isEmpty(image)?"未设置":"已设置");
        mProductQuyuTv.setText(TextUtils.isEmpty(province)?"未设置":province);
        if(isShare&&flag==1){
            isShare=false;
            new AlertDialog.Builder(this)
                    .setMessage("发布成功")
                    .setPositiveButton("分享一下", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            setResult(RESULT_OK);//确定按钮事件
                            UMImage UMimg;
                            if (TextUtils.isEmpty(image) || (image.equals("null"))) {
                                UMimg = new UMImage(ProductSettingActivity.this, R.mipmap.ic_logo);
                            } else {
                                UMimg = new UMImage(ProductSettingActivity.this, image);
                            }
                            web = new UMWeb(RetrofitUtils.BASE_API + "product/productinfo?OpenFromApp=1&proid=" + proid);
                            web.setTitle("正在招商：" + subject);//标题
                            web.setThumb(UMimg);  //缩略图
                            web.setDescription("产品优异，价格实惠。");//描述
                            new ShareUtils(ProductSettingActivity.this, web, mPreviewTv);
                        }
                    })
                    .setNegativeButton("下次吧", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //取消按钮事件

                        }
                    })
                    .show();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void closeLoading() {
    }

    @Override
    public void showToast(String msg) {

    }
}
