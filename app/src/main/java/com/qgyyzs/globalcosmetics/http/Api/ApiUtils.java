package com.qgyyzs.globalcosmetics.http.Api;

import com.qgyyzs.globalcosmetics.http.retrofit.RetrofitUtils;

/**
 * 接口工具类
 *
 * @author wangbo
 */

public class ApiUtils {
    private static HomeBannerTopApi homeBannerTopApi;

    private static HomeBannerFootApi homeBannerFootApi;

    private static HomeZSApi homeZSApi;

    private static HomeDLApi homeDLApi;

    private static HomeHeadLineApi homeHeadLineApi;

    private static HomeCompanyApi homeCompanyApi;

    private static CompanyCenterApi companyCenterApi;

    private static ProductLibarayApi productLibarayApi;

    private static CompanyProductListApi companyProductListApi;

    private static CompanyIsGuanzhuApi companyIsGuanzhuApi;

    private static CompanyGuanzhuApi companyGuanzhuApi;

    private static SortProductBigApi sortProductBigApi;

    private static SortProductSmallApi sortProductSmallApi;

    private static ProvinceApi provinceApi;

    private static CityApi cityApi;

    private static ProxyLibraryApi proxyLibraryApi;

    private static ProxyInfoApi proxyInfoApi;

    private static ZhongbiaoApi zhongbiaoApi;

    private static YibaoApi yibaoApi;

    private static NewsHeadlineApi newsHeadlineApi;

    private static NewsAllApi newsAllApi;

    private static NewsBidApi newsBidApi;

    private static NewsExpApi newsExpApi;

    private static RecruitApi recruitApi;

    private static IntentionMsgApi intentionMsgApi;

    private static ResentVisitApi resentVisitApi;

    private static LookVisOneApi lookVisOneApi;

    private static LookVisAllApi lookVisAllApi;

    private static FriendDelApi delFriendApi;

    private static FriendListApi myfriendApi;

    private static FriendAddApi friendAddApi;

    private static FriendSearchApi friendSearchApi;

    private static MyProductApi myProductApi;

    private static MyguanzhuApi myguanzhuApi;

    private static MyCollectApi myCollectApi;

    private static ChildAccountListApi  childAccountListApi;

    private static ChildAccountAddApi childAccountAddApi;

    private static ChildAccountDelApi childAccountDelApi;

    private static DelProxyApi delProxyApi;

    private static DelProductApi delProductApi;

    private static DelJobApi delJobApi;

    private static IntentionDetialApi intentionDetialApi;

    private static MeInfoCountApi meInfoCountApi;

    private static KefuTelApi kefuTelApi;

    private static RegisterApi registerApi;

    private static RegisterCodeApi registerCodeApi;

    private static LoginApi  loginApi;

    private static LinkManApi linkManApi;

    private static UserDetialApi userDetialApi;

    private static KefuAreaGetApi kefuAreaGetApi;

    private static KefuAreaSetApi kefuAreaSetApi;

    private static KeshiApi keshiApi;

    private static TypeApi typeApi;

    private static ChannelApi channelApi;

    private static JobTypeApi jobTypeApi;

    private static FreshProductApi freshProductApi;

    private static FreshCompanyApi freshCompanyApi;

    private static VersionApi versionApi;

    private static ZSUserListApi zsUserListApi;

    private static SimpleUserInfoApi simpleUserInfoApi;

    private static ProductDetialApi  productDetialApi;

    private static UpdateUserApi updateUserApi;

    private static PublishProxyApi  publishProxyApi;

    private static DelProductImgApi delProductImgApi;

    private static ProductAddApi  productAddApi;

    private static ProductUpdateApi productUpdateApi;

    private static ProxySetApi proxySetApi;

    private static ProxyInfoDetialApi proxyInfoDetialApi;

    private static MsgCountApi msgCountApi;

    private static VisterCountApi visterCountApi;

    private static ForgetPwdCodeApi forgetPwdCodeApi;

    private static ForgetPwdApi forgetPwdApi;

    private static BindPhoneApi bindPhoneApi;

    private static BindPhoneCodeApi  bindPhoneCodeApi;

    private static CancelBindPhoneApi cancelBindPhoneApi;

    private static ISfabuApi iSfabuApi;

    private static ZizhiStateApi  zizhiStateApi;

