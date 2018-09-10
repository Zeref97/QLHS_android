package com.example.tan.qunlhcsinh;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.sql.Blob;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lw;
    ImageButton imgAdd, imgResert;
    ArrayAdapter<HocSinh> adapter;
    ArrayList<HocSinh> arrayList;
    ArrayList<HocSinh> temp;
    Button btn;
    EditText edt;
    int i=-1;
    SQLiteDatabase database;
    RelativeLayout backgroundMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database=getDatabase();
        addViewAndEvent();
    }

    public void addViewAndEvent(){

        backgroundMain=(RelativeLayout)findViewById(R.id.BackgroundMain);
        backgroundMain.setBackgroundResource(R.drawable.backgroundmain);

        lw=(ListView) findViewById(R.id.listView);
        imgAdd=(ImageButton) findViewById(R.id.imgbtnAdd);
        imgResert=(ImageButton) findViewById(R.id.imgbtnResert);
        btn=(Button) findViewById(R.id.btnSearch);
        edt=(EditText) findViewById(R.id.edtSearch);

        edt.clearFocus();

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);

        imgResert.setVisibility(View.GONE);

        database=getDatabase();
        arrayList=new ArrayList<>();

        adapter=new HocSinhAdapter(this,R.layout.item,arrayList);
        lw.setAdapter(adapter);


        if(database!=null){
            Cursor cursor=database.query("HocSinh",null,null,null,null,null,null);

            cursor.moveToFirst();

            while(!cursor.isAfterLast()){
                HocSinh contact=new HocSinh(cursor.getInt(0), cursor.getString(1),cursor.getString(2),cursor.getString(3),Boolean.parseBoolean(cursor.getString(4)),cursor.getFloat(5),cursor.getFloat(6));
                arrayList.add(contact);
                adapter.notifyDataSetChanged();
                cursor.moveToNext();
            }
            cursor.close();
        }

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,EditActivity.class);
                intent.putExtra("index",0);
                startActivityForResult(intent,2);
            }
        });

        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                i=position;
                Intent intent=new Intent(MainActivity.this,EditActivity.class);
                intent.putExtra("index",1);
                intent.putExtra("Student",arrayList.get(position));
                startActivityForResult(intent,2);
            }
        });

        lw.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Bạn có muốn xóa học sinh "+arrayList.get(position).getHoTen().toString()+" hay không?");
                builder.setCancelable(true);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.delete("HocSinh","id"+"=?",new String[]{String.valueOf(arrayList.get(position).getId())});
                        arrayList.remove(position);
                        adapter.notifyDataSetChanged();
                        dialog.cancel();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog=builder.create();
                alertDialog.show();

                return true;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt.clearFocus();

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);

                if(edt.getText().toString().length()==0)
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập tên cần tìm.",Toast.LENGTH_SHORT).show();
                else {
                    imgResert.setVisibility(View.VISIBLE);
                    temp=new ArrayList<>();
                    for (int i = 0; i < arrayList.size(); i++)
                        if (arrayList.get(i).getHoTen().toLowerCase().contains(edt.getText().toString().toLowerCase()))
                            temp.add(arrayList.get(i));

                    arrayList.removeAll(arrayList);

                    for (int k = 0; k < temp.size(); k++)
                            arrayList.add(temp.get(k));

                    adapter.notifyDataSetChanged();
                    temp.removeAll(temp);
                }
            }
        });

        imgResert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.removeAll(arrayList);
                if(database!=null){
                    Cursor cursor=database.query("HocSinh",null,null,null,null,null,null);

                    cursor.moveToFirst();

                    while(!cursor.isAfterLast()){
                        HocSinh contact=new HocSinh(cursor.getInt(0), cursor.getString(1),cursor.getString(2),cursor.getString(3),Boolean.parseBoolean(cursor.getString(4)),cursor.getFloat(5),cursor.getFloat(6));
                        arrayList.add(contact);
                        adapter.notifyDataSetChanged();
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
                imgResert.setVisibility(View.GONE);
            }
        });
    }


    public SQLiteDatabase getDatabase(){
        SQLiteDatabase db=openOrCreateDatabase("appdate.db",SQLiteDatabase.CREATE_IF_NECESSARY,null);
        try {
            if (db != null) {
                String sqlHocSinh ="create table if not exists HocSinh " +
                "(id integer primary key, name text, class text, birth text, sex text, longitude real, latitude real)";

                db.execSQL(sqlHocSinh);
            }
            return db;
        }catch(Exception e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2){
            if(resultCode==1) {
                HocSinh hs = (HocSinh) data.getSerializableExtra("HS");
                arrayList.add(hs);
                adapter.notifyDataSetChanged();
                ContentValues contentValues=new ContentValues();
                contentValues.put("name",hs.getHoTen().toString());
                contentValues.put("class",hs.getLop().toString());
                contentValues.put("birth",hs.getNamSinh().toString());
                contentValues.put("sex",String.valueOf(hs.isGioiTinh()));
                contentValues.put("longitude",hs.getLongitude());
                contentValues.put("latitude",hs.getLatitude());

                if(database!=null)
                    database.insert("HocSinh",null,contentValues);
            }
            else if(resultCode==2){
                HocSinh hs=(HocSinh) data.getSerializableExtra("HSM");
                arrayList.set(i,hs);
                adapter.notifyDataSetChanged();
                ContentValues contentValues=new ContentValues();
                contentValues.put("name",hs.getHoTen().toString());
                contentValues.put("class",hs.getLop().toString());
                contentValues.put("birth",hs.getNamSinh().toString());
                contentValues.put("sex",String.valueOf(hs.isGioiTinh()));
                contentValues.put("longitude", hs.getLongitude());
                contentValues.put("latitude",hs.getLatitude());

                database.update("HocSinh", contentValues, "id" + "=?", new String[]{String.valueOf(hs.getId())});

            }
            else if(resultCode==3){
                HocSinh hs=(HocSinh) data.getSerializableExtra("HSM");
                arrayList.remove(i);
                adapter.notifyDataSetChanged();
                database.delete("HocSinh","id"+"=?",new String[]{String.valueOf(hs.getId())});
            }
        }
    }
}
