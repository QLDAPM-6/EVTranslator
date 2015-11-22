package com.example.qldapm.evtranslator.services;

import android.content.Context;
import android.widget.Toast;

import com.example.qldapm.evtranslator.DB_EV;
import com.example.qldapm.evtranslator.OpenNLPWord;
import com.example.qldapm.evtranslator.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

/**
 * @author Nhat Huy (ndnhuy)
 */
public class TranslatorService {

    Context context;
    GlobalVariables global;

    public TranslatorService(Context ct) {
        this.context = ct;
        global = GlobalVariables.getInstance();
    }

    public String toVietnamese(String englishSentence) {

        ArrayList<String> posTag_list = POSTags(englishSentence);
        ArrayList<OpenNLPWord> openNLPWords_list = assignStringtoOpenNLPWord(posTag_list);

        String vnSentence= "";
        for(OpenNLPWord item : openNLPWords_list){
            item.randomVnMeaing();
            vnSentence+= item.getVnMeaning()+ " " ;
            //+item.getVnMeaning() + "|POS_" + item.getPosTag()
        }

        return vnSentence;
    }


    //assign pos for all words in the enSentence
    public ArrayList<String> POSTags(String enSentence){
        ArrayList<String> str = new ArrayList<String>();
        InputStream file_en_pos_maxent = null;
        try{
            POSModel model = global.getModel();
            PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
            POSTaggerME tagger = new POSTaggerME(model);
            ObjectStream<String> lineStream = new PlainTextByLineStream(
                    new StringReader(enSentence));
            perfMon.start();
            String line;

            while ((line = lineStream.read()) != null) {
                String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
                        .tokenize(line);
                String[] tags = tagger.tag(whitespaceTokenizerLine);
                POSSample sample = new POSSample(whitespaceTokenizerLine, tags);

                String[] splitStr = sample.toString().split(" ");
                for(String postag : splitStr){
                    str.add(postag);
                }

                perfMon.incrementCounter();
            }
            perfMon.stopAndPrintFinalResult();
            global.getFile_en_pos_maxent().close();
        }catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            if (global.getFile_en_pos_maxent() != null) {
                try {
                    global.getFile_en_pos_maxent().close();
                }
                catch (IOException e) {
                    // Not an issue, training already finished.
                    // The exception should be logged and investigated
                    // if part of a production system.
                    e.printStackTrace();
                }
            }
        }
        return str;
    }

    //split the POSTag with syntax = word_postag into OpenNLPWord
    public OpenNLPWord postagToWord(String postag){
        String[] splitStr = postag.split("_");
        OpenNLPWord res = new OpenNLPWord(splitStr[0], splitStr[1],  splitStr[0]);
        global.getDb_ev().addMeaming_OpenNLPWord(res);
        return res;
    }

    // example.
    public ArrayList<OpenNLPWord> assignStringtoOpenNLPWord(ArrayList<String> postags){
        ArrayList<OpenNLPWord> res = new ArrayList<>();
        for (String temp : postags) {
            res.add(postagToWord(temp));
        }
        //DB_EV db_ev = new DB_EV(this);
        //db_ev.getOpenNLWord(words.get(0));
        //Toast.makeText(getApplication(),words.get(0).getVnMeaning(),Toast.LENGTH_LONG).show();
        return res;
    }
}
