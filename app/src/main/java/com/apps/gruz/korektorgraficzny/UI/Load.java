package com.apps.gruz.korektorgraficzny.UI;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.apps.gruz.korektorgraficzny.Core.DatabaseInterface;
import com.apps.gruz.korektorgraficzny.Core.DatabaseReader;
import com.apps.gruz.korektorgraficzny.Core.Pdata;
import com.apps.gruz.korektorgraficzny.CustomViews.CustomAdapter;
import com.apps.gruz.korektorgraficzny.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Load extends AppCompatActivity {

    private List<String> names;
    private List<Pdata> dbData;
    private DatabaseReader db;
    private CustomAdapter adapter;

    @BindView(R.id.loading_list) ListView loadingList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        ButterKnife.bind(this);

        db = new DatabaseReader(this);
        names = new ArrayList<>();
        dbData = db.read();

        for(Pdata data : dbData){
            names.add(data.getName());
        }

        adapter = new CustomAdapter(this,R.layout.item_list,names);
        adapter.setRemoveButtonListener(this::deleteDialog);
        loadingList.setAdapter(adapter);
        loadingList.setOnItemClickListener((a,v,i,l)->clickItem(i));  // adapterView a, View v , int position , long l

    }

    private void deleteDialog(int position){
        AlertDialog.Builder removeDialog = new AlertDialog.Builder(this);

        removeDialog.setTitle(getResources().getString(R.string.remove_title_dialog));
        removeDialog.setMessage(getResources().getString(R.string.remove_text_dialog));

        removeDialog.setPositiveButton(getResources().getText(R.string.ok_button),(d,i)->removeItem(position));
        removeDialog.setNegativeButton(getResources().getText(R.string.cancel_button),null);
        removeDialog.show();
    }

    private void clickItem(int position){
        Intent mainIntent = new Intent(this,Main.class);

        mainIntent.putExtra(DatabaseInterface.COLUMN_NAME_BAND_1,dbData.get(position).getBands()[0]);
        mainIntent.putExtra(DatabaseInterface.COLUMN_NAME_BAND_2,dbData.get(position).getBands()[1]);
        mainIntent.putExtra(DatabaseInterface.COLUMN_NAME_BAND_3,dbData.get(position).getBands()[2]);
        mainIntent.putExtra(DatabaseInterface.COLUMN_NAME_BAND_4,dbData.get(position).getBands()[3]);
        mainIntent.putExtra(DatabaseInterface.COLUMN_NAME_BAND_5,dbData.get(position).getBands()[4]);

        startActivity(mainIntent);
        finish();
    }

    private void removeItem(int position){
        Toast.makeText(this,"Usunięto "+names.get(position),Toast.LENGTH_SHORT).show();
        db.delete(dbData.get(position).getId());
        names.remove(position);
        adapter.notifyDataSetChanged();


    }
}
