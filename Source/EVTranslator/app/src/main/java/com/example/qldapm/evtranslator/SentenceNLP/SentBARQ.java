package com.example.qldapm.evtranslator.SentenceNLP;

import com.example.qldapm.evtranslator.EWord;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Tung on 07/12/2015.
 */
public class SentBARQ extends Sentence  {
    public SentBARQ(ArrayList<EWord> eWords){
        super(eWords);
        this.Sent_TYPE = "SBARQ";
    }

    protected void reverseEWords(){
        int count = this.eWords.size();
        for(int i = 0; i < count - 2; i++){
            Collections.swap(eWords, i, i + 1);
        }
    }

    public void arrageSequence(){
        reverseEWords();
        int start_i = arrageNegative();
        this.classifySequence(start_i);
    }

    /*protected int arrageNegative(){
        if(checkNot_VP(this.eWords) || this.eWords.get(0).getPosTag().equals("VBZ")){
            int count = this.eWords.size();
            this.eWords.get(0).labelParent = "VP";
            for(int i = 1; i < count; i++){
                if(this.eWords.get(i).labelParent.equals("NP"))
                    Collections.swap(eWords, i-1, i);
                else{
                    break;
                }
            }
            return 0;
        }
        return 1;
    }*/
}
