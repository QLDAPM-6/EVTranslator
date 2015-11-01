package com.example.qldapm.evtranslator.yeuThich;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qldapm.evtranslator.Dialogue.ThemmoiFolder;
import com.example.qldapm.evtranslator.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class favorite extends AppCompatActivity {


    ListView hienthifavorite;
    MyArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment add = new ThemmoiFolder();
                add.show(getFragmentManager(),"ThemmoiFolder");
            }
        });
        hienthifavorite = (ListView)findViewById(R.id.liv_danhsach);
        hienthifavorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplication(), "On item click", Toast.LENGTH_LONG).show();
                //On click
            }
        });
        adapter = new MyArrayAdapter(this,R.layout.listlayour,Managerfavorite.getIntands().ListFolder);
        hienthifavorite.setAdapter(adapter);
    }
}
