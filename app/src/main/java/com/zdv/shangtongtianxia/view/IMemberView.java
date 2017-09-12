package com.zdv.shangtongtianxia.view;


import okhttp3.ResponseBody;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:49
 */

public interface IMemberView extends IView{

    void ResolveActorMember(ResponseBody info);

    void ResolveApplyActorMember(ResponseBody info);
    void ResolveMemberSign(ResponseBody info);
    void ResolveMessage(ResponseBody info);
    void ResolveActorFirm(ResponseBody info);
    void ResolveMemberPayDetail(ResponseBody info);
    void ResolveVerifyMember(ResponseBody info);
    void ResolveTeamMember(ResponseBody info);
    void ResolveIsVerify(ResponseBody info);

}
