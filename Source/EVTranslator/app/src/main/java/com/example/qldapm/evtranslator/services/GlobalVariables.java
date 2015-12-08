package com.example.qldapm.evtranslator.services;

import com.example.qldapm.evtranslator.DB_EV;
import com.example.qldapm.evtranslator.R;

import java.io.InputStream;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;


/**
 * Created by Thanh Tung on 22/11/2015.
 */
public class GlobalVariables {
    private DB_EV db_ev;
    private LexicalizedParser parser;
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
    public LexicalizedParser getParser(){return this.parser; }
    public void setParser(){
        this.parser = LexicalizedParser.loadModel();
    }
    public DB_EV getDb_ev() {
        return db_ev;
    }
    public void setDb_ev(DB_EV db_ev) {
        this.db_ev = db_ev;
    }
}
