package com.zdv.shangtongtianxia.model;


import android.support.annotation.Nullable;

import com.zdv.shangtongtianxia.util.Constant;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by xyl on 2017/4/6.
 */

public interface IRequestMode {


    @FormUrlEncoded
    @POST("index.php?g=Member&m=Member&a=AddMember")
    Flowable<ResponseBody> QueryRegister(@Field(Constant.SECRET) String secret,
                                         @Field(Constant.TEL) String tel,
                                         @Field(Constant.PASSWORD) String password,
                                         @Field(Constant.NAME) String name,
                                         @Field(Constant.VERIIFY) String verify,
                                         @Field(Constant.VCODE) String vcode,
                                         @Nullable @Field(Constant.REFEREE) String referee);

    @FormUrlEncoded
    @POST("index.php?g=Member&m=Member&a=SendMemberVertify")
    Flowable<ResponseBody> QueryCode(@Field(Constant.SECRET) String secret,
                                     @Field("type") String type,
                                     @Nullable @Field(Constant.STATUS) String status,
                                     @Field(Constant.PHONE) String phone);

    @FormUrlEncoded
    @POST("index.php?g=Member&m=Notice&a=getShopNotice")
    Flowable<ResponseBody> QueryMessage(@Field(Constant.SECRET) String secret,
                                        @Field("type") String type);


    @FormUrlEncoded
    @POST("rest/160601/ocr/ocr_idcard.json")
    Flowable<ResponseBody> QueryOCRInfo(@Field("inputs") String inputs);

    @FormUrlEncoded
    @POST("index.php?g=Member&m=Member&a=LoginMember")
    Flowable<ResponseBody> QueryLogin(@Field(Constant.SECRET) String secret, @Field(Constant.TEL) String tel,
                                      @Field(Constant.PASSWORD) String password);


    @FormUrlEncoded
    @POST("index.php?g=Member&m=Member&a=UpdateMemPwdByVCode")
    Flowable<ResponseBody> QueryForget(@Field(Constant.SECRET) String secret, @Field(Constant.PHONE) String phone, @Field(Constant.VERIIFY) String verify
            , @Field(Constant.VCODE) String vcode, @Field(Constant.PASSWORD) String password);

    @FormUrlEncoded
    @POST("index.php?g=Member&m=MemOrder&a=SearchMemOrder")
    Flowable<ResponseBody> QuerySearchMemberOrder(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode, @Field(Constant.CODE) String code
            , @Field(Constant.PAYSTATE) String paystate, @Field(Constant.STATUS) String status);

    @FormUrlEncoded
    @POST("index.php?g=Member&m=MemPay&a=getPayMoneyAndToken")
    Flowable<ResponseBody> QueryQRCode(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode, @Field(Constant.MONEY) String money
            , @Field(Constant.PAYTYPE) String pay_type, @Field(Constant.ACTION) String action);

    @FormUrlEncoded
    @POST("index.php?g=Member&m=MemPay&a=ScanForExchange")
    Flowable<ResponseBody> QueryScanQrcode(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode, @Field(Constant.AUTH) String auth);

    @FormUrlEncoded
    @POST("index.php?g=Member&m=MemOrder&a=AddMemRecharge")
    Flowable<ResponseBody> QueryChargeSTCoin(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode, @Field(Constant.MONEY) String money, @Field(Constant.TYPE) String type
            , @Field(Constant.PAYTYPE) String pay_type, @Field(Constant.TRADE_TYPE) String trade_type);


    @FormUrlEncoded
    @POST("index.php?g=Member&m=Member&a=AddMemSigned")
    Flowable<ResponseBody> QueryMemberSign(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode, @Field(Constant.TYPE) String type);

    @FormUrlEncoded
    @POST("index.php?g=Member&m=MemOrder&a=SearchMemOrder")
    Flowable<ResponseBody> QueryTradeHistory(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode);

