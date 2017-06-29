package dk.vongriffen.mycountdown;
import android.os.*;
import android.view.*;

public class NewRunTimers
{
	int[] timer;
	View view;
	
	Handler handler = new Handler();
	
 	NewRunTimers(View v, int[] t){
		this.timer = t;
		this.view = v;
		
	}
	
	
	private Runnable r = new Runnable() {
		
		@Override
		public void run() {
			
			
			
			handler.postDelayed(this, 1000);
		}
		
	};
	
}
