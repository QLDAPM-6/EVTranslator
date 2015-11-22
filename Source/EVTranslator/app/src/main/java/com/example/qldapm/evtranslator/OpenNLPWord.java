package com.example.qldapm.evtranslator;

import java.util.List;
import java.util.Random;

/**
 * Created by Tung on 08/11/2015.
 */
/*
public class WORD {
    int id;
    String word;
    public WORD(int ID, String Word){
        this.id = ID;
        this.word = Word;

    }
    public static String TABLE_NAME = "WORD";
    public static String COL_ID = "ID";
    public static String COL_WORD = "Word";
}*/

public class OpenNLPWord{
    private String word;
    private String posTag;
   // private String enMeaning;
    private String vnMeaning;
    public  OpenNLPWord(String word, String posTag, String vnMeaning){
        //this.enMeaning = enMeaning;
        this.word = word;
        this.posTag = posTag;
        this.vnMeaning = vnMeaning;
    }

    public String getWord(){return word;}
    public String getPosTag(){return posTag;}
    //public String getEnMeaning(){return enMeaning;}
    public String getVnMeaning(){return vnMeaning;}

    public void setWord(String word){this.word = word;}
    public void setPosTag(String posTag){this.posTag = posTag;}
   // public void setEnMeaning(String enMeaning){this.enMeaning = enMeaning;}
    public void setVnMeaning(String vnMeaning){
        this.vnMeaning = vnMeaning;

        }
    public void randomVnMeaing(){
        String[] list = this.vnMeaning.split(", ");
        int idx = new Random().nextInt(list.length);
        this.vnMeaning = list[idx];
        if(this.posTag.equals("VBD")){
            this.vnMeaning = "đã " + this.vnMeaning;
        }
    }
    public void originalWordForVBD(){
        if(!this.posTag.equals("VBD")){
            return;
        }
        String res = "";
        String subString = this.word.substring(0, this.word.length() - 2);
        char[] stringChars = subString.toCharArray();
        res = String.valueOf(stringChars);
        int  lengthSubString = subString.length();
        char endChar = stringChars[lengthSubString-1];
        String vowelString = "aeiou";
        if(endChar=='i'){
            stringChars[lengthSubString-1] = 'y';
            res = String.valueOf(stringChars);
        }else{
            if(stringChars[lengthSubString-1]=='y' && vowelString.indexOf(Character.toString(stringChars[lengthSubString-2])) != - 1){
                //res = String.valueOf(stringChars);
                return;
            }
            // omitting some special words , for instance: fix
            if(!res.equals("fix")){
                char previousEndChar = stringChars[lengthSubString-3];
                if(vowelString.indexOf(Character.toString(previousEndChar)) != - 1 && stringChars[lengthSubString-1] == stringChars[lengthSubString-2]){
                    stringChars[lengthSubString-1] = '\0';
                    res = String.valueOf(stringChars);
                }
            }
        }
        this.word = res;
    }
    public void orignalWordForVBDMore(){
        if(!this.posTag.equals("VBD")){
            return;
        }
        this.word += "e";
    }
}

