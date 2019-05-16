package com.dagu.pojo;

public class MailModel {
    private String fromAddress;//发送人地址1个
    private String toAddresses;//接收人地址,可以为很多个，每个地址之间用";"分隔，比方说450065208@qq.com;lpf@sina.com
    private String subject;//邮件主题
    private String content;//邮件文本内容
    private String[] attachFileNames;//附件
    public String getFromAddress() {
        return fromAddress;
    }
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }
    public String getToAddresses() {
        return toAddresses;
    }
    public void setToAddresses(String toAddresses) {
        this.toAddresses = toAddresses;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public void setAttachFileNames(String[] attachFileNames) {
        this.attachFileNames = attachFileNames;
    }
    public String[] getAttachFileNames() {
        return attachFileNames;
    }

}