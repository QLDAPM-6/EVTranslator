package com.example.qldapm.evtranslator.services;

import com.example.qldapm.evtranslator.models.FavoriteObject;
import com.example.qldapm.evtranslator.models.Folder;
import com.example.qldapm.evtranslator.models.absFile;
import com.example.qldapm.evtranslator.models.database.EVTranslatorDbFavorite;
import com.example.qldapm.evtranslator.models.database.EVTranslatorDbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vanty on 11/1/2015.
 */
public class Managerfavorite {
    private static Managerfavorite manager_Favorite;
    //danh sach folder
    public List<absFile>ListFolder;
    // danh sach favorite;
    public List<absFile>Listchid;
    // favorite hien tai
    public FavoriteObject currentfavorite;
    // folder hien tai
    public Folder currentFolder;
    // database
    private EVTranslatorDbHelper db;
    //file xu li database
    public EVTranslatorDbFavorite dbprocess;
    //
    public boolean showFavorite;

    private Managerfavorite() {
        ListFolder = new ArrayList<absFile>();
        Listchid = new ArrayList<>();
        currentfavorite = new FavoriteObject();
        currentFolder = new Folder();
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
    //database
    public void Setdatabase(EVTranslatorDbHelper database)
    {
        db = database;
    }
    public EVTranslatorDbHelper Getdatabase()
    {
        return db;
    }
}
