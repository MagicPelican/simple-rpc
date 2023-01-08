package com.simple.rpc.common.protocol;

import lombok.Data;

import java.io.Serializable;

@Data
public class Header implements Serializable {

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 魔数
     */
    private short magic;

    /**
     * 版本号
     */
    private String version;

    public Header() {
    }

    public Header(String messageId, short magic, String version) {
        this.messageId = messageId;
        this.magic = magic;
        this.version = version;
    }


}

