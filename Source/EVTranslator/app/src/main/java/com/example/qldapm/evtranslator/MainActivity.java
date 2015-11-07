package com.example.qldapm.evtranslator;


import android.os.Bundle;

import android.provider.UserDictionary;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;


public class MainActivity extends AppCompatActivity  {

    public static InputStream file_en_token;
    public static InputStream file_en_ner_person;
    public static InputStream file_en_pos_maxent;
    public static InputStream file_enparser_chunking;
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
        file_en_pos_maxent = getResources().openRawResource(R.raw.en_pos_maxent);
        file_enparser_chunking = getResources().openRawResource(R.raw.enparserchunking);
        Exmple(POSTags("I make a cake"));
/*
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
*/
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

    public ArrayList<String> POSTags(String enSentence){
        ArrayList<String> str = new ArrayList<String>();
        try{
            POSModel model = new POSModel(file_en_pos_maxent);
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
                str.add(sample.toString());
                perfMon.incrementCounter();
            }
            perfMon.stopAndPrintFinalResult();
        }catch (Exception ex){

        }
return str;
    }

    public Word postagToWord(String postag){
        String[] splitStr = postag.split("_");
        Word res = new Word(splitStr[0], splitStr[1], "");
        return res;
    }

    public void Exmple(ArrayList<String> postags){
        ArrayList<Word> words = new ArrayList<>();
        for (String temp : postags) {

            words.add(postagToWord(temp));
        }
        int a;
    }
    public  void Parse() {
        try{
            ParserModel model = new ParserModel(file_enparser_chunking);

            Parser parser = ParserFactory.create(model);

            String sentence = "Programcreek is a very huge and useful website.";
            Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);

            for (Parse p : topParses)
                p.show();

        }catch (Exception exc){
            int a = 10;
        }
	/*
	 * (TOP (S (NP (NN Programcreek) ) (VP (VBZ is) (NP (DT a) (ADJP (RB
	 * very) (JJ huge) (CC and) (JJ useful) ) ) ) (. website.) ) )
	 */
    }
}
