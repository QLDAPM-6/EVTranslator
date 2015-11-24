package com.example.qldapm.evtranslator.models.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.qldapm.evtranslator.models.entity.FavoriteObject;
import com.example.qldapm.evtranslator.models.entity.Folder;
import com.example.qldapm.evtranslator.models.entity.absFile;
import com.example.qldapm.evtranslator.services.Managerfavorite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vanty on 11/21/2015.
 */
public class EVTranslatorDbFavorite {
    public EVTranslatorDbFavorite()
    {
    }
    public List<absFile> getFolder()
    {
       SQLiteDatabase db =  Managerfavorite.getIntance().Getdatabase().getReadableDatabase();
        ArrayList<absFile> Danhsach = null;
        try {

            String query  = "select * from " + EVTranslatorFavorite.TABLE_NAME;
            //SQLiteDatabase db    = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor  = db.rawQuery(query, null);

            // go over each row, build elements and add it to list
            Danhsach = new ArrayList<>();
            if(cursor.moveToFirst())
            {
                do{
                    absFile temp = new Folder();
                    temp.setId(cursor.getString(0));
                    temp.set_name(cursor.getString(1));
                    temp.setThuoctinhbosung(cursor.getString(2));
                    Danhsach.add(temp);
                }
                while (cursor.moveToNext());
                cursor.close();
            }
        } catch(Exception e) {
            // sql error
            Log.d("loi", e.getMessage());
        }

        return Danhsach;
    }
    public long SaveFolder(String body,String date)
    {
        SQLiteDatabase db =  Managerfavorite.getIntance().Getdatabase().getWritableDatabase();
        try
        {
            //String query  = "insert into " + EVTranslatorFavorite.TABLE_NAME +"(" + EVTranslatorFavorite.BODY +"," + EVTranslatorFavorite.DATECREATE +") values('" + body +"','" + date + "')";
            //db.execSQL(query);
            ContentValues values = new ContentValues();
            values.put(EVTranslatorFavorite.BODY,body);
            values.put(EVTranslatorFavorite.DATECREATE,date);
            return db.insert(EVTranslatorFavorite.TABLE_NAME,null,values);
        }
        catch (Exception e){Log.d("loi", e.getMessage());
            return -1;
        }
    }
    public boolean Removefolder(String body)
    {
        SQLiteDatabase db =  Managerfavorite.getIntance().Getdatabase().getWritableDatabase();
        try
        {
            String query = "delete from" + " " + EVTranslatorFavorite.TABLE_NAME + " "+ "where " + EVTranslatorFavorite.BODY +"='" + body + "'";
            db.execSQL(query);
            return true;
        }
        catch (Exception e){Log.d("loi", e.getMessage());
            return false;
        }
    }
    public boolean Renamefolder(String id,absFile a)
    {
        SQLiteDatabase db =  Managerfavorite.getIntance().Getdatabase().getWritableDatabase();
        try
        {
            String query = "update" + " " + EVTranslatorFavorite.TABLE_NAME + " set "+ EVTranslatorFavorite.BODY + "= '" + a.get_name() + "' where " + EVTranslatorFavorite.ID +"='" + id + "'";
            db.execSQL(query);
            return true;
        }
        catch (Exception e){Log.d("loi", e.getMessage());
            return false;
        }
    }
    // Xu li Favorite
    public boolean Themfavorite(FavoriteObject favorite)
    {
        SQLiteDatabase db =  Managerfavorite.getIntance().Getdatabase().getWritableDatabase();
        try
        {
            //String query  = "insert into " + EVTranslatorFavorite.TABLE_NAME_FAVORITE +"(" + EVTranslatorFavorite.BODY +"," + EVTranslatorFavorite.DATECREATE +") values('" + body +"','" + date + "')";
           // db.execSQL(query);
            ContentValues value = new ContentValues();
            value.put(EVTranslatorFavorite.English,favorite.get_name());
            value.put(EVTranslatorFavorite.VN,favorite.getThuoctinhbosung());
            value.put(EVTranslatorFavorite.IDFolder,favorite.getID_folder());
            db.insert(EVTranslatorFavorite.TABLE_NAME_FAVORITE, null, value);
            return true;
        }
        catch (Exception e){Log.d("loi", e.getMessage());
            return false;
        }
    }
    public List<absFile> getFavorite(String idfolder)
    {
        SQLiteDatabase db =  Managerfavorite.getIntance().Getdatabase().getReadableDatabase();
        ArrayList<absFile> Danhsach = null;
        try {

            String query  = "select * from " + EVTranslatorFavorite.TABLE_NAME_FAVORITE + " where " + EVTranslatorFavorite.IDFolder +
                    " = '" + idfolder + "'";
            //SQLiteDatabase db    = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor  = db.rawQuery(query, null);

            // go over each row, build elements and add it to list
            Danhsach = new ArrayList<>();
            if(cursor.moveToFirst())
            {
                do{
                    FavoriteObject temp = new FavoriteObject();
                    temp.setId(cursor.getString(0));
                    temp.set_name(cursor.getString(1));
                    temp.setThuoctinhbosung(cursor.getString(2));
                    temp.setID_folder(cursor.getString(3));
                    Danhsach.add(temp);
                }
                while (cursor.moveToNext());
                cursor.close();
            }
        } catch(Exception e) {
            // sql error
            Log.d("loi", e.getMessage());
        }

        return Danhsach;
    }
    public boolean Removefavorite(String id)
    {
        SQLiteDatabase db =  Managerfavorite.getIntance().Getdatabase().getWritableDatabase();
        try
        {
            String query = "delete from" + " " + EVTranslatorFavorite.TABLE_NAME_FAVORITE + " "+ "where " + EVTranslatorFavorite.ID +"='" + id + "'";
            db.execSQL(query);
            return true;
        }
        catch (Exception e){Log.d("loi", e.getMessage());
            return false;
        }
    }
}
