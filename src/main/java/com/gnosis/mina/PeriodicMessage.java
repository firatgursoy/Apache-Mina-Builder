package com.gnosis.mina;

/**
 * Created by Firat on 22.10.2014.
 */
public class PeriodicMessage {
    private long period;
    private Object message;

    public PeriodicMessage(Object message, long period) {
        this.message = message;
        this.period = period;
    }

    public long getPeriod() {

        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
