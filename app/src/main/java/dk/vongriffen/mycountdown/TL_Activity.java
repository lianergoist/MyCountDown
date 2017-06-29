package dk.vongriffen.mycountdown;

import dk.vongriffen.mycountdown.T_Add_DialogFragment.T_AddDialogListener;
import dk.vongriffen.mycountdown.T_Edit_DialogFragment.T_EditDialogListener;
import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;


public class TL_Activity extends Activity implements T_EditDialogListener, T_AddDialogListener
{
	long id;
	
	Context context;

	ListView lvTimer;
	Button bCreateNewTL;

	TL_DBAdapter tldb;
	
	TL_CustomAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tl_list);
		
		context = getBaseContext();

		tldb = new TL_DBAdapter(this);
		
		adapter = new TL_CustomAdapter(context, tldb);

		lvTimer = (ListView) findViewById(R.id.TL_ListView);
		
		lvTimer.setAdapter(adapter);

		registerForContextMenu(lvTimer);

		lvTimer.setOnCreateContextMenuListener(this);

		lvTimer.setOnCreateContextMenuListener(this);

		lvTimer.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
					Toast.makeText(getApplicationContext(), ""+pos, Toast.LENGTH_LONG).show(); 
				}
			});

		
		lvTimer.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
					//TextView tv = (TextView) view.findViewById(R.id.tl_singlerowTitleTextView);
					int secs = adapter.getSeconds(pos);
					Intent intent = new Intent(getApplicationContext(), TR_Activity.class);
					intent.putExtra("seconds", secs);
					startActivity(intent);
				}
			});
			
		
		
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
				id = adapter.getItemId(info.position);
				tldb.open();
				tldb.deleteTimer(id);
				tldb.close();
				
				adapter = new TL_CustomAdapter(context, tldb);
				lvTimer.setAdapter(adapter);
				
				return true;

			case R.id.c_menu_edit:
				FragmentManager manager = getFragmentManager();
				T_Edit_DialogFragment tl_edit_d = new T_Edit_DialogFragment();
				//String s = getResources().getString(R.string.edit_dialog_title);
				tl_edit_d.setDialogTitle("");
				tl_edit_d.show(manager, "TL_Edit");
				id = adapter.getItemId(info.position);
				return true;

			default:
				return super.onContextItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.a_menu, menu);
		menu.findItem(R.id.a_menu_mode_timers).setChecked(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent intent;

		switch (item.getItemId()) {
			case R.id.a_menu_add:
				FragmentManager manager = getFragmentManager();
				T_Add_DialogFragment t_add_d = new T_Add_DialogFragment();
				//String s = getResources().getString(R.string.add_dialog_title);
				t_add_d.setDialogTitle("");
				t_add_d.show(manager, "T_Add");
				return true;

			case R.id.a_menu_mode_simple:
				intent = new Intent(getApplicationContext(), S_Activity.class);
				startActivity(intent);
				return true;

			case R.id.a_menu_mode_timers:
				Toast.makeText(this, "@string/alreadyInTimersMode", Toast.LENGTH_LONG).show();
				return true;

			case R.id.a_menu_mode_intervals:
				intent = new Intent(getApplicationContext(), IL_Activity.class);
				startActivity(intent);
				return true;

			case R.id.a_menu_help:
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void OnGroupItemClick (MenuItem item){
		if (item.isChecked()) {
			item.setChecked(false);
		}
		else {
			item.setChecked(true);
		}
	}

	@Override
	public void T_onAddDialogMessage(int minutes, int seconds)
	{
		
		tldb.open();
		tldb.insertTimer(minutes*60+seconds);
		tldb.close();
		
		adapter = new TL_CustomAdapter(context, tldb);
		lvTimer.setAdapter(adapter);
		
		Intent intent = new Intent(getApplicationContext(), TR_Activity.class);
		intent.putExtra("seconds", minutes*60+seconds);
		startActivity(intent);
	}


	@Override
	public void T_onEditDialogMessage(int minutes, int seconds)
	{
		tldb.open();
		int secs = minutes * 60 + seconds;

		tldb.updateTimer(id, secs);
		tldb.close();

		adapter = new TL_CustomAdapter(context, tldb);
		lvTimer.setAdapter(adapter);
	}


}
