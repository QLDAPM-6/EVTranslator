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

import com.example.qldapm.evtranslator.models.database.EVTranslatorDbFavorite;
import com.example.qldapm.evtranslator.models.database.EVTranslatorDbHelper;
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
        //get danh sach folder
        Managerfavorite.getIntance().Setdatabase(new EVTranslatorDbHelper((this)));
        Managerfavorite.getIntance().dbprocess = new EVTranslatorDbFavorite();
        if(adapter == null)
            Managerfavorite.getIntance().ListFolder = Managerfavorite.getIntance().dbprocess.getFolder();

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
                if(parent.getItemAtPosition(position) instanceof FavoriteObject)
                {
                    // show result find world
                }
                else
                {
                    Folder temp = (Folder)parent.getItemAtPosition(position);
                    // thu nghiem
                    Managerfavorite.getIntance().currentFolder = temp;
                    // goi favorite
                    Intent intent = new Intent(getApplication(), FavoriteActivity.class);
                    startActivity(intent);
                }

            }
        });
        registerForContextMenu(hienthifavorite);
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
        if(thaotac == 0) {
            absFile temp = GeneFolder(value);

           long id = Managerfavorite.getIntance().dbprocess.SaveFolder(value, temp.getThuoctinhbosung());
            temp.setId(String.valueOf(id));
            Managerfavorite.getIntance().addChild(temp);// Them folder
            adapter.notifyDataSetChanged();
        }
        else {
            AddFolder dialogtemp = (AddFolder)dialog;
            int index = dialogtemp.GetID().intValue();
            absFile tam = adapter.getItem(index);
            tam.set_name(value);
            absFile folder = GeneFolder(value);
            Managerfavorite.getIntance().dbprocess.Renamefolder(tam.getId(),folder);
            adapter.notifyDataSetChanged();
            //dialogtemp
            //Long index = dialogtemp.getID
        }
    }

    private absFile GeneFolder(String value) {
        absFile temp = new Folder();
        temp.set_name(value);
        Calendar c = Calendar.getInstance();
        String day = c.getTime().toString();
        temp.setThuoctinhbosung(day);
        return temp;
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
                DialogFragment add = new AddFolder(t.get_name(),Long.parseLong(String.valueOf(info.position)));
                add.show(getFragmentManager(),"ThemmoiFolder");
                break;
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
        Managerfavorite.getIntance().dbprocess.Removefolder(t.get_name());
        //remove database here
    }
}
