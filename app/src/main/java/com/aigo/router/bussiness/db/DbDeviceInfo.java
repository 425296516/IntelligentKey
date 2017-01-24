package com.aigo.router.bussiness.db;

/**
 * Created by zhangcirui on 15/7/18.
 */
public class DbDeviceInfo {

    /*private DbDeviceInfoHelper mDbDeviceInfoHelper;
    private SQLiteDatabase mSqLiteDatabase;

    public DbDeviceInfo(Context context) {
        mDbDeviceInfoHelper = new DbDeviceInfoHelper(context);
    }

    public int insert(DbDeviceInfoObject deviceInfo) {

        mSqLiteDatabase = mDbDeviceInfoHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        *//*if(deviceInfo.getId()!= null){
            contentValues.put("_id",Integer.parseInt(deviceInfo.getId()));
        }*//*
        contentValues.put("bind_id", deviceInfo.getBind_id());
        contentValues.put("conditionerId", deviceInfo.getConditionerId());
        contentValues.put("conditionerStatus", deviceInfo.getConditionerStatus());
        contentValues.put("deviceType", deviceInfo.getDeviceType());
        contentValues.put("deviceName", deviceInfo.getDeviceName());
        contentValues.put("deviceInfo", deviceInfo.getDeviceInfo());
        contentValues.put("status", deviceInfo.getStatus());
        contentValues.put("openCode", deviceInfo.getOpenCode());
        contentValues.put("closeCode", deviceInfo.getCloseCode());
        contentValues.put("mark", deviceInfo.getMark());

        int result = (int) mSqLiteDatabase.insert(DbDeviceInfoHelper.TABLE_DEVICE_INFO, null, contentValues);
        mSqLiteDatabase.close();

        return result;
    }


    public boolean deleteById(String id) {
        mSqLiteDatabase = mDbDeviceInfoHelper.getWritableDatabase();
        String sql = "delete from " + mDbDeviceInfoHelper.TABLE_DEVICE_INFO +
                " where conditionerId = " + id+" and bind_id = '"+Constant.KT03_BIND_ID +"'";
        mSqLiteDatabase.execSQL(sql);
        mSqLiteDatabase.close();
        return true;
    }

    public boolean deleteAll() {
        mSqLiteDatabase = mDbDeviceInfoHelper.getWritableDatabase();
        String sql = "delete from " + mDbDeviceInfoHelper.TABLE_DEVICE_INFO ;
        mSqLiteDatabase.execSQL(sql);
        mSqLiteDatabase.close();
        return true;
    }

    //mark标记表示 0标准，1上传，2，删除，3，修改
    public void updateMark(String id, int mark) {
        try {
            String sql = "update " + mDbDeviceInfoHelper.TABLE_DEVICE_INFO + " set mark = '" +mark + "' where conditionerId = " + id+" and bind_id = '"+Constant.KT03_BIND_ID +"'";
            mSqLiteDatabase = mDbDeviceInfoHelper.getReadableDatabase();
            mSqLiteDatabase.execSQL(sql);
            mSqLiteDatabase.close();
        } catch (Exception e) {
            Ln.e(e);
        }
    }

    public void update(String id, String deviceName) {
        try {
            String sql = "update " + mDbDeviceInfoHelper.TABLE_DEVICE_INFO + " set deviceName = '" +deviceName + "' where conditionerId = " + id+" and bind_id = '"+Constant.KT03_BIND_ID +"'";
            mSqLiteDatabase = mDbDeviceInfoHelper.getReadableDatabase();
            mSqLiteDatabase.execSQL(sql);
            mSqLiteDatabase.close();
        } catch (Exception e) {
            Ln.e(e);
        }
    }

    public void setStatus(String id, String status) {
        try {
            String sql = "update " + mDbDeviceInfoHelper.TABLE_DEVICE_INFO + " set status = '" +status + "' where conditionerId = " + id+" and bind_id = '"+Constant.KT03_BIND_ID +"'";
            mSqLiteDatabase = mDbDeviceInfoHelper.getReadableDatabase();
            mSqLiteDatabase.execSQL(sql);
            mSqLiteDatabase.close();
        } catch (Exception e) {
            Ln.e(e);
        }
    }

    public void setConditionerStatus(String id, String status) {
        try {
            String sql = "update " + mDbDeviceInfoHelper.TABLE_DEVICE_INFO + " set conditionerStatus = '" +status + "' where conditionerId = " + id+" and bind_id = '"+Constant.KT03_BIND_ID +"'";
            mSqLiteDatabase = mDbDeviceInfoHelper.getReadableDatabase();
            mSqLiteDatabase.execSQL(sql);
            mSqLiteDatabase.close();
        } catch (Exception e) {
            Ln.e(e);
        }
    }

    public DbDeviceInfoObject queryById(String deviceId) {

        mSqLiteDatabase = mDbDeviceInfoHelper.getReadableDatabase();
        String sql = "select * from " + mDbDeviceInfoHelper.TABLE_DEVICE_INFO + " where conditionerId = " + deviceId+" and bind_id = '"+Constant.KT03_BIND_ID +"'";
        Cursor cursor = mSqLiteDatabase.rawQuery(sql, null);
        DbDeviceInfoObject dbDeviceInfoObject = new DbDeviceInfoObject();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String bind_id = cursor.getString(cursor.getColumnIndex("bind_id"));
            String conditionerId = cursor.getString(cursor.getColumnIndex("conditionerId"));
            String conditionerStatus = cursor.getString(cursor.getColumnIndex("conditionerStatus"));
            String deviceType = cursor.getString(cursor.getColumnIndex("deviceType"));
            String deviceName = cursor.getString(cursor.getColumnIndex("deviceName"));
            String deviceInfo = cursor.getString(cursor.getColumnIndex("deviceInfo"));
            String status = cursor.getString(cursor.getColumnIndex("status"));
            String openCode = cursor.getString(cursor.getColumnIndex("openCode"));
            String closeCode = cursor.getString(cursor.getColumnIndex("closeCode"));
            int mark = cursor.getInt(cursor.getColumnIndex("mark"));

            dbDeviceInfoObject.setId(conditionerId);
            dbDeviceInfoObject.setBind_id(bind_id);
            dbDeviceInfoObject.setConditionerId(conditionerId);
            dbDeviceInfoObject.setConditionerStatus(conditionerStatus);
            dbDeviceInfoObject.setDeviceType(deviceType);
            dbDeviceInfoObject.setDeviceName(deviceName);
            dbDeviceInfoObject.setDeviceInfo(deviceInfo);
            dbDeviceInfoObject.setStatus(status);
            dbDeviceInfoObject.setOpenCode(openCode);
            dbDeviceInfoObject.setCloseCode(closeCode);
            dbDeviceInfoObject.setMark(mark);

        }
        cursor.close();
        mSqLiteDatabase.close();
        return dbDeviceInfoObject;
    }


    public List<DbDeviceInfoObject> queryMark(int queryMark){

        mSqLiteDatabase = mDbDeviceInfoHelper.getReadableDatabase();
        List<DbDeviceInfoObject> list = new ArrayList<DbDeviceInfoObject>();
        String sql = "select * from " + mDbDeviceInfoHelper.TABLE_DEVICE_INFO +" where mark = "+ queryMark+ " and bind_id = '"+Constant.KT03_BIND_ID +"' order by _id";
        Cursor cursor = mSqLiteDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String bind_id = cursor.getString(cursor.getColumnIndex("bind_id"));
            String conditionerId = cursor.getString(cursor.getColumnIndex("conditionerId"));
            String conditionerStatus = cursor.getString(cursor.getColumnIndex("conditionerStatus"));
            String deviceType = cursor.getString(cursor.getColumnIndex("deviceType"));
            String deviceName = cursor.getString(cursor.getColumnIndex("deviceName"));
            String deviceInfo = cursor.getString(cursor.getColumnIndex("deviceInfo"));
            String status = cursor.getString(cursor.getColumnIndex("status"));
            String openCode = cursor.getString(cursor.getColumnIndex("openCode"));
            String closeCode = cursor.getString(cursor.getColumnIndex("closeCode"));
            int mark = cursor.getInt(cursor.getColumnIndex("mark"));

            DbDeviceInfoObject dbDeviceInfoObject = new DbDeviceInfoObject();
            dbDeviceInfoObject.setId(conditionerId);
            dbDeviceInfoObject.setBind_id(bind_id);
            dbDeviceInfoObject.setConditionerId(conditionerId);
            dbDeviceInfoObject.setConditionerStatus(conditionerStatus);
            dbDeviceInfoObject.setDeviceType(deviceType);
            dbDeviceInfoObject.setDeviceName(deviceName);
            dbDeviceInfoObject.setDeviceInfo(deviceInfo);
            dbDeviceInfoObject.setStatus(status);
            dbDeviceInfoObject.setOpenCode(openCode);
            dbDeviceInfoObject.setCloseCode(closeCode);
            dbDeviceInfoObject.setMark(mark);

            list.add(dbDeviceInfoObject);
        }
        cursor.close();
        mSqLiteDatabase.close();
        return list;

    }



    public List<DbDeviceInfoObject> queryAll() {

        mSqLiteDatabase = mDbDeviceInfoHelper.getReadableDatabase();
        List<DbDeviceInfoObject> list = new ArrayList<DbDeviceInfoObject>();
        String sql = "select * from " + mDbDeviceInfoHelper.TABLE_DEVICE_INFO +" where mark != "+ Constant.DEVICE_DELETE+" and bind_id = '"+Constant.KT03_BIND_ID +"' order by _id";
        Cursor cursor = mSqLiteDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String bind_id = cursor.getString(cursor.getColumnIndex("bind_id"));
            String conditionerId = cursor.getString(cursor.getColumnIndex("conditionerId"));
            String conditionerStatus = cursor.getString(cursor.getColumnIndex("conditionerStatus"));
            String deviceType = cursor.getString(cursor.getColumnIndex("deviceType"));
            String deviceName = cursor.getString(cursor.getColumnIndex("deviceName"));
            String deviceInfo = cursor.getString(cursor.getColumnIndex("deviceInfo"));
            String status = cursor.getString(cursor.getColumnIndex("status"));
            String openCode = cursor.getString(cursor.getColumnIndex("openCode"));
            String closeCode = cursor.getString(cursor.getColumnIndex("closeCode"));
            int mark = cursor.getInt(cursor.getColumnIndex("mark"));

            DbDeviceInfoObject dbDeviceInfoObject = new DbDeviceInfoObject();
            dbDeviceInfoObject.setId(conditionerId);
            dbDeviceInfoObject.setBind_id(bind_id);
            dbDeviceInfoObject.setConditionerId(conditionerId);
            dbDeviceInfoObject.setConditionerStatus(conditionerStatus);
            dbDeviceInfoObject.setDeviceType(deviceType);
            dbDeviceInfoObject.setDeviceName(deviceName);
            dbDeviceInfoObject.setDeviceInfo(deviceInfo);
            dbDeviceInfoObject.setStatus(status);
            dbDeviceInfoObject.setOpenCode(openCode);
            dbDeviceInfoObject.setCloseCode(closeCode);
            dbDeviceInfoObject.setMark(mark);

            list.add(dbDeviceInfoObject);
        }
        cursor.close();
        mSqLiteDatabase.close();
        return list;
    }
*/
}
