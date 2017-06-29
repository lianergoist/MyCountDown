package dk.vongriffen.mycountdown;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import dk.vongriffen.mycountdown.IR_Add_DialogFragment.*;
import dk.vongriffen.mycountdown.IR_Edit_DialogFragment.*;

public class IR_Activity extends Activity implements IR_EditDialogListener, IR_AddDialogListener
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
	
	RunTimers rt ;

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
		
		AssetManager assetmanager = getAssets();
		Typeface customfont = Typeface.createFromAsset(assetmanager, "fonts/digital-7-mono.ttf");
		tv.setTypeface(customfont);
		
		dbTableTitle = getIntent().getStringExtra("dbTableTitle");

		irdb = new IR_DBAdapter(this); 
		
		customAdapter = new IR_CustomAdapter(context, irdb, dbTableTitle);
		
		
		registerForContextMenu(lv);

		lv.setOnCreateContextMenuListener(this);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				//Toast.makeText(getApplicationContext(), ""+pos, Toast.LENGTH_LONG).show(); 
			}
		});
		
		lv.setAdapter(customAdapter);
		
		rt = new RunTimers(context, tv, customAdapter.getTimers());
		
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
				IR_Edit_DialogFragment t_edit_d = new IR_Edit_DialogFragment();
				String s = getResources().getString(R.string.t_edit_dialog_title);
				t_edit_d.setDialogTitle(s);
				t_edit_d.show(manager, "IR_Edit");
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
				IR_Add_DialogFragment t_add_d = new IR_Add_DialogFragment();
				String s = getResources().getString(R.string.ir_add_dialog_title);
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
		
	public void IR_onEditDialogMessage(String name, int minutes, int seconds)
	{
		irdb.open();
		int secs = minutes * 60 + seconds;

		irdb.updateTimer(dbTableTitle, id, name, secs);
		irdb.close();
		
		customAdapter = new IR_CustomAdapter(context, irdb, dbTableTitle);
		lv.setAdapter(customAdapter);
	}
	

	
	public void IR_onAddDialogMessage(String name, int minutes, int seconds)
	{
		irdb.open();
		int secs = minutes * 60 + seconds;
		irdb.insertTimer(dbTableTitle, name, secs);
		irdb.close();
		
		customAdapter = new IR_CustomAdapter(context, irdb, dbTableTitle);
		lv.setAdapter(customAdapter);
	}
}


