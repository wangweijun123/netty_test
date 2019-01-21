package com.weleadin.connection.service;

import java.io.Serializable;

public class MessageBean implements Serializable {
    // 服务端推送消息
    public static final String EVENT_MESSAGE_PUSH = "EVENT_MESSAGE_PUSH";

    private String instructions;
    private String msgId;
    private String usrCode;
    private String notifyTitle;
    private String msgTitle;
    private String content;
    private String msgType;
    private String c_id;
    private String issound;
    private String sendTime;
    private String orderid;

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getUsrCode() {
        return usrCode;
    }

    public void setUsrCode(String usrCode) {
        this.usrCode = usrCode;
    }

    public String getNotifyTitle() {
        return notifyTitle;
    }

    public void setNotifyTitle(String notifyTitle) {
        this.notifyTitle = notifyTitle;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getIssound() {
        return issound;
    }

    public void setIssound(String issound) {
        this.issound = issound;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "instructions='" + instructions + '\'' +
                ", msgId='" + msgId + '\'' +
                ", usrCode='" + usrCode + '\'' +
                ", notifyTitle='" + notifyTitle + '\'' +
                ", msgTitle='" + msgTitle + '\'' +
                ", content='" + content + '\'' +
                ", msgType='" + msgType + '\'' +
                ", c_id='" + c_id + '\'' +
                ", issound='" + issound + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", orderid='" + orderid + '\'' +
                '}';
    }
}
