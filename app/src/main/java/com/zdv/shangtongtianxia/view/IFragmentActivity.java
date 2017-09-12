package com.zdv.shangtongtianxia.view;

import com.zdv.shangtongtianxia.bean.MessageBean;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/8/22 17:09
 */

public interface IFragmentActivity {
    void gotoMain() ;
    void gotoLogin() ;
    void gotoRegister() ;
    void gotoForget() ;
    void gotoWealth() ;
    void gotoCoin() ;
    void gotoProfit() ;
    void gotoRecharge() ;
    void gotoResearch() ;
    void gotoUserManager() ;
    void gotoProfile() ;
    void gotoVerify() ;
    void gotoActor() ;
    void gotoTeam() ;
    void gotoOrder() ;
    void gotoAddress() ;
    void gotoSetting() ;
    void gotoFeedBack() ;
    void gotoPassword() ;
    void gotoPay() ;
    void gotoScan() ;
    void gotoPosProfit() ;
    void gotoTerminal() ;
    void gotoWithdraw() ;
    void gotoQRcode() ;
    void gotoShop() ;
    void gotoCoinAbout() ;
    void gotoMall();
    void gotoAlliance();

    void gotoChargeCoin();

    void gotoCoupon();

    void gotoCouponManager();

    void gotoWebview();

    void NotifyMessage(MessageBean messageBean);

    void restartScan();

    void print();
}
