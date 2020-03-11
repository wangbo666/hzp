package com.qgyyzs.globalcosmetics.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.IntentionDetialBean;
import com.qgyyzs.globalcosmetics.mvp.iface.IntentionDetialView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.IntentionDetialPresenter;
import com.qgyyzs.globalcosmetics.nim.session.SessionHelper;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.ScreenUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/12 0012.
 */

public class IntentionMsgDetialActivity extends BaseActivity implements IntentionDetialView,View.OnClickListener{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mTvContent)
    TextView mTvContent;
    @BindView(R.id.send_msg_tv)
    TextView mTvsendMsg;
    @BindView(R.id.send_phone_tv)
    TextView mTvsendPhone;

    private List<String> kefuList = new ArrayList<>();
    private String str[] = null;
    private Dialog mDialog;
    private ListView mListView;
    private ArrayAdapter<String> mArrayAdapter;

    private int id;
    private String nimID,linkTel;

    private IntentionDetialPresenter presenter=new IntentionDetialPresenter(this,this);

    @Override
    protected int getLayout() {
        return R.layout.activity_intentiondetial;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.kefu_layout);
        Window dialogWindow = mDialog.getWindow();
        mListView = (ListView) dialogWindow.findViewById(R.id.kefu_listview);
        WindowManager.LayoutParams lp1 = dialogWindow
                .getAttributes();// 创建布局
        lp1.width = ScreenUtils.getScreenWidth(this);
        lp1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp1);// 加载布局
    }

    @Override
    public void initData() {
        id = getIntent().getIntExtra("id",0);
        toolbar.setTitle(getIntent().getStringExtra("title"));

        new Thread(new Runnable() {
            @Override
            public void run() {
                presenter.getDetial();
            }
        }).start();
    }

    @Override
    public void initListener() {
        mTvsendMsg.setOnClickListener(this);
        mTvsendPhone.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                        && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermission(Manifest.permission.CALL_PHONE,
                            getString(R.string.permission_call),
                            1);
                }else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + kefuList.get(position)));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.send_msg_tv:
                SessionHelper.startP2PSession(this, nimID);
                break;
            case R.id.send_phone_tv:
                str = null;
                kefuList.clear();
                if (!TextUtils.isEmpty(linkTel)) {
                    str = linkTel.split("\\|");
                    for (int j = 0; j < str.length; j++) {
                        if (!str[j].equals(null)) {
                            kefuList.add(str[j]);
                        }
                    }
                }
                mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, kefuList);
                mListView.setAdapter(mArrayAdapter);
                mDialog.show();
                break;
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
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.e(jsonObject.toString());
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToast(this, msg,true);
    }

    @Override
    public void showDetialResult(IntentionDetialBean bean) {
        if (bean == null) {
            showToast("您无权操作此留言");
            return;
        }
        if (bean.getResult() == 1) {
            mTvContent.setText(Html.fromHtml(bean.getJsonData().getContent()));
            nimID = bean.getJsonData().getUserInfo().getNimID();
            if (!TextUtils.isEmpty(nimID)) {
                List<String> list = new ArrayList<>();
                list.add(nimID);
                NIMClient.getService(UserService.class).fetchUserInfo(list);
            }
            linkTel = bean.getJsonData().getUserInfo().getLinkTel();
        } else {
            if (!TextUtils.isEmpty(bean.getMsg())) {
                showToast(bean.getMsg());
            }
        }
        mTvsendMsg.setVisibility(TextUtils.isEmpty(nimID) ? View.GONE : View.VISIBLE);
        mTvsendPhone.setVisibility(TextUtils.isEmpty(linkTel) ? View.GONE : View.VISIBLE);
    }
}
