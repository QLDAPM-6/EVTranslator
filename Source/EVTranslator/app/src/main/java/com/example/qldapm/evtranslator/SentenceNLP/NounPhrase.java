package com.example.qldapm.evtranslator.SentenceNLP;

import com.example.qldapm.evtranslator.EWord;

import java.util.ArrayList;

/**
 * Created by Tung on 25/11/2015.
 */
public class NounPhrase extends Sequence {
   public NounPhrase(){
       this.type = "NP";
   }
    public String translate(){
        String res = "";
        for(int i = eWords.size()-1; i > -1; i--){
            if(eWords.get(i).getPosTag().equals("DT")){
               continue;
            }
            res += eWords.get(i).getVnMeaning() + " ";
        }
        return res;
    }

}
