package com.example.qldapm.evtranslator.presentation.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qldapm.evtranslator.R;
import com.example.qldapm.evtranslator.models.entity.absFile;

import java.util.List;

/**
 * Created by vanty on 10/23/2015.
 */
public class FolderFavoriteArrayAdapter extends ArrayAdapter<absFile> {
    public List<absFile>danhhSach;
    public int id_Layout;
    public Activity context;
    public FolderFavoriteArrayAdapter(Activity app, int id, List<absFile> danhsach)
    {
        super(app, id, danhsach);
        danhhSach = danhsach;
        id_Layout = id;
        context = app;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater infla = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infla.inflate(id_Layout,null);

        }
        if(position >=0 && position < danhhSach.size())
        {
            String type = danhhSach.get(position).getType();
            TextView Title = (TextView)convertView.findViewById(R.id.lbl_title_favorite);
            Title.setText(danhhSach.get(position).get_name());
            TextView date = (TextView)convertView.findViewById(R.id.lbl_favorite_date);
            date.setText(danhhSach.get(position).getThuoctinhbosung());
            if(type == "Folder") {
                date.setVisibility(View.GONE);
                Title.setTextSize(22);
            }
        }


        return  convertView;
    }
}
