package dk.vongriffen.mycountdown;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class IL_Add_DialogFragment extends DialogFragment
{
	EditText etTitle, etDescription;
	Button bCancel, bCreate;
	static String dialogTitle;
	
	public interface IL_Add_DialogListener {
		public void onAddDialogMessage(String title, String desc);

	}
	
	public IL_Add_DialogFragment () {
		//
	}

	public void setDialogTitle(String title) {
		dialogTitle = title;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view =inflater.inflate (R.layout.il_add_dialog, container);
		
		setCancelable(false);
		
		etTitle=(EditText) view.findViewById(R.id.IL_Add_etTitle);
		etDescription = (EditText) view.findViewById(R.id.IL_Add_etDescription);
		bCreate = (Button) view.findViewById(R.id.IL_Add_bCreate);
		bCreate.setText(R.string.create);
		bCancel = (Button) view.findViewById(R.id.IL_Add_bCancel);

		bCreate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				IL_Add_DialogListener activity = (IL_Add_DialogListener) getActivity();
				activity.onAddDialogMessage(etTitle.getText().toString(), etDescription.getText().toString());
				dismiss();
			}
		});
		
		bCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
		
		//etTitle.requestFocus();
		
		getDialog().setTitle(dialogTitle);
		
		return view;
	}
}
