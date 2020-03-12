package com.qgyyzs.globalcosmetics.fragment;

import android.content.Intent;
import com.google.android.material.tabs.TabLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.BidActivity;
import com.qgyyzs.globalcosmetics.activity.CompanyCenterActivity;
import com.qgyyzs.globalcosmetics.activity.CompanyDetialActivity;
import com.qgyyzs.globalcosmetics.activity.HomeSearchActivity;
import com.qgyyzs.globalcosmetics.activity.LoginActivity;
import com.qgyyzs.globalcosmetics.activity.MedicalNewsActivity;
import com.qgyyzs.globalcosmetics.activity.MedicineDetailActivity;
import com.qgyyzs.globalcosmetics.activity.ProductLibraryActivity;
import com.qgyyzs.globalcosmetics.activity.ProxyInfoActivity;
import com.qgyyzs.globalcosmetics.activity.ProxyLibraryActivity;
import com.qgyyzs.globalcosmetics.activity.RecruitActivity;
import com.qgyyzs.globalcosmetics.activity.SortProductActivity;
import com.qgyyzs.globalcosmetics.activity.UserDetailActivity;
import com.qgyyzs.globalcosmetics.activity.WebBaseActivity;
import com.qgyyzs.globalcosmetics.adapter.HomeCompanyAdapter;
import com.qgyyzs.globalcosmetics.adapter.ReleaseListAdapter;
import com.qgyyzs.globalcosmetics.base.BaseFragment;
import com.qgyyzs.globalcosmetics.bean.CompanyBean;
import com.qgyyzs.globalcosmetics.bean.HomeOptionBean;
import com.qgyyzs.globalcosmetics.bean.MedicalNewsBean;
import com.qgyyzs.globalcosmetics.bean.ReleaseBean;
import com.qgyyzs.globalcosmetics.bean.UPMarqueeViewData;
import com.qgyyzs.globalcosmetics.customview.GlideImageLoader;
import com.qgyyzs.globalcosmetics.customview.UPMarqueeView;
import com.qgyyzs.globalcosmetics.mvp.iface.BannerView;
import com.qgyyzs.globalcosmetics.mvp.iface.HomeCompanyView;
import com.qgyyzs.globalcosmetics.mvp.iface.HomeInfoView;
import com.qgyyzs.globalcosmetics.mvp.iface.HomeOptionView;
import com.qgyyzs.globalcosmetics.mvp.iface.MedicalNewsView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.HomeBannerFootPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.HomeBannerTopPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.HomeCompanyPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.HomeDLPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.HomeHeadLinePresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.HomeStarOptionPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.HomeZSPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.ScreenUtils;
import com.qgyyzs.globalcosmetics.utils.TabLayoutUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/5.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener,BannerView,HomeInfoView,MedicalNewsView,HomeCompanyView,HomeOptionView{
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.mListView)
    ListView mListView;
    @BindView(R.id.titlebar)
    LinearLayout titlebar;

    private GridView mGridView;
    private TextView mMoreInvestTv;
    private TabLayout mTablayout;
    private UPMarqueeView mUPMarqueeView;
    private Banner mBannerTop,mBannerFooter;
    private LinearLayout mHomeCompanyLl,mHomeProductLl,mHomeAgentLl,mHomeBidLl,mHomePrductsLl,mHomeProducttypeLl,mHomeNewsLl,mHomeZhaopinLl;
    private TabLayout mStarTabLayout;

    private List<String> image1s=new ArrayList<>(), image2s=new ArrayList<>();
    private List<String> lunboUrls=new ArrayList<>(), lunboUrl2s=new ArrayList<>();
    private List<String> jsonStringList1=new ArrayList<>(),jsonStringList2=new ArrayList<>();
    private List<String> linktextList1=new ArrayList<>(),linktextList2=new ArrayList<>();
    private List<MedicalNewsBean> mMedicalNewsBeanList=new ArrayList<>();

    private List<ReleaseBean> mReleaseBeanList=new ArrayList<>(),mReleaseBeanList1=new ArrayList<>(),mReleaseBeanList2=new ArrayList<>();
    private ReleaseListAdapter mReleaseListAdapter;
    private List<CompanyBean> mCompanyBeanList=new ArrayList<>();

    private HomeCompanyAdapter adapter;

    private int optionId = 1;
    private List<HomeOptionBean.JsonData> optionListBean=new ArrayList<>();

    private String moreTextString = "更多招商";

    private View headerView,footView;

    private int cur=1;
    private HomeBannerTopPresenter bannerTopPresenter=new HomeBannerTopPresenter(this, (RxFragmentActivity) getActivity());
    private HomeBannerFootPresenter bannerFootPresenter;
    private HomeZSPresenter zsPresenter=new HomeZSPresenter(this, (RxFragmentActivity) getActivity());
    private HomeDLPresenter dlPresenter;
    private HomeHeadLinePresenter headLinePresenter= new HomeHeadLinePresenter(this, (RxFragmentActivity) getActivity());
    private HomeCompanyPresenter companyPresenter=new HomeCompanyPresenter(this, (RxFragmentActivity) getActivity());
    private HomeStarOptionPresenter optionPresenter=new HomeStarOptionPresenter(this, (RxFragmentActivity) getActivity());

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {

        headerView = getLayoutInflater().inflate(R.layout.home_header, null);
        footView =getLayoutInflater().inflate(R.layout.home_foot,null);

//      mListView=headerView.findViewById(R.id.mListView);
        mBannerTop = headerView.findViewById(R.id.banner_top);
        mUPMarqueeView =  headerView.findViewById(R.id.upview1);
        mHomeCompanyLl= headerView.findViewById(R.id.home_company_ll);
        mHomeProductLl=  headerView.findViewById(R.id.home_product_ll);
        mHomeAgentLl=  headerView.findViewById(R.id.home_agent_ll);
        mHomeBidLl= headerView.findViewById(R.id.home_bid_ll);
        mHomePrductsLl= headerView.findViewById(R.id.home_prducts_ll);
        mHomeProducttypeLl= headerView.findViewById(R.id.home_producttype_ll);
        mHomeNewsLl= headerView.findViewById(R.id.home_news_ll);
        mHomeZhaopinLl= headerView.findViewById(R.id.home_zhaopin_ll);
        mTablayout= headerView.findViewById(R.id.tabLayout);

        mBannerFooter = footView.findViewById(R.id.banner_footer);
        mMoreInvestTv= footView.findViewById(R.id.more_invest_tv);
        mStarTabLayout = footView.findViewById(R.id.star_tabLayout);
        mGridView = footView.findViewById(R.id.mHomeGridView);


        mBannerTop.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ScreenUtils.getScreenWidth(getActivity()) / 8*3));
        mBannerFooter.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ScreenUtils.getScreenWidth(getActivity()) / 8*3));

        mListView.addHeaderView(headerView);
        mListView.addFooterView(footView);
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
            jsonObject.put("curpage",cur);
            jsonObject.put("pagesize",10);
            jsonObject.put("optionid",optionId);
            LogUtils.e(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToast(getActivity(),msg,true);
    }

    @Override
    public void showBannerResult(List<String> imageUrlList, List<String> imgLinkList, List<String> jsonString,List<String> linkText) {
        if(refreshLayout!=null)refreshLayout.finishRefresh();
        if(imageUrlList==null||imageUrlList.size()==0) {
            return;
        }
        image1s.clear();
        image1s.addAll(imageUrlList);

        lunboUrls.clear();
        lunboUrls.addAll(imgLinkList);

        jsonStringList1.clear();
        jsonStringList1.addAll(jsonString);

        linktextList1.clear();
        linktextList1.addAll(linkText);

        mBannerTop.setImageLoader(new GlideImageLoader());
        mBannerTop.setImages(image1s);
        mBannerTop.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mBannerTop.setBannerTitles(linktextList1);
        mBannerTop.setDelayTime(4000);
        mBannerTop.start();
    }

    @Override
    public void showHomeInfoResult(List<ReleaseBean> releaseBeanList) {
        if(refreshLayout!=null)refreshLayout.finishRefresh();
        if(releaseBeanList==null||releaseBeanList.size()==0)return;
        mReleaseBeanList1.clear();
        mReleaseBeanList1.addAll(releaseBeanList);
        if(moreTextString.equals("更多招商")) {
            mReleaseBeanList.clear();
            mReleaseBeanList.addAll(releaseBeanList);
            mReleaseListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showCompanyResult(List<CompanyBean> list,int totalPage) {
        if(refreshLayout!=null&&refreshLayout.isRefreshing())refreshLayout.finishRefresh();
        if(refreshLayout!=null&&refreshLayout.isLoading())refreshLayout.finishLoadmore();
        if(list==null)return;
        if(cur==1)mCompanyBeanList.clear();
        mCompanyBeanList.addAll(list);
        adapter.notifyDataSetChanged();
        if(refreshLayout!=null)refreshLayout.setLoadmoreFinished(cur>=totalPage?true:false);
    }

    @Override
    public void showHeadLineResult(List<MedicalNewsBean> list) {
        if(refreshLayout!=null)refreshLayout.finishRefresh();
        if(list==null||list.size()==0) return;
        mMedicalNewsBeanList.clear();
        mMedicalNewsBeanList.addAll(list);
        if (mMedicalNewsBeanList.size() > 1) {
            List<UPMarqueeViewData> data = new ArrayList<UPMarqueeViewData>();
            for (int j = 0; j < mMedicalNewsBeanList.size(); j++) {
                data.add(new UPMarqueeViewData(mMedicalNewsBeanList.get(j).getId(), "头条", mMedicalNewsBeanList.get(j).getTitle(), "" + j));
            }
            mUPMarqueeView.setViews(data);
            mUPMarqueeView.startFlipping();
        }
    }

    @Override
    public void showOptionResult(HomeOptionBean bean) {
        if(bean==null)return;
        optionListBean.clear();
        optionListBean.addAll(bean.getJsonData());
        optionId=bean.getJsonData().get(0).getId();

        mStarTabLayout.removeAllTabs();
        for(int i=0;i<optionListBean.size();i++){
            mStarTabLayout.addTab(mStarTabLayout.newTab().setText(optionListBean.get(i).getOptionName()));
        }
    }

    @Override
    protected void initData() {
        bannerFootPresenter =new HomeBannerFootPresenter(bannerFootView, (RxFragmentActivity) getActivity());
        dlPresenter =new HomeDLPresenter(dlView, (RxFragmentActivity) getActivity());
        mTablayout.addTab(mTablayout.newTab().setText("最新招商"));
        mTablayout.addTab(mTablayout.newTab().setText("最新代理商"));
//        mTablayout.post(new Runnable() {
//            @Override
//            public void run() {
//                TabLayoutUtils.setIndicator(mTablayout,50,50);
//            }
//        });


//        final FastScrollManger manager=new FastScrollManger(getActivity(), 2);
//        recyclerView.setLayoutManager(manager);
        mReleaseListAdapter = new ReleaseListAdapter(getActivity(),mReleaseBeanList);
        mListView.setAdapter(mReleaseListAdapter);

        adapter=new HomeCompanyAdapter(getActivity(),mCompanyBeanList);
        mGridView.setAdapter(adapter);
//        recyclerView.setAdapter(adapter);
//        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return adapter.isHeader(position) ? manager.getSpanCount() : 1;
//            }
//        });
//        recyclerView.addOnScrollListener(new MyRecyclerViewScrollListener());

        new Thread(new Runnable() {
            @Override
            public void run() {
                optionPresenter.getCompanyOption();
                refreshLayout.autoRefresh();
            }
        }).start();
    }

    @Override
    protected void initListener() {
        mHomeCompanyLl.setOnClickListener(this);
        mHomeZhaopinLl.setOnClickListener(this);
        mHomeAgentLl.setOnClickListener(this);
        mHomeBidLl.setOnClickListener(this);
        mHomeNewsLl.setOnClickListener(this);
        mHomePrductsLl.setOnClickListener(this);
        mHomeProductLl.setOnClickListener(this);
        mHomeProducttypeLl.setOnClickListener(this);
        mMoreInvestTv.setOnClickListener(this);
        mTvRight.setOnClickListener(this);
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                cur++;
                companyPresenter.getCompanyList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                cur=1;
                bannerTopPresenter.getBannerTop();
                bannerFootPresenter.getBannerFoot();
                headLinePresenter.getHeadLineList();
                zsPresenter.getZsList();
                dlPresenter.getDlList();
                optionPresenter.getCompanyOption();
//                companyPresenter.getCompanyList();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!MyApplication.islogin) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                ReleaseBean releaseBean;
                if(moreTextString.equals("更多招商")) {
                    releaseBean=mReleaseBeanList.get(position-1);
                    Intent intent = new Intent(getActivity(), MedicineDetailActivity.class);
                    intent.putExtra("proid", releaseBean.getReleaseid());
                    intent.putExtra("name", releaseBean.getTitle());
                    intent.putExtra("image", releaseBean.getPhoto());
                    intent.putExtra("muser", releaseBean.getMuser());
                    startActivity(intent);
                }else{
                    releaseBean=mReleaseBeanList.get(position-1);
                    Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                    intent.putExtra("info", 10);//代理商库
                    intent.putExtra("nickname", releaseBean.getTitle());
                    intent.putExtra("touserid",releaseBean.getReleaseid()+"");
                    startActivity(intent);
                }
            }
        });

        mStarTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mStarTabLayout.setFocusable(true);
                mStarTabLayout.setFocusable(true);
                optionId=optionListBean.get(tab.getPosition()).getId();
                cur=1;
                companyPresenter.getCompanyList();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        mReleaseBeanList.clear();
                        mReleaseBeanList.addAll(mReleaseBeanList1);
                        mReleaseListAdapter.notifyDataSetChanged();
                        moreTextString="更多招商";
                        mMoreInvestTv.setText(moreTextString);
                        break;
                    case 1:
                        mReleaseBeanList.clear();
                        mReleaseBeanList.addAll(mReleaseBeanList2);
                        mReleaseListAdapter.notifyDataSetChanged();
                        moreTextString="更多代理";
                        mMoreInvestTv.setText(moreTextString);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mBannerTop.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (!MyApplication.islogin) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                if(jsonStringList1==null)return;
                if (TextUtils.isEmpty(jsonStringList1.get(position))||jsonStringList1.get(position).equals("null")) {
                    return;
                }
                if (lunboUrls.get(position).equals("0")) {
                    Intent intent = new Intent(getActivity(), WebBaseActivity.class);
                    intent.putExtra("title", linktextList1.get(position));
                    intent.putExtra("url", jsonStringList1.get(position));
                    startActivity(intent);
                } else if (lunboUrls.get(position).equals("1")) {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStringList1.get(position));
                        Intent intent = new Intent(getActivity(), MedicineDetailActivity.class);
                        intent.putExtra("proid", jsonObject.getInt("id"));
                        intent.putExtra("name", jsonObject.getString("Subject"));
                        intent.putExtra("image", jsonObject.getString("image"));
                        intent.putExtra("company",jsonObject.getString("danwei"));
                        intent.putExtra("muser", jsonObject.getString("PcUsername"));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (lunboUrls.get(position).equals("2")) {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStringList1.get(position));
                        Intent companyIntent = new Intent(getActivity(), CompanyDetialActivity.class);
                        companyIntent.putExtra("company_username", jsonObject.getString("PcUsername"));
                        companyIntent.putExtra("title", jsonObject.getString("company"));
                        getActivity().startActivity(companyIntent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
            }
        });

        mBannerFooter.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (!MyApplication.islogin) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                if(jsonStringList2==null)return;
                if (TextUtils.isEmpty(jsonStringList2.get(position))||jsonStringList2.get(position).equals("null")) {
                    return;
                }
                if (lunboUrl2s.get(position).equals("0")) {
                    Intent intent = new Intent(getActivity(), WebBaseActivity.class);
                    intent.putExtra("title", linktextList2.get(position));
                    intent.putExtra("url", jsonStringList2.get(position));
                    startActivity(intent);
                } else if (lunboUrl2s.get(position).equals("1")) {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStringList2.get(position));
                        Intent intent = new Intent(getActivity(), MedicineDetailActivity.class);
                        intent.putExtra("proid", jsonObject.getInt("id"));
                        intent.putExtra("name", jsonObject.getString("Subject"));
                        intent.putExtra("image", jsonObject.getString("image"));
                        intent.putExtra("company",jsonObject.getString("danwei"));
                        intent.putExtra("muser", jsonObject.getString("PcUsername"));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (lunboUrl2s.get(position).equals("2")) {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStringList2.get(position));
                        Intent companyIntent = new Intent(getActivity(), CompanyDetialActivity.class);
                        companyIntent.putExtra("company_username", jsonObject.getString("PcUsername"));
                        companyIntent.putExtra("title", jsonObject.getString("company"));
                        getActivity().startActivity(companyIntent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_company_ll://名企展区
                if (!MyApplication.islogin) {
                    LoginActivity.start(getActivity());
                    return;
                }
                startActivity(new Intent(getActivity(), CompanyCenterActivity.class));
                break;
            case R.id.home_product_ll://产品分类
                if (!MyApplication.islogin) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), ProductLibraryActivity.class));
                break;
            case R.id.home_agent_ll://代理商库
                if (!MyApplication.islogin) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), ProxyLibraryActivity.class));
                break;
            case R.id.home_bid_ll://代理信息
                if (!MyApplication.islogin) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), ProxyInfoActivity.class));
                break;
            case R.id.home_prducts_ll://中标品种
                startActivity(new Intent(getActivity(), BidActivity.class));
                break;
            case R.id.home_producttype_ll://产品分类
                if (!MyApplication.islogin) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), SortProductActivity.class));
                break;
            case R.id.home_news_ll://医药资讯
                startActivity(new Intent(getActivity(), MedicalNewsActivity.class));
                break;
            case R.id.home_zhaopin_ll://人才招聘
                if (!MyApplication.islogin) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), RecruitActivity.class));
                break;
            case R.id.more_invest_tv://更多招商
                if (!MyApplication.islogin) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                if(moreTextString.equals("更多招商"))
                    startActivity(new Intent(getActivity(), ProductLibraryActivity.class));
                else
                    startActivity(new Intent(getActivity(), ProxyLibraryActivity.class));
                break;
            case R.id.tv_right://搜索
                startActivity(new Intent(getActivity(), HomeSearchActivity.class));
                break;
        }
    }

    private BannerView bannerFootView=new BannerView() {
        @Override
        public void showBannerResult(List<String> imageUrlList, List<String> imgLinkList, List<String> jsonString,List<String> linkText) {
            if(refreshLayout!=null)refreshLayout.finishRefresh();
            if(imageUrlList==null||imageUrlList.size()==0)return;

            image2s.clear();
            image2s.addAll(imageUrlList);

            lunboUrl2s.clear();
            lunboUrl2s.addAll(imgLinkList);

            jsonStringList2.clear();
            jsonStringList2.addAll(jsonString);

            linktextList2.clear();
            linktextList2.addAll(linkText);

            mBannerFooter.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
            mBannerFooter.setImageLoader(new GlideImageLoader());
            mBannerFooter.setImages(image2s);
            mBannerFooter.setBannerTitles(linktextList2);
            mBannerFooter.setDelayTime(4000);
            mBannerFooter.start();
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
    };
    private HomeInfoView dlView=new HomeInfoView() {
        @Override
        public void showHomeInfoResult(List<ReleaseBean> releaseBeanList) {
            if(refreshLayout!=null)refreshLayout.finishRefresh();
            if(releaseBeanList==null||releaseBeanList.size()==0)return;
            mReleaseBeanList2.clear();
            mReleaseBeanList2.addAll(releaseBeanList);
            if(moreTextString.equals("更多代理")) {
                mReleaseBeanList.clear();
                mReleaseBeanList.addAll(mReleaseBeanList2);
//                ListViewUtils.setListViewHeightBasedOnChildren(mListView);
                mReleaseListAdapter.notifyDataSetChanged();
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
    };
}