package dk.vongriffen.mycountdown;

import dk.vongriffen.mycountdown.TL_New_DialogFragment.NewDialogListener;
import dk.vongriffen.mycountdown.TL_Edit_DialogFragment.EditDialogListener;
import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;


public class TL_Activity extends Activity implements NewDialogListener, EditDialogListener
{
	long id;
	
	ListView lvTimer;
	Button bCreateNewTL;
	
	TL_DBAdapter tldb;
	//ListAdapter la;
	SimpleCursorAdapter myCursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tl_list);
		
		tldb = new TL_DBAdapter(this);
		
		lvTimer = (ListView) findViewById(R.id.TL_ListView);

		registerForContextMenu(lvTimer);
		
		lvTimer.setOnCreateContextMenuListener(this);
	
		lvTimer.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				TextView tv = (TextView) view.findViewById(R.id.timerlist_singlerowTitleTextView);
				String title = tv.getText().toString();
				Intent intent = new Intent(getApplicationContext(), T_Activity.class);
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
				tldb.open();
				tldb.deleteTimerList(id);
				tldb.close();
				populatelist();
				return true;
				
			case R.id.c_menu_edit:
				FragmentManager manager = getFragmentManager();
				TL_Edit_DialogFragment tl_edit_d = new TL_Edit_DialogFragment();
				String s = getResources().getString(R.string.edit_dialog_title);
				tl_edit_d.setDialogTitle(s);
				tl_edit_d.show(manager, "TL_Edit");
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
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
			
			case R.id.menu_add:
				FragmentManager manager = getFragmentManager();
				TL_New_DialogFragment tl_new_d = new TL_New_DialogFragment();
				String s = getResources().getString(R.string.new_dialog_title);
				tl_new_d.setDialogTitle(s);
				tl_new_d.show(manager, "TL_New");
				return true;
		
			default:
				return super.onOptionsItemSelected(item);
		}
	} 

	public void populatelist () {

		tldb.open();

		Cursor cursor = tldb.getAllTimerLists();
		String[] fromFieldNames = new String[] {TL_DBAdapter.KEY_TITLE, TL_DBAdapter.KEY_DESC};
		int[] toViewIDs = new int[] {R.id.timerlist_singlerowTitleTextView, R.id.timerlist_singlerowDescTextView};
		
		myCursor = new SimpleCursorAdapter(getBaseContext(), R.layout.tl_singlerow, cursor, fromFieldNames, toViewIDs, 0);
		lvTimer= (ListView) findViewById(R.id.TL_ListView);
		lvTimer.setAdapter(myCursor);

		tldb.close();
	}


	@Override
	public void onNewDialogMessage(String title, String desc)
	{
		tldb.open();
		tldb.insertTimerList(title, desc);
		tldb.close();
		
		T_DBAdapter tdb = new T_DBAdapter(this);
		tdb.open();
		tdb.createTable(title);
		tdb.close();

		populatelist();
		
		Intent intent = new Intent(getApplicationContext(), T_Activity.class);
		intent.putExtra("dbTableTitle", title);
		startActivity(intent);
	}


	@Override
	public void onEditDialogMessage(String title, String desc)
	{
		tldb.open();
		tldb.updateTimerList(id, title, desc);
		tldb.close();

		populatelist();

	}


}
