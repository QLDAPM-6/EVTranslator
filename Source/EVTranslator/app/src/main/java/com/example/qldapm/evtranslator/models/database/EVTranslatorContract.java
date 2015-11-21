package com.example.qldapm.evtranslator.models.database;

import android.provider.BaseColumns;

/**
 * Created by Nhat Huy (ndnhuy) on 21/11/2015.
 */
public class EVTranslatorContract {
    public EVTranslatorContract() {}

    public static final class Sentence implements BaseColumns {

        public static final String TABLE_NAME = "sentence";
        public static final String COLUMN_ENG_NAME = "english_sentence";
        public static final String COLUMN_VIE_NAME = "vietnamese_sentence";
    }
}
