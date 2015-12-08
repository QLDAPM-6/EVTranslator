package com.example.qldapm.evtranslator.SentenceNLP;

import com.example.qldapm.evtranslator.EWord;

import java.util.ArrayList;

import edu.stanford.nlp.trees.Tree;

/**
 * Created by Tung on 25/11/2015.
 */
public class  Sequence {
    protected ArrayList<EWord> eWords;
    protected String type;
    protected String sequencesStr;
    public void seteWords(ArrayList<EWord> e){
        this.eWords = e;
    }
    public void setListofwords(String sequencesStr){
        this.sequencesStr = sequencesStr;
    }
    public String getSequencesStr(){return this.sequencesStr;}
    public String translate(){
        String res = "";
        for(EWord e: eWords){
            if(!e.getPosTag().equals("TO")){
                res += e.getVnMeaning() + " ";
            }
        }
        return res;
    }

    public int countEWord(){return this.eWords.size();}
}
