package com.example.qldapm.evtranslator.services;

import com.example.qldapm.evtranslator.OpenNLPWord;

import java.util.ArrayList;

/**
 * Created by Tung on 25/11/2015.
 */
public class Negative extends ComponentInSentence {
    public Negative(){
        this.type = "NE";
        this.listofwords = new ArrayList<OpenNLPWord>();

    }
}
