package dk.vongriffen.mycountdown;

import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import dk.vongriffen.mycountdown.T_Add_DialogFragment.*;
import dk.vongriffen.mycountdown.T_Edit_DialogFragment.*;
import java.util.*;

public class T_Activity extends Activity implements EditDialogListener, AddDialogListener
{
	boolean running = false;
	boolean pause = false;
	long id;
	
	ListView lv;
	TextView tv;
	Button btnStart, btnPause;
	
	String dbTableTitle, mytimers_txt[];

	//Integer[] mytimers;

	T_DBAdapter tdb;
	
	Context context;
	
	//SimpleCursorAdapter myCursor;
	//ArrayAdapter<String> aadapter;
	T_MyCustomAdapter customAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.t_run);

		context = getApplicationContext();
		lv = (ListView) findViewById(R.id.T_ListView);
		tv = (TextView) findViewById(R.id.T_TextView);
		btnStart = (Button) findViewById(R.id.T_bStart);
		btnPause = (Button) findViewById(R.id.T_bPause);
		
		dbTableTitle = getIntent().getStringExtra("dbTableTitle");

		tdb = new T_DBAdapter(this); 
		
		customAdapter = new T_MyCustomAdapter(context, tdb, dbTableTitle);
		
		final RunTimers rt = new RunTimers(context, tv, customAdapter.getTimers());
		
		btnStart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (running) {
					running = false;
					btnStart.setText(R.string.start);
					rt.stop();
				}
				else {
					//start
					running = true;
					btnStart.setText(R.string.stop);
					rt.begin();
				}
			}
		});

		btnPause.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (running) {
					//pause
					running = false;
					pause = true;
					btnPause.setText(R.string.cont);
					rt.pause();	
				}
				else {
					// continue
					running = true;
					pause = false;
					btnPause.setText(R.string.pause);
				}
			}
		});
		
		registerForContextMenu(lv);

		lv.setOnCreateContextMenuListener(this);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				Toast.makeText(getApplicationContext(), ""+pos, Toast.LENGTH_LONG).show(); 
			}
		});
		
		
		//populatelist();
		
		lv.setAdapter(customAdapter);
	}
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
	{
		getMenuInflater().inflate(R.menu.c_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	
		switch (item.getItemId()){
		
			case R.id.c_menu_delete:
				id = customAdapter.getItemId(info.position);
				tdb.open();
				tdb.deleteTimer(dbTableTitle,id);
				tdb.close();
				customAdapter = new T_MyCustomAdapter(context, tdb, dbTableTitle);
				lv.setAdapter(customAdapter);
				return true;

			case R.id.c_menu_edit:
				FragmentManager manager = getFragmentManager();
				T_Edit_DialogFragment t_edit_d = new T_Edit_DialogFragment();
				t_edit_d.show(manager, "T_Edit");
				id = customAdapter.getItemId(info.position);
				return true;

			default:
				return super.onContextItemSelected(item);
		}
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.a_menu,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {

			case R.id.menu_add:
				FragmentManager manager = getFragmentManager();
				T_Add_DialogFragment t_add_d = new T_Add_DialogFragment();
				t_add_d.show(manager, "T_Add");
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

/*	
	public void populatelist () {

		tdb.open();

		Cursor cursor = tdb.getAllTimers(dbTableTitle);
		
		mytimers = new Integer [cursor.getCount()];
		mytimers_txt = new String [cursor.getCount()];
		
		for (int i=0;cursor.moveToNext();i++) {
			mytimers[i] = cursor.getInt(2);
			mytimers_txt[i] = String.format("%02d:%02d", mytimers[i]/60, mytimers[i] % 60);
		}
		
		aadapter = new ArrayAdapter<String> (
			this,
			R.layout.t_singlerow,
			R.id.timer_singlerowTimeTextView, 
			mytimers_txt);
			
		lv.setAdapter(aadapter);

		tdb.close();
	}
*/	
	
	public void onEditDialogMessage(int minutes, int seconds)
	{
		tdb.open();
		int secs = minutes * 60 + seconds;
		String name = "";
		
		tdb.updateTimer(dbTableTitle, id, name, secs);
		tdb.close();

		//populatelist();
		
		customAdapter = new T_MyCustomAdapter(context, tdb, dbTableTitle);
		lv.setAdapter(customAdapter);
	}
	

	
	public void onAddDialogMessage(int minutes, int seconds)
	{
		
		tdb.open();
		int secs = minutes * 60 + seconds;
		String name = "";

		tdb.insertTimer(dbTableTitle, name, secs);
		//Toast.makeText(this, "onAddDialog", Toast.LENGTH_LONG).show();
		tdb.close();

		//populatelist();
		
		customAdapter = new T_MyCustomAdapter(context, tdb, dbTableTitle);
		lv.setAdapter(customAdapter);
	}
	
	
	
}

class T_MyCustomAdapter extends BaseAdapter
{
	ArrayList<SingleRow> list;
	Context context;
	Integer mytimers[];

	T_MyCustomAdapter (Context c, T_DBAdapter db, String dbTableTitle) {
		context = c;
		list = new ArrayList<SingleRow>();

		db.open();
		Cursor cursor = db.getAllTimers(dbTableTitle);
		mytimers = new Integer [cursor.getCount()];
		for (int i=0;cursor.moveToNext();i++) {
			mytimers[i] = cursor.getInt(2);
			String tmp = String.format("%02d:%02d", cursor.getInt(2)/60, cursor.getInt(2) % 60);
			list.add(new SingleRow(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), tmp));
			//mytimers_txt[i] = String.format("%02d:%02d", mytimers[i]/60, mytimers[i] % 60);
		}
		
		while (cursor.moveToNext()) {
			String tmp = String.format("%02d:%02d", cursor.getInt(2)/60, cursor.getInt(2) % 60);
			list.add(new SingleRow(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), tmp));
		}
		db.close();
	}
	Integer[] getTimers() {
		return mytimers;
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
