package com.example.qldapm.evtranslator.presentation.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qldapm.evtranslator.DB_EV;
import com.example.qldapm.evtranslator.OpenNLPWord;
import com.example.qldapm.evtranslator.R;
import com.example.qldapm.evtranslator.models.database.EVTranslatorDbHelper;
import com.example.qldapm.evtranslator.models.entity.Sentence;
import com.example.qldapm.evtranslator.models.repository.SentenceRepository;
import com.example.qldapm.evtranslator.models.repository.SentenceRepositoryImpl;
import com.example.qldapm.evtranslator.presentation.adapters.ItemTouchHelperAdapter;
import com.example.qldapm.evtranslator.presentation.helpers.ItemTouchHelperCallback;
import com.example.qldapm.evtranslator.services.HistoryService;
import com.example.qldapm.evtranslator.services.Managerfavorite;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

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

    private final String LOG_TAG = HomeTranslateActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HistoryAdapter historyAdapter;
    private EditText input;
    private LinearLayout translatedText;
    private CardView cardview;
    private ImageButton clearButton;
    private Button translateButton;
    private TextView resultBox;

    //public  InputStream file_en_token;
    public  InputStream file_en_ner_person;
    //public
    public  InputStream file_enparser_chunking;
    DB_EV db_ev;

    SentenceRepository sentenceRepository;
    ImageButton imagefavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_translate);

        sentenceRepository = new SentenceRepositoryImpl(getApplicationContext());

        cardview = (CardView) findViewById(R.id.cardview);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        clearButton = (ImageButton) findViewById(R.id.btn_clear);
        translatedText = (LinearLayout)findViewById(R.id.translated_text);
        input = (EditText) findViewById(R.id.touch_to_type_area);
        translateButton = (Button) findViewById(R.id.btn_translate);
        resultBox = (TextView)translatedText.findViewById(R.id.textViewVi);
        ImageButton copyButton = (ImageButton)translatedText.findViewById(R.id.copy);

        imagefavorite = (ImageButton)findViewById(R.id.ngoisao);
        imagefavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Managerfavorite.getIntance().currentfavorite.set_name(input.getText().toString());
                Managerfavorite.getIntance().currentfavorite.setThuoctinhbosung(resultBox.getText().toString());
                Intent intent = new Intent(getApplication(), FolderActivity.class);
                startActivity(intent);
            }
        });
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        historyAdapter = new HistoryAdapter(this, new HistoryService(sentenceRepository));
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


        final EVTranslatorDbHelper dbHelper = new EVTranslatorDbHelper(getApplicationContext());

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new HistoryService(sentenceRepository).addToHistory(
                        input.getText().toString(), "Tieng viet"
                );

                List<Sentence> sentences = new HistoryService(sentenceRepository).getAllSentences();

                for (Sentence sentence : sentences) {
                    Log.v(LOG_TAG, sentence.getEnglishSentence() + " - " + sentence.getVietnameseSentence());
                }

                Translating();
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

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("evtranslator", resultBox.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(HomeTranslateActivity.this, "Text Copied", Toast.LENGTH_SHORT).show();
            }
        });
        //favorite
        if(Managerfavorite.getIntance().showFavorite)
        {
            input.setText(Managerfavorite.getIntance().currentfavorite.get_name());
            resultBox.setText(Managerfavorite.getIntance().currentfavorite.getThuoctinhbosung());
            recyclerView.setVisibility(View.GONE);
            translatedText.setVisibility(View.VISIBLE);
        }

        InitializeNLP();
    }

    private void InitializeNLP() {

        //file_en_token = getResources().openRawResource(R.raw.entoken);
        file_en_ner_person = getResources().openRawResource(R.raw.ennerperson);
        //file_en_pos_maxent = getResources().openRawResource(R.raw.en_pos_maxent);
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
        //String[] abc = getTokenizer(inputText);
        ArrayList<OpenNLPWord> openNLPWords_list =assignStringtoOpenNLPWord(posTag_list);

        String vnSentence= "";
        for(OpenNLPWord item : openNLPWords_list){
            item.randomVnMeaing();
            vnSentence+= item.getVnMeaning()+ " " ;
            //+item.getVnMeaning() + "|POS_" + item.getPosTag()
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
        if (id == R.id.action_favorite) {
            Intent intent = new Intent(this, FolderActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public String[] getTokenizer(String sent) {
        InputStream file_en_token = null;
        try {
            String tokens[];
            file_en_token = getResources().openRawResource(R.raw.entoken);
            TokenizerModel model = new TokenizerModel(file_en_token);
            Tokenizer tokenizer = new TokenizerME(model);
            tokens = tokenizer.tokenize(sent);
            return tokens;
        } catch (Exception e) {
            //log the exception
        }finally {
            if (file_en_token != null) {
                try {
                    file_en_token.close();
                }
                catch (IOException e) {
                    // Not an issue, training already finished.
                    // The exception should be logged and investigated
                    // if part of a production system.
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    //assign pos for all words in the enSentence
    public ArrayList<String> POSTags(String enSentence){
        ArrayList<String> str = new ArrayList<String>();
        InputStream file_en_pos_maxent = null;
        try{
            file_en_pos_maxent = getResources().openRawResource(R.raw.en_pos_maxent);
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
            file_en_pos_maxent.close();
        }catch (Exception ex){
            Toast.makeText(getApplication(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }finally {
            if (file_en_pos_maxent != null) {
                try {
                    file_en_pos_maxent.close();
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

    private class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> implements ItemTouchHelperAdapter {

        private final String TAG = HistoryAdapter.class.getSimpleName();

        private Context context;

        private HistoryService historyService;

        private List<Sentence> sentences;
        //private HashMap<String, String> historyContainerMap = new HashMap<String, String>();

        //private List<String> englishSentencesInHistory = new ArrayList<String>();

        public HistoryAdapter(Context context, HistoryService historyService) {
            this.context = context;
            this.historyService = historyService;
            sentences = historyService.getAllSentences();
        }

        @Override
        public HistoryAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.history_item_in_list, parent, false);
            // set the view's size, margins, paddings and layout parameters
            //...


            ViewHolder vh = new ViewHolder(v);
            return vh;

        }

        @Override
        public void onBindViewHolder(HistoryAdapter.ViewHolder viewHolder, int position) {
            TextView englishContent = (TextView) viewHolder.listView.findViewById(R.id.history_english_textView);
            final String englishSentence = sentences.get(position).getEnglishSentence();
            final String vietnameseSentence = sentences.get(position).getVietnameseSentence();
            englishContent.setText(englishSentence);

            TextView vietnameseContent = (TextView) viewHolder.listView.findViewById(R.id.history_vietnamese_textView);
            vietnameseContent.setText(vietnameseSentence);

            viewHolder.listView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    input.setText(englishSentence);
                    resultBox.setText(vietnameseSentence);
                    recyclerView.setVisibility(View.GONE);
                    translatedText.setVisibility(View.VISIBLE);
                }
            });
            final ImageButton favoriteIcon = (ImageButton) viewHolder.listView.findViewById(R.id.favorite_image_button);
            favoriteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO Enable favorite icon here
                    Log.d(TAG, "Replace favorite icon image");
                    Managerfavorite.getIntance().currentfavorite.set_name(englishSentence);
                    Managerfavorite.getIntance().currentfavorite.setThuoctinhbosung(vietnameseSentence);
                    Intent intent = new Intent(getApplication(), FolderActivity.class);
                    startActivity(intent);
                }
            });
        }


        @Override
        public int getItemCount() {
            return sentences.size();
        }

        @Override
        public void onItemDismiss(int position) {
            historyService.deleteSentenceById(sentences.get(position).getId());
            sentences.remove(position);
            notifyItemRemoved(position);
        }

        public void AddItem(String eng, String viet) {
            historyService.addToHistory(eng, viet);
            sentences.clear();
            sentences.addAll(historyService.getAllSentences());

            notifyDataSetChanged();

        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public View listView;
            public ViewHolder(View v) {
                super(v);
                listView = v;
            }
        }
    }
}
