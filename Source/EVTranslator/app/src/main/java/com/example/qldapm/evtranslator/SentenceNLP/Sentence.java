package com.example.qldapm.evtranslator.SentenceNLP;

import android.provider.UserDictionary;

import com.example.qldapm.evtranslator.EWord;
import java.util.ArrayList;
import java.util.Collections;

import edu.stanford.nlp.trees.Tree;

/**
 * Created by Tung on 25/11/2015.
 */
public class Sentence {
    public String Sent_TYPE;
    protected ArrayList<String> listNounPhrase;
    protected ArrayList<Sequence> sequences;
    protected ArrayList<EWord> eWords;
    public Sentence(ArrayList<EWord> eWords){
        this.eWords = eWords;
        sequences = new ArrayList<>();
    }
    public void setListNounPhrase(ArrayList<String> e){this.listNounPhrase = e;}

    public void arrageSequence(){
        classifySequence(0);
    }

    public void classifySequence(int index){
        int countEWord = this.eWords.size();
        ArrayList<EWord> eWords_phrase = new ArrayList<>();
        for(int i = index; i < countEWord; i++){
            EWord startEword = this.eWords.get(i);
            if(startEword.labelParent.equals("NP")){
                eWords_phrase.add(this.eWords.get(i));
                String listPhrase = getListPhrase(eWords_phrase, "NP");
                //(i+1 < countEWord && !this.eWords.get(i+1).labelParent.equals("NP")) || i + 1 == countEWord
                if(comparePhrase(listPhrase)){
                    Sequence nounPhrase = new NounPhrase();
                    nounPhrase.seteWords(eWords_phrase);
                    sequences.add(nounPhrase);
                    eWords_phrase =  new ArrayList<EWord>();
                }
                continue;
            }else{
                if(startEword.labelParent.equals("VP")){
                    eWords_phrase.add(this.eWords.get(i));
                    if((i+1 < countEWord && !this.eWords.get(i+1).labelParent.equals("VP")) || i + 1 == countEWord){
                        if(checkNot_VP(eWords_phrase)){
                            Sequence verbPhrase = new Negative();
                            verbPhrase.seteWords(eWords_phrase);
                            sequences.add(verbPhrase);
                        }else{
                            Sequence verbPhrase = new Affirmative();
                            verbPhrase.seteWords(eWords_phrase);
                            sequences.add(verbPhrase);
                        }
                        eWords_phrase =  new ArrayList<EWord>();
                    }
                    continue;
                }
            }
            Sequence sequence = new Sequence();
            ArrayList<EWord> arraylisteword = new ArrayList<>();
            arraylisteword.add(this.eWords.get(i));
            sequence.seteWords(arraylisteword);
            sequences.add(sequence);
        }
    }

    protected int arrageNegative(){
        String ommitString = "doesdidhavehas";
        if(checkNot_VP(this.eWords) || !ommitString.startsWith(this.eWords.get(0).getWord())){
            int count = this.eWords.size();
            this.eWords.get(0).labelParent = "VP";
            ArrayList<EWord> eWords_phrase = new ArrayList<>();
            for(int i = 1; i < count; i++){
                if(this.eWords.get(i).labelParent.equals("NP")) {
                    eWords_phrase.add(this.eWords.get(i));
                    Collections.swap(eWords, i - 1, i);
                    String listPhrase = getListPhrase(eWords_phrase, "NP");
                    if(comparePhrase(listPhrase)){
                        break;
                    }
                }
                else{
                    break;
                }
            }
            return 0;
        }
        return 1;
    }

    public boolean comparePhrase(String abc){
        int count = this.listNounPhrase.size();
        for(int i = 0; i < count; i++){
            if(this.listNounPhrase.get(i).equals(abc))
                return true;
        }
        return false;
    }
    public String getListPhrase(ArrayList<EWord> eWords, String initPhrase){
        String res = "";
        //(NP (PRP$ your) (NN father))
        int count = eWords.size();
        for(int i = 0; i < count; i++){
            res += " "+eWords.get(i).getLeafString();
        }
        res = "(" + initPhrase + res + ")";
        return res;
    }
    public String traslate(){

        String res = "";
        for(Sequence seq: sequences){
            res += seq.translate();
        }
        return res;
    }
    public boolean checkNot_VP(ArrayList<EWord> eWords){
        int countEWords = eWords.size();
        for(int i = 0; i < countEWords; i++){
            if(eWords.get(i).getPosTag().equals("RB")){
                return true;
            }
        }
        return false;
    }
}