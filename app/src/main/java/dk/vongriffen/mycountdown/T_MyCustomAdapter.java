import android.content.*;
import android.database.*;
import android.view.*;
import android.widget.*;
import dk.vongriffen.mycountdown.*;
import java.util.*;

class T_MyCustomAdapter extends BaseAdapter
{
	ArrayList<SingleRow> list;
	Context context;

	T_MyCustomAdapter (Context c, T_DBAdapter db, String dbTableTitle) {
		context = c;
		list = new ArrayList<SingleRow>();

		db.open();
		Cursor cursor = db.getAllTimers(dbTableTitle);

		while (cursor.moveToNext()) {
			String tmp = String.format("%02d:%02d", cursor.getInt(2)/60, cursor.getInt(2) % 60);
			list.add(new SingleRow(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), tmp));
		}
		db.close();
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
		View row = inflater.inflate(R.layout.t_singlerow, viewgroup, false);
		TextView name = (TextView) row.findViewById(R.id.timer_singlerowNameTextView);
		TextView secs = (TextView) row.findViewById(R.id.timer_singlerowTimeTextView);

		name.setText(list.get(i).name);
		secs.setText(list.get(i).time);
		return row;
	}


}	

class SingleRow {
	long id;
	String name;
	int seconds;
	String time;

	SingleRow(long id, String name, int secs, String t){
		this.id = id;
		this.name = name;
		this.seconds = secs;
		this.time = t;
	}
}
