package dk.vongriffen.mycountdown;

import android.content.*;
import android.database.*;
import android.view.*;
import android.widget.*;
import dk.vongriffen.mycountdown.*;
import java.util.*;

class IL_CustomAdapter extends BaseAdapter
{
	ArrayList<SingleRow> list;
	Context context;

	IL_CustomAdapter (Context c, IL_DBAdapter db) {
		context = c;
		list = new ArrayList<SingleRow>();

		db.open();
		Cursor cursor = db.getAllIntervalsLists();
		while (cursor.moveToNext()) {
			list.add(new SingleRow(cursor.getLong(0), cursor.getString(1), cursor.getString(2)));
		}
		db.close();
	}

	String getDesc(int i) {
		return list.get(i).description;
	}

	String getTitle(int i) {
		return list.get(i).title;
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
		View row = inflater.inflate(R.layout.il_singlerow, viewgroup, false);
		TextView tvTitle = (TextView) row.findViewById(R.id.il_singlerowTitleTextView);
		TextView tvDesc = (TextView) row.findViewById(R.id.il_singlerowDescTextView);

		tvTitle.setText(list.get(i).title);
		tvDesc.setText(list.get(i).description);

		return row;
	}

	class SingleRow {
		long id;
		String title;
		String description;

		SingleRow(long id, String title, String desc){
			this.id = id;
			this.title = title;
			this.description = desc;
		}
	}
}	


