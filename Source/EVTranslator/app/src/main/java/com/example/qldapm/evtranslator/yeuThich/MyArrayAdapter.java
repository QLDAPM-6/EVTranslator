package com.example.qldapm.evtranslator.yeuThich;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qldapm.evtranslator.R;
import com.example.qldapm.evtranslator.yeuThich.Allclass.absFile;

import java.util.List;

/**
 * Created by vanty on 10/23/2015.
 */
public class MyArrayAdapter extends ArrayAdapter<absFile> {
    public List<absFile>danhhSach;
    public int id_Layout;
    public Activity context;
    public MyArrayAdapter(Activity app,int id,List<absFile>danhsach)
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
            date.setText(danhhSach.get(position).getNgaySave());
            if(type == "Favorite")
            {
                ImageView image = (ImageView)convertView.findViewById(R.id.btn_image);
                image.setImageResource(R.mipmap.ngoisao);
            }
            else
            {
                ImageView image = (ImageView)convertView.findViewById(R.id.btn_image);
                image.setImageResource(R.mipmap.folder);
            }
        }


        return  convertView;
    }
}
