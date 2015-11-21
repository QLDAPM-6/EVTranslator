package com.example.qldapm.evtranslator.models;

import com.example.qldapm.evtranslator.models.absFile;

/**
 * Created by vanty on 10/23/2015.
 */
public class FavoriteObject extends absFile {
    @Override
    public String getType() {
        return "Favorite";
    }
    private String ID_folder;


    public String getID_folder() {
        return ID_folder;
    }

    public void setID_folder(String ID_folder) {
        this.ID_folder = ID_folder;
    }
}
