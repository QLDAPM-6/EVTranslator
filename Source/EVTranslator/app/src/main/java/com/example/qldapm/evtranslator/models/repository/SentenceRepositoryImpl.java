package com.example.qldapm.evtranslator.models.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.qldapm.evtranslator.models.database.EVTranslatorContract;
import com.example.qldapm.evtranslator.models.database.EVTranslatorDbHelper;
import com.example.qldapm.evtranslator.models.entity.Sentence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nhat Huy (ndnhuy) on 21/11/2015.
 */
public class SentenceRepositoryImpl implements SentenceRepository {

    private Context context;
    private EVTranslatorDbHelper dbHelper;

    public SentenceRepositoryImpl(Context context) {
        this.context = context;
        dbHelper = new EVTranslatorDbHelper(context);
    }

    @Override
    public Long add(Sentence sentence) {


        if (countByEnglishSentence(sentence.getEnglishSentence()) > 0 || sentence.getEnglishSentence().isEmpty()) {
            return new Long(-1);
        }

        ContentValues values = new ContentValues();

        values.put(EVTranslatorContract.Sentence.COLUMN_ENG_NAME, sentence.getEnglishSentence());
        values.put(EVTranslatorContract.Sentence.COLUMN_VIE_NAME, sentence.getVietnameseSentence());

        return dbHelper.getWritableDatabase().insert(
                EVTranslatorContract.Sentence.TABLE_NAME,
                null,
                values
        );
    }

    @Override
    public List<Sentence> findAll() {
        Cursor c = dbHelper.getReadableDatabase().query(
                EVTranslatorContract.Sentence.TABLE_NAME,
                new String[] {
                        EVTranslatorContract.Sentence._ID,
                        EVTranslatorContract.Sentence.COLUMN_ENG_NAME,
                        EVTranslatorContract.Sentence.COLUMN_VIE_NAME
                },
                null,
                null,
                null,
                null,
                null
        );

       List<Sentence> sentences = new ArrayList<Sentence>();
        
        while (c.moveToNext()) {
            Sentence sentence = new Sentence();
            sentence.setId(c.getLong(c.getColumnIndex(EVTranslatorContract.Sentence._ID)));
            sentence.setEnglishSentence(c.getString(c.getColumnIndex(EVTranslatorContract.Sentence.COLUMN_ENG_NAME)));
            sentence.setVietnameseSentence(c.getString(c.getColumnIndex(EVTranslatorContract.Sentence.COLUMN_VIE_NAME)));

            sentences.add(sentence);
        }
        
        c.close();

        return sentences;
    }

    @Override
    public HashMap<String, String> findAllAndPutIntoMap() {
        Cursor c = dbHelper.getReadableDatabase().query(
                EVTranslatorContract.Sentence.TABLE_NAME,
                new String[] {
                        EVTranslatorContract.Sentence._ID,
                        EVTranslatorContract.Sentence.COLUMN_ENG_NAME,
                        EVTranslatorContract.Sentence.COLUMN_VIE_NAME
                },
                null,
                null,
                null,
                null,
                null
        );

        HashMap<String, String> sentencesMap = new HashMap<String, String>();

        while (c.moveToNext()) {
            Sentence sentence = new Sentence();
            sentence.setId(c.getLong(c.getColumnIndex(EVTranslatorContract.Sentence._ID)));
            sentence.setEnglishSentence(c.getString(c.getColumnIndex(EVTranslatorContract.Sentence.COLUMN_ENG_NAME)));
            sentence.setVietnameseSentence(c.getString(c.getColumnIndex(EVTranslatorContract.Sentence.COLUMN_VIE_NAME)));

            sentencesMap.put(sentence.getEnglishSentence(), sentence.getVietnameseSentence());
        }

        c.close();

        return sentencesMap;
    }

    @Override
    public void delete(Long id) {
        dbHelper.getWritableDatabase().delete(
                EVTranslatorContract.Sentence.TABLE_NAME,
                EVTranslatorContract.Sentence._ID + " = ?",
                new String[] {String.valueOf(id)}
        );
    }

    public Integer countByEnglishSentence(String english) {
        Cursor c = dbHelper.getReadableDatabase().query(
                EVTranslatorContract.Sentence.TABLE_NAME,
                new String[] {
                        EVTranslatorContract.Sentence._ID,
                        EVTranslatorContract.Sentence.COLUMN_ENG_NAME,
                        EVTranslatorContract.Sentence.COLUMN_VIE_NAME
                },
                EVTranslatorContract.Sentence.COLUMN_ENG_NAME + " = ?",
                new String[] {english},
                null,
                null,
                null
        );

        return c.getCount();
    }
}
