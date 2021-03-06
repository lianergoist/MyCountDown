package dk.vongriffen.mycountdown;

import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import dk.vongriffen.mycountdown.IR_Add_DialogFragment.*;
import dk.vongriffen.mycountdown.IR_Edit_DialogFragment.*;
import android.support.v7.app.*;
import android.support.v4.app.*;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.*;
import android.os.PowerManager.*;

public class IR_Activity extends AppCompatActivity implements IR_EditDialogListener, IR_AddDialogListener
{
	boolean running = false;
	boolean pause = false;
	//long id;
	
	ListView lv;
	TextView tv;
	Button btnStart, btnPause;
	
	String dbTableTitle;
	
	Integer[] secs = {10};

	IR_DBAdapter irdb;
	
	Context context;
	
	RunTimers rt ;

	IR_CustomAdapter customAdapter;
	
	WakeLock wakeLock;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ir);
		
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		
		context = getBaseContext();
		
		lv = (ListView) findViewById(R.id.IR_ListView);
		tv = (TextView) findViewById(R.id.IR_TextView);
		btnStart = (Button) findViewById(R.id.IR_bStart);
		btnPause = (Button) findViewById(R.id.IR_bPause);
		
		AssetManager assetmanager = getAssets();
		Typeface customfont = Typeface.createFromAsset(assetmanager, "fonts/digital-7-mono.ttf");
		tv.setTypeface(customfont);
		
		PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,"MyWakelockTag");
		
		
		dbTableTitle = getIntent().getStringExtra("dbTableTitle");

		irdb = new IR_DBAdapter(this); 
		
		customAdapter = new IR_CustomAdapter(context, irdb, dbTableTitle);
		
		setTitle(getResources().getString(R.string.a_menu_mode_intervals)+" - "+dbTableTitle);
		
		registerForContextMenu(lv);

		lv.setOnCreateContextMenuListener(this);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
			
			}
		});
		
		lv.setAdapter(customAdapter);
		
		rt = new RunTimers(context, tv, customAdapter.getTimers());
		secs = customAdapter.getTimers();
		
		tv.setOnClickListener(new OnClickListener() {

				
				@Override
				public void onClick(View view)
				{
					if (running) {
						if (pause) {
							wakeLock.acquire();
							rt.cont();
							pause=false;
							running=true;
						}
						else {
							wakeLock.release();
							rt.pause();
							pause=true;
						}
					}
					else {
						wakeLock.acquire();
						rt.begin();
						running=true;
					}
				}
			});

		tv.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View p1)
				{
					wakeLock.release();
					rt.stop();
					tv.setText(String.format("%02d:%02d", secs[0]/60, secs[0]%60));
					running=false;
					pause=false;
					return true;
				}
			});	
			
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					FragmentManager manager = getSupportFragmentManager();
				IR_Add_DialogFragment t_add_d = new IR_Add_DialogFragment();
				String s = getResources().getString(R.string.ir_add_dialog_title);
				t_add_d.setDialogTitle(s);
				t_add_d.show(manager, "IR_Add");
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
		long id;
	
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
				id = customAdapter.getItemId(info.position);
				String name = customAdapter.getName(info.position);
				int seconds = customAdapter.getSeconds(info.position);
				
				FragmentManager manager = getSupportFragmentManager();
				IR_Edit_DialogFragment ir_edit_d = new IR_Edit_DialogFragment(id, name, seconds);
				String s = getResources().getString(R.string.t_edit_dialog_title);
				ir_edit_d.setDialogTitle(s);
				ir_edit_d.show(manager, "IR_Edit");
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

//			case R.id.a_menu_add:
//				FragmentManager manager = getSupportFragmentManager();
//				IR_Add_DialogFragment ir_add_d = new IR_Add_DialogFragment();
//				String s = getResources().getString(R.string.ir_add_dialog_title);
//				ir_add_d.setDialogTitle(s);
//				ir_add_d.show(manager, "IR_Add");
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
				Toast.makeText(this, getResources().getString(R.string.alreadyInIntervalsMode), Toast.LENGTH_LONG).show();
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
		
	public void IR_onEditDialogMessage(long id, String name, int minutes, int seconds)
	{
		irdb.open();
		int secs = minutes * 60 + seconds;

		irdb.updateTimer(dbTableTitle, id, name, secs);
		irdb.close();
		
		customAdapter = new IR_CustomAdapter(context, irdb, dbTableTitle);
		lv.setAdapter(customAdapter);
		rt = new RunTimers(context, tv, customAdapter.getTimers());
	}
	

	
	public void IR_onAddDialogMessage(String name, int minutes, int seconds)
	{
		irdb.open();
		int secs = minutes * 60 + seconds;
		irdb.insertTimer(dbTableTitle, name, secs);
		irdb.close();
		
		customAdapter = new IR_CustomAdapter(context, irdb, dbTableTitle);
		lv.setAdapter(customAdapter);
		rt = new RunTimers(context, tv, customAdapter.getTimers());
	}
}


