package com.jiang.learncode2.DataBase.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class Question extends BmobObject {
    private Integer Q_ID;
    private String Q_topic;
    private String Q_description;
    private String Q_raised_name;
    private BmobDate Q_time;
    private Integer Q_follow;
    private Integer Q_type;

    public void setQ_ID(Integer q_ID) {
        Q_ID = q_ID;
    }

    public Integer getQ_ID() {
        return Q_ID;
    }

    public void setQ_type(Integer q_type) {
        Q_type = q_type;
    }

    public Integer getQ_type() {
        return Q_type;
    }

    public String getQ_topic() {
        return Q_topic;
    }

    public String getQ_description() {
        return Q_description;
    }

    public String getQ_raised_name() {
        return Q_raised_name;
    }

    public BmobDate getQ_time() {
        return Q_time;
    }

    public Integer getQ_follow() {
        return Q_follow;
    }

    public void setQ_topic(String q_topic) {
        Q_topic = q_topic;
    }

    public void setQ_description(String q_description) {
        Q_description = q_description;
    }

    public void setQ_raised_name(String q_raise_name) {
        Q_raised_name = q_raise_name;
    }

    public void setQ_time(BmobDate q_time) {
        Q_time = q_time;
    }

    public void setQ_follow(Integer q_follow) {
        Q_follow = q_follow;
    }
}
