package dk.vongriffen.mycountdown;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import dk.vongriffen.mycountdown.*;

public class T_Edit_DialogFragment extends DialogFragment
{
	static final int MIN_VAL = 0;
	static final int MAX_VAL = 59;

	NumberPicker npMinutes, npSeconds;
	Button bCancel,bAddtimer;
	static String dialogTitle;


	public interface T_EditDialogListener {
		public void T_onEditDialogMessage(int minutes, int seconds);

	}

	public T_Edit_DialogFragment () {
	}

	public void setDialogTitle(String title) {
		dialogTitle = title;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(
			R.layout.t_add_dialog, null);

		setCancelable(false);

		npMinutes = (NumberPicker) view.findViewById(R.id.npMinutes);
		npSeconds = (NumberPicker) view.findViewById(R.id.npSeconds);

		bCancel = (Button) view.findViewById(R.id.addtimertolistCancelButton);
		bAddtimer =(Button) view.findViewById(R.id.addtimertolistAddButton);

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
					T_EditDialogListener activity = (T_EditDialogListener) getActivity();
					activity.T_onEditDialogMessage(npMinutes.getValue(), npSeconds.getValue());
					dismiss();
				}
			});

		bCancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dismiss();
				}
			});

		return view;
	}
}
