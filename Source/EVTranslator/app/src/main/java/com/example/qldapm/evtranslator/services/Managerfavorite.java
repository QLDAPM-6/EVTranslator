package com.example.qldapm.evtranslator.services;

import com.example.qldapm.evtranslator.models.entity.FavoriteObject;
import com.example.qldapm.evtranslator.models.entity.Folder;
import com.example.qldapm.evtranslator.models.entity.absFile;
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
    private static EVTranslatorDbHelper db;
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
        if(db == null)
            db = database;
    }
    public EVTranslatorDbHelper Getdatabase()
    {
        return db;
    }

    public long Addfolder(String value)
    {
        absFile temp = new Folder();
        temp.set_name(value);
        long id = Managerfavorite.getIntance().dbprocess.SaveFolder(value," ");
        temp.setId(String.valueOf(id));
        Managerfavorite.getIntance().addChild(temp);// Them folder
        return id;
    }
    public boolean AddFavorite(FavoriteObject favorite)
    {
        return Managerfavorite.getIntance().dbprocess.Themfavorite(favorite);
    }
}
