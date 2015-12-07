package com.example.qldapm.evtranslator.services;

import com.example.qldapm.evtranslator.DB_EV;
import com.example.qldapm.evtranslator.R;

import java.io.InputStream;

import opennlp.tools.postag.POSModel;

/**
 * Created by Thanh Tung on 22/11/2015.
 */
public class GlobalVariables {

    private InputStream file_en_ner_person;
    private InputStream file_enparser_chunking;
    private InputStream file_en_pos_maxent;
    private InputStream file_entoken;
    private POSModel model;
    private DB_EV db_ev;

    private static GlobalVariables instance;

    public GlobalVariables() {
        return;
    }

    public static GlobalVariables getInstance() {
        if(instance == null) {
            instance = new GlobalVariables();
        }
        return instance;
    }

    public InputStream getFile_en_ner_person() {
        return file_en_ner_person;
    }

    public void setFile_en_ner_person(InputStream file_en_ner_person) {
        this.file_en_ner_person = file_en_ner_person;
    }

    public InputStream getFile_enparser_chunking() {
        return file_enparser_chunking;
    }

    public void setFile_enparser_chunking(InputStream file_enparser_chunking) {
        this.file_enparser_chunking = file_enparser_chunking;
    }

    public DB_EV getDb_ev() {
        return db_ev;
    }

    public void setDb_ev(DB_EV db_ev) {
        this.db_ev = db_ev;
    }

    public InputStream getFile_en_pos_maxent() {
        return file_en_pos_maxent;
    }

    public void setFile_en_pos_maxent(InputStream file_en_pos_maxent) {
        this.file_en_pos_maxent = file_en_pos_maxent;
    }

    public void setFile_entoken(InputStream file_entoken){
        this.file_entoken = file_entoken;
    }
    public InputStream getFile_entoken(){
        return this.file_entoken;
    }

    public POSModel getModel() {
        return model;
    }

    public void setModel(POSModel model) {
        this.model = model;
    }
}
