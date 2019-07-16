package com.jiang.learncode2.HomeModule.textwarrior.simpleC.interfaces;


import com.jiang.learncode2.HomeModule.textwarrior.simpleC.util.ShellUtils;

/**
 * Created by zyw on 2017/11/14.
 */
public interface ExecCallback {
    void onResult(ShellUtils.CommandResult result);
}
