package com.example.qldapm.evtranslator.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.qldapm.evtranslator.models.entity.FavoriteObject;
import com.example.qldapm.evtranslator.models.entity.absFile;
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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // get danh sach

        Managerfavorite.getIntance().Listchid = Managerfavorite.getIntance().dbprocess.getFavorite(Managerfavorite.getIntance().currentFolder.getId());


        hienthifavorite = (ListView)findViewById(R.id.liv_danhsach);
        hienthifavorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //On click goi ben kia.
                FavoriteObject current = (FavoriteObject)parent.getItemAtPosition(position);
                Managerfavorite.getIntance().showFavorite = true;
                Managerfavorite.getIntance().currentfavorite = current;
                Intent intent = new Intent(getApplication(),HomeTranslateActivity.class);
                startActivity(intent);

            }
        });
        registerForContextMenu(hienthifavorite);
        adapter = new MyArrayAdapter(this,R.layout.listlayour, Managerfavorite.getIntance().Listchid);
        hienthifavorite.setAdapter(adapter);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Choose");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.huy_yeu_thich, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId())
        {
            case R.id.huylike:
            {
                removefavorite(info.position);
                break;
            }
        }
        return super.onContextItemSelected(item);
    }
    private void removefavorite(int position) {
        absFile t = adapter.getItem(position);
        adapter.remove(t);
        Managerfavorite.getIntance().dbprocess.Removefavorite(t.getId());
        //remove database here
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
