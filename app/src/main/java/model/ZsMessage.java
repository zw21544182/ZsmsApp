package model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by zhongwang on 2018/3/22.
 */

public class ZsMessage extends BmobObject {
    private BmobFile imageFile;
    private String messageUrl;
    private String instruction;
    private String title;
    private String userObjectId;

    public ZsMessage(BmobFile imageFile, String messageUrl, String title) {
        this.imageFile = imageFile;
        this.messageUrl = messageUrl;
        this.title = title;
    }

    public ZsMessage() {
    }

    public BmobFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(BmobFile imageFile) {
        this.imageFile = imageFile;
    }

    public String getMessageUrl() {
        return messageUrl;
    }

    public void setMessageUrl(String messageUrl) {
        this.messageUrl = messageUrl;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserObjectId() {
        return userObjectId;
    }

    public void setUserObjectId(String userObjectId) {
        this.userObjectId = userObjectId;
    }
}
