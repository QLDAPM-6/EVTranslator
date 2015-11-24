package com.example.qldapm.evtranslator.models.entity;

/**
 * Created by vanty on 11/1/2015.
 */
public abstract class absFile {
    private String id = "";
    private String _name;
    public String get_name()
    {
        return _name;
    }
    public void set_name(String name)
    {
        _name = name;
    }


    private String Thuoctinhbosung;
    public abstract String getType();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThuoctinhbosung() {
        return Thuoctinhbosung;
    }

    public void setThuoctinhbosung(String thuoctinhbosung) {
        Thuoctinhbosung = thuoctinhbosung;
    }
}
