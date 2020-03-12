package com.qgyyzs.globalcosmetics.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jaiky.imagespickers.ImageConfig;
import com.jaiky.imagespickers.ImageSelector;
import com.jaiky.imagespickers.ImageSelectorActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.base.MyBaseAdapter;
import com.qgyyzs.globalcosmetics.customview.GlideLoader;
import com.qgyyzs.globalcosmetics.customview.WaitDialog;
import com.qgyyzs.globalcosmetics.eventbus.AnyEventMyPdtList;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.DelProductImgPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.UpdateProductImgPresenter;
import com.qgyyzs.globalcosmetics.utils.AesUtils;
import com.qgyyzs.globalcosmetics.utils.FileUtils;
import com.qgyyzs.globalcosmetics.utils.ImageUtils;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProductImgsActivity extends BaseActivity implements View.OnClickListener,StringView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.add_photo)
    ImageView mAddImageView;
    @BindView(R.id.keep_btn)
    Button btnKeep;
    @BindView(R.id.myGridView)
    GridView mGridView;

    private ArrayList<String> mPathList = new ArrayList<>();
    private static final int REQUEST_CODE = 1001;
    private String path = "";
    private int proid;
    private String image;
    private ProductImgAdapter adapter;
    private WaitDialog mWaitDialog;

    private ImageConfig imageConfig;
    private List<File> fileList=new ArrayList<>();

    private DelProductImgPresenter deleteProductImgsPresenter;
    private UpdateProductImgPresenter productImgsPresenter=new UpdateProductImgPresenter(this,this);

    @Override
    protected int getLayout() {
        return R.layout.activity_product_imgs;
    }

    private void initImg(String image){
        mPathList.clear();
        if (!TextUtils.isEmpty(image)) {
            for (int i = 0; i < image.split("\\|").length; i++) {
                mPathList.add(image.split("\\|")[i]);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initData(){
        Intent intent = getIntent();
        proid = intent.getIntExtra("proid", 0);
        image = intent.getStringExtra("image");

        adapter = new ProductImgAdapter( this,mPathList);
        mGridView.setAdapter(adapter);
        initImg(image);
        deleteProductImgsPresenter=new DelProductImgPresenter(delView,this);
        productImgsPresenter=new UpdateProductImgPresenter(this,this);
    }
    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentData();
            }
        });
        mAddImageView.setOnClickListener(this);
        btnKeep.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intentData();
    }

    private void intentData(){
        Intent intent=new Intent();
        intent.putExtra("images", image);
        LogUtils.e("image"+image);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        mWaitDialog = new WaitDialog(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_photo:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                        && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            "是否允许读取您的相册",
                            1);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                            && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermission(Manifest.permission.CAMERA,
                                "请允许权限打开摄像头",
                                1);
                    } else {
                        if (mPathList.size() >= 3) {
                            ToastUtil.showToast(this, "最多只能设置三张图片哦", true);
                            return;
                        }
                        imageConfig = new ImageConfig.Builder(
                                new GlideLoader())
                                .steepToolBarColor(getResources().getColor(R.color.main_color))
                                .titleBgColor(getResources().getColor(R.color.main_color))
                                .titleSubmitTextColor(getResources().getColor(R.color.white))
                                .titleTextColor(getResources().getColor(R.color.white))
                                // 开启多选   （默认为多选）
                                .mutiSelect()
                                // 多选时的最大数量   （默认 9 张）
                                .mutiSelectMaxSize(3)
                                // 已选择的图片路径
                                .pathList(mPathList)
                                // 拍照后存放的图片路径（默认 /temp/picture）
                                .filePath("/temp")
                                // 开启拍照功能 （默认关闭）
                                .showCamera()
                                .requestCode(REQUEST_CODE)
                                .build();
                        ImageSelector.open(this, imageConfig);
                    }
                }
                break;
            case R.id.keep_btn:
                fileList.clear();
                for (int i=0;i<mPathList.size();i++){
                    if (!mPathList.get(i).startsWith("http")) {
                        fileList.add(FileUtils.compressImage(ImageUtils.getimage(mPathList.get(i))));
                        LogUtils.e("path"+i+mPathList.get(i));
                    }
                }
                if(fileList.size()==0) {
                    intentData();
                    return;
                }

                btnKeep.setEnabled(false);
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)//表单类型
                        .addFormDataPart("jsonstr", getJsonString());

                for(int i=0;i<fileList.size();i++) {
                    RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileList.get(i));
                    builder.addFormDataPart("productimg" + i, fileList.get(i).getName(), imageBody);
                    LogUtils.e("newpath"+i+fileList.get(i).getName());
                }

                List<MultipartBody.Part> parts = builder.build().parts();
                productImgsPresenter.updateImg(parts);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> list = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);

            adapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private StringView delView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if(TextUtils.isEmpty(result))return;

            image = result;
            initImg(image);
        }

        @Override
        public void showLoading() {
            if (mWaitDialog != null && !mWaitDialog.isShowing())
                mWaitDialog.show();
        }

        @Override
        public void closeLoading() {
            if (mWaitDialog != null && mWaitDialog.isShowing())
                mWaitDialog.dismiss();
        }

        @Override
        public String getJsonString() {
            JSONObject jsonObject1 = new JSONObject();
            try {
                jsonObject1.put("proid", proid);
                jsonObject1.put("imgpath",path);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject1.toString();
        }

        @Override
        public void showToast(String msg) {

        }
    };


    @Override
    public void showLoading() {
        if (mWaitDialog != null && !mWaitDialog.isShowing())
            mWaitDialog.show();
    }

    @Override
    public void closeLoading() {
        if(mWaitDialog!=null&&mWaitDialog.isShowing())
            mWaitDialog.dismiss();
    }

    @Override
    public String getJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("proid", proid);
            LogUtils.e( jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return AesUtils.AesString(jsonObject.toString());
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showStringResult(String result) {
        btnKeep.setEnabled(true);
        if(TextUtils.isEmpty(result))return;

        image = result;
        ToastUtil.showToast(this, "产品图片上传成功", true);
        intentData();
        EventBus.getDefault().post(new AnyEventMyPdtList());
    }

    private class ProductImgAdapter extends MyBaseAdapter {

        public ProductImgAdapter(Activity context,ArrayList<String> arrayList) {
            super(context,arrayList);
        }

        @Override
        public int getItemLayoutResId() {
            return R.layout.adapter_prdtimg;
        }


        @Override
        public Object getViewHolder(View rootView) {
            return new ViewHolder(rootView);
        }

        private class ViewHolder {
            ImageView img,imgx;

            public ViewHolder(View view){
                img = view.findViewById(R.id.adapter_prdtimg);
                imgx = view.findViewById(R.id.adapter_prdtx);
            }
        }


        @Override
        public void setItemData(final int position, Object dataItem, Object viewHolder) {
            final String item= (String) dataItem;
            ViewHolder myholder= (ViewHolder) viewHolder;
            Glide.with(mContext).load(dataItem).apply(new RequestOptions()
                    .error(R.drawable.global_img_default).placeholder(R.drawable.global_img_default)).into(myholder.img);
            myholder.imgx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new androidx.appcompat.app.AlertDialog.Builder(ProductImgsActivity.this)
                            .setMessage("是否删除图片?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    setResult(RESULT_OK);
                                    dataList.remove(position);
                                    notifyDataSetChanged();
                                    if (item.startsWith("http")) {//判断字符串是否是已上传图片
                                        path=item;
                                        deleteProductImgsPresenter.delProductImg();
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            })
                            .show();
                }
            });
        }


    }
}