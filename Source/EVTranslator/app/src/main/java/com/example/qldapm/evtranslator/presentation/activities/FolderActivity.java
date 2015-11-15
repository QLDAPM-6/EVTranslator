package com.example.qldapm.evtranslator.presentation.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qldapm.evtranslator.presentation.fragments.AddFolder;
import com.example.qldapm.evtranslator.R;
import com.example.qldapm.evtranslator.models.FavoriteObject;
import com.example.qldapm.evtranslator.models.Folder;
import com.example.qldapm.evtranslator.models.absFile;
import com.example.qldapm.evtranslator.services.Managerfavorite;
import com.example.qldapm.evtranslator.presentation.adapters.MyArrayAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class FolderActivity extends AppCompatActivity implements AddFolder.NoticeDialogListener {

    ListView hienthifavorite;
    MyArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call dialogue
                DialogFragment add = new AddFolder();
                add.show(getFragmentManager(),"ThemmoiFolder");
            }
        });
        hienthifavorite = (ListView)findViewById(R.id.liv_danhsachfolder);
        hienthifavorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplication(), "On item click", Toast.LENGTH_LONG).show();
                if(parent.getItemAtPosition(position) instanceof FavoriteObject)
                {
                    // show result find world
                }
                else
                {
                    Folder temp = (Folder)parent.getItemAtPosition(position);
                    // thu nghiem
                    Managerfavorite.getIntance().Listchid = new ArrayList<absFile>();
                    FavoriteObject temp1 = new FavoriteObject();
                    temp1.set_name("hello, everyone");
                    temp1.setNgaySave("29/02/1994");
                    Managerfavorite.getIntance().Listchid.add(temp1);
                    // goi favorite
                    Intent intent = new Intent(getApplication(), FavoriteActivity.class);
                    startActivity(intent);
                }

            }
        });
        adapter = new MyArrayAdapter(this,R.layout.listlayour,Managerfavorite.getIntance().ListFolder);
        hienthifavorite.setAdapter(adapter);
    }
    // event dialogue click oke
    @Override
    public void onDialogNegativeClick(DialogFragment dialog, String value,int thaotac) {

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String value,int thaotac) {
        // add new here
        absFile temp = new Folder();
        temp.set_name(value);
        Calendar c = Calendar.getInstance();
        String day = c.getTime().toString();
        temp.setNgaySave(day);
        Managerfavorite.getIntance().addChild(temp);// Them folder
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Choose");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuchoose, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId())
        {
            case R.id.rename:
            {
                absFile t = adapter.getItem(info.position);
                DialogFragment add = new AddFolder(t.get_name());
                add.show(getFragmentManager(),"ThemmoiFolder");
            }
            case R.id.delete:
            {
                removefolder(info.position);
                break;
            }
        }
        return super.onContextItemSelected(item);
    }

    private void removefolder(int position) {
        absFile t = adapter.getItem(position);
        adapter.remove(t);
        //remove database here
    }
}
