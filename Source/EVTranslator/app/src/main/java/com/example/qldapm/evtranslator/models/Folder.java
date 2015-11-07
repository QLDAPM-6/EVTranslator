package com.example.qldapm.evtranslator.models;

import com.example.qldapm.evtranslator.models.absFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vanty on 11/1/2015.
 */
public class Folder extends absFile {
    public List<absFile>Listchild;
    public Folder()
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
