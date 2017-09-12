package com.zdv.shangtongtianxia.model;


import com.zdv.shangtongtianxia.util.Constant;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;

/**
 * Info:接口实现类
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:42
 */
public class RequestModelImpl implements IRequestMode {
    IRequestMode iRequestMode;



    @Override
    public Flowable<ResponseBody> QueryRegister(@Field(Constant.SECRET) String secret, @Field(Constant.TEL) String tel, @Field(Constant.PASSWORD) String password, @Field(Constant.NAME) String name, @Field(Constant.VERIIFY) String verify, @Field(Constant.VCODE) String vcode, @Field(Constant.REFEREE) String referee) {
        return iRequestMode.QueryRegister(secret, tel, password, name, verify, vcode,referee);
    }


    @Override
    public Flowable<ResponseBody> QueryCode(@Field(Constant.SECRET) String secret,
                                            @Field("type") String type,
                                            @Field(Constant.STATUS) String status,
                                            @Field(Constant.PHONE) String phone) {
        return iRequestMode.QueryCode(secret, type, status,phone);
    }

    @Override
    public Flowable<ResponseBody> QueryMessage(@Field(Constant.SECRET) String secret, @Field("type") String type) {
        return iRequestMode.QueryMessage(secret, type);
    }

    @Override
    public Flowable<ResponseBody> QueryOCRInfo(@Field("inputs") String inputs) {
        return iRequestMode.QueryOCRInfo(inputs);
    }


    @Override
    public Flowable<ResponseBody> QueryLogin(@Field(Constant.SECRET) String secret, @Field(Constant.TEL) String tel,
                                                @Field(Constant.PASSWORD) String password) {
        return iRequestMode.QueryLogin(secret, tel, password);
    }


    @Override
    public Flowable<ResponseBody> QueryForget(@Field(Constant.SECRET) String secret, @Field(Constant.PHONE) String phone, @Field(Constant.VERIIFY) String verify
            , @Field(Constant.VCODE) String vcode, @Field(Constant.PASSWORD) String password) {
        return iRequestMode.QueryForget(secret, phone, verify, vcode, password);
    }

    @Override
    public Flowable<ResponseBody> QuerySearchMemberOrder(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode, @Field(Constant.CODE) String code, @Field(Constant.PAYSTATE) String paystate, @Field(Constant.STATUS) String status) {
        return iRequestMode.QuerySearchMemberOrder(secret, memcode, code, paystate, status);
    }

    @Override
    public Flowable<ResponseBody> QueryQRCode(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode, @Field(Constant.MONEY) String money, @Field(Constant.PAYTYPE) String pay_type, @Field(Constant.ACTION) String action) {
        return iRequestMode.QueryQRCode(secret, memcode, money, pay_type, action);
    }

    @Override
    public Flowable<ResponseBody> QueryScanQrcode(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode, @Field(Constant.AUTH) String auth) {
        return iRequestMode.QueryScanQrcode(secret, memcode, auth);
    }

    @Override
    public Flowable<ResponseBody> QueryChargeSTCoin(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode, @Field(Constant.MONEY) String money, @Field(Constant.TYPE) String type
     ,@Field(Constant.PAYTYPE) String pay_type  ,@Field(Constant.TRADE_TYPE) String trade_type) {
        return iRequestMode.QueryChargeSTCoin(secret, memcode, money, type, pay_type, trade_type);
    }

    @Override
    public Flowable<ResponseBody> QueryMemberSign(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode, @Field(Constant.TYPE) String type) {
        return iRequestMode.QueryMemberSign(secret, memcode,type);
    }

    @Override
    public Flowable<ResponseBody> QueryTradeHistory(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode) {
        return iRequestMode.QueryTradeHistory(secret, memcode);
    }

    @Override
    public Flowable<ResponseBody> QuerySetPayPassword(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode, @Field(Constant.PASSWORD) String password, @Field(Constant.MEMPAYPWD) String mempaypwd) {
        return iRequestMode.QuerySetPayPassword(secret, memcode, password, mempaypwd);
    }

    @Override
    public Flowable<ResponseBody> QueryVerifyPayPassword(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode, @Field(Constant.MEMPAYPWD) String mempaypwd) {
        return iRequestMode.QueryVerifyPayPassword(secret, memcode, mempaypwd);
    }

    @Override
    public Flowable<ResponseBody> QueryResumeRecord(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode) {
        return iRequestMode.QueryResumeRecord(secret, memcode);
    }

    @Override
    public Flowable<ResponseBody> QueryActorMember(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode) {
        return iRequestMode.QueryActorMember(secret, memcode);
    }

    @Override
    public Flowable<ResponseBody> QueryApplyActorMember(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode, @Field(Constant.LEVEL_ID) String level_id, @Field(Constant.COMPANY_ID) String company_id, @Field(Constant.PAYTYPE) String pay_type, @Field(Constant.TRADE_TYPE) String trade_type,
                                                        @Field(Constant.TEL) String tel) {
        return iRequestMode.QueryApplyActorMember(secret, memcode, level_id, company_id, pay_type, trade_type,tel);
    }

    @Override
    public Flowable<ResponseBody> QueryActorFirm(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode) {
        return iRequestMode.QueryActorFirm(secret, memcode);
    }

    @Override
    public Flowable<ResponseBody> QueryMemberPayDetail(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode, @Field(Constant.ORDER_ID) String order_id) {
        return iRequestMode.QueryMemberPayDetail(secret, memcode, order_id);
    }

    @Override
    public Flowable<ResponseBody> QueryAddress(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode) {
        return iRequestMode.QueryAddress(secret, memcode);
    }

    @Override
    public Flowable<ResponseBody> QueryAddAddress(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode, @Field(Constant.USER_NAME) String username, @Field(Constant.TEL) String tel, @Field(Constant.ADDRESS) String address, @Field(Constant.DISTRICT) String district) {
        return iRequestMode.QueryAddAddress(secret, memcode, username, tel, address, district);
    }

    @Override
    public Flowable<ResponseBody> QueryDeleteAddress(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode, @Field(Constant.CODE) String code) {
        return iRequestMode.QueryDeleteAddress(secret, memcode, code);
    }

    @Override
    public Flowable<ResponseBody> QueryDefaultAddress(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode, @Field(Constant.CODE) String code) {
        return iRequestMode.QueryDefaultAddress(secret, memcode, code);
    }

    @Override
    public Flowable<ResponseBody> QueryVerifyMember(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode, @Field(Constant.USERNAME) String username, @Field(Constant.F_IMG) String f_img, @Field(Constant.B_IMG) String b_img, @Field(Constant.ID_CARD) String id_card) {
        return iRequestMode.QueryVerifyMember(secret, memcode, username, f_img, b_img, id_card);
    }

    @Override
    public Flowable<ResponseBody> QueryTeamMember(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode) {
        return iRequestMode.QueryTeamMember(secret, memcode);
    }

    @Override
    public Flowable<ResponseBody> QueryIsVerify(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode) {
        return iRequestMode.QueryIsVerify(secret, memcode);
    }


}
