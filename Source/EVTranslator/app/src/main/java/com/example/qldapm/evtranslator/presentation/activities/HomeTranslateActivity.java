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

import com.example.qldapm.evtranslator.R;
import com.example.qldapm.evtranslator.models.entity.Sentence;
import com.example.qldapm.evtranslator.models.repository.SentenceRepository;
import com.example.qldapm.evtranslator.models.repository.SentenceRepositoryImpl;
import com.example.qldapm.evtranslator.presentation.adapters.ItemTouchHelperAdapter;
import com.example.qldapm.evtranslator.presentation.helpers.ItemTouchHelperCallback;
import com.example.qldapm.evtranslator.services.GlobalVariables;
import com.example.qldapm.evtranslator.services.HistoryService;
import com.example.qldapm.evtranslator.services.Managerfavorite;
import com.example.qldapm.evtranslator.services.TranslatorService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;


public class HomeTranslateActivity extends AppCompatActivity implements View.OnClickListener {

    private final String LOG_TAG = HomeTranslateActivity.class.getSimpleName();

    private RecyclerView historyListHolder;             // view dùng để chứa History List
    private RecyclerView.LayoutManager layoutManager;   // quản lý layout cho historyListHolder
    private HistoryAdapter historyListAdapter;          // adapter cho History List
    private EditText input;                             // input chính điền từ cần dịch
    private LinearLayout translatedTextComponent;       // component lưu kết quả dịch (màu xanh phía dưới sau khi dịch)
    private CardView inputHolder;                       // view chứa phần input chính
    private ImageButton clearButton;                    // Dấu X trên input chính khi có chữ
    private Button translateButton;                     // Nút dịch
    private TextView resultBox;                         // TextView chứa từ tiếng Việt sau khi dịch
    private GlobalVariables global;                     // Các biến global (singleton)

