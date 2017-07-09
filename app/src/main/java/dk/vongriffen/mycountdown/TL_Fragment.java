package dk.vongriffen.mycountdown;

import android.view.*;
import android.os.*;
import android.widget.*;
import android.app.*;


public class TL_Fragment extends Fragment
{
	TL_Communicator communicator;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.frag_tl, container, false);
			
		return view;
		
	}
	
	
	public void setCommunicator(TL_Communicator communicator) 
	{
		this.communicator = communicator;
	}
	
	public interface TL_Communicator 
	{
		public void tl_respond(int index);
	}
	
}
