package com.example.qldapm.evtranslator.services;

import com.example.qldapm.evtranslator.OpenNLPWord;

import java.util.ArrayList;

/**
 * Created by Tung on 25/11/2015.
 */
public class IndirectObject extends ComponentInSentence {
    public IndirectObject(){
        this.type = "IO";
        this.listofwords = new ArrayList<OpenNLPWord>();
    }
}
