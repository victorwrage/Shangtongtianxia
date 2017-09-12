package com.zdv.shangtongtianxia.bean;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/8/22 16:25
 */

public class ActorLevel {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
    String level_name;
    String l_integral;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany_type() {
        return company_type;
    }

    public void setCompany_type(String company_type) {
        this.company_type = company_type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    String name;
    String company_type;
    String address;

    ActorLevel() {

    }


    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }

    public String getL_integral() {
        return l_integral;
    }

    public void setL_integral(String l_integral) {
        this.l_integral = l_integral;
    }
}
