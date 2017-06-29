package dk.vongriffen.mycountdown;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class IL_Edit_DialogFragment extends DialogFragment
{
	EditText etTitle, etDescription;
	Button bCancel, bCreate;
	static String dialogTitle;

	public interface IL_EditDialogListener {
		public void onEditDialogMessage(String title, String desc);

	}

	public IL_Edit_DialogFragment () {
		//empty
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
		bCancel = (Button) view.findViewById(R.id.IL_Add_bCancel);
		bCreate.setText(R.string.update);
		bCreate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				IL_EditDialogListener activity = (IL_EditDialogListener) getActivity();
				activity.onEditDialogMessage(etTitle.getText().toString(), etDescription.getText().toString());
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
