package com.example.qldapm.evtranslator.presentation.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.qldapm.evtranslator.R;
import com.example.qldapm.evtranslator.domain.domainservice.HistoryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Nhat Huy (ndnhuy)
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final static String TAG = HistoryAdapter.class.getSimpleName();

    private Context context;

    private HistoryService historyService = new HistoryService();

    private HashMap<String, String> historyContainerMap = new HashMap<String, String>();

    private List<String> englishSentencesInHistory = new ArrayList<String>();

    public HistoryAdapter(Context context, HistoryService historyService) {
        this.context = context;
        this.historyService = historyService;
        this.historyContainerMap = historyService.getHistory();
        englishSentencesInHistory.addAll(historyService.getHistory().keySet());
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item_in_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //...
        final ImageButton favoriteIcon = (ImageButton) v.findViewById(R.id.favorite_image_button);
        favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Enable favorite icon here
                Log.d(TAG, "Replace favorite icon image");
            }
        });

        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder viewHolder, int position) {
        TextView englishContent = (TextView) viewHolder.listView.findViewById(R.id.history_english_textView);
        englishContent.setText(englishSentencesInHistory.get(position));

        TextView vietnameseContent = (TextView) viewHolder.listView.findViewById(R.id.history_vietnamese_textView);
        vietnameseContent.setText(historyContainerMap.get(englishSentencesInHistory.get(position)));
    }


    @Override
    public int getItemCount() {
        return englishSentencesInHistory.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View listView;
        public ViewHolder(View v) {
            super(v);
            listView = v;
        }
    }
}
