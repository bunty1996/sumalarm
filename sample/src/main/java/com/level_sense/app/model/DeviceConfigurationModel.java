package com.level_sense.app.model;

public class DeviceConfigurationModel {


    public String getSentry_offset() {
        return sentry_offset;
    }

    public void setSentry_offset(String sentry_offset) {
        this.sentry_offset = sentry_offset;
    }

    public String getSentry_offset_op() {
        return sentry_offset_op;
    }

    public void setSentry_offset_op(String sentry_offset_op) {
        this.sentry_offset_op = sentry_offset_op;
    }

    public String getSentry_offset_unit() {
        return sentry_offset_unit;
    }

    public void setSentry_offset_unit(String sentry_offset_unit) {
        this.sentry_offset_unit = sentry_offset_unit;
    }

    public String getTemp_delay_status() {
        return temp_delay_status;
    }

    public void setTemp_delay_status(String temp_delay_status) {
        this.temp_delay_status = temp_delay_status;
    }

    public String getTemp_delay() {
        return temp_delay;
    }

    public void setTemp_delay(String temp_delay) {
        this.temp_delay = temp_delay;
    }

    public String getRh_delay_status() {
        return rh_delay_status;
    }

    public void setRh_delay_status(String rh_delay_status) {
        this.rh_delay_status = rh_delay_status;
    }

    public String getRh_delay() {
        return rh_delay;
    }

    public void setRh_delay(String rh_delay) {
        this.rh_delay = rh_delay;
    }

    private String sentry_offset;
    private String sentry_offset_op;
    private String sentry_offset_unit;
    private String temp_delay_status;
    private String temp_delay;
    private String rh_delay_status;
    private String rh_delay;

}
