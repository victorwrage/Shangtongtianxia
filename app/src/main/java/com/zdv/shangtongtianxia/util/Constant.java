package com.zdv.shangtongtianxia.util;

import com.baidu.location.BDLocation;
import com.zdv.shangtongtianxia.bean.MessageBean;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/5/13 14:43
 */

public class Constant {

    public static final String URL_BAIBAO = "http://www.o2obaibao.com/bsystem/";//百宝
    public static final String URL_SHANGTONGTIANXIA = "http://wdt.qianhaiwei.com/ThinkCmf/";//百宝
    public static final String CONSTANT_MERCHANT_ID = "617061910264433";//
    public static final String URL_MESSAGE = "http://c.kf10000.com/";//短信
    public static final String PUBLIC_KEY = "z4sgErHeEww5uwhTrjF6kM5qdKjpifQg6bp2iRAumnvRx7GV57laelZmX1e405QD";//公钥
    public static final String AID = "17060518292214";//AID
    public static final String WDT_SECRET = "10d496ypzBenyswInzfrkyrPLgXIz8ykrQjXMAnK5Bz/4Rr+3F7EfSX1k9yY0jbhe0fC2Ceqr+qdll2WvM0+J3xan3Vl4AvqrSoKC5w13zNQKhf6m8mX1m30";//SECRET
    public static final String WDT_KEY = "VJm6RQQsvvp";//SECRET

    public static final String PUBLIC_BMOB_KEY = "832ec665f9d515517f302be15f6c8bf6";//BMOB APP KEY

    public static final String PUBLIC_OCR_KEY = "RdQ9PnT67K4eyqEOPv6T9ZRH";//baidu ocr api key
    public static final String PUBLIC_OCR_SECRET = "WCfE8GCjHdrnftXVNbIj1XyRIGeGgHUH";//secret


    public static final String MESSAGE_USER_NAME = "5234TSGF2";//账号
    public static  String MESSAGE_PASSWORD = "852741";//密码


    public static  String MESSAGE_UPDATE_TIP = "";//
    public static  String MESSAGE_SIGN_TIP = "签到成功";//

    public static  String MESSAGE_CODE = "";	//生成的验证码
    public static final int MESSAGE_SEND_SUCCESS = 100;	//成功
    public static final int MESSAGE_SEND_FAIL = MESSAGE_SEND_SUCCESS+1;	//失败
    public static final int MESSAGE_SEND_PW_ERR = MESSAGE_SEND_FAIL+1;	//密码不对
    public static final int MESSAGE_SEND_NUM_ERR = MESSAGE_SEND_PW_ERR+1;	//接收号码格式错误
    public static final int MESSAGE_SEND_CONTENT_ERR = MESSAGE_SEND_NUM_ERR+1;	//敏感内容,会同时返回
    public static final int MESSAGE_SEND_TOO_FREQUENCY = MESSAGE_SEND_CONTENT_ERR+1;	//频率过快
    public static final int MESSAGE_SEND_LIMIT = MESSAGE_SEND_TOO_FREQUENCY+1;	//限制发送


    public static final int DEFAULT_TIMEOUT = 20;//超时时间(S)

    public static final int DEFAULT_MESSAGE_TIMEOUT = 120;//短信验证码超时时间(S)
    public static final String RECEIVE_LOCATION_SUCCESS = "location_success";	//定位成功

    public static JSONObject user_info;
    public static HashMap<String,String> temp_info;
    public static String photo_path;
    public static int photo_idx;
    public static final String USER_INFO_ID = "user_id";//缓存
    public static final String USER_INFO_MERCHANT_ID = "merchant_id";//缓存
    public static final String USER_INFO_PHONE = "user_phone";//缓存
    public static final String UP_AGENT_ID = "up_agent_id";//缓存
    public static final String REFERCODES = "refercodes";//缓存
    public static final String USER_INFO_TOKEN = "token";//缓存
    public static final String USER_INFO_EMAIL = "user_email";//缓存
    public static final String USER_INFO_IDCARD = "id_card_num";//缓存
    public static final String USER_INFO_NAME = "real_name";//缓存
    public static final String USER_INFO_SAVE = "save_info";//缓存
    public static final String USER_INFO_EMAIL_STATUS = "email_status";//缓存
    public static final String USER_INFO_ISAUTH = "is_auth";//缓存
    public static final String USER_INFO_USER_NAME = "user_name";//缓存
    public static final String USER_INFO_SHOPPER_ID = "shopper_id";//缓存
    public static final String USER_INFO_SESSION_ID = "user_session";//缓存
    public static final String USER_INFO_PW = "user_pw";//缓存
    public static final String USER_INFO_INVITE_CODE = "invite_code";//缓存

    public static final int FRAGMENT_CLEAR_DATE = 1000;//重新加载数据

    /**---------------数据提交字段集合----------------- **/
    public static final String MACHINE_ID = "machine_id";


    public static final String SIGN = "sign";
    public static final String SECRET = "secret";
    public static final String TEL = "tel";
    public static final String AID_STR = "aid";
    public static final String NAME = "name";
    public static final String RATE = "rate";
    public static final String TRADE_TYPE = "trade_type";
    public static final String TYPE = "type";
    public static final String CONTACT = "contact";
    public static final String ERRCODE = "errcode";
    public static final String ERRMSG = "errmsg";
    public static final String USER_NAME = "user_name";
    public static final String USERNAME = "username";
    public static final String F_IMG = "f_img";
    public static final String B_IMG = "b_img";
    public static final String CONTENT = "content";
    public static final String COMPANY_ID = "company_id";
    public static final String PHONE = "phone";
    public static final String MEMCODE = "memcode";
    public static final String LEVEL_ID = "level_id";
    public static final String MEMPAYPWD = "mempaypwd";
    public static final String STATUS = "status";
    public static final String ACTION = "action";
    public static final String CODE = "code";
    public static final String VERIIFY = "vertify";
    public static final String VCODE = "vcode";
    public static final String REFEREE = "referee";
    public static final String PAYSTATE = "paystate";
    public static final String PAYTYPE = "pay_type";
    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String ADDRESS = "address";
    public static final String DISTRICT = "district";
    public static final String ORDER_ID = "order_id";

    public static final String MONEY = "money";
    public static final String AUTH = "auth";
    public static final String NUM = "num";
    public static final String ID_CARD = "id_card";
    public static final String ID_CARD_NAME = "id_card_name";
    public static final String BANK = "bank";
    public static final String SUB_BRANCH = "sub_branch";
    public static final String BANK_ACCOUNT = "bank_account";
    public static final String BANK_ACCOUNT_NAME = "bank_account_name";
    public static final String BANKFIRM = "bankfirm";
    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";


    public static int InDirectTo = 0;
    public static BDLocation location;
    public static ArrayList<MessageBean> message;
    public static boolean ENABLE_SCAN_PRINT = false;
    public static String PAY_TOTAL = "0";
}
