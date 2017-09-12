package com.zdv.shangtongtianxia.view;


import okhttp3.ResponseBody;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:49
 */

public interface IAddressView extends IView{

    void ResolveAddress(ResponseBody info);

    void ResolveAddAddress(ResponseBody info);

    void ResolveDeleteAddress(ResponseBody info);

    void ResolveDefaultAddress(ResponseBody info);

}
