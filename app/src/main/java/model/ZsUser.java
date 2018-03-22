package model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by zhongwang on 2018/3/16.
 */

public class ZsUser extends BmobObject {
    private String userName;
    private String passWord;
    private String nickName;
    private String idNum;
    private int state=1;//    1表示未审批，2表示可用
    private BmobFile imageHead;

    public ZsUser(String userName, String passWord, String nickName, String idNum, int state, BmobFile imageHead) {
        this.userName = userName;
        this.passWord = passWord;
        this.nickName = nickName;
        this.idNum = idNum;
        this.state = state;
        this.imageHead = imageHead;
    }

    public ZsUser() {
    }

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

    public BmobFile getImageHead() {
        return imageHead;
    }

    public void setImageHead(BmobFile imageHead) {
        this.imageHead = imageHead;
    }
}
