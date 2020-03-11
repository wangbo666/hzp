package com.qgyyzs.globalcosmetics.activity;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.customview.WaitDialog;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.FreshCompanyPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.FreshProductPresenter;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/24 0024.
 */

public class FreshActivity extends BaseActivity implements StringView{
    private FreshProductPresenter freshProductPresenter=new FreshProductPresenter(this,this);
    private FreshCompanyPresenter freshCompanyPresenter=new FreshCompanyPresenter(this,this);
    @BindView(R.id.product_btn)
    Button btnProduct;
    @BindView(R.id.company_btn)
    Button btnCompany;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private WaitDialog waitDialog;
    @Override
    protected int getLayout() {
        return R.layout.activity_fresh;
    }

    @Override
    protected void initView() {
        StatusBarUtil.setPaddingSmart(this,toolbar);
        StatusBarUtil.immersive(this);

        waitDialog=new WaitDialog(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                freshProductPresenter.getFresh();
            }
        });
        btnCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                freshCompanyPresenter.getFresh();
            }
        });
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
        return null;
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showStringResult(String result) {
        if(TextUtils.isEmpty(result))return;
        new AlertDialog.Builder(this)
                .setMessage(result)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        setResult(RESULT_OK);//确定按钮事件
                    }
                })
                .show();
    }
}
