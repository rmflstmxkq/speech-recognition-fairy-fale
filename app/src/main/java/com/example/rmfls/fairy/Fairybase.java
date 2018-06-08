package com.example.rmfls.fairy;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class Fairybase extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private static String DATABASE_NAME = "Fairybase";
    private static String TABLE_NAME = "choice";//동화 개수많큼 있어야 함
    private static int DATABASE_VERSION = 1;
    private static DatabaseHelper dbHelper;
    private static SQLiteDatabase db;
    static boolean createDb = false;
    TagManage tagmanage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);

        Intent it = getIntent();
        tagmanage = (TagManage) it.getSerializableExtra("tagmanage");

        if (createDb == false) {
            createDb = true;
            boolean isOpen = openDatabase();//헬퍼 객체화 및 데이터베이스 생성
            if (isOpen) {
                executeRawQuery();//데이터 베이스 명령문 사용
            }
        } else {
            executeRawQuery();
        }
    }

    private boolean openDatabase() {
        dbHelper = new DatabaseHelper(this);//헬퍼 생성후 데이터베이스 객체 참조
        db = dbHelper.getWritableDatabase();//실질적으로 여기서 데이터베이스 생성됨

        return true;
    }

    public void executeRawQuery() {//데이터베이스에 날리는 명령문
        Cursor c1 = db.rawQuery("select * from " + TABLE_NAME, null);
        c1.moveToNext();

        //Toast.makeText(this, c1.getString(2), Toast.LENGTH_LONG).show();
        c1.close();

        finish();
    }

    private class DatabaseHelper extends SQLiteOpenHelper {//헬퍼 ////////////////////////////////////

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);//상위클레스의 생성자 호출
        }

        public void onCreate(SQLiteDatabase db) {//헬퍼 객체가 생성될때 자동적으로 데이터베이스 생성

            try {
                String DROP_SQL = "drop table if exists " + TABLE_NAME;//테이블이 있다면 테이블 삭제
                db.execSQL(DROP_SQL);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in DROP_SQL", ex);
            }

            String CREATE_SQL = "create table " + TABLE_NAME + "("//테이블 생성 명령문
                    + " _id integer PRIMARY KEY autoincrement, "
                    + " respath text, "
                    + " tag text)";

            try {
                db.execSQL(CREATE_SQL);//테이블 생성
            } catch (Exception ex) {
                Log.e(TAG, "Exception in CREATE_SQL", ex);
            }
        }

        void pushBase() {
            try {//데이터 베이스에 입력
                db.execSQL("insert into " + TABLE_NAME + "(respath, tag) values ('res/11111', '#토끼');");
            } catch (Exception ex) {
                Log.e(TAG, "Exception in insert SQL", ex);
            }
        }

        void pushTag() {//태그메니저한테 테그 전달
            try{
                Cursor c1 = db.rawQuery("select * from " + TABLE_NAME, null);
                int count = c1.getInt(0);
                for(int i=0;i< count ;i++){
                    c1.moveToNext();
                    String tagnote = c1.getString(2);
                    tagmanage.push(tagnote);
                }
            }catch(Exception ex){
                Log.e(TAG, "Exception in pushTag", ex);
            }
        }

        public void onOpen(SQLiteDatabase db) {

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ".");
        }
    }
}

