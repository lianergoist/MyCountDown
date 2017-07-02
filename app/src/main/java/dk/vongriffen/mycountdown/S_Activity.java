package dk.vongriffen.mycountdown;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import dk.vongriffen.mycountdown.TL_Add_DialogFragment.*;

public class S_Activity extends Activity implements TL_AddDialogListener
{
	TextView tv;
	Integer time[]={0};
	boolean running=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.s_layout);
		
		setTitle(getResources().getString(R.string.a_menu_mode_simple));
		
		Context context = getBaseContext();
		
		tv = (TextView) findViewById(R.id.S_TextView);
		
		AssetManager assetmanager = getAssets();
		Typeface customfont = Typeface.createFromAsset(assetmanager, "fonts/digital-7-mono.ttf");
		tv.setTypeface(customfont);
		
		final RunTimers rt = new RunTimers(context, tv, time);
		
		tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view)
				{
					if (running) {
						rt.pause();
						running= false;
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
					FragmentManager manager = getFragmentManager();
					TL_Add_DialogFragment t_add_d = new TL_Add_DialogFragment();
					String s = getResources().getString(R.string.t_add_dialog_title);
					t_add_d.setDialogTitle(s);
					t_add_d.show(manager, "T_Add");
					
					return true;
				}
		});
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.a_menu,menu);
		menu.findItem(R.id.a_menu_add).setVisible(false);
		menu.findItem(R.id.a_menu_mode_simple).setChecked(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent intent;
		
		switch (item.getItemId()) {

			case R.id.a_menu_add:
				//
				return true;
			case R.id.a_menu_mode_simple:
				Toast.makeText(this, "@string/alreadyInSimpleMode", Toast.LENGTH_LONG).show();
				return true;
				
			case R.id.a_menu_mode_timers:
				item.setChecked(true);
				intent = new Intent(getApplicationContext(), TL_Activity.class);
				startActivity(intent);
				return true;
				
			case R.id.a_menu_mode_intervals:
				item.setChecked(true);
				intent = new Intent(getApplicationContext(), IL_Activity.class);
				startActivity(intent);
				return true;
				
			case R.id.a_menu_help:
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}


	public void TL_onAddDialogMessage(int minutes, int seconds)
	{
		time[0] = minutes*60+seconds;
		tv.setText(String.format("%02d:%02d", minutes, seconds));
	}

	
}
