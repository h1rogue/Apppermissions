package com.example.a91910.apppermissions;

public class Upload {

    private String mname;
    private String mimageUrl;

    public Upload(){
        ///////////
    };

    public Upload(String mname, String mimageUrl) {
        this.mname = mname;
        this.mimageUrl = mimageUrl;
    }

    public String getMname() {
        return mname;
    }

    public String getMimageUrl() {
        return mimageUrl;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public void setMimageUrl(String mimageUrl) {
        this.mimageUrl = mimageUrl;
    }
}
