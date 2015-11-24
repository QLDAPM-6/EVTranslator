package com.example.qldapm.evtranslator.presentation.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qldapm.evtranslator.R;
import com.example.qldapm.evtranslator.models.entity.Message;
import com.example.qldapm.evtranslator.presentation.adapters.MyAdapter;

import java.util.ArrayList;

public class ListMessagesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<Message> m_ListMessage;
    MyAdapter m_Adapter;
    ListView m_lv;
    String m_text = ""; // Nội dung tin nhắn cần dịch

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_messages);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        m_ListMessage = new ArrayList<Message>();

        Uri uri = Uri.parse("content://sms/inbox");
        Cursor c = getContentResolver().query(uri, null, null ,null,null);
        startManagingCursor(c);

        // Đọc tin nhắn từ máy
        if(c.moveToFirst())
        {
            for(int i=0; i < c.getCount(); i++)
            {
                Message message = new Message();
                message.setText(c.getString(c.getColumnIndexOrThrow("body")).toString());
                message.setPhoneNumner(c.getString(c.getColumnIndexOrThrow("address")).toString());
                message.setImage(getResources().getDrawable(R.mipmap.image_message));
                m_ListMessage.add(message);
                c.moveToNext();
            }
        }
        c.close();

        // Hiển thị tin nhắn lên listview
        m_Adapter = new MyAdapter(this, R.layout.list_message_layout, m_ListMessage);
        m_lv = (ListView)findViewById(R.id.listView_listMessage);
        m_lv.setAdapter(m_Adapter);

        m_lv.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        m_text = m_ListMessage.get(position).getText();
        Intent intent = new Intent();
        intent.putExtra("english", m_text);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
