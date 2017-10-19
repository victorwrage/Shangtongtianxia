package com.zdv.shangtongtianxia.present;


/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:46
 */

public interface IRequestPresent {


    void QueryRegister(String secret, String tel, String password, String name, String verify, String vcode, String referee);

    void QueryOCRInfo(String inputs);

    void QueryCode(String secret, String status, String phone);


    void QueryLogin(String secret, String tel, String password);

    void QueryMessage(String secret, String type);

    void QueryForget(String secret, String phone, String verify, String vcode, String password);

    void QuerySearchMemberOrder(String secret, String memcode, String code, String paystate, String status);

    void QueryQRCode(String secret, String memcode, String money, String pay_type, String action);

    void QueryScanQrcode(String secret, String memcode, String auth);

    void QueryChargeSTCoin(String secret, String memcode, String money, String type, String pay_type, String trade_type);

    void QueryMemberSign(String secret, String memcode, String type);

    void QueryTradeHistory(String secret, String memcode);

    void QuerySetPayPassword(String secret, String memcode, String password, String mempaypwd);

    void QueryVerifyPayPassword(String secret, String memcode, String mempaypwd);

    void QueryResumeRecord(String secret, String memcode);

    void QueryActorMember(String secret, String memcode);

    void QueryTeamMember(String secret, String memcode);

    void QueryApplyActorMember(String secret, String memcode,
                               String level_id, String company_id, String pay_type, String trade_type, String tel);

    void QueryActorFirm(String secret, String memcode);

    void QueryMemberPayDetail(String secret, String memcode, String order_id);

    void QueryAddress(String secret, String memcode);

    void QueryAddAddress(String secret, String memcode, String username, String tel, String address, String district);

    void QueryDeleteAddress(String secret, String memcode, String code);

    void QueryDefaultAddress(String secret, String memcode, String code);

    void QueryVerifyMember(String secret, String memcode, String username, String f_img, String b_img, String id_card);

    void QueryIsVerify(String secret, String memcode);

    /**
     * 查询会员卡劵接口
     *
     * @param memcode
     * @param sign
     */
    void QueryMemCurpons(String memcode, String sign);

    /**
     * 抵消卡劵接口
     *
     * @param sign
     * @param memcode
     * @param sn_code
     * @param number
     */
    void QueryUseCurpon(String sign, String memcode, String sn_code, String number);
}
