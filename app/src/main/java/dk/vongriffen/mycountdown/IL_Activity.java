package dk.vongriffen.mycountdown;

import dk.vongriffen.mycountdown.IL_Add_DialogFragment.IL_Add_DialogListener;
import dk.vongriffen.mycountdown.IL_Edit_DialogFragment.IL_EditDialogListener;
import android.content.*;
import android.database.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.printservice.*;
import android.support.v7.app.*;
import android.support.v4.app.*;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.*;


public class IL_Activity extends AppCompatActivity implements IL_Add_DialogListener, IL_EditDialogListener
{
	//long id;
	
	Context context;
	
	ListView lvTimer;
	Button bCreateNewTL;
	
	IL_DBAdapter ildb;
	//SimpleCursorAdapter myCursor;
	IL_CustomAdapter customAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.il_list);
		
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		
		setTitle(getResources().getString(R.string.a_menu_mode_intervals));
		
		context = getBaseContext();
		
		ildb = new IL_DBAdapter(this);
		
		lvTimer = (ListView) findViewById(R.id.IL_ListView);

		registerForContextMenu(lvTimer);
		
		lvTimer.setOnCreateContextMenuListener(this);
	
		lvTimer.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				TextView tv = (TextView) view.findViewById(R.id.il_singlerowTitleTextView);
				String title = tv.getText().toString();
				Intent intent = new Intent(getApplicationContext(), IR_Activity.class);
				intent.putExtra("dbTableTitle", title);
				startActivity(intent);
			}
		});

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					FragmentManager manager = getSupportFragmentManager();
					IL_Add_DialogFragment il_add_d = new IL_Add_DialogFragment();
					String s = getResources().getString(R.string.il_add_dialog_title);
					il_add_d.setDialogTitle(s);
					il_add_d.show(manager, "IL_Add");
				}
		});
			
		customAdapter = new IL_CustomAdapter(context, ildb);
		lvTimer.setAdapter(customAdapter);
		
		
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
		long id;
		
		switch (item.getItemId()){
			
			case R.id.c_menu_delete:
				id = customAdapter.getItemId(info.position);
				
				String table = customAdapter.getTitle(info.position);
				IR_DBAdapter irdb = new IR_DBAdapter(context);
				irdb.open();
				irdb.deleteTable(table);
				irdb.close();
				
				ildb.open();
				ildb.deleteIntervalsList(id);
				ildb.close();
				
				customAdapter = new IL_CustomAdapter(context, ildb);
				lvTimer.setAdapter(customAdapter);
				
				return true;
				
			case R.id.c_menu_edit:
				id = customAdapter.getItemId(info.position);
				String title = customAdapter.getTitle(info.position);
				String desc = customAdapter.getDesc(info.position);
				FragmentManager manager = getSupportFragmentManager();
				IL_Edit_DialogFragment il_edit_d = new IL_Edit_DialogFragment(id, title, desc);
				String s = getResources().getString(R.string.il_edit_dialog_title);
				il_edit_d.setDialogTitle(s);
				il_edit_d.show(manager, "IL_Edit");
				return true;
				
			default:
				return super.onContextItemSelected(item);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.a_menu, menu);
		menu.findItem(R.id.a_menu_mode_intervals).setChecked(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent intent;
		
		switch (item.getItemId()) {
//			case R.id.a_menu_add:
//				FragmentManager manager = getSupportFragmentManager();
//				IL_Add_DialogFragment il_add_d = new IL_Add_DialogFragment();
//				String s = getResources().getString(R.string.il_add_dialog_title);
//				il_add_d.setDialogTitle(s);
//				il_add_d.show(manager, "IL_Add");
//				return true; 
				
			case R.id.a_menu_mode_simple:
				intent = new Intent(getApplicationContext(), S_Activity.class);
				startActivity(intent);
				return true;
				
			case R.id.a_menu_mode_timers:
				intent = new Intent(getApplicationContext(), TL_Activity.class);
				startActivity(intent);
				return true;
				
			case R.id.a_menu_mode_intervals:
				Toast.makeText(this, "@string/alreadyInIntervalsMode", Toast.LENGTH_LONG).show();
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
	public void onAddDialogMessage(String title, String desc)
	{
		ildb.open();
		ildb.insertIntervalsList(title, desc);
		ildb.close();
		
		IR_DBAdapter tdb = new IR_DBAdapter(this);
		tdb.open();
		tdb.createTable(title);
		tdb.close();

		customAdapter = new IL_CustomAdapter(context, ildb);
		lvTimer.setAdapter(customAdapter);
		
		Intent intent = new Intent(getApplicationContext(), IR_Activity.class);
		intent.putExtra("dbTableTitle", title);
		startActivity(intent);
	}


	@Override
	public void IL_onEditDialogMessage(long id, String title, String desc)
	{
		ildb.open();
		ildb.updateIntervalsList(id, title, desc);
		ildb.close();

		customAdapter = new IL_CustomAdapter(context, ildb);
		lvTimer.setAdapter(customAdapter);

	}


}
