package base;

import android.app.Application;

import cn.bmob.v3.Bmob;
import model.ZsUser;

/**
 * Created by zhongwang on 2018/3/16.
 */

public class ZsmsApplication extends Application{
    private boolean isManageer = false;
    private ZsUser zsUser;
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "b0fae3ebe3b32e499fd665bd47319b00");
    }

    public boolean isManageer() {
        return isManageer;
    }

    public void setManageer(boolean manageer) {
        isManageer = manageer;
    }

    public ZsUser getZsUser() {
        return zsUser;
    }

    public void setZsUser(ZsUser zsUser) {
        this.zsUser = zsUser;
        setManageer(zsUser.getState()==3);
    }
}
