package com.example.rmfls.speechtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rmfls.fairy.R;

public class Fairybase extends AppCompatActivity {


    public static final String TAG = "Fairybase";

    private static String DATABASE_NAME = "Fairyall";
    private static String TABLE_NAME = "choice";//동화 개수많큼 있어야 함
    private static int DATABASE_VERSION = 1;
    private static DatabaseHelper dbHelper;
    private static SQLiteDatabase db;

    static boolean createDb = false;
    com.example.rmfls.speechtest.TagManage tagmanage;

    Intent it;
    int select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (createDb == false) {//있으면 사용 없으면 생성
            it = getIntent();
            tagmanage = (com.example.rmfls.speechtest.TagManage) it.getSerializableExtra("tagmanage");
            select = it.getExtras().getInt("selectfairy");
            createDb = true;

            switch (select) {//동화목록
                case 1:
                    TABLE_NAME = "rabbit_tortoise";
                    break;
                case 2:
                    TABLE_NAME = "sun_moon";
                    break;
                case 3:
                    TABLE_NAME = "uturi";
                    break;
            }
        }
        boolean isOpen = openDatabase();//헬퍼 객체화 및 데이터베이스 생성
        if (isOpen) {
            executeRawQuery();
        }
    }

    private boolean openDatabase() {
        dbHelper = new DatabaseHelper(this);//헬퍼 생성후 데이터베이스 객체 참조
        db = dbHelper.getWritableDatabase();//실질적으로 여기서 데이터베이스 생성됨
        return true;
    }

    public void executeRawQuery() {//데이터베이스에 날리는 명령문
        try {
            it = getIntent();
            String node = it.getStringExtra("ftag");
            String query = "select * from " + TABLE_NAME +" where ftag=\""+ node + "\";";
            Cursor c1 = db.rawQuery(query, null);
             c1.moveToNext();
             String res = c1.getString(1);//얻은 이미지 경로 이걸 전달해 변경
             it = new Intent(this, com.example.rmfls.speechtest.MainActivity.class);
             it.putExtra("fres", res);
            setResult(Activity.RESULT_OK, it);
            c1.close();
            finish();
        } catch (Exception ex) {
            Log.e(TAG, "Exception in fairy tagmanage", ex);
        }
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
                    + " frespath text, "
                    + " ftag text)";

            try {
                db.execSQL(CREATE_SQL);//테이블 생성
            } catch (Exception ex) {
                Log.e(TAG, "Exception in CREATE_SQL", ex);
            }
            try {//데이터 베이스에 입력
                String tag = getResources().getString(R.string.tag);
                String res = getResources().getString(R.string.res);
                String[] tags = tag.split(" ");
                String[] ress = res.split(" ");

                for (int i = 0; i < tags.length; i++) {
                    String query = "insert into " + TABLE_NAME + " (frespath, ftag) values ('" + ress[i] + "', '" + tags[i] + "');";

                    db.execSQL(query);
                }
            } catch (Exception ex) {
                Log.e(TAG, "Exception in insert SQL", ex);
            }

            try {//태그메니저에 태크 넣는 과정
                Cursor c1 = db.rawQuery("select count(*) as Total from " + TABLE_NAME, null);
                c1.moveToNext();
                int count = c1.getInt(0);
                c1.close();

                Cursor c2 = db.rawQuery("select * from " + TABLE_NAME, null);
                for (int i = 0; i < count; i++) {
                    c2.moveToNext();
                    String tagnote = c2.getString(2);
                    tagmanage.push(tagnote);
                }
                c2.close();
            } catch (Exception ex) {
                Log.e(TAG, "Exception in pushTag", ex);
            }
        }

        void pushBase() {

        }

        void pushTag() {//태그메니저한테 테그 전달

        }

        public void onOpen(SQLiteDatabase db) {

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ".");
        }
    }
}