    private static UpdatePwdApi updatePwdApi;

    private static UpdatePwdCodeApi updatePwdCodeApi;

    private static UpdateFriendStateApi updateFriendStateApi;

    private static FeedBackApi feedBackApi;

    private static PublishJobApi publishJobApi;

    private static ProductIsCollectApi productIsCollectApi;

    private static ProductCollectApi productCollectApi;

    private static ProductVisterAddApi productVisterAddApi;

    private static ProductAreaApi productAreaApi;

    private static ProductKeshiApi productKeshiApi;

    private static LogoutApi logoutApi;

    private static ZizhiApi zizhiApi;

    private static UpdateAvtarApi updateAvtarApi;

    private static UpdateZizhiImgApi updateZizhiImgApi;

    private static UpdateProductImgApi updateProductImgApi;

    private static UpdateFriendRemakeApi updateFriendRemakeApi;

    private static IntentionMsgDelApi intentionMsgDelApi;

    private static HomeStarOptionApi homeStarOptionApi;

    private static ZhanhuiTypeApi zhanhuiTypeApi;

    private static RegisterSalemanApi registerSalemanApi;

    private static ISshowSalemanApi iSshowSalemanApi;

    private static UpdatePWApi updatePWApi;

    private static ShowRegisterApi showRegisterApi;

    private static LoginCodeApi loginCodeApi;

    private static RegisterApi2 registerApi2;

    public static RegisterApi2 getRegisterApi2(){
        if(registerApi2 == null){
            registerApi2 = RetrofitUtils.get().retrofit().create(RegisterApi2.class);
        }
        return registerApi2;
    }

    public static LoginCodeApi getLoginCodeApi(){
        if(loginCodeApi == null){
            loginCodeApi = RetrofitUtils.get().retrofit().create(LoginCodeApi.class);
        }
        return loginCodeApi;
    }

    public static ShowRegisterApi getShowRegisterApi(){
        if(showRegisterApi == null){
            showRegisterApi = RetrofitUtils.get().retrofit().create(ShowRegisterApi.class);
        }
        return showRegisterApi;
    }

    public static UpdatePWApi getUpdatePWApi(){
        if(updatePWApi == null){
            updatePWApi = RetrofitUtils.get().retrofit().create(UpdatePWApi.class);
        }
        return updatePWApi;
    }

    public static ISshowSalemanApi getiSshowSalemanApi(){
        if(iSshowSalemanApi == null){
            iSshowSalemanApi = RetrofitUtils.get().retrofit().create(ISshowSalemanApi.class);
        }
        return iSshowSalemanApi;
    }

    public static RegisterSalemanApi getRegisterSalemanApi(){
        if(registerSalemanApi == null){
            registerSalemanApi = RetrofitUtils.get().retrofit().create(RegisterSalemanApi.class);
        }
        return registerSalemanApi;
    }

    public static ZhanhuiTypeApi getZhanhuiTypeApi(){
        if(zhanhuiTypeApi == null){
            zhanhuiTypeApi = RetrofitUtils.get().retrofit().create(ZhanhuiTypeApi.class);
        }
        return zhanhuiTypeApi;
    }

    public static HomeStarOptionApi getHomeStarOptionApi(){
        if(homeStarOptionApi == null){
            homeStarOptionApi = RetrofitUtils.get().retrofit().create(HomeStarOptionApi.class);
        }
        return homeStarOptionApi;
    }

    public static IntentionMsgDelApi getIntentionMsgDelApi(){
        if(intentionMsgDelApi == null){
            intentionMsgDelApi = RetrofitUtils.get().retrofit().create(IntentionMsgDelApi.class);
        }
        return intentionMsgDelApi;
    }

    public static UpdateFriendRemakeApi getUpdateFriendRemakeApi(){
        if(updateFriendRemakeApi == null){
            updateFriendRemakeApi = RetrofitUtils.get().retrofit().create(UpdateFriendRemakeApi.class);
        }
        return updateFriendRemakeApi;
    }

    public static UpdateProductImgApi getUpdateProductImgApi(){
        if(updateProductImgApi == null){
            updateProductImgApi = RetrofitUtils.get().retrofit().create(UpdateProductImgApi.class);
        }
        return updateProductImgApi;
    }

