package com.zdv.shangtongtianxia.bean;

import java.util.List;

/**
 * Created by Hello_world on 2017/9/22.
 */

public class ConponsBean {


    /**
     * errcode : 200
     * errmsg : success
     * content : [{"starttime":"2017-09-21 12:03:00","stoptime":"2018-09-22 23:23:00","title":"牛肉卡券","img":"/Working/Curpons/data/upload/default/20170916/59bcddeab0832.jpg","name":"优惠券","onsaled":"1","takein_ids":"5777","code":"CL2017092216114115","use_type":"1","cp_code":"09221611417663","shop_id":"5325","number":"5","use_num":"0","sn_code":"AhutA12z","member":"周健淦"}]
     */

    private int errcode;
    private String errmsg;
    /**
     * starttime : 2017-09-21 12:03:00
     * stoptime : 2018-09-22 23:23:00
     * title : 牛肉卡券
     * img : /Working/Curpons/data/upload/default/20170916/59bcddeab0832.jpg
     * name : 优惠券
     * onsaled : 1
     * takein_ids : 5777
     * code : CL2017092216114115
     * use_type : 1
     * cp_code : 09221611417663
     * shop_id : 5325
     * number : 5
     * use_num : 0
     * sn_code : AhutA12z
     * member : 周健淦
     */

    private List<ContentBean> content;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        private String starttime;
        private String stoptime;
        private String title;
        private String img;
        private String name;
        private String onsaled;
        private String takein_ids;
        private String code;
        private String use_type;
        private String cp_code;
        private String shop_id;
        private String number;
        private String use_num;
        private String sn_code;
        private String member;
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getStoptime() {
            return stoptime;
        }

        public void setStoptime(String stoptime) {
            this.stoptime = stoptime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOnsaled() {
            return onsaled;
        }

        public void setOnsaled(String onsaled) {
            this.onsaled = onsaled;
        }

        public String getTakein_ids() {
            return takein_ids;
        }

        public void setTakein_ids(String takein_ids) {
            this.takein_ids = takein_ids;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getUse_type() {
            return use_type;
        }

        public void setUse_type(String use_type) {
            this.use_type = use_type;
        }

        public String getCp_code() {
            return cp_code;
        }

        public void setCp_code(String cp_code) {
            this.cp_code = cp_code;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getUse_num() {
            return use_num;
        }

        public void setUse_num(String use_num) {
            this.use_num = use_num;
        }

        public String getSn_code() {
            return sn_code;
        }

        public void setSn_code(String sn_code) {
            this.sn_code = sn_code;
        }

        public String getMember() {
            return member;
        }

        public void setMember(String member) {
            this.member = member;
        }
    }
}
