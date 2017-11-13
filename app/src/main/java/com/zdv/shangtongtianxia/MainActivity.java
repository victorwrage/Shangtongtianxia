package com.zdv.shangtongtianxia;


import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.gyf.barlibrary.ImmersionBar;
import com.jakewharton.rxbinding2.view.RxView;
import com.pos.api.Printer;
import com.socks.library.KLog;
import com.zdv.shangtongtianxia.acticity.BaseActivity;
import com.zdv.shangtongtianxia.bean.MessageBean;
import com.zdv.shangtongtianxia.bean.MessageBeanDao;
import com.zdv.shangtongtianxia.customView.DragFloatActionButton;
import com.zdv.shangtongtianxia.fragment.FragmentActor;
import com.zdv.shangtongtianxia.fragment.FragmentAddress;
import com.zdv.shangtongtianxia.fragment.FragmentChargeSTCoin;
import com.zdv.shangtongtianxia.fragment.FragmentCoin;
import com.zdv.shangtongtianxia.fragment.FragmentCoupon;
import com.zdv.shangtongtianxia.fragment.FragmentCouponManager;
import com.zdv.shangtongtianxia.fragment.FragmentFeedBack;
import com.zdv.shangtongtianxia.fragment.FragmentForget;
import com.zdv.shangtongtianxia.fragment.FragmentLogin;
import com.zdv.shangtongtianxia.fragment.FragmentMain;
import com.zdv.shangtongtianxia.fragment.FragmentMap;
import com.zdv.shangtongtianxia.fragment.FragmentMessage;
import com.zdv.shangtongtianxia.fragment.FragmentOrder;
import com.zdv.shangtongtianxia.fragment.FragmentPassword;
import com.zdv.shangtongtianxia.fragment.FragmentPay;
import com.zdv.shangtongtianxia.fragment.FragmentPosProfit;
import com.zdv.shangtongtianxia.fragment.FragmentProfile;
import com.zdv.shangtongtianxia.fragment.FragmentProfit;
import com.zdv.shangtongtianxia.fragment.FragmentQrcode;
import com.zdv.shangtongtianxia.fragment.FragmentRecharge;
import com.zdv.shangtongtianxia.fragment.FragmentRegister;
import com.zdv.shangtongtianxia.fragment.FragmentResearch;
import com.zdv.shangtongtianxia.fragment.FragmentScan;
import com.zdv.shangtongtianxia.fragment.FragmentSetting;
import com.zdv.shangtongtianxia.fragment.FragmentShop;
import com.zdv.shangtongtianxia.fragment.FragmentTeam;
import com.zdv.shangtongtianxia.fragment.FragmentTerminalProfit;
import com.zdv.shangtongtianxia.fragment.FragmentUserManager;
import com.zdv.shangtongtianxia.fragment.FragmentVerify;
import com.zdv.shangtongtianxia.fragment.FragmentWealth;
import com.zdv.shangtongtianxia.fragment.FragmentWebview;
import com.zdv.shangtongtianxia.fragment.FragmentWithdraw;
import com.zdv.shangtongtianxia.present.QueryPresent;
import com.zdv.shangtongtianxia.util.Constant;
import com.zdv.shangtongtianxia.util.D2000V1ScanInitUtils;
import com.zdv.shangtongtianxia.util.Utils;
import com.zdv.shangtongtianxia.util.VToast;
import com.zdv.shangtongtianxia.view.IFragmentActivity;
import com.zdv.shangtongtianxia.view.IMemberView;

import org.greenrobot.greendao.query.WhereCondition;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateStatus;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.ResponseBody;

public class MainActivity extends BaseActivity implements IFragmentActivity, IMemberView {
    public static String RECEIVE_REDIRECT_MESSAGE = "receive_redirect_message";
    protected static final String SUCCESS = "200";
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    private final String COOKIE_KEY = "cookie";
    private final int OCR_CODE_OCR_CAED = 1011;
    private static int REQUEST_THUMBNAIL = 1;// 请求缩略图信号标识

    D2000V1ScanInitUtils d2000V1ScanInitUtils;
    private final static int SCAN_CLOSED = 20;
    Printer printer;


    @Bind(R.id.main_message_lay)
    FrameLayout main_message_lay;
    private Executor executor;

    @Bind(R.id.main_mine_lay)
    LinearLayout main_mine_lay;
    @Bind(R.id.main_fortune_lay)
    LinearLayout main_fortune_lay;
    @Bind(R.id.main_nearby_lay)
    LinearLayout main_nearby_lay;
    @Bind(R.id.main_mall_lay)
    LinearLayout main_mall_lay;

    @Bind(R.id.float_btn)
    DragFloatActionButton float_btn;

    @Bind(R.id.main_title_lay)
    LinearLayout main_title_lay;

    @Bind(R.id.main_user_login)
    TextView main_user_login;

    @Bind(R.id.main_bottom_lay)
    LinearLayout main_bottom_lay;

    @Bind(R.id.main_header_lay)
    RelativeLayout main_header_lay;

    @Bind(R.id.main_fortune_tv)
    TextView main_fortune_tv;
    @Bind(R.id.main_nearby_tv)
    TextView main_nearby_tv;
    @Bind(R.id.main_mall_tv)
    TextView main_mall_tv;

