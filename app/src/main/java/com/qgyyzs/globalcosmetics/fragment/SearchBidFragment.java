package com.qgyyzs.globalcosmetics.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.CompanyCenterActivity;
import com.qgyyzs.globalcosmetics.base.BaseFragment;
import com.qgyyzs.globalcosmetics.utils.StringsUtils;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2017/3/28.
 */

public class SearchBidFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.search_edit)
    EditText mSearchEdit;
    @BindView(R.id.cancel_tv)
    TextView mCancelTv;
    @BindView(R.id.hostory_listview)
    ListView mListView;
    @BindView(R.id.clear_hostory)
    TextView mClearHostory;
    @BindView(R.id.empty_view)
    View emptyView;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private String historyString;
    private String[] mStrings;
    private ArrayAdapter<String> mArrayAdapter;
    private List<String> mStringList;
    private List<String> searchList=new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.fragment_search_company;
    }

    @Override
    protected void initView(View view) {
        mSharedPreferences = getActivity().getSharedPreferences("Search", MODE_PRIVATE);
        mSearchEdit.setHint("搜索公司名称");
    }

    @Override
    protected void initData() {
        mStringList = new ArrayList<>();
        historyString = mSharedPreferences.getString("bid_history", "");
        mArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, searchList);
        mListView.setAdapter(mArrayAdapter);
        mListView.setEmptyView(emptyView);

        if(TextUtils.isEmpty(historyString))return;
        mClearHostory.setVisibility(View.VISIBLE);
        mStrings = historyString.split("\\,");
        mStrings = StringsUtils.ToOnlyStrs(mStrings);
        for (int i = 0; i < mStrings.length; i++) {
            mStringList.add(mStrings[i]);
        }
        Collections.reverse(mStringList);
        searchList.addAll(mStringList.size()>8?mStringList.subList(0, 8):mStringList);
        mArrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {
        mCancelTv.setOnClickListener(this);
        mClearHostory.setOnClickListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CompanyCenterActivity.class);
                intent.putExtra("searchStr", searchList.get(position).toString());
                getActivity().startActivity(intent);
            }
        });
        mSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    //点击搜索要做的操作
                    if(mSearchEdit.getText().length()>0){
                        Intent intent = new Intent(getActivity(), CompanyCenterActivity.class);
                        intent.putExtra("searchStr", mSearchEdit.getText().toString().trim());
                        getActivity().startActivity(intent);

                        mEditor = mSharedPreferences.edit();
                        mEditor.putString("bid_history", historyString + mSearchEdit.getText().toString().trim() + ",");
                        mEditor.commit();
                    }else{
                        ToastUtil.showToast(getActivity(), "请输入搜索内容", true);
                    }
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_tv:
                getActivity().finish();
                break;
            case R.id.clear_hostory:
                mStringList.clear();
                mSharedPreferences.edit().clear().commit();
                historyString="";
                mArrayAdapter.notifyDataSetChanged();
                mClearHostory.setVisibility(View.GONE);
                break;
        }
    }
}

