package com.zdv.shangtongtianxia.present;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.socks.library.KLog;
import com.zdv.shangtongtianxia.model.IRequestMode;
import com.zdv.shangtongtianxia.model.converter.CustomGsonConverter;
import com.zdv.shangtongtianxia.util.Constant;
import com.zdv.shangtongtianxia.view.IAddressView;
import com.zdv.shangtongtianxia.view.IMemberView;
import com.zdv.shangtongtianxia.view.IPayView;
import com.zdv.shangtongtianxia.view.IUserView;
import com.zdv.shangtongtianxia.view.IVerifyView;
import com.zdv.shangtongtianxia.view.IView;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;


/**
 * Created by Administrator on 2017/4/6.
 */
public class QueryPresent implements IRequestPresent {
    private IView iView;
    private Context context;
    private IRequestMode iRequestMode;
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .build();
    private static QueryPresent instance = null;

    public void setView(Activity activity) {
        iView = (IView) activity;
    }

    public void setView(Fragment fragment) {
        iView = (IView) fragment;
    }

    private QueryPresent(Context context_) {
        context = context_;
    }

    public static QueryPresent getInstance(Context context) {
        if (instance == null) {
            synchronized (QueryPresent.class) {
                if (instance == null) {
                    return new QueryPresent(context);
                }
            }
        }
        return instance;
    }

