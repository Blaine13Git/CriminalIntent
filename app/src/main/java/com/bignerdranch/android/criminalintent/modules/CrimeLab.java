package com.bignerdranch.android.criminalintent.modules;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.bignerdranch.android.criminalintent.database.CrimeBaseHelper;
import com.bignerdranch.android.criminalintent.database.CrimeCursorWrapper;

import com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    /**
     * context使用注解;
     * 使用Context的主要原因是出于对生命周期的考虑。
     * 这里使用getApplicationContext(应用程序上下文)，
     * 而不是使用getActivity（界面上下文）是因为Application的生命周期比任何activity都长
     * 只有activity对象存在肯定就有Application对象存在。
     * 根据对象的周期选择不同的上下文对象（context）
     * <p>
     * CrimeLab是个单例（实例存储在内存中）。这表明，一旦创建，它就会一直存在，直至整个应用进程被销毁。
     * 如果使用getActivity的上下文，势必会造成内存资源的浪费
     */
    private Context mContext;
    private static CrimeLab sCrimeLab;
    private SQLiteDatabase mDatabase;

    // 在构造函数中调用打开数据库的方法
    private CrimeLab(Context context) {
        mContext = context.getApplicationContext(); // ……见context注解
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase(); // 以写的方式打开数据库
    }

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    /**
     * ContentValues注解：
     * 负责处理数据库写入和更新操作的辅助类它是个键值存储类，类似于Java的HashMap和前面用过的Bundle。
     * 不同的是， ContentValues只能用于处理SQLite数据。
     * 在CrimeLab中创建ContentValues实例，实际就是将Crime记录转换为ContentValues。
     *
     * @param crime
     * @return
     */
    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT,crime.getSuspect());

        return values;
    }

    public void addCrime(Crime crime) {
        ContentValues values = getContentValues(crime);
        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(
                CrimeTable.NAME,
                values,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{uuidString}
        );
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new CrimeCursorWrapper(cursor);
    }


    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            crimes.add(cursor.getCrime());
            cursor.moveToNext();
        }
        cursor.close();

        return crimes;
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) return null;
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Crime crime){
        // 获取系统存储图片的目录；
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(externalFilesDir == null) return null;
        return new File(externalFilesDir,crime.getPhotoFilename());

    }
}
