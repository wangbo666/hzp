package com.qgyyzs.globalcosmetics.http.retrofit;

import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.utils.SystemUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * RetrofitUtils工具类
 *
 * @author ZhongDaFeng
 */
public class RetrofitUtils {
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

    public static final String AppSearct="HZP1F176W2B5LD";//
    /**
     * 接口地址
     */
    public static final String BASE_API = "http://app.hzpzs.net/";//
    public static final int CONNECT_TIME_OUT = 10;//连接超时时长x秒
    public static final int READ_TIME_OUT = 10;//读数据超时时长x秒
    public static final int WRITE_TIME_OUT = 10;//写数据接超时时长x秒
    private static RetrofitUtils mInstance = null;

    private RetrofitUtils() {
    }

    public static RetrofitUtils get() {
        if (mInstance == null) {
            synchronized (RetrofitUtils.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置okHttp
     *
     * @author ZhongDaFeng
     */
    private static OkHttpClient okHttpClient() {
        //开启Log
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
//                LogUtils.e("okHttp:" + message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .proxy(Proxy.NO_PROXY)
                .addInterceptor(logging);
        //设置头部
        Interceptor headerInterceptor = new Interceptor() {

            String Nonce=getNonce();
            String CurTime=  df.format(new Date());
            String checkStr=GetCheckStr(AppSearct+Nonce+CurTime);
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        .header("Nonce",Nonce)
                        .header("CurTime",CurTime)
                        .header("CheckStr",checkStr)
                        .header("Token", MyApplication.TOKEN)
                        .header("version", SystemUtil.getVersion())
                        .header("OS","Android")
                        .header("Language", SystemUtil.getSystemLanguage())
                        .header("OsVer",SystemUtil.getSystemVersion())
                        .header("Manufacturer",SystemUtil.getDeviceBrand())
                        .header("Model",SystemUtil.getSystemModel())
                        .method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        builder.addInterceptor(headerInterceptor);
        OkHttpClient client = builder.build();
        return client;
    }

    /**
     * 获取Retrofit
     *
     * @author ZhongDaFeng
     */
    public Retrofit retrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient())
                .baseUrl(BASE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }
    public static String getNonce(){
        int min=99999;
        int max=999999;
        Random random = new Random();

        int s = random.nextInt(max-min) + min;
        return s+"";
    }
    public static String GetCheckStr(String str){
        String result="";
        if (null == str || 0 == str.length()){
            return null;
        }
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            result=new String(buf);
            return result.toString().replace("a", "#").replace("4", "G").replace("0", "j").replace("d", "X").replace("9", "p");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  result.toString().replace("a", "#").replace("4", "G").replace("0", "j").replace("d", "X").replace("9", "p");
    }
}
