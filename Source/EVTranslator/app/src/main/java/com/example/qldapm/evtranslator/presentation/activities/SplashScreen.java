package com.example.qldapm.evtranslator.presentation.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.qldapm.evtranslator.DB_EV;
import com.example.qldapm.evtranslator.R;
import com.example.qldapm.evtranslator.services.GlobalVariables;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StringReader;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;
import opennlp.tools.postag.POSModel;

public class SplashScreen extends Activity {
    private final static String PCG_MODEL = "englishpcfgser.gz";

    private  TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "invertible=true");




    public Tree parse(String str) {
        List<CoreLabel> tokens = tokenize(str);
        ObjectInputStream ois = null;
        Tree tree = null;
        FileInputStream fileInputStream = null;
        try {
            String FileName = "englishpcfgser.gz";
            fileInputStream = openFileInput(FileName);
            ois = new ObjectInputStream(fileInputStream);
            LexicalizedParser parser = LexicalizedParser.loadModel(ois);
            if(parser != null){
                tree = parser.apply(tokens);
            }
        }catch(FileNotFoundException fnf)
        {
        }
        catch (IOException ioexc){

        }
        finally{
            try{
                if(fileInputStream!=null)
                    fileInputStream.close();
            }catch (IOException io){
            }
        }
        return tree;
    }

    private List<CoreLabel> tokenize(String str) {
        Tokenizer<CoreLabel> tokenizer =
                tokenizerFactory.getTokenizer(
                        new StringReader(str));
        return tokenizer.tokenize();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        String str = "The handsome boy wants to talk something to you";

        List<CoreLabel> tokens = tokenize(str);
        ObjectInputStream ois = null;
        Tree tree = null;
        FileInputStream fileInputStream = null;
        try {
            LexicalizedParser parser = LexicalizedParser.loadModel();
            if(parser != null){
                tree = parser.apply(tokens);
            }
        }/*catch(FileNotFoundException fnf)
        {
            int a=10;
        }
        catch (IOException ioexc){
int c =10;
        }*/
        finally{
            try{
                if(fileInputStream!=null)
                    fileInputStream.close();
            }catch (IOException io){
            }
        }

        //Tree tree = parserTestStandford.parse(str);
        if(tree != null){
            String treestr = tree.pennString();
        }

        new PrefetchData().execute();

    }

    private void InitializeNLP() {
        GlobalVariables global = GlobalVariables.getInstance();
        //global.setFile_en_ner_person(getResources().openRawResource(R.raw.ennerperson));
        //global.setFile_enparser_chunking(getResources().openRawResource(R.raw.en_parser_chunking));
        global.setFile_en_pos_maxent(getResources().openRawResource(R.raw.en_pos_maxent));
        global.setFile_entoken(getResources().openRawResource(R.raw.entoken));
        try {
            global.setModel(new POSModel(global.getFile_en_pos_maxent()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        global.setDb_ev(new DB_EV(this));
    }

    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            InitializeNLP();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Intent i = new Intent(SplashScreen.this, HomeTranslateActivity.class);
            startActivity(i);

            // close this activity
            finish();
        }

    }
}
