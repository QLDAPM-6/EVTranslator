package com.example.qldapm.evtranslator.presentation.activities;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qldapm.evtranslator.presentation.fragments.AddFolder;
import com.example.qldapm.evtranslator.R;
import com.example.qldapm.evtranslator.services.Managerfavorite;
import com.example.qldapm.evtranslator.presentation.adapters.MyArrayAdapter;

public class FavoriteActivity extends AppCompatActivity {


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
                DialogFragment add = new AddFolder();
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
        adapter = new MyArrayAdapter(this,R.layout.listlayour, Managerfavorite.getIntance().ListFolder);
        hienthifavorite.setAdapter(adapter);
    }
}
