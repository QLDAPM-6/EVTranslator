package com.example.qldapm.evtranslator;

/**
 * Created by Tung on 07/11/2015.
 */


public class Word {
    private String word;
    private String posTag;
    private String enMeaning;
    private String vnMeaning;
    public Word(String word, String posTag, String enMeaningg){
        this.enMeaning = enMeaningg;
        this.posTag = posTag;
        this.word = word;
    }

    public String getWord(){
        return word;
    }
    public String getposTag(){
        return posTag;
    }
    public String getEnMeaning(){
        return enMeaning;
    }
    public String getVnMeaning(){return vnMeaning;}
    public void setWord(String word){
        this.word = word;
    }

    public void setPosTag(String posTag){
        this.posTag = word;
    }

    public void setEnMeaning(String enMeaning){
        this.enMeaning = enMeaning;
    }
    public void setVnMeaning(String vnMeaning){this.vnMeaning = vnMeaning;}
}
