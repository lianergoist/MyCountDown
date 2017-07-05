package dk.vongriffen.mycountdown;

import android.os.*;
import android.view.*;
import android.widget.*;
import dk.vongriffen.mycountdown.*;
import android.support.v4.app.*;


public class TL_Add_DialogFragment extends DialogFragment
{
	static final int MIN_VAL = 0;
	static final int MAX_VAL = 59;

	NumberPicker npMinutes, npSeconds;
	Button bCancel,bAddtimer;
	static String dialogTitle;


	public interface TL_AddDialogListener {
		public void TL_onAddDialogMessage(int minutes, int seconds);

	}

	public TL_Add_DialogFragment () {
	}

	public void setDialogTitle(String title) {

		dialogTitle = title;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(
			R.layout.tl_add_dialog, null);

		setCancelable(false);

		npMinutes = (NumberPicker) view.findViewById(R.id.TL_npMinutes);
		npSeconds = (NumberPicker) view.findViewById(R.id.TL_npSeconds);

		bCancel = (Button) view.findViewById(R.id.TL_addtimertolistCancelButton);
		bAddtimer =(Button) view.findViewById(R.id.TL_addtimertolistAddButton);

		npMinutes.setMinValue(MIN_VAL);
		npMinutes.setMaxValue(MAX_VAL);
		npMinutes.setWrapSelectorWheel(true);
		npMinutes.setValue(0);

		npSeconds.setMinValue(MIN_VAL);
		npSeconds.setMaxValue(MAX_VAL);
		npSeconds.setWrapSelectorWheel(true);
		npSeconds.setValue(0);

		bAddtimer.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					TL_AddDialogListener activity = (TL_AddDialogListener) getActivity();
					activity.TL_onAddDialogMessage(npMinutes.getValue(), npSeconds.getValue());
					dismiss();
				}
			});

		bCancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dismiss();
				}
			});
			
		getDialog().setTitle(dialogTitle);
		
		return view;
	}
}
