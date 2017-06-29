package dk.vongriffen.mycountdown;
  
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import dk.vongriffen.mycountdown.*;

public class IR_Edit_DialogFragment extends DialogFragment
{
	static final int MIN_VAL = 0;
	static final int MAX_VAL = 59;

	EditText etName;
	NumberPicker npMinutes, npSeconds;
	Button bCancel,bAddtimer;
	static String dialogTitle;


	public interface IR_EditDialogListener {
		public void IR_onEditDialogMessage(String name, int minutes, int seconds);

	}

	public IR_Edit_DialogFragment () {
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
		npMinutes.setValue(0);

		npSeconds.setMinValue(MIN_VAL);
		npSeconds.setMaxValue(MAX_VAL);
		npSeconds.setWrapSelectorWheel(true);
		npSeconds.setValue(0);
		
		bAddtimer.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					IR_EditDialogListener activity = (IR_EditDialogListener) getActivity();
					activity.IR_onEditDialogMessage(etName.getText().toString(), npMinutes.getValue(), npSeconds.getValue());
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
