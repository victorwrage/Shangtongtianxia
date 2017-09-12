package com.zdv.shangtongtianxia.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {
        @Index(value = "idx, createtime DESC", unique = true)
})
/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/5/13 14:47
 */

public class MessageBean {


    String id;
    @Id
    Long idx;
    String type;
    String view;
    String address;
    String img;
    String title;
    String details;
    String status;
    String url;
    String createtime;
    String starttime;
    String stoptime;
    String operator;
    String company_id;
    Boolean is_read = false;
@Generated(hash = 2067872873)
public MessageBean(String id, Long idx, String type, String view,
        String address, String img, String title, String details, String status,
        String url, String createtime, String starttime, String stoptime,
        String operator, String company_id, Boolean is_read) {
    this.id = id;
    this.idx = idx;
    this.type = type;
    this.view = view;
    this.address = address;
    this.img = img;
    this.title = title;
    this.details = details;
    this.status = status;
    this.url = url;
    this.createtime = createtime;
    this.starttime = starttime;
    this.stoptime = stoptime;
    this.operator = operator;
    this.company_id = company_id;
    this.is_read = is_read;
}
@Generated(hash = 1588632019)
public MessageBean() {
}
public String getId() {
    return this.id;
}
public void setId(String id) {
    this.id = id;
}
public Long getIdx() {
    return this.idx;
}
public void setIdx(Long idx) {
    this.idx = idx;
}
public String getType() {
    return this.type;
}
public void setType(String type) {
    this.type = type;
}
public String getView() {
    return this.view;
}
public void setView(String view) {
    this.view = view;
}
public String getAddress() {
    return this.address;
}
public void setAddress(String address) {
    this.address = address;
}
public String getImg() {
    return this.img;
}
public void setImg(String img) {
    this.img = img;
}
public String getTitle() {
    return this.title;
}
public void setTitle(String title) {
    this.title = title;
}
public String getDetails() {
    return this.details;
}
public void setDetails(String details) {
    this.details = details;
}
public String getStatus() {
    return this.status;
}
public void setStatus(String status) {
    this.status = status;
}
public String getUrl() {
    return this.url;
}
public void setUrl(String url) {
    this.url = url;
}
public String getCreatetime() {
    return this.createtime;
}
public void setCreatetime(String createtime) {
    this.createtime = createtime;
}
public String getStarttime() {
    return this.starttime;
}
public void setStarttime(String starttime) {
    this.starttime = starttime;
}
public String getStoptime() {
    return this.stoptime;
}
public void setStoptime(String stoptime) {
    this.stoptime = stoptime;
}
public String getOperator() {
    return this.operator;
}
public void setOperator(String operator) {
    this.operator = operator;
}
public String getCompany_id() {
    return this.company_id;
}
public void setCompany_id(String company_id) {
    this.company_id = company_id;
}
public Boolean getIs_read() {
    return this.is_read;
}
public void setIs_read(Boolean is_read) {
    this.is_read = is_read;
}


}
