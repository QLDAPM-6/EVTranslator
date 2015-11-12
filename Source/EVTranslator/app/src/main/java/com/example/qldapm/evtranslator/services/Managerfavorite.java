package com.example.qldapm.evtranslator.services;

import com.example.qldapm.evtranslator.models.absFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vanty on 11/1/2015.
 */
public class Managerfavorite {
    private static Managerfavorite manager_Favorite;
    public List<absFile>ListFolder;
    public List<absFile>Listchid;

    private Managerfavorite() {
        ListFolder = new ArrayList<absFile>();
        Listchid = new ArrayList<>();
    }

    public static Managerfavorite getIntance()
    {
        if(manager_Favorite == null)
        {
            manager_Favorite = new Managerfavorite();
        }
        return  manager_Favorite;
    }
    public void addChild(absFile a)
    {
        ListFolder.add(a);
    }
    public void removeChild(int index)
    {
        ListFolder.remove(index);
    }
}
