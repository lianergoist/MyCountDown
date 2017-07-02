package dk.vongriffen.mycountdown;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.widget.*;
import android.util.*;

public class IR_DBAdapter
{
    static final String KEY_ROWID = "_id";
    static final String KEY_NAME = "name";
    static final String KEY_TIME = "time";

    static final int COL_ROWID = 0;
    static final int COL_NAME = 1;
    static final int COL_TIME = 2;

    static final String TAG = "IR_DBAdapter";
    static final String DATABASE_NAME = "IR";
    static final int DATABASE_VERSION = 1;

    final Context context;

    DatabaseHelper IR_DBHelper;
    SQLiteDatabase db;

    public IR_DBAdapter(Context ctx)
    {
        this.context = ctx;
        IR_DBHelper = new DatabaseHelper(context);
    }

    public IR_DBAdapter open()
    {
        db = IR_DBHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {

        IR_DBHelper.close();
    }

    public long insertTimer(String table, String name, int secs)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_TIME, secs);
        return db.insert(table, null, initialValues);
    }

    public void createTable(String table)
    {
        String table_create = "create table " + table + " ("
                + KEY_ROWID + " integer primary key autoincrement, "
                + KEY_NAME + " text not null, "
                + KEY_TIME + " integer);";

        db.execSQL(table_create);
    }

	public void deleteTable (String table) 
	{	
		String table_delete = "drop table if exist " + table + ";";
        db.execSQL(table_delete);
	}
    public boolean deleteTimer(String table, long rowId)
    {
        return db.delete(table, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor getAllTimers(String table)
    {
        return db.query(table, new String[] {KEY_ROWID, KEY_NAME,
                KEY_TIME}, null, null, null, null, null);
    }

    public Cursor getTimer(String table, long rowId)
    {
        Cursor c = db.query(true, table, new String[] {KEY_ROWID,
                        KEY_NAME, KEY_TIME}, KEY_ROWID + "=" + rowId,
                null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public boolean updateTimer(String table, long rowId, String name, Integer secs)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_TIME, secs);
        return db.update(table, args, KEY_ROWID + "=" + rowId, null) > 0;
    }


    private static class DatabaseHelper extends SQLiteOpenHelper
    {
		DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
			Log.i(TAG, DATABASE_NAME + " created successfully");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            //Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
            //           + newVersion + ", which will destroy all old data");
        
		
			//TODO Handle upgrade
			
		}
    }
}

