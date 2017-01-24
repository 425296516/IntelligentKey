package com.aigo.router.bussiness.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhangcirui on 15/7/18.
 */
public class DbDeviceInfoHelper extends SQLiteOpenHelper {

    public DbDeviceInfoHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

  /*  public static final String TABLE_DEVICE_INFO = "table_device_info";

    public DbDeviceInfoHelper(Context context) {
        this(context, "db_device_info", null, 2);
    }

    public DbDeviceInfoHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DbDeviceInfoHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_DEVICE_INFO + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "bind_id String," +
                "conditionerId String," +
                "conditionerStatus String," +
                "deviceType String," +
                "deviceName String," +
                "deviceInfo String," +
                "status String," +
                "openCode String," +
                "closeCode String," +
                "mark INTEGER" +
                ")");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion == 2) {
            db.execSQL("ALTER TABLE " + TABLE_DEVICE_INFO + " ADD COLUMN bind_id");
        }

    }*/

}
