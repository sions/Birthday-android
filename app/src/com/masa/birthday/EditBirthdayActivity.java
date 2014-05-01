package com.masa.birthday;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EditBirthdayActivity extends FragmentActivity {
	protected static int RESULT_LOAD_IMAGE = 1;

	private EditText mName;
	private TextView mDateView;
	private ImageView mImageView;
	private Button mSaveButton;
	private int mYear;
	private int mMonth;
	private int mDay;

	static final int DATE_DIALOG_ID = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_birthday);

		// capture our View elements
		mName = (EditText)findViewById(R.id.name);
		mDateView = (TextView) findViewById(R.id.date);
		mImageView = (ImageView) findViewById(R.id.imageView);
		mSaveButton = (Button) findViewById(R.id.btnSave);

		String name = null;
		String birthday = null;
		String photo = null;
		
		Bundle params = getIntent().getExtras();
		if (params != null) {
		  name = params.getString("name");
		  if (name != null) {
			  mName.setText(name);
		  }
		  birthday = params.getString("birthday");
		  if (birthday != null) {
			  mDateView.setText(birthday);
		  }
		  photo = params.getString("photo");
		  if (photo != null) {
			  mImageView.setImageBitmap(BitmapFactory.decodeFile(photo));
			  mImageView.setTag(photo);
		  }
		}

		// add a click listener to the date view
		mDateView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		// add a click listener to the image view
		mImageView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});

		// add a click listener to the image view
		mSaveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				v.setEnabled(false);
				openMainActivity();
				v.setEnabled(true);
			}
		});

		// get the current date
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// display the current date (this method is below)
		updateDisplay();
	}

	// updates the date in the TextView
	private void updateDisplay() {
		mDateView.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth + 1).append("-").append(mDay).append("-")
				.append(mYear).append(" "));
	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			mImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			mImageView.setTag(picturePath);
		}
	}

	private Date convertToDate(String dateString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		Date convertedDate;
		try {
			convertedDate = dateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return convertedDate;
	}

	private void openMainActivity() {
		Date date = convertToDate((mDateView.getText()).toString());
		Intent intent = new Intent(EditBirthdayActivity.this,
				MainActivity.class);
		intent.putExtra("name", mName.getText());
		intent.putExtra("birthday", date);
		intent.putExtra("photo", mImageView.getTag().toString());
		startActivity(intent);
	}
}
