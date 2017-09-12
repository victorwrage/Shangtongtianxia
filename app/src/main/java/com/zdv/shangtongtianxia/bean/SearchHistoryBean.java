package com.zdv.shangtongtianxia.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {
        @Index(value = "ids, createtime DESC", unique = true)
})
/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/8/17 10:50
 */
public class SearchHistoryBean {
    @Id
    private Long ids;
    private int type;
    private int idx;
    private String key_word;
    private long createtime;
    private String key_type;
@Generated(hash = 1952848657)
public SearchHistoryBean(Long ids, int type, int idx, String key_word,
        long createtime, String key_type) {
    this.ids = ids;
    this.type = type;
    this.idx = idx;
    this.key_word = key_word;
    this.createtime = createtime;
    this.key_type = key_type;
}
@Generated(hash = 1570282321)
public SearchHistoryBean() {
}
public Long getIds() {
    return this.ids;
}
public void setIds(Long ids) {
    this.ids = ids;
}
public int getType() {
    return this.type;
}
public void setType(int type) {
    this.type = type;
}
public int getIdx() {
    return this.idx;
}
public void setIdx(int idx) {
    this.idx = idx;
}
public String getKey_word() {
    return this.key_word;
}
public void setKey_word(String key_word) {
    this.key_word = key_word;
}
public long getCreatetime() {
    return this.createtime;
}
public void setCreatetime(long createtime) {
    this.createtime = createtime;
}
public String getKey_type() {
    return this.key_type;
}
public void setKey_type(String key_type) {
    this.key_type = key_type;
}


}
