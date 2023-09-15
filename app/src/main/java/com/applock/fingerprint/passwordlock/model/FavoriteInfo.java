package com.applock.fingerprint.passwordlock.model;

import org.litepal.crud.DataSupport;

/**
 * Created by xian on 2017/2/17.
 *推荐加锁的应用信息
 */

public class FavoriteInfo extends DataSupport {
    public String packageName;

    public FavoriteInfo() {
    }

    public FavoriteInfo(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