    public static UpdateZizhiImgApi getUpdateZizhiImgApi(){
        if(updateZizhiImgApi == null){
            updateZizhiImgApi = RetrofitUtils.get().retrofit().create(UpdateZizhiImgApi.class);
        }
        return updateZizhiImgApi;
    }

    public static UpdateAvtarApi getUpdateAvtarApi(){
        if(updateAvtarApi == null){
            updateAvtarApi = RetrofitUtils.get().retrofit().create(UpdateAvtarApi.class);
        }
        return updateAvtarApi;
    }

    public static ZizhiApi getZizhiApi(){
        if(zizhiApi == null){
            zizhiApi = RetrofitUtils.get().retrofit().create(ZizhiApi.class);
        }
        return zizhiApi;
    }

    public static LogoutApi getLogoutApi(){
        if(logoutApi == null){
            logoutApi = RetrofitUtils.get().retrofit().create(LogoutApi.class);
        }
        return logoutApi;
    }

    public static ProductKeshiApi getProductKeshiApi(){
        if(productKeshiApi == null){
            productKeshiApi = RetrofitUtils.get().retrofit().create(ProductKeshiApi.class);
        }
        return productKeshiApi;
    }

    public static ProductAreaApi getProductAreaApi(){
        if(productAreaApi == null){
            productAreaApi = RetrofitUtils.get().retrofit().create(ProductAreaApi.class);
        }
        return productAreaApi;
    }

    public static ProductVisterAddApi getProductVisterAddApi(){
        if(productVisterAddApi == null){
            productVisterAddApi = RetrofitUtils.get().retrofit().create(ProductVisterAddApi.class);
        }
        return productVisterAddApi;
    }

    public static ProductCollectApi getProductCollectApi(){
        if(productCollectApi == null){
            productCollectApi = RetrofitUtils.get().retrofit().create(ProductCollectApi.class);
        }
        return productCollectApi;
    }

    public static ProductIsCollectApi getProductIsCollectApi(){
        if(productIsCollectApi == null){
            productIsCollectApi = RetrofitUtils.get().retrofit().create(ProductIsCollectApi.class);
        }
        return productIsCollectApi;
    }

    public static PublishJobApi getPublishJobApi(){
        if(publishJobApi == null){
            publishJobApi = RetrofitUtils.get().retrofit().create(PublishJobApi.class);
        }
        return publishJobApi;
    }

    public static FeedBackApi getFeedBackApi(){
        if(feedBackApi == null){
            feedBackApi = RetrofitUtils.get().retrofit().create(FeedBackApi.class);
        }
        return feedBackApi;
    }

    public static UpdateFriendStateApi getUpdateFriendStateApi(){
        if(updateFriendStateApi == null){
            updateFriendStateApi = RetrofitUtils.get().retrofit().create(UpdateFriendStateApi.class);
        }
        return updateFriendStateApi;
    }

    public static UpdatePwdCodeApi getUpdatePwdCodeApi(){
        if(updatePwdCodeApi == null){
            updatePwdCodeApi = RetrofitUtils.get().retrofit().create(UpdatePwdCodeApi.class);
        }
        return updatePwdCodeApi;
    }

    public static UpdatePwdApi getUpdatePwdApi(){
        if(updatePwdApi == null){
            updatePwdApi = RetrofitUtils.get().retrofit().create(UpdatePwdApi.class);
        }
        return updatePwdApi;
    }

    public static ZizhiStateApi getZizhiStateApi(){
        if(zizhiStateApi == null){
            zizhiStateApi = RetrofitUtils.get().retrofit().create(ZizhiStateApi.class);
        }
        return zizhiStateApi;
    }

    public static ISfabuApi getiSfabuApi(){
        if(iSfabuApi == null){
            iSfabuApi = RetrofitUtils.get().retrofit().create(ISfabuApi.class);
        }
        return iSfabuApi;
    }

    public static CancelBindPhoneApi getCancelBindPhoneApi(){
        if(cancelBindPhoneApi == null){
            cancelBindPhoneApi = RetrofitUtils.get().retrofit().create(CancelBindPhoneApi.class);
        }
        return cancelBindPhoneApi;
    }

    public static BindPhoneCodeApi getBindPhoneCodeApi(){
        if(bindPhoneCodeApi == null){
            bindPhoneCodeApi = RetrofitUtils.get().retrofit().create(BindPhoneCodeApi.class);
        }
        return bindPhoneCodeApi;
    }

