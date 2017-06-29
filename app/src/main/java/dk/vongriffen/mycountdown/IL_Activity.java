package dk.vongriffen.mycountdown;

import dk.vongriffen.mycountdown.IL_Add_DialogFragment.IL_Add_DialogListener;
import dk.vongriffen.mycountdown.IL_Edit_DialogFragment.IL_EditDialogListener;
import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;


public class IL_Activity extends Activity implements IL_Add_DialogListener, IL_EditDialogListener
{
	long id;
	
	ListView lvTimer;
	Button bCreateNewTL;
	
	IL_DBAdapter ildb;
	SimpleCursorAdapter myCursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.il_list);
		
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

		populatelist();
		
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
				id = myCursor.getItemId(info.position);
				ildb.open();
				ildb.deleteIntervalsList(id);
				ildb.close();
				populatelist();
				return true;
				
			case R.id.c_menu_edit:
				FragmentManager manager = getFragmentManager();
				IL_Edit_DialogFragment il_edit_d = new IL_Edit_DialogFragment();
				String s = getResources().getString(R.string.il_edit_dialog_title);
				il_edit_d.setDialogTitle(s);
				il_edit_d.show(manager, "IL_Edit");
				id = myCursor.getItemId(info.position);
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
			case R.id.a_menu_add:
				FragmentManager manager = getFragmentManager();
				IL_Add_DialogFragment il_add_d = new IL_Add_DialogFragment();
				String s = getResources().getString(R.string.il_add_dialog_title);
				il_add_d.setDialogTitle(s);
				il_add_d.show(manager, "IL_Add");
				return true;
				
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
	
	public void populatelist () {

		ildb.open();

		Cursor cursor = ildb.getAllIntervalsLists();
		String[] fromFieldNames = new String[] {IL_DBAdapter.KEY_TITLE, IL_DBAdapter.KEY_DESC};
		int[] toViewIDs = new int[] {R.id.il_singlerowTitleTextView, R.id.il_singlerowDescTextView};
		
		myCursor = new SimpleCursorAdapter(getBaseContext(), R.layout.il_singlerow, cursor, fromFieldNames, toViewIDs, 0);
		lvTimer= (ListView) findViewById(R.id.IL_ListView);
		lvTimer.setAdapter(myCursor);

		ildb.close();
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

		populatelist();
		
		Intent intent = new Intent(getApplicationContext(), IR_Activity.class);
		intent.putExtra("dbTableTitle", title);
		startActivity(intent);
	}


	@Override
	public void onEditDialogMessage(String title, String desc)
	{
		ildb.open();
		ildb.updateIntervalsList(id, title, desc);
		ildb.close();

		populatelist();

	}


}
