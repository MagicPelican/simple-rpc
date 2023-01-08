package com.simple.rpc.common.protocol;

import lombok.ToString;

import java.io.Serializable;

@ToString
public class Message<T> implements Serializable {

    private Header header;

    private T content;

    public Message() {
    }

    public Message(Header header, T content) {
        this.header = header;
        this.content = content;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

}