    public static BindPhoneApi getBindPhoneApi(){
        if(bindPhoneApi == null){
            bindPhoneApi = RetrofitUtils.get().retrofit().create(BindPhoneApi.class);
        }
        return bindPhoneApi;
    }

    public static ForgetPwdApi getForgetPwdApi(){
        if(forgetPwdApi == null){
            forgetPwdApi = RetrofitUtils.get().retrofit().create(ForgetPwdApi.class);
        }
        return forgetPwdApi;
    }

    public static ForgetPwdCodeApi getForgetPwdCodeApi(){
        if(forgetPwdCodeApi == null){
            forgetPwdCodeApi = RetrofitUtils.get().retrofit().create(ForgetPwdCodeApi.class);
        }
        return forgetPwdCodeApi;
    }

    public static VisterCountApi getVisterCountApi(){
        if(visterCountApi == null){
            visterCountApi = RetrofitUtils.get().retrofit().create(VisterCountApi.class);
        }
        return visterCountApi;
    }

    public static MsgCountApi getMsgCountApi(){
        if(msgCountApi == null){
            msgCountApi = RetrofitUtils.get().retrofit().create(MsgCountApi.class);
        }
        return msgCountApi;
    }

    public static ProxyInfoDetialApi getProxyInfoDetialApi(){
        if(proxyInfoDetialApi == null){
            proxyInfoDetialApi = RetrofitUtils.get().retrofit().create(ProxyInfoDetialApi.class);
        }
        return proxyInfoDetialApi;
    }

    public static ProxySetApi getProxySetApi(){
        if(proxySetApi == null){
            proxySetApi = RetrofitUtils.get().retrofit().create(ProxySetApi.class);
        }
        return proxySetApi;
    }

    public static ProductUpdateApi getProductUpdateApi(){
        if(productUpdateApi == null){
            productUpdateApi = RetrofitUtils.get().retrofit().create(ProductUpdateApi.class);
        }
        return productUpdateApi;
    }

    public static ProductAddApi getProductAddApi(){
        if(productAddApi == null){
            productAddApi = RetrofitUtils.get().retrofit().create(ProductAddApi.class);
        }
        return productAddApi;
    }

    public static DelProductImgApi getDelProductImgApi(){
        if(delProductImgApi == null){
            delProductImgApi = RetrofitUtils.get().retrofit().create(DelProductImgApi.class);
        }
        return delProductImgApi;
    }

    public static PublishProxyApi getPublishProxyApi(){
        if(publishProxyApi == null){
            publishProxyApi = RetrofitUtils.get().retrofit().create(PublishProxyApi.class);
        }
        return publishProxyApi;
    }

    public static UpdateUserApi getUpdateUserApi(){
        if(updateUserApi == null){
            updateUserApi = RetrofitUtils.get().retrofit().create(UpdateUserApi.class);
        }
        return updateUserApi;
    }

    public static ProductDetialApi getProductDetialApi(){
        if(productDetialApi == null){
            productDetialApi = RetrofitUtils.get().retrofit().create(ProductDetialApi.class);
        }
        return productDetialApi;
    }

    public static SimpleUserInfoApi getSimpleUserInfoApi(){
        if(simpleUserInfoApi == null){
            simpleUserInfoApi = RetrofitUtils.get().retrofit().create(SimpleUserInfoApi.class);
        }
        return simpleUserInfoApi;
    }

    public static ZSUserListApi getZsUserListApi(){
        if(zsUserListApi == null){
            zsUserListApi = RetrofitUtils.get().retrofit().create(ZSUserListApi.class);
        }
        return zsUserListApi;
    }

    public static VersionApi getVersionApi(){
        if(versionApi == null){
            versionApi = RetrofitUtils.get().retrofit().create(VersionApi.class);
        }
        return versionApi;
    }

    public static FreshProductApi getFreshProductApi(){
        if(freshProductApi == null){
            freshProductApi = RetrofitUtils.get().retrofit().create(FreshProductApi.class);
        }
        return freshProductApi;
    }

    public static FreshCompanyApi getFreshCompanyApi(){
        if(freshCompanyApi == null){
            freshCompanyApi = RetrofitUtils.get().retrofit().create(FreshCompanyApi.class);
        }
        return freshCompanyApi;
    }

