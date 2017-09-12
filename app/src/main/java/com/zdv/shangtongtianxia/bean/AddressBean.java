package com.zdv.shangtongtianxia.bean;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/5/13 14:47
 */

public class AddressBean {
    String errcode;
    String code;
    String memcode;
    String username;

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    String postcode;
    String tel;
    Boolean isDefault = false;
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMemcode() {
        return memcode;
    }

    public void setMemcode(String memcode) {
        this.memcode = memcode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    String address;
    String district;
    String used;
    String address_name;
    String address_phone;

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getAddress_phone() {
        return address_phone;
    }

    public void setAddress_phone(String address_phone) {
        this.address_phone = address_phone;
    }

    public String getAddress_str() {
        return address_str;
    }

    public void setAddress_str(String address_str) {
        this.address_str = address_str;
    }

    String address_str;

    public String getAddress_district_str() {
        return address_district_str;
    }

    public void setAddress_district_str(String address_district_str) {
        this.address_district_str = address_district_str;
    }

    String address_district_str;


    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public HeoMerchantInfoResponse getContent() {
        return content;
    }

    public void setContent(HeoMerchantInfoResponse content) {
        this.content = content;
    }

    String errmsg;
    HeoMerchantInfoResponse content;
    @Override
    public String toString() {
        if(content!=null ) {
            return "errcode:" + errcode + "errmsg:" + errmsg + "content:" + content.toString() ;
        }
        return "errcode:" + errcode + "errmsg:"+errmsg;
    }
}
