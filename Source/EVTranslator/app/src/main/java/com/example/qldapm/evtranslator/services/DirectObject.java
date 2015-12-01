package com.example.qldapm.evtranslator.services;

import com.example.qldapm.evtranslator.OpenNLPWord;

import java.util.ArrayList;

/**
 * Created by Tung on 25/11/2015.
 */
public class DirectObject extends ComponentInSentence {
    public DirectObject(){
        this.type ="O";
        this.listofwords = new ArrayList<OpenNLPWord >();
    }
}
