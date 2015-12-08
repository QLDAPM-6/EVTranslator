package com.example.qldapm.evtranslator.SentenceNLP;

import java.util.Collections;

/**
 * Created by Tung on 07/12/2015.
 */
public class Negative extends VerbPhrase {
    public String translate(){
        arange();
        String res = "";
        int countEWords = this.countEWord();
        for(int i = 0; i < countEWords; i++){
            res += eWords.get(i).getVnMeaning() + " ";
        }
        return res;
    }

    private void arange(){
        int countOpenNLPWord = this.countEWord();
        for(int i = 0; i < countOpenNLPWord; i++ ){
                if(eWords.get(i).getPosTag().equals("MD") ){
                  //  Collections.swap(eWords, i, i + 1);
                    return;
                }
                else{
                    eWords.remove(i) ;
                    return;
                }
        }
    }
}
