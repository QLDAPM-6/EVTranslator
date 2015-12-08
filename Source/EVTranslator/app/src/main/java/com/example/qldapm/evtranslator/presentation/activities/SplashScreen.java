package com.example.qldapm.evtranslator.presentation.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.example.qldapm.evtranslator.DB_EV;
import com.example.qldapm.evtranslator.models.database.DataHelper;
import com.example.qldapm.evtranslator.services.GlobalVariables;
import java.io.IOException;

public class SplashScreen extends Activity {

    public static String PACKAGE_NAME;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        new PrefetchData().execute();
    }

    private void InitializeNLP() {
        GlobalVariables global = GlobalVariables.getInstance();
        global.setDb_ev(new DB_EV(this));
        global.setParser();
    }

    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            InitializeNLP();
            DataHelper dataHelper = new DataHelper(getApplicationContext());
            try {
                dataHelper.createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
