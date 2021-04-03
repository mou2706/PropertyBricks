package com.openwebsolutions.propertybricks.Sqlitedata_Recycler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.openwebsolutions.propertybricks.Model.Sqlite_ModelDemo.ModelDemo;

import java.util.ArrayList;

public class Sqlitedata extends SQLiteOpenHelper {

    public static final int database_version=1;
    public static final String database_name="User_id.db";
    public static final String table_name="User";
    public static final String col_name="Name";
    public static final String col_loc="Location";
    public static final String col_type="Type";
    public static final String col_pro_id="Pro_id";
    public static final String col_id="id";
    Context context;

    public Sqlitedata(Context context) {
        super(context,database_name,null,database_version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "+table_name+" ("+col_id+" integer primary key autoincrement, "+col_name+" text, "+col_loc+" varchar, "+col_type+" varchar, "+col_pro_id+" varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("Drop table if exists "+table_name);
    }

    public void insert(ModelDemo modelDemo) {
        SQLiteDatabase database=getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(col_name,modelDemo.getName());
        contentValues.put(col_loc,modelDemo.getLoc());
        contentValues.put(col_type,modelDemo.getType());
        contentValues.put(col_pro_id,modelDemo.getId());

        long a=database.insert(table_name,null,contentValues);
        database.close();

        Log.d("insert no: ",""+a);
    }
    public ArrayList<ModelDemo> getAllUser() {
        ArrayList<ModelDemo>arrayList=new ArrayList<>();
        SQLiteDatabase database=getReadableDatabase();

        String query="SELECT * FROM " + table_name;
        Cursor cursor=database.rawQuery(query,null);


        if(cursor.moveToFirst()){
            do {
                ModelDemo modelDemo=new ModelDemo();
                modelDemo.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(col_id))));
                modelDemo.setName(cursor.getString(cursor.getColumnIndex(col_name)));
                modelDemo.setLoc(cursor.getString(cursor.getColumnIndex(col_loc)));
                modelDemo.setType(cursor.getString(cursor.getColumnIndex(col_type)));
                modelDemo.setPro_id(cursor.getString(cursor.getColumnIndex(col_pro_id)));

                arrayList.add(modelDemo);
            }
            while (cursor.moveToNext());

        }
        cursor.close();
        database.close();

        return arrayList;
    }

    public void delete(ArrayList<ModelDemo> arrayList_recent) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" DELETE  FROM " + table_name);
        db.close();
    }
}
