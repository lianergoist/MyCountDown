package dk.vongriffen.mycountdown;

import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.support.v4.app.*;

public class IL_Edit_DialogFragment extends DialogFragment
{
	EditText etTitle, etDescription;
	Button bCancel, bCreate;
	static String dialogTitle;
	String title, description;
	int seconds;
	long id;

	public interface IL_EditDialogListener {
		public void IL_onEditDialogMessage(long id, String title, String desc);
	}

	public IL_Edit_DialogFragment (long id, String title, String desc) {
		this.id = id;
		this.title = title;
		this.description = desc;
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
		etTitle.setText(title);
		etDescription = (EditText) view.findViewById(R.id.IL_Add_etDescription);
		etDescription.setText(description);
		
		bCreate = (Button) view.findViewById(R.id.IL_Add_bCreate);
		bCancel = (Button) view.findViewById(R.id.IL_Add_bCancel);
		bCreate.setText(R.string.update);
		bCreate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				IL_EditDialogListener activity = (IL_EditDialogListener) getActivity();
				activity.IL_onEditDialogMessage(id, etTitle.getText().toString(), etDescription.getText().toString());
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
