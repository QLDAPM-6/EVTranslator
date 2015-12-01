package com.example.qldapm.evtranslator.services;

import com.example.qldapm.evtranslator.OpenNLPWord;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Tung on 25/11/2015.
 */
public class Sentence {
    private int countOpenNLPWord ;
    public Tense tense;
    public ArrayList<ComponentInSentence> components;
    public ArrayList<OpenNLPWord> openNLPWords_list;
    public void setOpenNLPWords_list(ArrayList<OpenNLPWord> opennlpwords_list){
        this.openNLPWords_list = opennlpwords_list;
        countOpenNLPWord = this.openNLPWords_list.size();
    }
    public void classifyGroupComponent(){
        NounFunction nounFunction = new NounFunction();
        Negative negative = new Negative();
        Verb verb = new Verb();
        DirectObject directObject = new DirectObject();
        IndirectObject indirectObject = new IndirectObject();
    }

    public void arrangeComponents(){

    }

    public void arrageNegativeForm(){
        String auxiliaryverbs = "doesdidhashavehadisamarewaswerebeingbeenmaymustmightshouldcouldwouldshallwillcan";
        for(int i = 0; i < countOpenNLPWord; i++ ){
            //if(openNLPWords_list.get(i).getPosTag().equals("DT") && i + 1 <countOpenNLPWord && openNLPWords_list.get(i+1).getPosTag().equals("NNS") )
            {
                //if((openNLPWords_list.get(i-1).getPosTag().equals("NN") || openNLPWords_list.get(i-1).getPosTag().equals("NNS")) && i - 1> -1 ){
                //    Collections.swap(openNLPWords_list,i, i-1);
                //}
            }
            if(openNLPWords_list.get(i).getPosTag().equals("RB")){
                   if(openNLPWords_list.get(i-1).getPosTag().equals("MD") )
                     Collections.swap(openNLPWords_list,i, i-1);
                   else{
                       openNLPWords_list.remove(i-1) ;
                       countOpenNLPWord = this.openNLPWords_list.size();
                       i = i - 1;
                   }
            }



        }
    }

    public String printVnMeaning(){
        String vnSentence= "";
        for(OpenNLPWord item : openNLPWords_list){
            item.randomVnMeaing();
            vnSentence+= item.getVnMeaning()+ " " ;
            // +item.getVnMeaning() + "|POS_" + item.getPosTag()
        }
        return vnSentence;
    }
}