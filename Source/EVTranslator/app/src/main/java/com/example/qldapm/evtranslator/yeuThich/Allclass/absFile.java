package com.example.qldapm.evtranslator.yeuThich.Allclass;

/**
 * Created by vanty on 11/1/2015.
 */
public abstract class absFile {
    private String _name;
    public String get_name()
    {
        return _name;
    }
    public void set_name(String name)
    {
        _name = name;
    }
    public String getNgaySave() {
        return ngaySave;
    }

    public void setNgaySave(String ngaySave) {
        this.ngaySave = ngaySave;
    }

    private String ngaySave;
    public abstract String getType();
}
