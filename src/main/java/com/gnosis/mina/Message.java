package com.gnosis.mina;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * Created by Firat on 24.10.2014.
 */
public class Message implements Serializable{
    private Object message;
    private InetSocketAddress to;
    private InetSocketAddress from;
    private int pinCode;
    public Message(Object message, InetSocketAddress to, InetSocketAddress from,int pinCode) {
        this.message = message;
        this.to = to;
        this.from = from;
        this.pinCode = pinCode;
    }

    public Message(Object message, InetSocketAddress to,int pinCode) {
        this.message = message;
        this.to = to;
        this.pinCode = pinCode;
    }

    public Message(Object message) {
        this.message = message;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public InetSocketAddress getTo() {
        return to;
    }

    public void setTo(InetSocketAddress to) {
        this.to = to;
    }

    public InetSocketAddress getFrom() {
        return from;
    }

    public void setFrom(InetSocketAddress from) {
        this.from = from;
    }

    public int getPinCode() {
        return pinCode;
    }

    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }
}