    @Bind(R.id.main_mine_tv)
    TextView main_mine_tv;

    @Bind(R.id.main_fortune_cv)
    ImageView main_fortune_cv;
    @Bind(R.id.main_nearby_cv)
    ImageView main_nearby_cv;
    @Bind(R.id.main_mall_cv)
    ImageView main_mall_cv;

    @Bind(R.id.main_mine_cv)
    ImageView main_mine_cv;

    SharedPreferences sp;
    QueryPresent present;
    Utils util;
    private static final String ALIAS_KEY = "alias";
    private String SetAlias = "setAlias";
    private static final int MSG_SET_ALIAS = 1001;
    StringBuffer tip_str;
    private String type;
    MessageBeanDao messageBeanDao;
    BroadcastReceiver receiver_redirect = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            KLog.d("onReceive - " + intent.getAction());
            if (RECEIVE_REDIRECT_MESSAGE.equals(intent.getAction())) {
                type = bundle.getString("type");
                char[] str = type.toCharArray();
                KLog.v(type+"");
                tip_str = new StringBuffer();
                if (str.length == 2) {

                    switch (Integer.parseInt(str[1] + "")) {
                        case 0:
                            tip_str.append("微信");
                            break;
                        case 1:
                            tip_str.append("支付宝");
                            break;
                        case 2:
                            tip_str.append("QQ支付");
                            break;
                        case 3:
                            tip_str.append("商通币");
                            break;
                        case 4:
                            tip_str.append("余额");
                            break;
                    }
                    switch (Integer.parseInt(str[0] + "")) {
                        case 1:
                            tip_str.append("充值");
                            tip_str.append("成功,是否查看余额");
                            break;
                        case 2:
                            tip_str.append("角色申请");
                            tip_str.append("成功！");
                            break;
                        case 3:
                            tip_str.append("转账");
                            tip_str.append("成功,是否查看余额");
                            break;

                    }

                }
                if (Constant.temp_info.get(Constant.ORDER_ID) == null) {
                    KLog.v("onReceive" + Integer.parseInt(type));
                    showSuccessDialog(tip_str.toString(), Integer.parseInt(str[1] + ""));
                    return;
                }

                showWaitDialog("请稍等");
                present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
                present.QueryMemberPayDetail(util.UrlEnco(Constant.WDT_SECRET), Constant.user_info.optString("code"),
                        Constant.temp_info.get(Constant.ORDER_ID));
            }
        }
    };

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    KLog.v("Set alias in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    KLog.i("Unhandled msg - " + msg.what);
            }
        }
    };
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    KLog.i(logs);
                    sp.edit().putBoolean(SetAlias, true).commit();
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    KLog.i(logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    KLog.e(logs);
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fore_lay);
        ButterKnife.bind(MainActivity.this);
        initDate();
        initView();
    }

    private void showSuccessDialog(String content, int type) {
        new MaterialDialog.Builder(MainActivity.this)
                .title("提示")
                .content(content)
                .positiveText(R.string.bga_pp_confirm)
                .negativeText(R.string.cancle)
                .autoDismiss(true)
                .cancelable(false)
                .onNegative((materialDialog, dialogAction) -> {
                    //gotoMain();
                })
                .onPositive((materialDialog, dialogAction) -> {
                    switch (type) {
                        case 1:
                            gotoCoin();
                            break;
                        case 2:
                            gotoMain();
                            break;
                        case 3:
                            gotoCoin();
                            break;
                        case 4:
                            gotoMain();
                            break;
                        default:
                            gotoCoin();
                            break;
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        ImmersionBar.with(this).destroy();
        super.onDestroy();
        unregisterReceiver(receiver_redirect);
        OCR.getInstance().release();
        if (Constant.ENABLE_SCAN_PRINT) {
            if (d2000V1ScanInitUtils != null) {
                d2000V1ScanInitUtils.close();
                if (printer != null) {
                    printer.DLL_PrnRelease();
                }
                d2000V1ScanInitUtils = null;
            }
        }
    }

    private void initDate() {
        Constant.temp_info = new HashMap<>();
        present = QueryPresent.getInstance(context);
        present.setView(MainActivity.this);
        executor = Executors.newSingleThreadScheduledExecutor();
        util = Utils.getInstance();
        sp = getSharedPreferences(COOKIE_KEY, 0);

        messageBeanDao = STTXApplication.getDaoSession(context).getMessageBeanDao();

        IntentFilter filter = new IntentFilter();
        filter.addAction(RECEIVE_REDIRECT_MESSAGE);
        registerReceiver(receiver_redirect, filter);

        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(MainActivity.this);
        // builder.statusBarDrawable = R.drawable.ic_launcher;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
        //    builder.notificationDefaults = Notification.;  // 设置为铃声、震动、呼吸灯闪烁都要
        JPushInterface.setPushNotificationBuilder(6, builder);


        BmobUpdateAgent.setUpdateListener((updateStatus, updateInfo) -> {
            if (updateStatus == UpdateStatus.Yes) {//版本有更新

            } else if (updateStatus == UpdateStatus.No) {
                KLog.v("版本无更新");
                if (Constant.MESSAGE_UPDATE_TIP.equals("")) {
                    Constant.MESSAGE_UPDATE_TIP = "<<商通天下>>已是最新版本!";
                } else {
                    VToast.toast(context, Constant.MESSAGE_UPDATE_TIP);
                }
            } else if (updateStatus == UpdateStatus.EmptyField) {//此提示只是提醒开发者关注那些必填项，测试成功后，无需对用户提示
                KLog.v("请检查你AppVersion表的必填项，1、target_size（文件大小）是否填写；2、path或者android_url两者必填其中一项。");
            } else if (updateStatus == UpdateStatus.IGNORED) {
                KLog.v("该版本已被忽略更新");
            } else if (updateStatus == UpdateStatus.ErrorSizeFormat) {
                KLog.v("请检查target_size填写的格式，请使用file.length()方法获取apk大小。");
            } else if (updateStatus == UpdateStatus.TimeOut) {
                KLog.v("查询出错或查询超时");
            }
        });
        // BmobUpdateAgent.initAppVersion(context);
        BmobUpdateAgent.update(MainActivity.this);
        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                // 调用成功，返回AccessToken对象
                String token = result.getAccessToken();
                OCR.getInstance().initWithToken(getApplicationContext(), token);//初始化OCR
                KLog.v("baidu " + token);
            }

            @Override
            public void onError(OCRError error) {
                KLog.v("baidu " + error.toString());
                // 调用失败，返回OCRError子类SDKError对象
            }
        }, context, Constant.PUBLIC_OCR_KEY, Constant.PUBLIC_OCR_SECRET);
        startLocation();
        initScannerPrinter();
    }

    private Handler promptHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 6:
                    KLog.v((String) msg.obj);
                    sendData((String) msg.obj);
                    break;
                case SCAN_CLOSED:
                    //        if (fragment1 != null) fragment1.print();
                    break;
                default:
                    break;
            }
        }


    };

    private void sendData(String obj) {
        fragment23.queryPay(obj);
    }

    private void closeScan() {
        showWaitDialog("请稍等");
        executor.execute(() -> d2000V1ScanInitUtils.setScanState());
        promptHandler.postDelayed(() -> {
            hideWaitDialog();
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constant.ENABLE_SCAN_PRINT && cur_fragment instanceof FragmentScan) {
            showWaitDialog("请稍后");
            initDevice();
            promptHandler.postDelayed(() -> {
                hideWaitDialog();
            }, 5000);
        }
    }

    private void startScan() {
        showWaitDialog("请稍等");
        promptHandler.postDelayed(() -> {
            hideWaitDialog();
            executor.execute(() -> {
                d2000V1ScanInitUtils.open();
                d2000V1ScanInitUtils.d2000V1ScanOpen();
            });
        }, 2000);
    }

    @Override
    public void print() {
        KLog.v("print" + Constant.ENABLE_SCAN_PRINT);
        if (!Constant.ENABLE_SCAN_PRINT) {
            return;
        }
        showWaitDialog("请等待打印完成");
        KLog.v("print" + Constant.ENABLE_SCAN_PRINT);
        printer = new Printer(this, bRet -> executor.execute(() -> {
            int iRet = -1;
            iRet = printer.DLL_PrnInit();
            KLog.v("iRet" + iRet);
            if (iRet == 0) {
                printContent();
            } else {
                hideWaitDialog();
                VToast.toast(context, "打印错误");
            }
        }));

        promptHandler.postDelayed(() -> {
            hideWaitDialog();
            new MaterialDialog.Builder(MainActivity.this)
                    .title("提示")
                    .content("是否重新打印?")
                    .positiveText(R.string.bga_pp_confirm)
                    .negativeText(R.string.cancle)
                    .onNegative((materialDialog, dialogAction) -> {

                    })
                    .onPositive((materialDialog, dialogAction) -> {
                        print();
                    })
                    .autoDismiss(true)
                    .cancelable(false)
                    .show();
        }, 5000);
    }

    /**
     * 打印条目
     */
    private void printContent() {
        String pay_tp = "";
        type = "00";
        switch (Integer.parseInt(type.charAt(0) + "")) {
            case 0:
                pay_tp = "微信";
                break;
            case 1:
                pay_tp = "支付宝";
                break;
            case 2:
                pay_tp = "QQ支付";
                break;
            case 3:
                pay_tp = "商通币";
                break;
            case 4:
                pay_tp = "余额";
                break;
        }
        Bitmap bitmap = util.readBitMap(context, R.drawable.print_icon);
        Bitmap allBitmap2 = util.createLogo2(context,bitmap);
        printer.DLL_PrnBmp(allBitmap2);

        printer.DLL_PrnSetFont((byte) 24, (byte) 24, (byte) 0x00);
        printer.DLL_PrnStr("-------------------------------\n");
        if (Constant.user_info != null) {
            printer.DLL_PrnStr("商户名:" + Constant.user_info.optString("name") + "\n");
        }

        //printer.DLL_PrnStr("商户编号:" + Constant.user_info==null?"":Constant.user_info.optString("code") + "\n");
        printer.DLL_PrnStr("-------------------------------\n");
        if (Constant.temp_info.get(Constant.ORDER_ID) != null) {
            printer.DLL_PrnStr("订单号:" + Constant.temp_info.get(Constant.ORDER_ID) + "\n");
        }
        printer.DLL_PrnStr("支付类型:" + pay_tp + "\n");
      //  printer.DLL_PrnStr("支付渠道:" + "T1" + "\n");
      //  printer.DLL_PrnStr("机器序号:" + util.getSerialNumber() + "\n");
        printer.DLL_PrnStr("-------------------------------\n");
        printer.DLL_PrnStr("支付日期:" + currentDate("yyyyMMdd HH:mm:ss") + "\n");
        printer.DLL_PrnStr("总计(Total):(RMB) " + Constant.PAY_TOTAL + "元\n");
        printer.DLL_PrnStr("  \n");
        printer.DLL_PrnSetFont((byte) 20, (byte) 20, (byte) 0x00);
        printer.DLL_PrnStr("备注:" + "  \n");
        printer.DLL_PrnStr("  \n");
        printer.DLL_PrnStr("          签名：" + "  \n");
        printer.DLL_PrnStr("  \n");
        Bitmap bitmap2 = syncEncodeQRCode("http://weixin.qq.com/r/VC6MlO3Ew7C8ran393tG");

        Bitmap allBitmap = util.createLogo(context,bitmap2);

        printer.DLL_PrnBmp(allBitmap);
        printer.DLL_PrnStr("  \n");
        printer.DLL_PrnSetFont((byte) 17, (byte) 17, (byte) 0x00);
        printer.DLL_PrnStr("             欢迎关注商通天下公众号  \n");
        printer.DLL_PrnStr("             客服热线：400-1686-315 \n");
        printer.DLL_PrnStr("            \n");
        printer.DLL_PrnStr("-------------------------------\n");
        printer.DLL_PrnStr("            \n");
        printer.DLL_PrnStr("            \n");
        printer.DLL_PrnStart();

    }


    private Bitmap syncEncodeQRCode(String qcode) {
        return QRCodeEncoder.syncEncodeQRCode(qcode, 160, Color.BLACK , BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_gray));

    }

    private void initDevice() {
        executor.execute(() -> {
            KLog.v("initDevice");
            d2000V1ScanInitUtils = D2000V1ScanInitUtils.getInstance(MainActivity.this, promptHandler);
            if (!d2000V1ScanInitUtils.getStart()) {
                d2000V1ScanInitUtils.open();
            }
            d2000V1ScanInitUtils.d2000V1ScanOpen();
        });
    }

    private void initScannerPrinter() {
        String model = android.os.Build.MODEL;
        if (model.equals("D2000")) {
            Constant.ENABLE_SCAN_PRINT = true;
        }
    }


    private void initView() {

        main_header_lay.setVisibility(View.GONE);
        main_bottom_lay.setVisibility(View.GONE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragment0 = new FragmentLogin();
        cur_fragment = fragment0;
        ft.add(R.id.fragment_container, fragment0, PAGE_0);
        ft.show(fragment0);
        ft.commit();

        RxView.clicks(main_fortune_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> gotoWealth());
        RxView.clicks(main_nearby_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> gotoPage(6));
        RxView.clicks(main_mall_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> gotoPage(10));
        RxView.clicks(main_mine_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> gotoPage(11));

        RxView.clicks(main_title_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> gotoMain());
        RxView.clicks(main_message_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> gotoPage(12));
        RxView.clicks(float_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> SearchMall());


        float_btn.setSize(FloatingActionButton.SIZE_MINI);
        float_btn.setColorNormalResId(R.color.shangtongtianx_tab2);
        float_btn.setColorPressedResId(R.color.shangtongtianx_btn_back);
        float_btn.setIcon(R.drawable.map_research);
        float_btn.setStrokeVisible(false);
    }

    private void fetchMessage() {
        if (Constant.message == null) {
            Constant.message = new ArrayList<>();
        }
        Constant.message.clear();
        Constant.message.addAll(messageBeanDao.loadAll());
        present.initRetrofit(Constant.URL_SHANGTONGTIANXIA, false);
        present.QueryMessage(util.UrlEnco(Constant.WDT_SECRET), "2");
    }


    private void SearchMall() {
        if (fragment10 != null) {
            fragment10.setLoadUrl("http://wdt.qianhaiwei.com/Project/BeforeSea/shoppingMall/search.html");
        }
    }


    private void gotoPage(int pageId) {
        if (pageId == cur_page) {
            return;
        }
        cur_page = pageId;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(cur_fragment);
        float_btn.setVisibility(View.GONE);
        if (cur_fragment instanceof FragmentScan) {
            closeScan();
        }
        if (cur_fragment instanceof FragmentMap) {
            ft.remove(cur_fragment);
            fragment6 = null;
        }
        if (cur_fragment instanceof FragmentScan) {
            ft.remove(cur_fragment);
            fragment23 = null;
        }
        if (cur_fragment instanceof FragmentWebview) {
            ft.remove(cur_fragment);
            fragment10 = null;
        }
        if (cur_fragment instanceof FragmentCoupon) {
            ft.remove(cur_fragment);
            fragment29 = null;
        }
        if (cur_fragment instanceof FragmentTeam) {
            ft.remove(cur_fragment);
            fragment16 = null;
        }
        switch (pageId) {
            case 0:
                if (fragment0 == null) {
                    fragment0 = new FragmentLogin();
                    ft.add(R.id.fragment_container, fragment0, PAGE_0);
                } else {
                    fragment0.refreshState();
                }
                cur_fragment = fragment0;
                ft.show(fragment0);
                break;
            case 1:
                if (fragment1 == null) {
                    fragment1 = new FragmentRegister();
                    ft.add(R.id.fragment_container, fragment1, PAGE_1);
                } else {
                    fragment1.refreshState();
                }
                ft.show(fragment1);
                cur_fragment = fragment1;
                break;
            case 2:
                if (fragment2 == null) {
                    fragment2 = new FragmentMain();
                }
                if (!fragment2.isAdded()) {
                    ft.add(R.id.fragment_container, fragment2, PAGE_2);
                } else {
                    fragment2.refreshState();
                }
                cur_fragment = fragment2;
                ft.show(fragment2);
                main_header_lay.setVisibility(View.VISIBLE);
                main_bottom_lay.setVisibility(View.VISIBLE);
                break;
            case 3:
                if (fragment3 == null) {
                    fragment3 = new FragmentForget();
                    ft.add(R.id.fragment_container, fragment3, PAGE_3);
                } else {
                    fragment3.refreshState();
                }
                cur_fragment = fragment3;
                ft.show(fragment3);
                break;
            case 4:
                if (fragment4 == null) {
                    fragment4 = new FragmentWealth();
                    ft.add(R.id.fragment_container, fragment4, PAGE_4);
                } else {
                    fragment4.refreshState();
                }
                cur_fragment = fragment4;
                ft.show(fragment4);
                selectTab(0);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.VISIBLE);
                break;
            case 5:
                if (fragment5 == null) {
                    fragment5 = new FragmentCoin();
                    ft.add(R.id.fragment_container, fragment5, PAGE_5);
                } else {
                    fragment5.refreshState();
                }
                cur_fragment = fragment5;
                ft.show(fragment5);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 6:
                if (fragment6 == null) {
                    fragment6 = new FragmentMap();
                }
                if (!fragment6.isAdded()) {
                    ft.add(R.id.fragment_container, fragment6, PAGE_6);
                } else {
                    fragment6.refreshState();
                }
                cur_fragment = fragment6;
                ft.show(fragment6);
                selectTab(1);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.VISIBLE);
                break;
            case 7:
                if (fragment7 == null) {
                    fragment7 = new FragmentProfit();
                    ft.add(R.id.fragment_container, fragment7, PAGE_7);
                } else {
                    fragment7.refreshState();
                }
                cur_fragment = fragment7;
                ft.show(fragment7);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 8:
                if (fragment8 == null) {
                    fragment8 = new FragmentRecharge();
                    ft.add(R.id.fragment_container, fragment8, PAGE_8);
                } else {
                    fragment8.refreshState();
                }
                cur_fragment = fragment8;
                ft.show(fragment8);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 9:
                if (fragment9 == null) {
                    fragment9 = new FragmentResearch();
                    ft.add(R.id.fragment_container, fragment9, PAGE_9);
                } else {
                    fragment9.refreshState();
                }
                cur_fragment = fragment9;
                ft.show(fragment9);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 10:
                if (fragment10 == null) {
                    fragment10 = new FragmentWebview();
                    fragment10.setLoadUrl("http://wdt.qianhaiwei.com/Project/BeforeSea/shoppingMall/index.html?key=L8NNvvqqqZr&company_id=5325");
                    ft.add(R.id.fragment_container, fragment10, PAGE_10);
                } else {
                    fragment10.refreshState();
                }
                float_btn.setVisibility(View.VISIBLE);
                cur_fragment = fragment10;
                ft.show(fragment10);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.VISIBLE);
                selectTab(2);
                break;
            case 11:
                if (fragment11 == null) {
                    fragment11 = new FragmentUserManager();
                    ft.add(R.id.fragment_container, fragment11, PAGE_11);
                } else {
                    fragment11.refreshState();
                }
                cur_fragment = fragment11;
                ft.show(fragment11);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.VISIBLE);
                selectTab(3);
                break;
            case 12:
                if (fragment12 == null) {
                    fragment12 = new FragmentMessage();
                    ft.add(R.id.fragment_container, fragment12, PAGE_12);
                } else {
                    fragment12.refreshState();
                }
                cur_fragment = fragment12;
                ft.show(fragment12);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 13:
                if (fragment13 == null) {
                    fragment13 = new FragmentProfile();
                    ft.add(R.id.fragment_container, fragment13, PAGE_13);
                } else {
                    fragment13.refreshState();
                }
                cur_fragment = fragment13;
                ft.show(fragment13);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 14:
                if (fragment14 == null) {
                    fragment14 = new FragmentVerify();
                    ft.add(R.id.fragment_container, fragment14, PAGE_14);
                } else {
                    fragment14.refreshState();
                }
                cur_fragment = fragment14;
                ft.show(fragment14);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 15:
                if (fragment15 == null) {
                    fragment15 = new FragmentActor();
                    ft.add(R.id.fragment_container, fragment15, PAGE_15);
                } else {
                    fragment15.refreshState();
                }
                cur_fragment = fragment15;
                ft.show(fragment15);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 16:
                if (fragment16 == null) {
                    fragment16 = new FragmentTeam();
                    ft.add(R.id.fragment_container, fragment16, PAGE_16);
                } else {
                    fragment16.refreshState();
                }
                cur_fragment = fragment16;
                ft.show(fragment16);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 17:
                if (fragment17 == null) {
                    fragment17 = new FragmentOrder();
                    ft.add(R.id.fragment_container, fragment17, PAGE_17);
                } else {
                    fragment17.refreshState();
                }
                cur_fragment = fragment17;
                ft.show(fragment17);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 18:
                if (fragment18 == null) {
                    fragment18 = new FragmentAddress();
                    ft.add(R.id.fragment_container, fragment18, PAGE_18);
                } else {
                    fragment18.refreshState();
                }
                cur_fragment = fragment18;
                ft.show(fragment18);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 19:
                if (fragment19 == null) {
                    fragment19 = new FragmentSetting();
                    ft.add(R.id.fragment_container, fragment19, PAGE_19);
                } else {
                    fragment19.refreshState();
                }
                cur_fragment = fragment19;
                ft.show(fragment19);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 20:
                if (fragment20 == null) {
                    fragment20 = new FragmentFeedBack();
                    ft.add(R.id.fragment_container, fragment20, PAGE_20);
                } else {
                    fragment20.refreshState();
                }
                cur_fragment = fragment20;
                ft.show(fragment20);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 21:
                if (fragment21 == null) {
                    fragment21 = new FragmentPassword();
                    ft.add(R.id.fragment_container, fragment21, PAGE_21);
                } else {
                    fragment21.refreshState();
                }
                cur_fragment = fragment21;
                ft.show(fragment21);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 22:
                if (fragment22 == null) {
                    fragment22 = new FragmentPay();
                    ft.add(R.id.fragment_container, fragment22, PAGE_22);
                } else {
                    fragment22.refreshState();
                }
                cur_fragment = fragment22;
                ft.show(fragment22);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 23:
                if (fragment23 == null) {
                    fragment23 = new FragmentScan();
                    ft.add(R.id.fragment_container, fragment23, PAGE_23);
                } else {
                    fragment23.refreshState();
                }
                cur_fragment = fragment23;
                ft.show(fragment23);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 24:
                if (fragment24 == null) {
                    fragment24 = new FragmentPosProfit();
                    ft.add(R.id.fragment_container, fragment24, PAGE_24);
                } else {
                    fragment24.refreshState();
                }
                cur_fragment = fragment24;
                ft.show(fragment24);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 25:
                if (fragment25 == null) {
                    fragment25 = new FragmentTerminalProfit();
                    ft.add(R.id.fragment_container, fragment25, PAGE_25);
                } else {
                    fragment25.refreshState();
                }
                cur_fragment = fragment25;
                ft.show(fragment25);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 26:
                if (fragment26 == null) {
                    fragment26 = new FragmentWithdraw();
                    ft.add(R.id.fragment_container, fragment26, PAGE_26);
                } else {
                    fragment26.refreshState();
                }

                cur_fragment = fragment26;
                ft.show(fragment26);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 27:
                if (fragment27 == null) {
                    fragment27 = new FragmentQrcode();
                    ft.add(R.id.fragment_container, fragment27, PAGE_27);
                } else {
                    fragment27.refreshState();
                }

                cur_fragment = fragment27;
                ft.show(fragment27);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 28:
                if (fragment10 == null) {
                    fragment10 = new FragmentWebview();
                    fragment10.setLoadUrl("http://wdt.qianhaiwei.com/Project/BeforeSea/shoppingMall/index.html?key=L8NNvvqqqZr&company_id=5325");
                    ft.add(R.id.fragment_container, fragment10, PAGE_10);
                } else {
                    fragment10.refreshState();
                }
                cur_fragment = fragment10;
                ft.show(fragment10);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 29:
                if (fragment10 == null) {
                    fragment10 = new FragmentWebview();
                    fragment10.setLoadUrl("http://www.qianhaiwei.com");
                    ft.add(R.id.fragment_container, fragment10, PAGE_10);
                } else {
                    fragment10.refreshState();
                }
                cur_fragment = fragment10;
                ft.show(fragment10);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 30:
                if (fragment28 == null) {
                    fragment28 = new FragmentChargeSTCoin();
                    ft.add(R.id.fragment_container, fragment28, PAGE_28);
                } else {
                    fragment28.refreshState();
                }
                cur_fragment = fragment28;
                ft.show(fragment28);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 31:
                if (fragment29 == null) {
                    fragment29 = new FragmentCoupon();
                    ft.add(R.id.fragment_container, fragment29, PAGE_29);
                } else {
                    fragment29.refreshState();
                }
                cur_fragment = fragment29;
                ft.show(fragment29);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 32:
                if (fragment30 == null) {
                    fragment30 = new FragmentShop();
                    ft.add(R.id.fragment_container, fragment30, PAGE_30);
                } else {
                    fragment30.refreshState();
                }
                cur_fragment = fragment30;
                ft.show(fragment30);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;
            case 33:
                if (fragment31 == null) {
                    fragment31 = new FragmentCouponManager();
                    ft.add(R.id.fragment_container, fragment31, PAGE_31);
                } else {
                    fragment31.refreshState();
                }
                cur_fragment = fragment31;
                ft.show(fragment31);
                main_header_lay.setVisibility(View.GONE);
                main_bottom_lay.setVisibility(View.GONE);
                break;

        }
        ft.commitNowAllowingStateLoss();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(9000000);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(false);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(false);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(true);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    private void startLocation() {

        mLocationClient = new LocationClient(getApplicationContext());
        initLocation();
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.start();
    }

    @Override
    public void ResolveActorMember(ResponseBody info) {

    }

    @Override
    public void ResolveApplyActorMember(ResponseBody info) {

    }

    @Override
    public void ResolveMemberSign(ResponseBody info) {

    }

    @Override
    public void ResolveMessage(ResponseBody info) {
        if (info.source() == null) {
            hideWaitDialog();
            return;
        }
        JSONObject jsonObject = null;
        try {
            String res = info.string();
            KLog.v(res);
            jsonObject = new JSONObject(res);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            hideWaitDialog();
            return;
        }

        if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {

            String str = jsonObject.optString("content");
            ArrayList<MessageBean> temp = new ArrayList<>();
            temp.addAll(JSON.parseArray(str, MessageBean.class));
            /*temp.addAll(JSON.parseArray(str, MessageBean.class));
            temp.addAll(JSON.parseArray(str, MessageBean.class));
            temp.get(1).setId("10");
            temp.get(2).setId("11");*/
            for (MessageBean messageBean : temp) {
//                KLog.v("ResolveMessage-" + messageBean.getId() + "------" + messageBean.getIs_read());
                WhereCondition wc1 = MessageBeanDao.Properties.Id.eq(messageBean.getId() == null ? "-1" : messageBean.getId());
                MessageBean add_temp = messageBeanDao.queryBuilder().where(wc1).
                        orderAsc(MessageBeanDao.Properties.Createtime).limit(1).unique();

                if (add_temp == null) {
                    messageBeanDao.insert(messageBean);
                    Constant.message.add(messageBean);
                } else {
//                    KLog.v("ResolveMessage---add_temp:" + add_temp.getId() + "--" + add_temp.getIs_read() + "--" + add_temp.getIdx());
                }
            }
           // KLog.v("ResolveMessage---Constant.message:" + Constant.message.size());
            ArrayList<MessageBean> d_temp = new ArrayList<>();
            ArrayList<MessageBean> d_temp2 = new ArrayList<>();
            d_temp2.addAll(Constant.message);
            for (MessageBean messageBean : Constant.message) {
                for (MessageBean messageBean2 : temp) {
                    if (messageBean.getId().equals(messageBean2.getId())) {
                        d_temp.add(messageBean);
                        break;
                    }
                }
            }
//            KLog.v("ResolveMessage---d_temp2:" + d_temp2.size());
   //         KLog.v("ResolveMessage---d_temp:" + d_temp.size());
            d_temp2.removeAll(d_temp);

            for (MessageBean messageBean : d_temp2) {
                messageBeanDao.delete(messageBean);
                Constant.message.remove(messageBean);
            }
            KLog.v("ResolveMessage:" + Constant.message.size());
            int c = 0;
            for (MessageBean messageBean : Constant.message) {
                if (!messageBean.getIs_read()) {
                    c++;
                }
            }
            if (c == 0) {
                main_user_login.setVisibility(View.GONE);
            } else {
                main_user_login.setVisibility(View.VISIBLE);
                main_user_login.setText(c + "");
            }
        }
        hideWaitDialog();
    }

    @Override
    public void ResolveActorFirm(ResponseBody info) {

    }

    @Override
    public void ResolveMemberPayDetail(ResponseBody info) {
        if (info.source() == null) {
            hideWaitDialog();
            return;
        }
        JSONObject jsonObject = null;
        try {
            String res = info.string();
            KLog.v(res);
            jsonObject = new JSONObject(res);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            hideWaitDialog();
            return;
        }
        if (jsonObject.optString(Constant.ERRCODE).equals(SUCCESS)) {
            print();
            switch (type.charAt(1)) {
                case 1://充值
                    showSuccessDialog(tip_str.toString(), 1);
                    break;
                case 2://角色申请
                    showSuccessDialog(tip_str.toString(), 2);
                    break;
                default:
                    showSuccessDialog("交易成功，但类型不确定", -1);
                    break;

            }
            Constant.temp_info.put(Constant.ORDER_ID, null);
        }else{
            showSuccessDialog("交易失败", -1);
        }
        hideWaitDialog();
    }

    @Override
    public void ResolveVerifyMember(ResponseBody info) {

    }

    @Override
    public void ResolveTeamMember(ResponseBody info) {

    }

    @Override
    public void ResolveIsVerify(ResponseBody info) {

    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                KLog.v("onReceiveLocation" + location.getAddrStr());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                KLog.v("onReceiveLocation" + location.getAddrStr());
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                KLog.v("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                KLog.v("定位失败，不能使用排序功能");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                KLog.v("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            } else {
                KLog.v("无法获取有效定位依据导致定位失败");
            }
            Constant.location = location;
            sendBroadcast(new Intent(Constant.RECEIVE_LOCATION_SUCCESS));
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            KLog.v(s);
        }
    }

    private void selectTab(int seq) {
        main_mine_cv.setImageDrawable(getResources().getDrawable(R.drawable.geren));
        main_fortune_cv.setImageDrawable(getResources().getDrawable(R.drawable.caifu));
        main_nearby_cv.setImageDrawable(getResources().getDrawable(R.drawable.fujin));
        main_mall_cv.setImageDrawable(getResources().getDrawable(R.drawable.shangcheng));
        main_fortune_tv.setTextColor(getResources().getColor(R.color.shangtongtianx_tab_txt));
        main_nearby_tv.setTextColor(getResources().getColor(R.color.shangtongtianx_tab_txt));
        main_mall_tv.setTextColor(getResources().getColor(R.color.shangtongtianx_tab_txt));
        main_mine_tv.setTextColor(getResources().getColor(R.color.shangtongtianx_tab_txt));
        switch (seq) {
            case 0:
                main_fortune_cv.setImageDrawable(getResources().getDrawable(R.drawable.caifu_selec));
                main_fortune_tv.setTextColor(getResources().getColor(R.color.shangtongtianx_txt));
                break;
            case 1:
                main_nearby_cv.setImageDrawable(getResources().getDrawable(R.drawable.fujin_selec));
                main_nearby_tv.setTextColor(getResources().getColor(R.color.shangtongtianx_txt));
                break;
            case 2:
                main_mall_cv.setImageDrawable(getResources().getDrawable(R.drawable.shangcheng_selec));
                main_mall_tv.setTextColor(getResources().getColor(R.color.shangtongtianx_txt));
                break;
            case 3:
                main_mine_cv.setImageDrawable(getResources().getDrawable(R.drawable.geren_selec));
                main_mine_tv.setTextColor(getResources().getColor(R.color.shangtongtianx_txt));
                break;
        }
    }

    @Override
    public void gotoMain() {
        if (cur_fragment instanceof FragmentLogin) {
            if (!sp.getBoolean(SetAlias, false)) {
                KLog.v("MSG_SET_ALIAS");
                mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, Constant.user_info.opt(Constant.CODE)));//设置推送别名
            }
            ImmersionBar.with(this).barColor(R.color.shangtongtianx_txt).init();
            fetchMessage();
        }
        gotoPage(2);
        selectTab(-1);
    }

    @Override
    public void gotoLogin() {
        gotoPage(0);
    }


    @Override
    public void gotoRegister() {
        gotoPage(1);
    }

    @Override
    public void gotoForget() {
        gotoPage(3);
    }

    @Override
    public void gotoWealth() {
        gotoPage(4);
    }

    @Override
    public void gotoCoin() {
        gotoPage(5);
    }

    @Override
    public void gotoProfit() {
        gotoPage(7);
    }

    @Override
    public void gotoRecharge() {
        gotoPage(8);
    }

    @Override
    public void gotoResearch() {
        gotoPage(9);
    }

    @Override
    public void gotoUserManager() {
        gotoPage(11);
    }

    @Override
    public void gotoProfile() {
        gotoPage(13);
    }

    @Override
    public void gotoVerify() {
        gotoPage(14);
    }

    @Override
    public void gotoActor() {
        gotoPage(15);
    }

    @Override
    public void gotoTeam() {
        gotoPage(16);
    }

    @Override
    public void gotoOrder() {
        gotoPage(17);
    }

    @Override
    public void gotoAddress() {
        gotoPage(18);
    }

    @Override
    public void gotoSetting() {
        gotoPage(19);
    }

    @Override
    public void gotoFeedBack() {
        gotoPage(20);
    }

    @Override
    public void gotoPassword() {
        gotoPage(21);
    }

    @Override
    public void gotoPay() {
        gotoPage(22);
    }

    @Override
    public void gotoScan() {
        gotoPage(23);
        if (Constant.ENABLE_SCAN_PRINT) {
            initDevice();
        }
    }

    @Override
    public void gotoPosProfit() {
        gotoPage(24);
    }

    @Override
    public void gotoTerminal() {
        gotoPage(25);
    }

    @Override
    public void gotoWithdraw() {
        gotoPage(26);
    }

    @Override
    public void gotoQRcode() {
        gotoPage(27);
    }

    @Override
    public void gotoShop() {
        gotoPage(32);
    }

    @Override
    public void gotoCoinAbout() {
        gotoPage(29);
    }

    @Override
    public void gotoChargeCoin() {
        gotoPage(30);
    }

    @Override
    public void gotoCoupon() {
        gotoPage(31);
    }

    @Override
    public void gotoCouponManager() {
        gotoPage(33);
    }

    @Override
    public void gotoWebview() {
        gotoPage(28);
    }

    @Override
    public void NotifyMessage(MessageBean messageBean_) {
        messageBeanDao.insertOrReplace(messageBean_);
        int c = 0;
        for (MessageBean messageBean : Constant.message) {
            if (!messageBean.getIs_read()) {
                c++;
            }
        }
        if (c == 0) {
            main_user_login.setVisibility(View.GONE);
        } else {
            main_user_login.setVisibility(View.VISIBLE);
            main_user_login.setText(c + "");
        }
    }

    @Override
    public void restartScan() {
        if (Constant.ENABLE_SCAN_PRINT) {
            startScan();
        }
    }

    @Override
    public void gotoMall() {

    }

    @Override
    public void gotoAlliance() {

    }

}
