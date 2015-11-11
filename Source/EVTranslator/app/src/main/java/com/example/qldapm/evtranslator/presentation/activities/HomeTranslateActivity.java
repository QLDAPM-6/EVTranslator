package com.example.qldapm.evtranslator.presentation.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qldapm.evtranslator.R;
import com.example.qldapm.evtranslator.presentation.helpers.ItemTouchHelperCallback;
import com.example.qldapm.evtranslator.services.HistoryService;
import com.example.qldapm.evtranslator.presentation.adapters.HistoryAdapter;
import com.example.qldapm.evtranslator.services.TranslatorService;


public class HomeTranslateActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HistoryAdapter historyAdapter;
    private EditText input;
    private LinearLayout translatedText;
    private CardView cardview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_translate);

        cardview = (CardView) findViewById(R.id.cardview);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        historyAdapter = new HistoryAdapter(this, new HistoryService());
        recyclerView.setAdapter(historyAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(historyAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        translatedText = (LinearLayout)findViewById(R.id.translated_text);

        input = (EditText) findViewById(R.id.touch_to_type_area);
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
    }

    private void Translating(){

        String inputText = input.getText().toString();
        if (inputText.isEmpty()) {
            ChangeBack();
            HideSoftKey();
            return;
        }
        String outputText = TranslatorService.toVietnamese(inputText);
        Translated(outputText);
        historyAdapter.AddItem(inputText, outputText);
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
}