    public static JobTypeApi getJobTypeApi(){
        if(jobTypeApi == null){
            jobTypeApi = RetrofitUtils.get().retrofit().create(JobTypeApi.class);
        }
        return jobTypeApi;
    }

    public static TypeApi getTypeApi(){
        if(typeApi == null){
            typeApi = RetrofitUtils.get().retrofit().create(TypeApi.class);
        }
        return typeApi;
    }

    public static ChannelApi getChannelApi(){
        if(channelApi == null){
            channelApi = RetrofitUtils.get().retrofit().create(ChannelApi.class);
        }
        return channelApi;
    }

    public static KeshiApi getKeshiApi(){
        if(keshiApi == null){
            keshiApi = RetrofitUtils.get().retrofit().create(KeshiApi.class);
        }
        return keshiApi;
    }

    public static KefuAreaSetApi getKefuAreaSetApi(){
        if(kefuAreaSetApi == null){
            kefuAreaSetApi = RetrofitUtils.get().retrofit().create(KefuAreaSetApi.class);
        }
        return kefuAreaSetApi;
    }

    public static KefuAreaGetApi getKefuAreaGetApi(){
        if(kefuAreaGetApi == null){
            kefuAreaGetApi = RetrofitUtils.get().retrofit().create(KefuAreaGetApi.class);
        }
        return kefuAreaGetApi;
    }

    public static UserDetialApi getUserDetialApi(){
        if(userDetialApi == null){
            userDetialApi = RetrofitUtils.get().retrofit().create(UserDetialApi.class);
        }
        return userDetialApi;
    }

    public static LinkManApi getLinkManApi(){
        if(linkManApi == null){
            linkManApi = RetrofitUtils.get().retrofit().create(LinkManApi.class);
        }
        return linkManApi;
    }

    public static LoginApi getLoginApi(){
        if(loginApi == null){
            loginApi = RetrofitUtils.get().retrofit().create(LoginApi.class);
        }
        return loginApi;
    }

    public static RegisterCodeApi getRegisterCodeApi(){
        if(registerCodeApi == null){
            registerCodeApi = RetrofitUtils.get().retrofit().create(RegisterCodeApi.class);
        }
        return registerCodeApi;
    }

    public static RegisterApi getRegisterApi(){
        if(registerApi == null){
            registerApi = RetrofitUtils.get().retrofit().create(RegisterApi.class);
        }
        return registerApi;
    }

    public static KefuTelApi getKefuTelApi(){
        if(kefuTelApi == null){
            kefuTelApi = RetrofitUtils.get().retrofit().create(KefuTelApi.class);
        }
        return kefuTelApi;
    }

    public static MeInfoCountApi getMeInfoCountApi(){
        if(meInfoCountApi == null){
            meInfoCountApi = RetrofitUtils.get().retrofit().create(MeInfoCountApi.class);
        }
        return meInfoCountApi;
    }


    public static IntentionDetialApi getIntentionDetialApi(){
        if(intentionDetialApi == null){
            intentionDetialApi = RetrofitUtils.get().retrofit().create(IntentionDetialApi.class);
        }
        return intentionDetialApi;
    }

    public static DelJobApi getDelJobApi(){
        if(delJobApi == null){
            delJobApi = RetrofitUtils.get().retrofit().create(DelJobApi.class);
        }
        return delJobApi;
    }

    public static DelProductApi getDelProductApi(){
        if(delProductApi == null){
            delProductApi = RetrofitUtils.get().retrofit().create(DelProductApi.class);
        }
        return delProductApi;
    }

    public static DelProxyApi getDelProxyApi(){
        if(delProxyApi == null){
            delProxyApi = RetrofitUtils.get().retrofit().create(DelProxyApi.class);
        }
        return delProxyApi;
    }

    public static ChildAccountAddApi getChildAccountAddApi(){
        if(childAccountAddApi == null){
            childAccountAddApi = RetrofitUtils.get().retrofit().create(ChildAccountAddApi.class);
        }
        return childAccountAddApi;
    }

    public static ChildAccountDelApi getChildAccountDelApi(){
        if(childAccountDelApi == null){
            childAccountDelApi = RetrofitUtils.get().retrofit().create(ChildAccountDelApi.class);
        }
        return childAccountDelApi;
    }

