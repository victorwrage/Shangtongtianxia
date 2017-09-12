package com.zdv.shangtongtianxia.view;


import okhttp3.ResponseBody;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:49
 */

public interface IPayView extends IView{

    void ResolveSearchMemberOrder(ResponseBody info);
    void ResolveQRCode(ResponseBody info);
    void ResolveScanQrcode(ResponseBody info);
    void ResolveChargeSTCoin(ResponseBody info);
    void ResolveTradeHistory(ResponseBody info);
    void ResolveSetPayPassword(ResponseBody info);
    void ResolveVerifyPayPassword(ResponseBody info);
    void ResolveResumeRecord(ResponseBody info);

}
