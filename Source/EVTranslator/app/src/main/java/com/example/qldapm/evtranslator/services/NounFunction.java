package com.example.qldapm.evtranslator.services;

import com.example.qldapm.evtranslator.OpenNLPWord;

import java.util.ArrayList;

/**
 * Created by Tung on 25/11/2015.
 */
public class NounFunction extends ComponentInSentence {
   public NounFunction(){
       this.type = "NF";
       this.listofwords = new ArrayList<OpenNLPWord>();
   }
}