    SentenceRepository sentenceRepository;
    ImageButton favoriteButton;                         // Nút lưu vào mục yêu thích

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_translate);

        sentenceRepository = new SentenceRepositoryImpl(getApplicationContext());

        // Gán view vào các biến
        inputHolder = (CardView) findViewById(R.id.cardview);
        historyListHolder = (RecyclerView) findViewById(R.id.my_recycler_view);
        clearButton = (ImageButton) findViewById(R.id.btn_clear);
        translatedTextComponent = (LinearLayout)findViewById(R.id.translated_text);
        input = (EditText) findViewById(R.id.touch_to_type_area);
        translateButton = (Button) findViewById(R.id.btn_translate);
        resultBox = (TextView) translatedTextComponent.findViewById(R.id.textViewVi);
        ImageButton copyButton = (ImageButton) translatedTextComponent.findViewById(R.id.copy);
        favoriteButton = (ImageButton)findViewById(R.id.ngoisao);
        final View homeRootView = findViewById(R.id.home_translate_root);

        // Cài đặt sự kiện click cho các button
        favoriteButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);
        translateButton.setOnClickListener(this);
        copyButton.setOnClickListener(this);

        // Cài đặt historyListHolder
        layoutManager = new LinearLayoutManager(this);
        historyListHolder.setLayoutManager(layoutManager);
        historyListHolder.setHasFixedSize(true);

        historyListAdapter = new HistoryAdapter(this, new HistoryService(sentenceRepository));
        historyListHolder.setAdapter(historyListAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(historyListAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(historyListHolder);

        // Cài đặt sự kiện khi nhấn done (enter) trên bàn phím ảo
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

        // Cài đặt nút clear để hiện khi có text trong input chính
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    clearButton.setVisibility(View.VISIBLE);
                } else {
                    clearButton.setVisibility(View.GONE);
                }
            }
        });


        //final EVTranslatorDbHelper dbHelper = new EVTranslatorDbHelper(getApplicationContext());

        // Kỹ thuật để xét coi bàn phím ảo có đang hiện hay ko, nếu có thực hiện dàn phần input hết màn hình
        homeRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = homeRootView.getRootView().getHeight() - homeRootView.getHeight();
                if (heightDiff > 1000) {
                    inputHolder.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT, 4.0f));
                } else {
                    inputHolder.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            0, 4.0f));
                }
            }
        });

        //favorite
        if(Managerfavorite.getIntance().showFavorite)
        {
            input.setText(Managerfavorite.getIntance().currentfavorite.get_name());
            resultBox.setText(Managerfavorite.getIntance().currentfavorite.getThuoctinhbosung());
            historyListHolder.setVisibility(View.GONE);
            translatedTextComponent.setVisibility(View.VISIBLE);
        }

        // Lấy biến toàn cục
        global = GlobalVariables.getInstance();
    }


    // Cài đặt sự kiện on click cho các button
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.copy:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("evtranslator", resultBox.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(HomeTranslateActivity.this, "Text Copied", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ngoisao:
                Managerfavorite.getIntance().currentfavorite.set_name(input.getText().toString());
                Managerfavorite.getIntance().currentfavorite.setThuoctinhbosung(resultBox.getText().toString());
                Intent intent = new Intent(getApplication(), FolderActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_clear:
                input.setText("");
                ChangeUIBack();
                HideSoftKey();
                break;
            case R.id.btn_translate:
                Translating();
        }

    }


    // Ẩn bàn phím ảo
    private void HideSoftKey(){
        InputMethodManager imm;
        imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    // Chuyển UI lại lúc chưa dịch
    private void ChangeUIBack() {
        translatedTextComponent.setVisibility(View.GONE);
        historyListHolder.setVisibility(View.VISIBLE);
        clearButton.setVisibility(View.GONE);
    }

    // Chuyển UI lúc đã dịch
    private void Translated(String outputText) {
        resultBox.setText(outputText);
        historyListHolder.setVisibility(View.GONE);
        translatedTextComponent.setVisibility(View.VISIBLE);
        HideSoftKey();
    }


    // Hàm dịch chính
    private void Translating(){

        String inputText = input.getText().toString();
        if (inputText.isEmpty()) {
            ChangeUIBack();
            HideSoftKey();
            return;
        }

        TranslatorService translatorService = new TranslatorService(this);
        String vnSentence = translatorService.toVietnamese(inputText);
        historyListAdapter.AddItem(inputText, vnSentence);
        new HistoryService(sentenceRepository).addToHistory(inputText, vnSentence);
        Translated(vnSentence);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favorite) { // Chạy khi người dùng tap vào favorite ở menu góc phải
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


    // Adapter cho History List (đặt inner class vì để tiện thay đổi UI)
    private class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> implements ItemTouchHelperAdapter {

        private final String TAG = HistoryAdapter.class.getSimpleName();
        private Context context;
        private HistoryService historyService;
        private List<Sentence> sentences;

        public HistoryAdapter(Context context, HistoryService historyService) {
            this.context = context;
            this.historyService = historyService;
            sentences = historyService.getAllSentences();
        }

        // Tạo View history item mới
        @Override
        public HistoryAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.history_item_in_list, parent, false);

            ViewHolder vh = new ViewHolder(v);
            return vh;

        }


        // Gắn dữ liệu vào view history item
        @Override
        public void onBindViewHolder(HistoryAdapter.ViewHolder viewHolder, int position) {
            final String englishSentence = sentences.get(position).getEnglishSentence();
            final String vietnameseSentence = sentences.get(position).getVietnameseSentence();

            // Gán dữ liệu dòng tiếng Anh
            TextView englishContent = (TextView) viewHolder.listView.findViewById(R.id.history_english_textView);
            englishContent.setText(englishSentence);

            // Gán dữ liệu dòng tiếng Việt
            TextView vietnameseContent = (TextView) viewHolder.listView.findViewById(R.id.history_vietnamese_textView);
            vietnameseContent.setText(vietnameseSentence);

            // Cài đặt phương thức click của view history item để load lại kết quả
            viewHolder.listView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    input.setText(englishSentence);
                    resultBox.setText(vietnameseSentence);
                    historyListHolder.setVisibility(View.GONE);
                    translatedTextComponent.setVisibility(View.VISIBLE);
                }
            });

            // Cái đặt nút thêm vào mục yêu thích
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

        // Xóa History khi swipe qua trái hoặc phải
        @Override
        public void onItemDismiss(int position) {
            historyService.deleteSentenceById(sentences.get(position).getId());
            sentences.remove(position);
            notifyItemRemoved(position);
        }

        // Phương thức thêm History Item
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
