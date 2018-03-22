package model;

import cn.bmob.v3.BmobObject;

/**
 * Created by zhongwang on 2018/3/16.
 */

public class ZsUser extends BmobObject {
    private String userName;
    private String passWord;
    private String nickName;
    private String idNum;
    private int state=1;//    1表示未审批，2表示可用
    private BmobObject imageHead;
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public BmobObject getImageHead() {
        return imageHead;
    }

    public void setImageHead(BmobObject imageHead) {
        this.imageHead = imageHead;
    }
}
