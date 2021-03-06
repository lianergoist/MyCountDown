package dk.vongriffen.mycountdown;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.widget.*;
import android.util.*;

public class IL_DBAdapter
{
    static final String KEY_ROWID = "_id";
    static final String KEY_TITLE = "title";
    static final String KEY_DESC = "description";

    static final String TAG = "IL_DBAdapter";
	static final String DATABASE_NAME = "IL1";
	static final String DATABASE_TABLE = "intervals";
    static final int DATABASE_VERSION = 1;

    static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " ("
                    + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_TITLE + " text not null, "
                    + KEY_DESC + " text);";

    final Context context;

    DatabaseHelper TL_DBHelper;
    SQLiteDatabase db;

    public IL_DBAdapter(Context ctx)
    {
        this.context = ctx;
        TL_DBHelper = new DatabaseHelper(context);
    }

    public IL_DBAdapter open()
    {
        db = TL_DBHelper.getWritableDatabase();
		return this;
    }

    public void close()
    {

        TL_DBHelper.close();
    }

    public long insertIntervalsList(String title, String desc)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_DESC, desc);
		return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteIntervalsList(long rowId)
    {
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor getAllIntervalsLists()
    {
		return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
								KEY_DESC}, null, null, null, null, null);
    }

    public Cursor getIntervalsList(long rowId)
    {
		Cursor c = db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                        KEY_TITLE, KEY_DESC}, KEY_ROWID + "=" + rowId,
                        null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public boolean updateIntervalsList(long rowId, String title, String desc)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_DESC, desc);
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
            try {
                db.execSQL(DATABASE_CREATE);
                Log.w(TAG, "create ok");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            //   Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
            //           + newVersion + ", which will destroy all old data");

            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE + ";");
            onCreate(db);
        }
    }
}

