package com.example.qldapm.evtranslator.yeuThich;

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

import com.example.qldapm.evtranslator.Dialogue.ThemmoiFolder;
import com.example.qldapm.evtranslator.R;
import com.example.qldapm.evtranslator.yeuThich.Allclass.Favorite_Object;
import com.example.qldapm.evtranslator.yeuThich.Allclass.Folder_class;
import com.example.qldapm.evtranslator.yeuThich.Allclass.absFile;

import java.util.ArrayList;
import java.util.Calendar;

public class folder extends AppCompatActivity implements ThemmoiFolder.NoticeDialogListener {

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
                DialogFragment add = new ThemmoiFolder();
                add.show(getFragmentManager(),"ThemmoiFolder");
            }
        });
        hienthifavorite = (ListView)findViewById(R.id.liv_danhsach);
        hienthifavorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplication(), "On item click", Toast.LENGTH_LONG).show();
                if(parent.getItemAtPosition(position) instanceof Favorite_Object)
                {
                    // show result find world
                }
                else
                {
                    Folder_class temp = (Folder_class)parent.getItemAtPosition(position);
                    // thu nghiem
                    Managerfavorite.getIntands().Listchid = new ArrayList<absFile>();
                    Favorite_Object temp1 = new Favorite_Object();
                    temp1.set_name("hello, everyone");
                    temp1.setNgaySave("29/02/1994");
                    Managerfavorite.getIntands().Listchid.add(temp1);
                    // goi favorite
                    Intent intent = new Intent(getApplication(),favorite.class);
                    startActivity(intent);
                }

            }
        });
        adapter = new MyArrayAdapter(this,R.layout.listlayour,Managerfavorite.getIntands().ListFolder);
        hienthifavorite.setAdapter(adapter);
    }
    // event dialogue click oke
    @Override
    public void onDialogNegativeClick(DialogFragment dialog, String value,int thaotac) {
        if(thaotac == 1)
        {
            //rename here
        }
        else
        {
            // add new here
            absFile temp = new Folder_class();
            temp.set_name(value);
            Calendar c = Calendar.getInstance();
            String day = c.getTime().toString();
            temp.setNgaySave(day);
            Managerfavorite.getIntands().addChild(temp);// Them folder
        }

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String value,int thaotac) {

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
                DialogFragment add = new ThemmoiFolder(t.get_name());
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