    public static ChildAccountListApi getChildAccountListApi(){
        if(childAccountListApi == null){
            childAccountListApi = RetrofitUtils.get().retrofit().create(ChildAccountListApi.class);
        }
        return childAccountListApi;
    }

    public static MyguanzhuApi getMyguanzhuApi(){
        if(myguanzhuApi == null){
            myguanzhuApi = RetrofitUtils.get().retrofit().create(MyguanzhuApi.class);
        }
        return myguanzhuApi;
    }

    public static MyCollectApi getMyCollectApi(){
        if(myCollectApi == null){
            myCollectApi = RetrofitUtils.get().retrofit().create(MyCollectApi.class);
        }
        return myCollectApi;
    }

    public static MyProductApi getMyProductApi(){
        if(myProductApi == null){
            myProductApi = RetrofitUtils.get().retrofit().create(MyProductApi.class);
        }
        return myProductApi;
    }

    public static FriendSearchApi getFriendSearchApi(){
        if(friendSearchApi == null){
            friendSearchApi = RetrofitUtils.get().retrofit().create(FriendSearchApi.class);
        }
        return friendSearchApi;
    }

    public static FriendAddApi getFriendAddApi(){
        if(friendAddApi == null){
            friendAddApi = RetrofitUtils.get().retrofit().create(FriendAddApi.class);
        }
        return friendAddApi;
    }

    public static FriendListApi getMyfriendApi(){
        if(myfriendApi == null){
            myfriendApi = RetrofitUtils.get().retrofit().create(FriendListApi.class);
        }
        return myfriendApi;
    }

    public static FriendDelApi getDelFriendApi(){
        if(delFriendApi == null){
            delFriendApi = RetrofitUtils.get().retrofit().create(FriendDelApi.class);
        }
        return delFriendApi;
    }

    public static LookVisAllApi getLookVisAllApi(){
        if(lookVisAllApi == null){
            lookVisAllApi = RetrofitUtils.get().retrofit().create(LookVisAllApi.class);
        }
        return lookVisAllApi;
    }

    public static LookVisOneApi getLookVisOneApi(){
        if(lookVisOneApi == null){
            lookVisOneApi = RetrofitUtils.get().retrofit().create(LookVisOneApi.class);
        }
        return lookVisOneApi;
    }

    public static ResentVisitApi getResentVisitApi(){
        if(resentVisitApi == null){
            resentVisitApi = RetrofitUtils.get().retrofit().create(ResentVisitApi.class);
        }
        return resentVisitApi;
    }

    public static IntentionMsgApi getIntentionMsgApi(){
        if(intentionMsgApi == null){
            intentionMsgApi = RetrofitUtils.get().retrofit().create(IntentionMsgApi.class);
        }
        return intentionMsgApi;
    }

    public static RecruitApi getRecruitApi(){
        if(recruitApi == null){
            recruitApi = RetrofitUtils.get().retrofit().create(RecruitApi.class);
        }
        return recruitApi;
    }

    public static NewsExpApi getNewsExpApi(){
        if(newsExpApi == null){
            newsExpApi = RetrofitUtils.get().retrofit().create(NewsExpApi.class);
        }
        return newsExpApi;
    }

    public static NewsBidApi getNewsBidApi(){
        if(newsBidApi == null){
            newsBidApi = RetrofitUtils.get().retrofit().create(NewsBidApi.class);
        }
        return newsBidApi;
    }

    public static NewsAllApi getNewsAllApi(){
        if(newsAllApi == null){
            newsAllApi = RetrofitUtils.get().retrofit().create(NewsAllApi.class);
        }
        return newsAllApi;
    }

    public static NewsHeadlineApi getNewsHeadlineApi(){
        if(newsHeadlineApi == null){
            newsHeadlineApi = RetrofitUtils.get().retrofit().create(NewsHeadlineApi.class);
        }
        return newsHeadlineApi;
    }

    public static ZhongbiaoApi getZhongbiaoApi(){
        if(zhongbiaoApi == null){
            zhongbiaoApi = RetrofitUtils.get().retrofit().create(ZhongbiaoApi.class);
        }
        return zhongbiaoApi;
    }

    public static YibaoApi getYibaoApi(){
        if(yibaoApi == null){
            yibaoApi = RetrofitUtils.get().retrofit().create(YibaoApi.class);
        }
        return yibaoApi;
    }

