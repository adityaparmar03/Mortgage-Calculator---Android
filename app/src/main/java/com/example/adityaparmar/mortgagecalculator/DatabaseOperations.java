package com.example.adityaparmar.mortgagecalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.adityaparmar.mortgagecalculator.TableData.TableInfo;

/**
 * Created by deepika on 3/20/17.
 */

public class DatabaseOperations extends SQLiteOpenHelper {

    public static final int database_version = 1;
    public String CREATE_QUERY = "CREATE TABLE " + TableInfo.TABLE_NAME + "(" + TableInfo.STREET + " TEXT,"
            + TableInfo.CITY + " TEXT," + TableInfo.STATE + " TEXT,"+ TableInfo.ZIP + " TEXT,"
            + TableInfo.PropertyType + " TEXT," + TableInfo.PropertyPrice + " TEXT," + TableInfo.Downpayment + " TEXT,"+
            TableInfo.APR + " TEXT," + TableInfo.Term + " TEXT,"  + TableInfo.MONTHLY_PAYMENT + " TEXT);";

    public DatabaseOperations(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, TableInfo.DATABASE_NAME, null, database_version);
        Log.d("Database Operations", "DatabaseCreated");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
        Log.d("Database Operations", "Table is created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertInfo(DatabaseOperations dop, String street, String city, String state, String zip,String propertytype,String propertyprice,String downpayment,String apr, String terms,String payment){
        SQLiteDatabase sqLiteDatabase = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableInfo.STREET, street);
        cv.put(TableInfo.CITY, city);
        cv.put(TableInfo.STATE, state);
        cv.put(TableInfo.ZIP, zip);
        cv.put(TableInfo.PropertyType,propertytype);
        cv.put(TableInfo.PropertyPrice,propertyprice);
        cv.put(TableInfo.Downpayment,downpayment);
        cv.put(TableInfo.APR,apr);
        cv.put(TableInfo.Term,terms);
        cv.put(TableInfo.MONTHLY_PAYMENT, payment);

        long k = sqLiteDatabase.insert(TableInfo.TABLE_NAME, null, cv);
        Log.d("Database Operations", "Data inserted");

        return k;
    }

    public String[][] getInfo(DatabaseOperations databaseOperations){
        SQLiteDatabase sqLiteDatabase = databaseOperations.getReadableDatabase();
        String[] columns = {"rowid", TableInfo.STREET, TableInfo.CITY, TableInfo.STATE,TableInfo.ZIP,TableInfo.PropertyType,TableInfo.PropertyPrice,TableInfo.Downpayment, TableInfo.APR,TableInfo.Term,TableInfo.MONTHLY_PAYMENT};
        Cursor c = sqLiteDatabase.query(TableInfo.TABLE_NAME, columns, null, null, null, null, null);

        String[][] data = new String[c.getCount()][12];
        int i=0;
        if (c.moveToFirst()) {
            do {
                // get the data into array, or class variable

                data[i][0]=c.getString(0);
                data[i][1]=c.getString(1);
                data[i][2]=c.getString(2);
                data[i][3]=c.getString(3);
                data[i][4]=c.getString(4);
                data[i][5]=c.getString(5);
                data[i][6]=c.getString(6);
                data[i][7]=c.getString(7);
                data[i][8]=c.getString(8);
                data[i][9]=c.getString(9);
                data[i][10]=c.getString(10);
                data[i][11]=String.valueOf(c.getInt(6) - c.getInt(7));





                i++;
            } while (c.moveToNext());
        }
        c.close();

        return data;
    }

    public String[][] getInfobyID(DatabaseOperations databaseOperations,int rid){
        SQLiteDatabase sqLiteDatabase = databaseOperations.getReadableDatabase();
        String[] columns = {"rowid", TableInfo.STREET, TableInfo.CITY, TableInfo.STATE,TableInfo.ZIP,TableInfo.PropertyType,TableInfo.PropertyPrice,TableInfo.Downpayment, TableInfo.APR,TableInfo.Term,TableInfo.MONTHLY_PAYMENT};
        String[] ids = new String[1];
        ids[0] = String.valueOf(rid);
        Cursor c = sqLiteDatabase.query(TableInfo.TABLE_NAME, columns, "rowid=?", new String[] { String.valueOf(rid) }, null, null, null);

        String[][] data = new String[c.getCount()][12];
        int i=0;
        if (c.moveToFirst()) {
            do {
                // get the data into array, or class variable

                data[i][0]=c.getString(0);
                data[i][1]=c.getString(1);
                data[i][2]=c.getString(2);
                data[i][3]=c.getString(3);
                data[i][4]=c.getString(4);
                data[i][5]=c.getString(5);
                data[i][6]=c.getString(6);
                data[i][7]=c.getString(7);
                data[i][8]=c.getString(8);
                data[i][9]=c.getString(9);
                data[i][10]=c.getString(10);







                i++;
            } while (c.moveToNext());
        }
        c.close();

        return data;
    }
    public int update(DatabaseOperations databaseOperations, String street, String city, String state, String zip,String propertytype,String propertyprice,String downpayment,String apr, String terms,String payment){
        SQLiteDatabase sqLiteDatabase = databaseOperations.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableInfo.STREET, street);
        cv.put(TableInfo.CITY, city);
        cv.put(TableInfo.STATE, state);
        cv.put(TableInfo.ZIP, zip);
        cv.put(TableInfo.PropertyType,propertytype);
        cv.put(TableInfo.PropertyPrice,propertyprice);
        cv.put(TableInfo.Downpayment,downpayment);
        cv.put(TableInfo.APR,apr);
        cv.put(TableInfo.Term,terms);
        cv.put(TableInfo.MONTHLY_PAYMENT, payment);


        int if_updated = sqLiteDatabase.update(TableInfo.TABLE_NAME, cv, "rowid=?", new String[] { String.valueOf(ID.Id) });
        if(if_updated > 0)
            return if_updated;
        return -1;
    }
    public int delete(DatabaseOperations databaseOperations,int id){


        SQLiteDatabase sqLiteDatabase = databaseOperations.getWritableDatabase();
        int no_of_rows_deleted = sqLiteDatabase.delete(TableInfo.TABLE_NAME, "rowid=?", new String[] { String.valueOf(id) });
        if(no_of_rows_deleted>0){
            return no_of_rows_deleted;
        }
        return -1;
    }

}
