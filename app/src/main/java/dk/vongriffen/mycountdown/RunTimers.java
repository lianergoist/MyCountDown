package dk.vongriffen.mycountdown;

import android.content.*;
import android.media.*;
import android.os.*;
import android.widget.*;
import android.icu.text.*;

public class RunTimers
 {
	static final int UPDATE_INTERVAL = 1000;
	
	MediaPlayer mp;
	CountDownTimer cdt;
	TextView tv;
	Integer[] t;
	int seconds, minutes, numTimer;
	long milliSecondsRemaining;
	
	Context c;
	
	public RunTimers(Context context, TextView textview, Integer [] timers)
	{
		c = context;
		tv = textview;
		t = timers;
		numTimer = 0;
	}

	void begin() {
		run(t[numTimer]*1000);
		cdt.start();
	}

	void stop(){
		cdt.cancel();
	}

	void pause() {
		cdt.cancel();
	}

	void cont() {
		run(milliSecondsRemaining);
		cdt.start();
	}

	void jumpToTimer(int index) {
		numTimer = index;
		minutes = (int) t[numTimer] / 60;
		seconds = (int) t[numTimer] % 60 ;
		tv.setText(String.format("%02d:%02d", minutes, seconds));
	}

	private void run(long milliSecondsToRun){

		cdt = new CountDownTimer(milliSecondsToRun, UPDATE_INTERVAL) {

			public void onTick(long millisUntilFinished) {
				milliSecondsRemaining = millisUntilFinished;
				minutes = (int) millisUntilFinished/1000 / 60 ;
				seconds = (int) millisUntilFinished/1000 % 60;
				tv.setText(String.format("%02d:%02d", minutes, seconds));
			}

			public void onFinish() {
				if (numTimer < t.length) {
					mp = MediaPlayer.create(c, R.raw.air);
					mp.start();
					numTimer++;
					begin();
				} else {
					mp.release();
					mp = null;
					numTimer = 0;
					tv.setText("done!");
				}
				
			}
		};
	}
}
