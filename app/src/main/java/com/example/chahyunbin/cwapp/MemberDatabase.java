package com.example.chahyunbin.cwapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class MemberDatabase {
    public static final String TAG = "BookDatabase";
    private static MemberDatabase database;
    public static String DATABASENAME = "member.db";
    private SQLiteDatabase db;
    public static String TABLENAME = "MEMBER_INFO";
    public static int DATABASEVERSION = 1;
    public static String TABLE_MEMBER_INFO = "MEMBER_INFO";

    private DBHelper dbHelper;
    private Context context;




    public MemberDatabase(Context context) {
        this.context = context;
    }


    public static MemberDatabase getInstance(Context context) {
        if (database == null) {
            database = new MemberDatabase(context);
        }
        return database;
    }



        public boolean open(){
            dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();

            return true;
        }
        public void close () {
            db.close();
            database = null;
        }


        public void executeRawQuery(){
        Cursor c1 = db.rawQuery("select count(*) as Total from " + TABLENAME,null);
        c1.moveToNext();
        c1.close();
        }

    public Cursor rawQuery(String SQL, Object o) {


        Cursor c1 = null;
        try {
            c1 = db.rawQuery(SQL, null);

        } catch(Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
        }

        return c1;
    }


        public void insertRecord (String name, String phonenumber, int age, int month, int day){
            try {


                String query =
                        "INSERT INTO "+TABLENAME+" VALUES (null, "+name+", "+phonenumber+", "+age+", "+month+", "+day+" );";
            db.execSQL(query);

        }catch(Exception ex){
            Log.d(TAG, "exception in insertRecord", ex);
        }
    }

    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASENAME, null, DATABASEVERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase _db) {

            // create table
            String CREATE_SQL = "create table " + TABLE_MEMBER_INFO + " ("
                    + "  ID_ INTEGER  PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "PHONENUMBER TEXT, "
                    + "AGE INTEGER, "
                    + "MONTH INTEGER, "
                    + "DAY INTEGER )";

            try {
                _db.execSQL(CREATE_SQL);
            } catch(Exception ex) {
                Log.e(TAG, "Exception in CREATE_SQL", ex);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS person");
            onCreate(db);
        }
        private void insertRecord(SQLiteDatabase _db, String name, String phonenumber, int age, int month, int day) {
            try {
                String query =
                        "INSERT INTO "+TABLENAME+" VALUES (null, "+name+", "+phonenumber+", "+age+", "+month+", "+day+" );";
                db.execSQL(query);
            } catch(Exception ex) {
                Log.e(TAG, "Exception in executing insert SQL.", ex);
            }
        }
    }


    public ArrayList<MemberItem> selectAll() {
        ArrayList<MemberItem> result = new ArrayList<MemberItem>();
        Log.d(TAG,"1");

        try {
            Cursor cursor = db.rawQuery("select * from " + TABLE_MEMBER_INFO, null);
            Log.d(TAG,"2");
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                String name = cursor.getString(0);
                String phone = cursor.getString(1);
                int age = cursor.getInt(2);
                String birth = cursor.getString(3)+ "월 " +cursor.getString(4)+"일 ";
                MemberItem info = new MemberItem(name, phone, age, birth);
                result.add(info);
                Log.d(TAG,"3");
            }

        } catch(Exception ex) {
            Log.e(TAG, "Exception in executing insert SQL.", ex);
        }

        return result;
    }


}