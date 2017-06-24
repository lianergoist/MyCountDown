package dk.vongriffen.mycountdown;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class TL_New_DialogFragment extends DialogFragment
{
	EditText etTitle, etDescription;
	Button bCancel, bCreate;
	static String dialogTitle;
	
	public interface NewDialogListener {
		public void onNewDialogMessage(String title, String desc);

	}
	
	public TL_New_DialogFragment () {
		//
	}

	public void setDialogTitle(String title) {
		dialogTitle = title;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view =inflater.inflate (R.layout.tl_new_dialog, container);
		
		setCancelable(false);
		
		etTitle=(EditText) view.findViewById(R.id.TL_New_etTitle);
		etDescription = (EditText) view.findViewById(R.id.TL_New_etDescription);
		bCreate = (Button) view.findViewById(R.id.TL_New_bCreate);
		bCreate.setText(R.string.create);
		bCancel = (Button) view.findViewById(R.id.TL_New_bCancel);

		bCreate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				NewDialogListener activity = (NewDialogListener) getActivity();
				activity.onNewDialogMessage(etTitle.getText().toString(), etDescription.getText().toString());
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
