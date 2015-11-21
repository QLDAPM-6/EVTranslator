package com.example.qldapm.evtranslator.models.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nhat Huy (ndnhuy) on 21/11/2015.
 */
public class EVTranslatorDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "evtrans.db";

    public EVTranslatorDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SENTENCE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_SENTENCE_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = " ,";
    private static final String SQL_CREATE_SENTENCE_TABLE = "CREATE TABLE " + EVTranslatorContract.Sentence.TABLE_NAME + " (" +
            EVTranslatorContract.Sentence._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
            EVTranslatorContract.Sentence.COLUMN_ENG_NAME + TEXT_TYPE + " NOT NULL UNIQUE" + COMMA_SEP +
            EVTranslatorContract.Sentence.COLUMN_VIE_NAME + TEXT_TYPE + " NOT NULL" +
            ")";
    private static final String SQL_DROP_SENTENCE_TABLE =
            "DROP TABLE IF EXISTS " + EVTranslatorContract.Sentence.TABLE_NAME;
}
