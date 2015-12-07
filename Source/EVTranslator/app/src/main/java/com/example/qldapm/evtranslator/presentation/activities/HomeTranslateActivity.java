package com.example.qldapm.evtranslator.presentation.activities;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qldapm.evtranslator.R;
import com.example.qldapm.evtranslator.models.database.EVTranslatorDbFavorite;
import com.example.qldapm.evtranslator.models.database.EVTranslatorDbHelper;
import com.example.qldapm.evtranslator.models.entity.Folder;
import com.example.qldapm.evtranslator.models.entity.Sentence;
import com.example.qldapm.evtranslator.models.entity.absFile;
import com.example.qldapm.evtranslator.models.repository.SentenceRepository;
import com.example.qldapm.evtranslator.models.repository.SentenceRepositoryImpl;
import com.example.qldapm.evtranslator.presentation.adapters.ItemTouchHelperAdapter;
import com.example.qldapm.evtranslator.presentation.fragments.AddFolder;
import com.example.qldapm.evtranslator.presentation.helpers.ItemTouchHelperCallback;
import com.example.qldapm.evtranslator.services.GlobalVariables;
import com.example.qldapm.evtranslator.services.HistoryService;
import com.example.qldapm.evtranslator.services.Managerfavorite;
import com.example.qldapm.evtranslator.services.TranslatorService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;


public class HomeTranslateActivity extends AppCompatActivity implements View.OnClickListener, AddFolder.NoticeDialogListener {

    private final String LOG_TAG = HomeTranslateActivity.class.getSimpleName();

    private RecyclerView historyListHolder;             // view dùng để chứa History List
    private RecyclerView.LayoutManager layoutManager;   // quản lý layout cho historyListHolder
    private HistoryAdapter historyListAdapter;          // adapter cho History List
    private EditText input;                             // input chính điền từ cần dịch
    private LinearLayout translatedTextComponent;       // component lưu kết quả dịch (màu xanh phía dưới sau khi dịch)
    private CardView inputHolder;                       // view chứa phần input chính
    private ImageButton clearButton;                    // Dấu X trên input chính khi có chữ
    private ImageButton favoriteButton;                 // Nút lưu vào mục yêu thích
    private Button translateButton;                     // Nút dịch
    private TextView resultBox;                         // TextView chứa từ tiếng Việt sau khi dịch
    private GlobalVariables global;                     // Các biến global (singleton)
    private boolean flagTranslated;                     // cờ cho thấy có đang ở trạng thái đã dịch hay không
    private int FAVORITE_REQUEST_CODE = 0;              // request code cho favorite activity
    private int SMS_TRANSLATION_REQUEST_CODE = 1;       // request code cho list message activity

    SentenceRepository sentenceRepository;


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
        //
        //get danh sach folder
        Managerfavorite.getIntance().Setdatabase(new EVTranslatorDbHelper((this)));
        Managerfavorite.getIntance().dbprocess = new EVTranslatorDbFavorite();
        Managerfavorite.getIntance().ListFolder = Managerfavorite.getIntance().dbprocess.getFolder();
    }


    // Cài đặt sự kiện on click cho các button
    private ArrayAdapter<String> adapter;
    private AlertDialog dialog;
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
                DisplayFavoriteSave(input.getText().toString(), resultBox.getText().toString());

                break;
            case R.id.btn_clear:
                ChangeUIBack();
                HideSoftKey();
                break;
            case R.id.btn_translate:
                Translating();
        }

    }

    private void DisplayFavoriteSave(String english, String vietnamese) {
        Managerfavorite.getIntance().currentfavorite.set_name(english);
        Managerfavorite.getIntance().currentfavorite.setThuoctinhbosung(vietnamese);
        //Intent intent = new Intent(getApplication(), FolderActivity.class);
        //startActivity(intent);
        List<String> option = new ArrayList<>();
        List<absFile>dsFolder = Managerfavorite.getIntance().ListFolder;

        if(dsFolder.size() == 0)
        {
            option.add("Please add a favorite folder");
        }
        else
        {
            for(absFile temp : Managerfavorite.getIntance().ListFolder)
            {
                option.add(temp.get_name());
            }
            option.add("Add New Folder");
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add To Favorite");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(which == Managerfavorite.getIntance().ListFolder.size())
                {
                    DialogFragment add = new AddFolder();
                    add.show(getFragmentManager(),"ThemmoiFolder");
                    return;
                }
                // luu favorite
                absFile current = Managerfavorite.getIntance().ListFolder.get(which);
                Managerfavorite.getIntance().currentFolder = (Folder)current;
                Managerfavorite.getIntance().currentfavorite.setID_folder(Managerfavorite.getIntance().currentFolder.getId());
                Managerfavorite.getIntance().dbprocess.Themfavorite(Managerfavorite.getIntance().currentfavorite);
                Toast.makeText(getApplication(),"Added to folder " + adapter.getItem(which),Toast.LENGTH_LONG).show();
            }
        });
        dialog = builder.create();
        dialog.show();
    }
    // event dialogue click oke

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String value, int thaotac) {
        absFile temp = new Folder();
        temp.set_name(value);
        long id = Managerfavorite.getIntance().dbprocess.SaveFolder(value, " ");
        temp.setId(String.valueOf(id));
        Managerfavorite.getIntance().addChild(temp);// Them folder
        adapter.add(value);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog, String value, int thaotac) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(flagTranslated == true) {
            ChangeUIBack();
        } else {
            super.onBackPressed();
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
        input.setText("");
        translatedTextComponent.setVisibility(View.GONE);
        historyListHolder.setVisibility(View.VISIBLE);
        clearButton.setVisibility(View.GONE);
        flagTranslated = false;
    }

    // Chuyển UI lúc đã dịch
    private void Translated(String outputText) {
        flagTranslated = true;
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
            startActivityForResult(intent, FAVORITE_REQUEST_CODE);
        } else if (id == R.id.action_sms_translate) { // Chạy khi người dùng tap vào SMS Tranlate ở menu góc phải
            Intent intent = new Intent(this, ListMessagesActivity.class);
            startActivityForResult(intent, SMS_TRANSLATION_REQUEST_CODE);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FAVORITE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String englishSentence = data.getStringExtra("english");
                String vietnameseSentence = data.getStringExtra("vietnam");
                input.setText(englishSentence);
                resultBox.setText(vietnameseSentence);
                historyListHolder.setVisibility(View.GONE);
                translatedTextComponent.setVisibility(View.VISIBLE);
            }
        } else if (requestCode == SMS_TRANSLATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String englishSentence = data.getStringExtra("english");
                input.setText(englishSentence);
                Translating();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                    flagTranslated = true;
                }
            });

            // Cái đặt nút thêm vào mục yêu thích
            final ImageButton favoriteIcon = (ImageButton) viewHolder.listView.findViewById(R.id.favorite_image_button);
            favoriteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DisplayFavoriteSave(englishSentence, vietnameseSentence);
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
