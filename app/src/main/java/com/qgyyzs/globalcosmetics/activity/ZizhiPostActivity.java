package com.qgyyzs.globalcosmetics.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jaiky.imagespickers.ImageConfig;
import com.jaiky.imagespickers.ImageSelector;
import com.jaiky.imagespickers.ImageSelectorActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.ZizhiInfoBean;
import com.qgyyzs.globalcosmetics.customview.GlideLoader;
import com.qgyyzs.globalcosmetics.customview.WaitDialog;
import com.qgyyzs.globalcosmetics.eventbus.AnyEventZizhi;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.iface.ZizhiInfoView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.UpdateZizhiImgPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ZizhiInfoPresenter;
import com.qgyyzs.globalcosmetics.utils.AesUtils;
import com.qgyyzs.globalcosmetics.utils.FileUtils;
import com.qgyyzs.globalcosmetics.utils.ImageUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.qgyyzs.globalcosmetics.application.MyApplication.USERSPINFO;

/**
 * Created by Administrator on 2017/10/19 0019.
 */

public class ZizhiPostActivity extends BaseActivity implements View.OnClickListener,ZizhiInfoView{
    private UpdateZizhiImgPresenter postPresenter;
    private ZizhiInfoPresenter zizhiPresenter=new ZizhiInfoPresenter(this,this);
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mTvRemake)
    TextView mTvRemake;
    @BindView(R.id.company)
    EditText mEtCompany;
    @BindView(R.id.mTvStart)
    TextView mTvStart;
    @BindView(R.id.mTvEnd)
    TextView mTvEnd;
    @BindView(R.id.rlPost)
    RelativeLayout rlPost;
    @BindView(R.id.imgZhizhao)
    ImageView imgZhizhao;
    @BindView(R.id.submit_btn)
    Button submit_btn;
    @BindView(R.id.txtzhizhao)
    TextView mTvzhizhao;

    private String company;
    private SharedPreferences mSharedPreferences;

    private File file=null;
    private static final int REQUEST_CODE = 1001;
    private ImageConfig imageConfig;

    private WaitDialog waitDialog;

    @Override
    protected int getLayout() {
        return R.layout.activity_zizhi;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        mSharedPreferences = getSharedPreferences(USERSPINFO, MODE_PRIVATE);

        waitDialog=new WaitDialog(this);
    }

    @Override
    public void initData() {
        String remake=getIntent().getStringExtra("remake");
        if(!TextUtils.isEmpty(remake)){
            if(remake.equals("-1")) {
                remake="请保证信息真实有效";
                mEtCompany.setEnabled(true);
                mTvStart.setEnabled(true);
                mTvEnd.setEnabled(true);
                rlPost.setEnabled(true);
                submit_btn.setVisibility(View.VISIBLE);
            }else if(remake.equals("0")){
                remake="审核中";
                mEtCompany.setEnabled(true);
                mTvStart.setEnabled(true);
                mTvEnd.setEnabled(true);
                rlPost.setEnabled(true);
                submit_btn.setVisibility(View.VISIBLE);
            }else if(remake.equals("1")){
                remake="已审核";
                mEtCompany.setEnabled(false);
                mTvStart.setEnabled(false);
                mTvEnd.setEnabled(false);
                rlPost.setEnabled(false);
                submit_btn.setVisibility(View.GONE);
            }
            mTvRemake.setText(remake);
        }

        company=mSharedPreferences.getString("Company","");
        if(!TextUtils.isEmpty(company)){
            mEtCompany.setText(company);
            mEtCompany.setSelection(company.length());
        }

        postPresenter=new UpdateZizhiImgPresenter(postView,this);

        zizhiPresenter.getZizhiInfo();
    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTvStart.setOnClickListener(this);
        mTvEnd.setOnClickListener(this);
        rlPost.setOnClickListener(this);
        submit_btn.setOnClickListener(this);
    }

    protected void showDatePickDlg(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                textView.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private StringView postView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if(!TextUtils.isEmpty(result)){
                ToastUtil.showToast(ZizhiPostActivity.this,"资质上传成功，请等待审核",true);
                SharedPreferences.Editor mEditor=mSharedPreferences.edit();
                mEditor.putString("Company",mEtCompany.getText().toString().trim());
                mEditor.commit();
                EventBus.getDefault().post(new AnyEventZizhi());
                finish();
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
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("zsstart",mTvStart.getText().toString());
                jsonObject.put("zsend",mTvEnd.getText().toString());
                jsonObject.put("company",mEtCompany.getText().toString().trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return AesUtils.AesString(jsonObject.toString());
        }

        @Override
        public void showToast(String msg) {

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mTvStart:
                showDatePickDlg(mTvStart);
                break;
            case R.id.mTvEnd:
                showDatePickDlg(mTvEnd);
                break;
            case R.id.rlPost:
                imageConfig = new ImageConfig.Builder(
                        new GlideLoader())
                        .steepToolBarColor(getResources().getColor(R.color.main_color))
                        .titleBgColor(getResources().getColor(R.color.main_color))
                        .titleSubmitTextColor(getResources().getColor(R.color.white))
                        .titleTextColor(getResources().getColor(R.color.white))
                        // 开启单选   （默认为多选）
                        .singleSelect()
                        // 开启拍照功能 （默认关闭）
                        .showCamera()
                        .requestCode(REQUEST_CODE)
                        .build();
                ImageSelector.open(this, imageConfig);
                break;
            case R.id.submit_btn:
                if(mEtCompany.getText().toString().trim().length()>0) {
                    if(mTvStart.getText().toString().length()>0) {
                        if(mTvEnd.getText().toString().length()>0) {
                            if(file!=null) {
                                MultipartBody.Builder builder = new MultipartBody.Builder()
                                        .setType(MultipartBody.FORM)//表单类型
                                        .addFormDataPart("jsonstr", postView.getJsonString());
                                RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                                builder.addFormDataPart("zhengshu", file.getName(), imageBody);//imgfile 后台接收图片流的参数名

                                List<MultipartBody.Part> parts = builder.build().parts();
                                postPresenter.updateImg(parts);
                            }else{
                                ToastUtil.showToast(this,"请上传营业执照",true);
                            }
                        }else {
                            ToastUtil.showToast(this,"证书结束日期不能为空",true);
                        }
                    }else {
                        ToastUtil.showToast(this,"证书开始日期不能为空",true);
                    }
                }else{
                    ToastUtil.showToast(this,"公司名称不能为空",true);
                }
                break;
        }
    }

    @Override
    public void showzizhiResult(ZizhiInfoBean bean) {
        if(bean==null) {
            mTvzhizhao.setVisibility(View.VISIBLE);
            return;
        }
        if(bean.getResult()==1){
            if(!TextUtils.isEmpty(company)) {
                mEtCompany.setText(company);
                mEtCompany.setSelection(company.length());
            }
            mTvStart.setText(getDate(bean.getJsonData().getZsstart()));
            mTvEnd.setText(getDate(bean.getJsonData().getZsend()));
            if(TextUtils.isEmpty(bean.getJsonData().getZsimage())) {
                mTvzhizhao.setVisibility(View.VISIBLE);
            }
            Glide.with(this).load(bean.getJsonData().getZsimage()).into(imgZhizhao);
            Glide.with(this).load(bean.getJsonData().getZsimage()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    file = FileUtils.compressImage(resource);
                }
            });
        }
    }

    private String getDate(String seconds){
        seconds=seconds.substring(6,seconds.length()-2);
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);

            file = FileUtils.compressImage(ImageUtils.getimage(pathList.get(0)));
            imgZhizhao.setImageBitmap(ImageUtils.getimage(pathList.get(0)));
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
        return null;
    }

    @Override
    public void showToast(String msg) {

    }
}
