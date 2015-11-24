package com.example.qldapm.evtranslator.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qldapm.evtranslator.R;
import com.example.qldapm.evtranslator.models.entity.Message;

import java.util.ArrayList;

/**
 * Created by Tùng Lê on 12-Nov-15.
 */
public class MyAdapter extends ArrayAdapter<Message>
{
    private Context m_Context;
    private int m_TextViewResourceId;
    private ArrayList<Message> m_message;

    public MyAdapter(Context context, int TextViewResourceId, ArrayList<Message> Infor)
    {
        super(context, TextViewResourceId, Infor);
        m_Context = context;
        m_TextViewResourceId = TextViewResourceId;
        m_message = Infor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        if(v == null)
        {
            LayoutInflater inflater = (LayoutInflater)m_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(m_TextViewResourceId, null);
        }
        Message infor = m_message.get(position);
        if(infor != null)
        {
            TextView tvPhoneNumber = (TextView) v.findViewById(R.id.textViewPhoneNumber);
            tvPhoneNumber.setText(infor.getPhoneNumner());
            TextView tvText = (TextView) v.findViewById(R.id.textViewText);
            tvText.setText(infor.getText());
            ImageView image = (ImageView) v.findViewById(R.id.Image);
            image.setImageDrawable(infor.getImage());
        }
        return  v;
    }
}
