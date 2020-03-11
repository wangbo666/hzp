package com.qgyyzs.globalcosmetics.activity;

import android.content.Intent;

import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import butterknife.BindView;

public class JobDescribActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_right)
    TextView mRightTv;
    @BindView(R.id.zhengce_edit)
    EditText mJobEdit;


    @Override
    protected int getLayout() {
        return R.layout.activity_product_policy;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        toolbar.setTitle("岗位职责/要求");
        mJobEdit.setHint("填写职位描述");
        mRightTv.setText("完成");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mJobEdit.getText().toString().trim().equals("")) {
                    ToastUtil.showToast(JobDescribActivity.this, "不能为空", true);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("jobDescrib", mJobEdit.getText().toString().trim());
                    //记录下要返回给那个父亲，可能还有什么继父什么的
                    setResult(002, intent);
                    finish();
                }
            }
        });
    }
}
