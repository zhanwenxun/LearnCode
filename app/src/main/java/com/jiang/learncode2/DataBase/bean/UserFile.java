package com.jiang.learncode2.DataBase.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class UserFile extends BmobObject {

    private String F_U_name;
    private BmobFile file;

    public String getF_U_name() {
        return F_U_name;
    }

    public BmobFile getFile() {
        return file;
    }

    public void setF_U_name(String f_U_name) {
        F_U_name = f_U_name;
    }

    public void setFile(BmobFile file) {
        this.file = file;
    }
}
