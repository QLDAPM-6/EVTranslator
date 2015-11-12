package com.example.qldapm.evtranslator.presentation.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qldapm.evtranslator.DB_EV;
import com.example.qldapm.evtranslator.OpenNLPWord;
import com.example.qldapm.evtranslator.R;
import com.example.qldapm.evtranslator.presentation.helpers.ItemTouchHelperCallback;
import com.example.qldapm.evtranslator.services.HistoryService;
import com.example.qldapm.evtranslator.presentation.adapters.HistoryAdapter;
import com.example.qldapm.evtranslator.services.TranslatorService;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;


public class HomeTranslateActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HistoryAdapter historyAdapter;
    private EditText input;
    private LinearLayout translatedText;
    private CardView cardview;
    private ImageButton clearButton;

    public static InputStream file_en_token;
    public static InputStream file_en_ner_person;
    public static InputStream file_en_pos_maxent;
    public static InputStream file_enparser_chunking;
    DB_EV db_ev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_translate);

        cardview = (CardView) findViewById(R.id.cardview);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        clearButton = (ImageButton) findViewById(R.id.btn_clear);
        translatedText = (LinearLayout)findViewById(R.id.translated_text);
        input = (EditText) findViewById(R.id.touch_to_type_area);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        historyAdapter = new HistoryAdapter(this, new HistoryService());
        recyclerView.setAdapter(historyAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(historyAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);


        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Translating();
                    return true;
                } else {
                    return false;
                }
            }
        });

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) {
                    clearButton.setVisibility(View.VISIBLE);
                } else {
                    clearButton.setVisibility(View.GONE);
                }
            }
        });

        //Clear button

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.setText("");
                ChangeBack();
                HideSoftKey();
            }
        });


        final View homeRootView = findViewById(R.id.home_translate_root);
        homeRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = homeRootView.getRootView().getHeight() - homeRootView.getHeight();
                if (heightDiff > 1000) {
                    cardview.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT, 4.0f));
                } else {
                    cardview.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            0, 4.0f));
                }
            }
        });

        InitializeNLP();
    }

    private void InitializeNLP() {

        file_en_token = getResources().openRawResource(R.raw.entoken);
        file_en_ner_person = getResources().openRawResource(R.raw.ennerperson);
        file_en_pos_maxent = getResources().openRawResource(R.raw.en_pos_maxent);
        file_enparser_chunking = getResources().openRawResource(R.raw.enparserchunking);
        db_ev = new DB_EV(this);


    }

    private void Translating(){

        String inputText = input.getText().toString();
        if (inputText.isEmpty()) {
            ChangeBack();
            HideSoftKey();
            return;
        }

        ArrayList<String> posTag_list = POSTags(inputText);
        String[] abcd = getTokenizer(inputText);
        ArrayList<OpenNLPWord> openNLPWords_list =assignStringtoOpenNLPWord(posTag_list);

        String vnSentence= "";
        for(OpenNLPWord item : openNLPWords_list){
            item.randomVnMeaing();
            vnSentence+= item.getVnMeaning() + " ";
        }
        historyAdapter.AddItem(inputText, vnSentence);
        Translated(vnSentence);
        HideSoftKey();
    }

    private void HideSoftKey(){
        InputMethodManager imm;
        imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    private void ChangeBack() {
        translatedText.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        clearButton.setVisibility(View.GONE);
    }

    private void Translated(String outputText) {
        TextView resultBox = (TextView)translatedText.findViewById(R.id.textViewVi);
        resultBox.setText(outputText);
        recyclerView.setVisibility(View.GONE);
        translatedText.setVisibility(View.VISIBLE);
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
            TokenizerModel model = new TokenizerModel(this.file_en_token);
            Tokenizer tokenizer = new TokenizerME(model);
            tokens = tokenizer.tokenize(sent);
            return tokens;
        } catch (Exception e) {
            //log the exception
        }
        return null;
    }

    //assign pos for all words in the enSentence
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

                String[] splitStr = sample.toString().split(" ");
                for(String postag : splitStr){
                    str.add(postag);
                }

                perfMon.incrementCounter();
            }
            perfMon.stopAndPrintFinalResult();
        }catch (Exception ex){

        }
        return str;
    }

    //split the POSTag with syntax = word_postag into OpenNLPWord
    public OpenNLPWord postagToWord(String postag){
        String[] splitStr = postag.split("_");
        OpenNLPWord res = new OpenNLPWord(splitStr[0], splitStr[1], "");
        db_ev.addMeaming_OpenNLPWord(res);
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
