package base;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Created by zhongwang on 2018/3/16.
 */

public class ZsmsApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "b0fae3ebe3b32e499fd665bd47319b00");

    }
}
