package com.example.qldapm.evtranslator;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tung on 08/11/2015.
 */
public class DB_EV extends SQLiteOpenHelper{

    public static final String DB_NAME  = "en_vn.db";

    public DB_EV(Context context){
        super(context,DB_NAME, null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public OpenNLPWord getOpenNLWord(OpenNLPWord openNLPWord) {
        ArrayList<ELMEANING> abc;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorWord = db.query(WORD.TABLE_NAME, new String[] { WORD.COL_ID,
                        WORD.COL_WORD }, WORD.COL_WORD + "=?",
                new String[] { openNLPWord.getWord().toLowerCase() }, null, null, null, null);
        if (cursorWord != null) {
            cursorWord.moveToFirst();
            int ID_WORD = Integer.parseInt(cursorWord.getString(0));
            abc = getElMeaning(ID_WORD);
            if(abc != null){

            }
        }

        return openNLPWord;
    }

    // get all of the meaning of a word which has ID_WORD equal to ID_WORD parameter from the ELEMEANING Table
    public ArrayList<ELMEANING> getElMeaning(int ID_WORD){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursorELMEANING = db.query(ELMEANING.TABLE_NAME, new String[]{ELMEANING.COL_ID,
                        ELMEANING.COL_ID_WORD, ELMEANING.COL_MEANING, ELMEANING.COL_TYPE}, ELMEANING.COL_ID_WORD + "=?",
                new String[]{String.valueOf(ID_WORD)}, null, null, null, null);
        ArrayList<ELMEANING> res = null;
        if (cursorELMEANING.moveToFirst()) {
            res = new ArrayList<>();
            do {
                int id = Integer.parseInt(cursorELMEANING.getString(0));
                int col_id_word = Integer.parseInt(cursorELMEANING.getString(1));
                //int col_id_postag = Integer.parseInt(cursorELMEANING.getString(2));
                String meaning = cursorELMEANING.getString(2);
                String type = cursorELMEANING.getString(3);
                ELMEANING temp = new ELMEANING(id,meaning,col_id_word,0,type );
                res.add(temp);
            } while (cursorELMEANING.moveToNext());
        }
        return res;
    }

    private String mapPOS_Type(ArrayList<ELMEANING> list, String postag ){
        String res = "";
        

        return res;
    }

}

class WORD {
    int id;
    public  String word;
    public WORD(int ID, String Word){
        this.id = ID;
        this.word = Word;

    }
    public static final String TABLE_NAME = "WORD";
    public static final String COL_ID = "ID";
    public static final String COL_WORD = "Word";
}

 class ELMEANING {

    public static final String TABLE_NAME = "ELMEANING";
    public static final String COL_ID = "ID";
    public static final String COL_MEANING = "Meaning";
    public static final String COL_ID_WORD = "ID_WORD";
    public static final String COL_ID_POSTAG = "ID_POSTAG";
    public static final String COL_TYPE = "Type";


    int id;
    String meaning;
    int id_word;
    int id_postag;
    String type;

    public ELMEANING(int ID, String Meaning, int ID_WORD, int ID_POSTAG, String Type){
        this.id = ID;
        this.meaning = Meaning;
        this.id_word = ID_WORD;
        this.id_postag = ID_POSTAG;
        this.type = Type;
    }
}

