package com.example.qldapm.evtranslator.services;

import com.example.qldapm.evtranslator.OpenNLPWord;

import java.util.ArrayList;

/**
 * Created by Tung on 25/11/2015.
 */
public class Tense {
    public String mainVerb;
    public String posVerb;
    ArrayList<OpenNLPWord> openNLPWords_list;
    public void setOpenNLPWords_list(ArrayList<OpenNLPWord> opennlpwords_list){
        this.openNLPWords_list = opennlpwords_list;
    }
    public OpenNLPWord findMainVerb(){
        for(OpenNLPWord item : openNLPWords_list){
            if(item.equals("VBD") || item.equals("VBG") || item.equals("VBN") || item.equals("VBP") || item.equals("VBZ")){
                return item;
            }
        }
        return null;
    }
    public void ommitUnnecessaryWord(){
            
    }
}
