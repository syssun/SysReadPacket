package com.sys.readh.adapter.items;

public class HomeGrid extends  BaseItem{
    private Integer id;
    private Integer iamge;
    private String title;
    private String ctl;
    private String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    private String baseUrl;

    public String getCtl() {
        return ctl;
    }

    public void setCtl(String ctl) {
        this.ctl = ctl;
    }

    public String getBaseUrl() {
        return baseUrl="http://"+getIp()+":8080/ctl/";
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public HomeGrid(){

    }
    public HomeGrid(Integer image,String title,String ctl){
        this.iamge = image;
        this.title = title;
        this.ctl = ctl;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIamge() {
        return iamge;
    }

    public void setIamge(Integer iamge) {
        this.iamge = iamge;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
