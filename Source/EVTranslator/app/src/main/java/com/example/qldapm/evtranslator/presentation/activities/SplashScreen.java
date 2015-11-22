package com.example.qldapm.evtranslator.presentation.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.qldapm.evtranslator.DB_EV;
import com.example.qldapm.evtranslator.R;
import com.example.qldapm.evtranslator.services.GlobalVariables;

import java.io.IOException;

import opennlp.tools.postag.POSModel;


public class SplashScreen extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new PrefetchData().execute();

    }

    private void InitializeNLP() {
        GlobalVariables global = GlobalVariables.getInstance();
        global.setFile_en_ner_person(getResources().openRawResource(R.raw.ennerperson));
        global.setFile_enparser_chunking(getResources().openRawResource(R.raw.enparserchunking));
        global.setFile_en_pos_maxent(getResources().openRawResource(R.raw.en_pos_maxent));
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
