package com.jiang.learncode2.DataBase.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class Answer extends BmobObject {
    private Integer A_Q_ID;
    private String A_U_name;
    private String A_content;
    private BmobDate A_time;

    public Integer getA_Q_ID() {
        return A_Q_ID;
    }

    public String getA_U_name() {
        return A_U_name;
    }

    public String getA_content() {
        return A_content;
    }

    public BmobDate getA_time() {
        return A_time;
    }

    public void setA_Q_ID(Integer a_Q_ID) {
        A_Q_ID = a_Q_ID;
    }

    public void setA_U_name(String a_U_name) {
        A_U_name = a_U_name;
    }

    public void setA_content(String a_content) {
        A_content = a_content;
    }

    public void setA_time(BmobDate a_time) {
        A_time = a_time;
    }
}
