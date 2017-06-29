package dk.vongriffen.mycountdown;

import android.content.*;
import android.database.*;
import android.view.*;
import android.widget.*;
import dk.vongriffen.mycountdown.*;
import java.util.*;

class TL_CustomAdapter extends BaseAdapter
{
	ArrayList<SingleRow> list;
	Context context;
	int mytimers[];

	TL_CustomAdapter (Context c, TL_DBAdapter db) {
		context = c;
		list = new ArrayList<SingleRow>();

		db.open();
		Cursor cursor = db.getAllTimers();
		mytimers = new int [cursor.getCount()];
		for (int i=0;cursor.moveToNext();i++) {
			mytimers[i] = cursor.getInt(1);
			String tmp = String.format("%02d:%02d", cursor.getInt(1)/60, cursor.getInt(1) % 60);
			list.add(new SingleRow(cursor.getLong(0), cursor.getInt(1), tmp));
		}

		while (cursor.moveToNext()) {
			String tmp = String.format("%02d:%02d", cursor.getInt(1)/60, cursor.getInt(1) % 60);
			list.add(new SingleRow(cursor.getLong(0), cursor.getInt(1), tmp));
		}
		db.close();
	}

	int[] getTimers() {
		return mytimers;
	}
	
	int getSeconds(int i) {
		return list.get(i).seconds;
	}

	@Override
	public int getCount()
	{
		return list.size();
	}

	@Override
	public Object getItem(int i)
	{
		return list.get(i);
	}

	@Override
	public long getItemId(int i)
	{
		return list.get(i).id;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewgroup)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.tl_singlerow, viewgroup, false);
		TextView time = (TextView) row.findViewById(R.id.tl_singlerowTitleTextView);

		time.setText(list.get(i).time);

		return row;
	}

	class SingleRow {
		long id;
		int seconds;
		String time;

		SingleRow(long id, int secs, String t){
			this.id = id;
			this.seconds = secs;
			this.time = t;
		}
	}
}	


