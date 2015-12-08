package com.example.qldapm.evtranslator.SentenceNLP;

/**
 * Created by Tung on 07/12/2015.
 */
public class Affirmative extends VerbPhrase {
    public String translate(){
        String res = "";
        int countEWords = this.countEWord();
        for(int i = 0; i < countEWords; i++){
            res += eWords.get(i).getVnMeaning() + " ";
        }
        return res;
    }
}
