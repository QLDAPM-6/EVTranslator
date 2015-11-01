package com.example.qldapm.evtranslator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.example.qldapm.evtranslator.yeuThich.Allclass.Folder_class;
import com.example.qldapm.evtranslator.yeuThich.Managerfavorite;
import com.example.qldapm.evtranslator.yeuThich.Allclass.absFile;
import com.example.qldapm.evtranslator.yeuThich.folder;

import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;


public class MainActivity extends AppCompatActivity  {

    public static InputStream file_en_token;
    public static InputStream file_en_ner_person;
    public static InputStream file_en_pos_maxent;
    private TextView sent;
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sent = (TextView)findViewById(R.id.sentence);

        // init
        file_en_token = getResources().openRawResource(R.raw.entoken);
        file_en_ner_person = getResources().openRawResource(R.raw.ennerperson);
        file_en_pos_maxent = getResources().openRawResource(R.raw.enposmaxent);

        String []tokens = getTokenizer("Cat is a student");

        Span[] spans = findName(tokens);
        int a = 10;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        absFile temp = new Folder_class();
        temp.set_name("Hello");
        temp.setNgaySave("29/10/1994");
        Managerfavorite.getIntands().addChild(temp);
        Intent intent = new Intent(this,folder.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public String[] getTokenizer(String sent) {
        try {
            String tokens[];
            TokenizerModel model = new TokenizerModel(MainActivity.file_en_token);
            Tokenizer tokenizer = new TokenizerME(model);
            tokens = tokenizer.tokenize(sent);
            return tokens;
        } catch (Exception e) {
            //log the exception
        }
        return null;
    }

    public Span[]  findName(String[] sent) {
        try {
        TokenNameFinderModel model = new TokenNameFinderModel(file_en_ner_person);
        NameFinderME nameFinder = new NameFinderME(model);
        Span nameSpans[] = nameFinder.find(sent);

        for(Span s: nameSpans){
            Toast.makeText(getApplicationContext(),s.toString(),Toast.LENGTH_LONG).show();
        }
            //System.out.println();
        return nameSpans;
        } catch (Exception e) {
            //log the exception
        }
        return null;
    }


    public  void POSTag(){

    }
}
