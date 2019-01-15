package com.weleadin.connection.service;

public class MessageBean {
    // 服务端推送消息
    public static final String EVENT_MESSAGE_PUSH = "EVENT_MESSAGE_PUSH";
    /**
     * instructions : EVENT_MESSAGE_PUSH
     * msgId : 123
     * usrCode : 232
     * notifyTitle : 通知标题
     * msgTitle : 消息主题
     * content : 消息内容
     */
    private String instructions;
    private String msgId;
    private String usrCode;
    private String notifyTitle;
    private String msgTitle;
    private String content;

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
}
