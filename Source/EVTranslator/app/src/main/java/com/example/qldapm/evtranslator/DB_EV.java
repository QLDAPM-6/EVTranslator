package com.example.qldapm.evtranslator;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import opennlp.tools.util.ext.ExtensionNotLoadedException;

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

    public WORD getWord(String enterword){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorWord = db.query(WORD.TABLE_NAME, new String[]{WORD.COL_ID,
                        WORD.COL_WORD}, WORD.COL_WORD + "=?",
                new String[]{enterword.toLowerCase()}, null, null, null, null);
        if(cursorWord !=null) {
            cursorWord.moveToNext();

            int id = Integer.parseInt(cursorWord.getString(0));
            String word = cursorWord.getString(1);
            WORD res = new WORD(id, word);
            return res;
        }
        return null;
    }

    public void getAllELMeaning(WORD word, ArrayList<ELMEANING> allelmeaning){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursorELMEANING = db.query(ELMEANING.TABLE_NAME, new String[]{ELMEANING.COL_ID,
                        ELMEANING.COL_ID_WORD, ELMEANING.COL_MEANING, ELMEANING.COL_TYPE}, ELMEANING.COL_ID_WORD + "=?",
                new String[]{String.valueOf(word.id)}, null, null, null, null);
        if (cursorELMEANING.moveToFirst()) {
            do {

                //int col_id_postag = Integer.parseInt(cursorELMEANING.getString(2));
                String meaning = cursorELMEANING.getString(2);
                if(meaning.toLowerCase().startsWith("xem $") == true){
                    String newWord = meaning.toLowerCase().substring(5);
                    WORD recursiveWord = getWord(newWord);
                    getAllELMeaning(recursiveWord,allelmeaning);
                }else {
                    int id = Integer.parseInt(cursorELMEANING.getString(0));
                    int col_id_word = Integer.parseInt(cursorELMEANING.getString(1));
                    String type = cursorELMEANING.getString(3);
                    ELMEANING temp = new ELMEANING(id,meaning,col_id_word,0,type );
                    allelmeaning.add(temp);
                }
            } while (cursorELMEANING.moveToNext());
        }

    }



    public void addMeaming_OpenNLPWord(OpenNLPWord openNLPWord) {
        WORD word = getWord(openNLPWord.getWord());
        if(word != null){
            ArrayList<ELMEANING> allElMeaning = new ArrayList<>();
            getAllELMeaning(word,allElMeaning);
            String vnMeaning = chooseProperMeaning(allElMeaning, openNLPWord.getPosTag());
            openNLPWord.setVnMeaning(vnMeaning);
        }

    }


    private String chooseProperMeaning(ArrayList<ELMEANING> list, String postag ){
        String res = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String meaning;
        Cursor cursorPOSTAG = db.query(POSTAG.TABLE_NAME, new String[] { POSTAG.COL_MEANING,
                       }, POSTAG.COL_INIT + "=?",
                new String[] { postag }, null, null, null, null);
        if (cursorPOSTAG != null) {
            cursorPOSTAG.moveToFirst();
            meaning = cursorPOSTAG.getString(0);
            String[] types = meaning.split("_");
            boolean flag = false;
            for(ELMEANING elemeaning: list){

                for(String type : types){
                    if(type.equals(elemeaning.getType())){
                        res = elemeaning.getMeaning();
                        flag = true;
                        break;
                    }

                }
                if(flag)
                    break;
            }
        }
        return res;
    }

}

class WORD {
    public int id;
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

     public String getType(){return type;}
     public String getMeaning(){return meaning;}

    public ELMEANING(int ID, String Meaning, int ID_WORD, int ID_POSTAG, String Type){
        this.id = ID;
        this.meaning = Meaning;
        this.id_word = ID_WORD;
        this.id_postag = ID_POSTAG;
        this.type = Type;
    }
}

class POSTAG{
    public static final String TABLE_NAME = "POSTAG";
    public static final String COL_ID = "ID";
    public static final String COL_INIT = "Init";
    public static final String COL_MEANING = "Meaning";
    int id;
    String meaning;
    int init;


    public POSTAG(int ID, String Meaning, int Init){
        this.id = ID;
        this.meaning = Meaning;
       this.init = Init;

    }
}

