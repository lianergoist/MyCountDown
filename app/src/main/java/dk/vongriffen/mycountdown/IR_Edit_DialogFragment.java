package dk.vongriffen.mycountdown;

import android.os.*;
import android.view.*;
import android.widget.*;
import dk.vongriffen.mycountdown.*;
import android.support.v4.app.*;

public class IR_Edit_DialogFragment extends DialogFragment
{
	static final int MIN_VAL = 0;
	static final int MAX_VAL = 59;

	EditText etName;
	NumberPicker npMinutes, npSeconds;
	Button bCancel,bAddtimer;
	static String dialogTitle;
	String name;
	int seconds;
	long id;


	public interface IR_EditDialogListener {
		public void IR_onEditDialogMessage(long id, String name, int minutes, int seconds);

	}

	public IR_Edit_DialogFragment (long id, String name, int seconds) {
		this.id = id;
		this.name = name;
		this.seconds = seconds;
	}

	public void setDialogTitle(String title) {
		dialogTitle = title;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(
			R.layout.ir_add_dialog, null);
			
		setCancelable(false);
			
		etName = (EditText) view.findViewById(R.id.IR_etDescription);
		npMinutes = (NumberPicker) view.findViewById(R.id.IR_npMinutes);
		npSeconds = (NumberPicker) view.findViewById(R.id.IR_npSeconds);

		bCancel = (Button) view.findViewById(R.id.IR_addtimertolistCancelButton);
		bAddtimer =(Button) view.findViewById(R.id.IR_addtimertolistAddButton);
		
		npMinutes.setMinValue(MIN_VAL);
		npMinutes.setMaxValue(MAX_VAL);
		npMinutes.setWrapSelectorWheel(true);
		npMinutes.setValue(seconds/60);

		npSeconds.setMinValue(MIN_VAL);
		npSeconds.setMaxValue(MAX_VAL);
		npSeconds.setWrapSelectorWheel(true);
		npSeconds.setValue(seconds%60);
		
		etName.setText(name);
		
		bAddtimer.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					IR_EditDialogListener activity = (IR_EditDialogListener) getActivity();
					activity.IR_onEditDialogMessage(id, etName.getText().toString(), npMinutes.getValue(), npSeconds.getValue());
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
