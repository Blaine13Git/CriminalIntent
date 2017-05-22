package com.bignerdranch.android.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.DATE;
import static com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.SOLVED;
import static com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.TITLE;
import static com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.UUID;
import static com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable.NAME;

/**
 * Helper的主要作用是：
 * 1、管理--数据库的创建；
 * 2、管理--数据库的版本
 */
public class CrimeBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "crimeBase.db";

    // 必须给出构造函数
    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + NAME + "(" +
                " _id integer primary key autoincrement, " +
                UUID + ", " +
                TITLE + ", " +
                DATE + ", " +
                SOLVED +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
