package dk.vongriffen.mycountdown;

import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import dk.vongriffen.mycountdown.IR_Add_DialogFragment.*;
import dk.vongriffen.mycountdown.IR_Edit_DialogFragment.*;
import java.util.*;

public class IR_Activity extends Activity implements EditDialogListener, AddDialogListener
{
	boolean running = false;
	boolean pause = false;
	long id;
	
	ListView lv;
	TextView tv;
	Button btnStart, btnPause;
	
	String dbTableTitle;

	IR_DBAdapter irdb;
	
	Context context;

	IR_CustomAdapter customAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_layout);

		context = getBaseContext();
		lv = (ListView) findViewById(R.id.IR_ListView);
		tv = (TextView) findViewById(R.id.IR_TextView);
		btnStart = (Button) findViewById(R.id.IR_bStart);
		btnPause = (Button) findViewById(R.id.IR_bPause);
		
		dbTableTitle = getIntent().getStringExtra("dbTableTitle");

		irdb = new IR_DBAdapter(this); 
		
		customAdapter = new IR_CustomAdapter(context, irdb, dbTableTitle);
		
		final RunTimers rt = new RunTimers(context, tv, customAdapter.getTimers());
		
		registerForContextMenu(lv);

		lv.setOnCreateContextMenuListener(this);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				Toast.makeText(getApplicationContext(), ""+pos, Toast.LENGTH_LONG).show(); 
			}
		});
		
		lv.setAdapter(customAdapter);
		
		
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
				irdb.open();
				irdb.deleteTimer(dbTableTitle,id);
				irdb.close();
				customAdapter = new IR_CustomAdapter(context, irdb, dbTableTitle);
				lv.setAdapter(customAdapter);
				return true;

			case R.id.c_menu_edit:
				FragmentManager manager = getFragmentManager();
				T_Edit_DialogFragment t_edit_d = new T_Edit_DialogFragment();
				String s = getResources().getString(R.string.edit_dialog_title);
				t_edit_d.setDialogTitle(s);
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
		menu.findItem(R.id.a_menu_mode_intervals).setVisible(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent intent;
		
		switch (item.getItemId()) {

			case R.id.a_menu_add:
				FragmentManager manager = getFragmentManager();
				IR_Add_DialogFragment t_add_d = new IR_Add_DialogFragment();
				String s = getResources().getString(R.string.add_dialog_title);
				t_add_d.setDialogTitle(s);
				t_add_d.show(manager, "T_Add");
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
				item.setChecked(true);
				intent = new Intent(getApplicationContext(), IL_Activity.class);
				startActivity(intent);
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
		
	public void onEditDialogMessage(int minutes, int seconds)
	{
		irdb.open();
		int secs = minutes * 60 + seconds;
		String name = "";
		
		irdb.updateTimer(dbTableTitle, id, name, secs);
		irdb.close();
		
		customAdapter = new IR_CustomAdapter(context, irdb, dbTableTitle);
		lv.setAdapter(customAdapter);
	}
	

	
	public void onAddDialogMessage(int minutes, int seconds)
	{
		irdb.open();
		int secs = minutes * 60 + seconds;
		String name = "";
		irdb.insertTimer(dbTableTitle, name, secs);
		irdb.close();
		
		customAdapter = new IR_CustomAdapter(context, irdb, dbTableTitle);
		lv.setAdapter(customAdapter);
	}
}


