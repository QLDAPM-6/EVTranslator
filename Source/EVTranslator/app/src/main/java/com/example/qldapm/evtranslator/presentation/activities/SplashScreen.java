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

public class SplashScreen extends Activity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new PrefetchData().execute();

    }

    private void InitializeNLP() {
        GlobalVariables global = GlobalVariables.getInstance();
        global.setParser();
        global.setDb_ev(new DB_EV(this));
        //global.setFile_en_ner_person(getResources().openRawResource(R.raw.ennerperson));
        //global.setFile_enparser_chunking(getResources().openRawResource(R.raw.en_parser_chunking));
        /*global.setFile_en_pos_maxent(getResources().openRawResource(R.raw.en_pos_maxent));
        global.setFile_entoken(getResources().openRawResource(R.raw.entoken));
        try {
            global.setModel(new POSModel(global.getFile_en_pos_maxent()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        global.setDb_ev(new DB_EV(this));*/
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
