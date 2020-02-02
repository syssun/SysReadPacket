package com.sys.readh.adapter.items;

import com.alibaba.fastjson.JSONObject;

public class InfoGrid {
    private String code;
    private String label;
    private String value;
    private Integer icon;
    private boolean flag = false;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isFlag() {
        return flag;
    }

    public InfoGrid(String code, String label, String value) {
        this.label = label;
        this.value = value;
        this.code = code ;

    }
    public InfoGrid(String code, String label, String value, boolean flag) {
        this.label = label;
        this.value = value;
        this.flag = flag ;
        this.code = code ;

    }
    public InfoGrid(String code, String label, String value, Integer icon, boolean flag) {
        this.label = label;
        this.value = value;
        this.icon = icon;
        this.flag = flag;
        this.code = code ;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
