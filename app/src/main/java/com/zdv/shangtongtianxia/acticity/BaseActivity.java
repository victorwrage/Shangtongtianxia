package com.zdv.shangtongtianxia.acticity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.zdv.shangtongtianxia.R;
import com.zdv.shangtongtianxia.STTXApplication;
import com.zdv.shangtongtianxia.customView.ProgressBarItem;
import com.zdv.shangtongtianxia.fragment.BaseFragment;
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
import com.zdv.shangtongtianxia.util.DoubleConfirm;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @ClassName: BaseActivity
 * @Description:TODO(界面的基类)
 * @author: xiaoyl
 * @date: 2013-7-10 下午2:30:06
 */
public class BaseActivity extends FragmentActivity {
    public FragmentLogin fragment0;
    public FragmentRegister fragment1;
    public FragmentMain fragment2;
    public FragmentForget fragment3;
    public FragmentWealth fragment4;
    public FragmentCoin fragment5;
    public FragmentMap fragment6;
    public FragmentProfit fragment7;
    public FragmentRecharge fragment8;
    public FragmentResearch fragment9;
    public FragmentWebview fragment10;
    public FragmentUserManager fragment11;
    public FragmentMessage fragment12;
    public FragmentProfile fragment13;
    public FragmentVerify fragment14;
    public FragmentActor fragment15;
    public FragmentTeam fragment16;
    public FragmentOrder fragment17;
    public FragmentAddress fragment18;
    public FragmentSetting fragment19;
    public FragmentFeedBack fragment20;
    public FragmentPassword fragment21;
    public FragmentPay fragment22;
    public FragmentScan fragment23;
    public FragmentPosProfit fragment24;
    public FragmentTerminalProfit fragment25;
    public FragmentWithdraw fragment26;
    public FragmentQrcode fragment27;
    public FragmentChargeSTCoin fragment28;
    public FragmentCoupon fragment29;
    public FragmentShop fragment30;
    public FragmentCouponManager fragment31;


    protected static final String PAGE_0 = "page_0";
    protected static final String PAGE_1 = "page_1";
    protected static final String PAGE_2 = "page_2";
    protected static final String PAGE_3 = "page_3";
    protected static final String PAGE_4 = "page_4";
    protected static final String PAGE_5 = "page_5";
    protected static final String PAGE_6 = "page_6";
    protected static final String PAGE_7 = "page_7";
    protected static final String PAGE_8 = "page_8";
    protected static final String PAGE_9 = "page_9";
    protected static final String PAGE_10 = "page_10";
    protected static final String PAGE_11 = "page_11";
    protected static final String PAGE_12 = "page_12";
    protected static final String PAGE_13 = "page_13";
    protected static final String PAGE_14 = "page_14";
    protected static final String PAGE_15 = "page_15";
    protected static final String PAGE_16 = "page_16";
    protected static final String PAGE_17 = "page_17";
    protected static final String PAGE_18 = "page_18";
    protected static final String PAGE_19 = "page_19";
    protected static final String PAGE_20 = "page_20";
    protected static final String PAGE_21 = "page_21";
    protected static final String PAGE_22 = "page_22";
    protected static final String PAGE_23 = "page_23";
    protected static final String PAGE_24 = "page_24";
    protected static final String PAGE_25 = "page_25";
    protected static final String PAGE_26 = "page_26";
    protected static final String PAGE_27 = "page_27";
    protected static final String PAGE_28 = "page_28";
    protected static final String PAGE_29 = "page_29";
    protected static final String PAGE_30 = "page_30";
    protected static final String PAGE_31 = "page_31";

    protected  BaseFragment cur_fragment;

    private DoubleConfirm double_c;

    protected Context context;
    protected int cur_page = -1;

    /**
     * 双击事件
     */
    private DoubleConfirm.DoubleConfirmEvent doubleConfirmEvent = new DoubleConfirm.DoubleConfirmEvent() {
        public void doSecondConfirmEvent() {
            new STTXApplication().getInstance().exitApplication();
        }

        public int getFirstConfirmTipsId() {
            return R.string.msg_exit;
        }
    };

    public String currentDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    protected void showWaitDialog(String tip){
        ProgressBarItem.show(BaseActivity.this,tip,false,null);
    }
    protected void hideWaitDialog() {
        ProgressBarItem.hideProgress();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new STTXApplication().getInstance().addActivitys(this);
        context = getApplicationContext();
        this.double_c = new DoubleConfirm();
        this.double_c.setEvent(this.doubleConfirmEvent);
    }


    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if (paramInt == KeyEvent.KEYCODE_BACK) {
            switch (cur_page) {
                case 2:
                    this.double_c.onKeyPressed(paramKeyEvent, this);
                    return true;
                case 1:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                    cur_fragment.Back();
                    return true;
                default:
                    this.double_c.onKeyPressed(paramKeyEvent, this);
                    return true;
            }
        }
        return false;
    }

    protected void gotoMain() {

    }


}
