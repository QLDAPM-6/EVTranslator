package com.example.qldapm.evtranslator;

import java.util.List;
import java.util.Random;

/**
 * Created by Tung on 08/11/2015.
 */



public class OpenNLPWord{
    private String word;
    private String posTag;
   // private String enMeaning;
    private String vnMeaning;
    private String tempword;
    public  OpenNLPWord(String word, String posTag, String vnMeaning){
        //this.enMeaning = enMeaning;
        this.word = word;
        this.tempword = word;
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
        if(vnMeaning == "")
            return;
        this.vnMeaning = vnMeaning;
    }
    public void randomVnMeaing(){
        String[] list = this.vnMeaning.split(", ");
        int idx = new Random().nextInt(list.length);
        this.vnMeaning = list[idx];
        if(this.posTag.equals("VBD") || this.posTag.equals("VBN")){
            this.vnMeaning = "đã " + this.vnMeaning;
        }
        if(this.posTag.equals("NNS")){
            this.vnMeaning = "những " + this.vnMeaning;
        }
    }
    public void originalWordForVBD(){
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
    public void orignalWordForVBD_e(){
        if(this.posTag.equals("VBD") || this.posTag.equals("VBN")){
            this.word += "e";
        }
    }
    public void originalWordForNNS_S(){
        // remove S the end character and find the original word
        String res = "";
        String subString = this.word.substring(0, this.word.length() - 1);
        //char[] stringChars = subString.toCharArray();
        this.word =subString;
    }
    public void originalWordForNNS_ES(){
        this.word = this.tempword;
        String res = "";
        int lenghtWord = this.word.length();
        String startingSubString = this.word.substring(0, lenghtWord - 2);
        //String endingSubString = this.word.substring(lenghtWord - 2 , lenghtWord);
        this.word = startingSubString;
        //char[] stringChars = subString.toCharArray();
        //this.word =subString;
    }
    public void orignalWordForNNS_VES_IES(){
        this.word = this.tempword;
        int lenghtWord = this.word.length();
        String startingSubString = this.word.substring(0, lenghtWord - 3);
        String endingSubString = this.word.substring(lenghtWord - 3 , lenghtWord);
        if(endingSubString.equals("ves")){
            this.word = startingSubString + "fe";
        }
        if(endingSubString.equals("ies")){
            this.word = startingSubString + "y";
        }
    }

}

