package com.example.qldapm.evtranslator.yeuThich;

import com.example.qldapm.evtranslator.yeuThich.Allclass.absFile;

import java.util.List;

/**
 * Created by vanty on 11/1/2015.
 */
public class Managerfavorite {
    private static Managerfavorite manager_Favorite;
    public List<absFile>ListFolder;
    public List<absFile>Listchid;
    public static Managerfavorite getIntands()
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