    @FormUrlEncoded
    @POST("index.php?g=Member&m=Member&a=SetPayPassword")
    Flowable<ResponseBody> QuerySetPayPassword(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode
            , @Field(Constant.PASSWORD) String password, @Field(Constant.MEMPAYPWD) String mempaypwd);

    @FormUrlEncoded
    @POST("index.php?g=Member&m=Member&a=VertifyPayPwd")
    Flowable<ResponseBody> QueryVerifyPayPassword(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode
            , @Field(Constant.MEMPAYPWD) String mempaypwd);

    @FormUrlEncoded
    @POST("index.php?g=Member&m=Member&a=SearchMemberCostLog")
    Flowable<ResponseBody> QueryResumeRecord(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode);

    @FormUrlEncoded
    @POST("index.php?g=Member&m=Member&a=getMemLevels")
    Flowable<ResponseBody> QueryActorMember(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode);

    @FormUrlEncoded
    @POST("index.php?g=Member&m=MemOrder&a=getMemberLevel")
    Flowable<ResponseBody> QueryApplyActorMember(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode,
                                                 @Field(Constant.LEVEL_ID) String level_id, @Field(Constant.COMPANY_ID) String company_id, @Field(Constant.PAYTYPE) String pay_type
            , @Field(Constant.TRADE_TYPE) String trade_type, @Field(Constant.TEL) String tel);

    @FormUrlEncoded
    @POST("index.php?g=Member&m=Shop&a=getComInfo")
    Flowable<ResponseBody> QueryActorFirm(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode);

    @FormUrlEncoded
    @POST("index.php?g=Member&m=MemOrder&a=getMemPayOrderDetails")
    Flowable<ResponseBody> QueryMemberPayDetail(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode
            , @Field(Constant.ORDER_ID) String order_id);

    @FormUrlEncoded
    @POST("index.php?g=Member&m=Address&a=Index")
    Flowable<ResponseBody> QueryAddress(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode);

    @FormUrlEncoded
    @POST("index.php?g=Member&m=Address&a=AddMemAddr")
    Flowable<ResponseBody> QueryAddAddress(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode
            , @Field(Constant.USER_NAME) String username, @Field(Constant.TEL) String tel, @Field(Constant.ADDRESS) String address
            , @Field(Constant.DISTRICT) String district);

    @FormUrlEncoded
    @POST("index.php?g=Member&m=Address&a=DeleteMemAddr")
    Flowable<ResponseBody> QueryDeleteAddress(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode
            , @Field(Constant.CODE) String code);

    @FormUrlEncoded
    @POST("index.php?g=Member&m=Address&a=SetDefaultAddr")
    Flowable<ResponseBody> QueryDefaultAddress(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode
            , @Field(Constant.CODE) String code);

    @FormUrlEncoded
    @POST("index.php?g=Member&m=Member&a=VertifyMember")
    Flowable<ResponseBody> QueryVerifyMember(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode
            , @Field(Constant.USERNAME) String username, @Field(Constant.F_IMG) String f_img
            , @Field(Constant.B_IMG) String b_img, @Field(Constant.ID_CARD) String id_card);

    @FormUrlEncoded
    @POST("index.php?g=Member&m=Member&a=ListMemberStatus")
    Flowable<ResponseBody> QueryTeamMember(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode);


    @FormUrlEncoded
    @POST("index.php?g=Member&m=Member&a=CheckMemberVertify")
    Flowable<ResponseBody> QueryIsVerify(@Field(Constant.SECRET) String secret, @Field(Constant.MEMCODE) String memcode);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Curpons&a=getMemCurpons")
    Flowable<ResponseBody> queryMemCurpons(@Field(Constant.MEMCODE) String memcode, @Field(Constant.SIGN) String sign);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Curpons&a=UseCurpon")
    Flowable<ResponseBody> queryUseCurpon(@Field(Constant.SIGN) String sign, @Field(Constant.MEMCODE) String memcode, @Field(Constant.SN_CODE) String sn_code, @Field(Constant.NUMBER) String number);
}
