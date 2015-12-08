package com.example.qldapm.evtranslator.SentenceNLP;

import com.example.qldapm.evtranslator.EWord;

import java.util.ArrayList;

/**
 * Created by Tung on 25/11/2015.
 */
public class Tense {
    public String mainVerb;
    public String posVerb;
    ArrayList<EWord> words_list;
    public void setWords_list(ArrayList<EWord> opennlpwords_list){
        this.words_list = opennlpwords_list;
    }
    public EWord findMainVerb(){
        for(EWord item : words_list){
            if(item.equals("VBD") || item.equals("VBG") || item.equals("VBN") || item.equals("VBP") || item.equals("VBZ")){
                return item;
            }
        }
        return null;
    }
}
