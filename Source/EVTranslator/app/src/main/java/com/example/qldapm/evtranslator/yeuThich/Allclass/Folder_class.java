package com.example.qldapm.evtranslator.yeuThich.Allclass;

import com.example.qldapm.evtranslator.yeuThich.Allclass.absFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vanty on 11/1/2015.
 */
public class Folder_class extends absFile {
    public List<absFile>Listchild;
    public Folder_class()
    {
        Listchild = new ArrayList<>();
    }
    public void addChid(absFile a)
    {
        Listchild.add(a);
    }
    public void removeChild(int index)
    {
        Listchild.remove(index);
    }
    public List<absFile> getListchild()
    {
        return  Listchild;
    }

    @Override
    public String getType() {
        return "Folder";
    }
}
