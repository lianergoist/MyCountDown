package dk.vongriffen.mycountdown;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.widget.*;
import android.util.*;

public class TL_DBAdapter
{
    static final String KEY_ROWID = "_id";
    static final String KEY_TIME = "time";

    static final int COL_ROWID = 0;
    static final int COL_TIME = 1;

    static final String TAG = "TL_DBAdapter";
    static final String DATABASE_NAME = "TL";
	static final String DATABASE_TABLE = "timers_list";
    static final int DATABASE_VERSION = 1;

    final Context context;

    DatabaseHelper TL_DBHelper;
    SQLiteDatabase db;

    public TL_DBAdapter(Context ctx)
    {
        this.context = ctx;
        TL_DBHelper = new DatabaseHelper(context);
    }

    public TL_DBAdapter open()
    {
        db = TL_DBHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {

        TL_DBHelper.close();
    }

    public long insertTimer(int secs)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TIME, secs);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteTimer(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor getAllTimers()
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TIME}, null, null, null, null, null);
    }

    public Cursor getTimer(long rowId)
    {
        Cursor c = db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TIME}, KEY_ROWID 
								+ "=" + rowId, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public boolean updateTimer(long rowId, int secs)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_TIME, secs);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
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
			String table_create = "create table " + DATABASE_TABLE + " ("
				+ KEY_ROWID + " integer primary key autoincrement, "
				+ KEY_TIME + " integer);";

			db.execSQL(table_create);
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

