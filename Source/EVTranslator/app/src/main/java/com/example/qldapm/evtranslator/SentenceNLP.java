package com.example.qldapm.evtranslator;
import android.app.Activity;

import android.content.res.AssetManager;
import android.os.Environment;

import com.example.qldapm.evtranslator.MainActivity;
import com.example.qldapm.evtranslator.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.postag.POSDictionary;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
/**
 * Created by Tung on 20/10/2015.
 */
 /*class SentenceNLP{

    public String[] GetTokenizer(String sent) {

       try {

            String tokens[];
            TokenizerModel model = new TokenizerModel(MainActivity.fos);
            Tokenizer tokenizer = new TokenizerME(model);

            tokens = tokenizer.tokenize("Hi. How are you? I look for Mike.");
            return tokens;
        } catch (Exception e) {
            //log the exception
        }
        return null;
    }
}
*/