    public void initRetrofit(String url, boolean isXml) {

        try {
            if (isXml) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .client(client)
                        // .addConverterFactory(Xm.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
                iRequestMode = retrofit.create(IRequestMode.class);
            } else {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
                iRequestMode = retrofit.create(IRequestMode.class);
            }

        } catch (IllegalArgumentException e) {
            e.fillInStackTrace();
        }
    }

    public void initRetrofitSendMessage(String url) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .client(genericClient())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            iRequestMode = retrofit.create(IRequestMode.class);


        } catch (IllegalArgumentException e) {
            e.fillInStackTrace();
        }
    }

    /**
     * 添加统一header,超时时间,http日志打印
     *
     * @return
     */
    public static OkHttpClient genericClient() {
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder requestBuilder = request.newBuilder();
                        request = requestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=GBK"),
                                URLDecoder.decode(bodyToString(request.body()), "UTF-8")))
                                .build();
                        return chain.proceed(request);
                    }
                })
                //  .addInterceptor(logging)
                .connectTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        return httpClient;
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public void initRetrofit2(String url, boolean isXml) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(CustomGsonConverter.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(new OkHttpClient.Builder()
                            .addNetworkInterceptor(
                                    new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                            .addNetworkInterceptor(
                                    new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                            .addNetworkInterceptor(
                                    new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
                    .build();
            iRequestMode = retrofit.create(IRequestMode.class);

        } catch (IllegalArgumentException e) {
            e.fillInStackTrace();
        }
    }


    @Override
    public void QueryRegister(String secret, String tel, String password, String name, String verify, String vcode , String referee) {
        iRequestMode.QueryRegister(secret, tel, password, name, verify, vcode, referee)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolveRegisterInfo(s));
    }

    @Override
    public void QueryOCRInfo(String inputs) {
        iRequestMode.QueryOCRInfo(inputs)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IVerifyView) iView).ResolveOCRInfo(s));
    }

    @Override
    public void QueryCode(@Field(Constant.SECRET) String secret,
                          @Field(Constant.STATUS) String status,
                          @Field(Constant.PHONE) String phone) {
        iRequestMode.QueryCode(secret, "8",status, phone)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolveCodeInfo(s));
    }


    @Override
    public void QueryLogin(String secret, String tel,
                           String password) {
        iRequestMode.QueryLogin(secret, tel, password)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolveLoginInfo(s));
    }

    @Override
    public void QueryMessage(String secret, String type) {
        iRequestMode.QueryMessage(secret, type)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IMemberView) iView).ResolveMessage(s));
    }

    @Override
    public void QueryForget(String secret, String phone, String verify
            , String vcode, String password) {
        iRequestMode.QueryForget(secret, phone, verify, vcode, password)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolveForgetInfo(s));
    }

    @Override
    public void QuerySearchMemberOrder(String secret, String memcode, String code, String paystate, String status) {
        iRequestMode.QuerySearchMemberOrder(secret, memcode, code, paystate, status)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolveSearchMemberOrder(s));
    }

    @Override
    public void QueryQRCode(String secret, String memcode, String money, String pay_type, String action) {
        iRequestMode.QueryQRCode(secret, memcode, money, pay_type, action)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolveQRCode(s));
    }

    @Override
    public void QueryScanQrcode(String secret, String memcode, String auth) {
        iRequestMode.QueryScanQrcode(secret, memcode, auth)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolveScanQrcode(s));
    }

    @Override
    public void QueryChargeSTCoin(String secret, String memcode, String money, String type, String pay_type, String trade_type) {
        iRequestMode.QueryChargeSTCoin(secret, memcode, money, type, pay_type, trade_type)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolveChargeSTCoin(s));
    }

    @Override
    public void QueryMemberSign(String secret, String memcode,String type) {
        iRequestMode.QueryMemberSign(secret, memcode,type)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IMemberView) iView).ResolveMemberSign(s));
    }

    @Override
    public void QueryTradeHistory(String secret, String memcode) {
        iRequestMode.QueryTradeHistory(secret, memcode)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolveTradeHistory(s));
    }

    @Override
    public void QuerySetPayPassword(String secret, String memcode, String password, String mempaypwd) {
        iRequestMode.QuerySetPayPassword(secret, memcode, password, mempaypwd)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolveSetPayPassword(s));
    }

    @Override
    public void QueryVerifyPayPassword(String secret, String memcode, String mempaypwd) {
        iRequestMode.QueryVerifyPayPassword(secret, memcode, mempaypwd)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolveVerifyPayPassword(s));
    }

    @Override
    public void QueryResumeRecord(String secret, String memcode) {
        iRequestMode.QueryResumeRecord(secret, memcode)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolveResumeRecord(s));
    }

    @Override
    public void QueryActorMember(String secret, String memcode) {
        KLog.v(iRequestMode + "");
        iRequestMode.QueryActorMember(secret, memcode)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IMemberView) iView).ResolveActorMember(s));
    }

    @Override
    public void QueryTeamMember(String secret, String memcode) {
        iRequestMode.QueryTeamMember(secret, memcode)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IMemberView) iView).ResolveTeamMember(s));
    }

    @Override
    public void QueryApplyActorMember(String secret, String memcode, String level_id, String company_id, String pay_type, String trade_type, String tel) {
        iRequestMode.QueryApplyActorMember(secret, memcode, level_id, company_id, pay_type, trade_type, tel)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IMemberView) iView).ResolveApplyActorMember(s));
    }

    @Override
    public void QueryActorFirm(String secret, String memcode) {
        iRequestMode.QueryActorFirm(secret, memcode)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IMemberView) iView).ResolveActorFirm(s));
    }

    @Override
    public void QueryMemberPayDetail(String secret, String memcode, String order_id) {
        iRequestMode.QueryMemberPayDetail(secret, memcode, order_id)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IMemberView) iView).ResolveMemberPayDetail(s));
    }

    @Override
    public void QueryAddress(String secret, String memcode) {
        iRequestMode.QueryAddress(secret, memcode)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IAddressView) iView).ResolveAddress(s));
    }

    @Override
    public void QueryAddAddress(String secret, String memcode, String username, String tel, String address, String district) {
        iRequestMode.QueryAddAddress(secret, memcode, username, tel, address, district)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IAddressView) iView).ResolveAddAddress(s));
    }

    @Override
    public void QueryDeleteAddress(String secret, String memcode, String code) {
        iRequestMode.QueryDeleteAddress(secret, memcode, code)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IAddressView) iView).ResolveDeleteAddress(s));
    }

    @Override
    public void QueryDefaultAddress(String secret, String memcode, String code) {
        iRequestMode.QueryDefaultAddress(secret, memcode, code)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IAddressView) iView).ResolveDefaultAddress(s));
    }

    @Override
    public void QueryVerifyMember(String secret, String memcode, String username, String f_img, String b_img, String id_card) {
        iRequestMode.QueryVerifyMember(secret, memcode, username, f_img, b_img, id_card)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IMemberView) iView).ResolveVerifyMember(s));
    }

    @Override
    public void QueryIsVerify(String secret, String memcode) {
        iRequestMode.QueryIsVerify(secret, memcode)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IMemberView) iView).ResolveIsVerify(s));
    }

}