    public static ProxyInfoApi getProxyInfoApi(){
        if(proxyInfoApi == null){
            proxyInfoApi = RetrofitUtils.get().retrofit().create(ProxyInfoApi.class);
        }
        return proxyInfoApi;
    }

    public static ProxyLibraryApi getProxyLibraryApi(){
        if(proxyLibraryApi == null){
            proxyLibraryApi = RetrofitUtils.get().retrofit().create(ProxyLibraryApi.class);
        }
        return proxyLibraryApi;
    }

    public static CityApi getCityApi(){
        if(cityApi == null){
            cityApi = RetrofitUtils.get().retrofit().create(CityApi.class);
        }
        return cityApi;
    }

    public static ProvinceApi getProvinceApi(){
        if(provinceApi == null){
            provinceApi = RetrofitUtils.get().retrofit().create(ProvinceApi.class);
        }
        return provinceApi;
    }

    public static SortProductSmallApi getSortProductSmallApi(){
        if(sortProductSmallApi == null){
            sortProductSmallApi = RetrofitUtils.get().retrofit().create(SortProductSmallApi.class);
        }
        return sortProductSmallApi;
    }

    public static SortProductBigApi getSortProductBigApi(){
        if(sortProductBigApi == null){
            sortProductBigApi = RetrofitUtils.get().retrofit().create(SortProductBigApi.class);
        }
        return sortProductBigApi;
    }

    public static CompanyGuanzhuApi getCompanyGuanzhuApi(){
        if(companyGuanzhuApi == null){
            companyGuanzhuApi = RetrofitUtils.get().retrofit().create(CompanyGuanzhuApi.class);
        }
        return companyGuanzhuApi;
    }

    public static CompanyIsGuanzhuApi getCompanyIsGuanzhuApi(){
        if(companyIsGuanzhuApi == null){
            companyIsGuanzhuApi = RetrofitUtils.get().retrofit().create(CompanyIsGuanzhuApi.class);
        }
        return companyIsGuanzhuApi;
    }

    public static CompanyProductListApi getCompanyProductListApi(){
        if(companyProductListApi == null){
            companyProductListApi = RetrofitUtils.get().retrofit().create(CompanyProductListApi.class);
        }
        return companyProductListApi;
    }

    public static ProductLibarayApi getProductLibarayApi(){
        if(productLibarayApi == null){
            productLibarayApi = RetrofitUtils.get().retrofit().create(ProductLibarayApi.class);
        }
        return productLibarayApi;
    }

    public static CompanyCenterApi getCompanyCenterApi(){
        if(companyCenterApi == null){
            companyCenterApi = RetrofitUtils.get().retrofit().create(CompanyCenterApi.class);
        }
        return companyCenterApi;
    }

    public static HomeCompanyApi getHomeCompanyApi(){
        if(homeCompanyApi == null){
            homeCompanyApi = RetrofitUtils.get().retrofit().create(HomeCompanyApi.class);
        }
        return homeCompanyApi;
    }

    public static HomeHeadLineApi getHomeHeadLineApi(){
        if(homeHeadLineApi == null){
            homeHeadLineApi = RetrofitUtils.get().retrofit().create(HomeHeadLineApi.class);
        }
        return homeHeadLineApi;
    }

    public static HomeBannerTopApi getHomeBannerTopApi(){
        if(homeBannerTopApi == null){
            homeBannerTopApi = RetrofitUtils.get().retrofit().create(HomeBannerTopApi.class);
        }
        return homeBannerTopApi;
    }

    public static HomeBannerFootApi getHomeBannerFootApi(){
        if(homeBannerFootApi == null){
            homeBannerFootApi = RetrofitUtils.get().retrofit().create(HomeBannerFootApi.class);
        }
        return homeBannerFootApi;
    }

    public static HomeZSApi getHomeZSApi(){
        if(homeZSApi == null){
            homeZSApi = RetrofitUtils.get().retrofit().create(HomeZSApi.class);
        }
        return homeZSApi;
    }

    public static HomeDLApi getHomeDLApi(){
        if(homeDLApi == null){
            homeDLApi = RetrofitUtils.get().retrofit().create(HomeDLApi.class);
        }
        return homeDLApi;
    }
}
