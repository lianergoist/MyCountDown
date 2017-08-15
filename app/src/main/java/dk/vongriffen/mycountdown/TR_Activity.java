package dk.vongriffen.mycountdown;

import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.Toolbar;
import android.view.View.*;

public class TR_Activity extends AppCompatActivity
{
	boolean running = false;
	boolean pause = false;

	TextView tv;
	Button btnStart, btnPause;

	Integer[] secs = {10};

	Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tr);

		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		
		secs[0] = getIntent().getIntExtra("seconds", 8);
		
		context = getBaseContext();
		
		setTitle(getResources().getString(R.string.a_menu_mode_timers));
		
		tv = (TextView) findViewById(R.id.TR_TextView);
		btnStart = (Button) findViewById(R.id.TR_bStart);
		btnPause = (Button) findViewById(R.id.TR_bPause);

		AssetManager assetmanager = getAssets();
		Typeface customfont = Typeface.createFromAsset(assetmanager, "fonts/digital-7-mono.ttf");
		tv.setTypeface(customfont);
		
		tv.setText(String.format("%02d:%02d", secs[0]/60, secs[0]%60));
		
		final RunTimers rt = new RunTimers(context, tv, secs);

		tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view)
				{
					if (running) {
						if (pause) {
							rt.cont();
							pause=false;
							running=true;
						}
						else {
							rt.pause();
							pause=true;
						}
					}
					else {
						rt.begin();
						running=true;
					}
				}
			});

		tv.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View p1)
				{
					rt.stop();
					tv.setText(String.format("%02d:%02d", secs[0]/60, secs[0]%60));
					running=false;
					pause=false;
					return true;
				}
			});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.a_menu,menu);
		//menu.findItem(R.id.a_menu_add).setVisible(false);
		menu.findItem(R.id.a_menu_mode_timers).setChecked(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent intent;

		switch (item.getItemId()) {

//			case R.id.a_menu_add:
//				FragmentManager manager = getFragmentManager();
//				TL_Add_DialogFragment t_add_d = new TL_Add_DialogFragment();
//				String s = getResources().getString(R.string.t_add_dialog_title);
//				t_add_d.setDialogTitle(s);
//				t_add_d.show(manager, "TR_Add");
//				return true;
//
			case R.id.a_menu_mode_simple:
				intent = new Intent(getApplicationContext(), S_Activity.class);
				startActivity(intent);
				return true;

			case R.id.a_menu_mode_timers:
				Toast.makeText(this,getResources().getString(R.string.alreadyInTimersMode), Toast.LENGTH_LONG).show();
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

}


