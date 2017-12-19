package mytime.snu.com.mytime;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DaytaActivity extends AppCompatActivity {

	Button btnAddStudent,btnAddAdmin;
	int studentCount=0, adminCount=0;

	DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
	DatabaseReference mAdminRef = mRootRef.child("Admins");
	DatabaseReference mStudentRef = mRootRef.child("Students");
	@SuppressLint("RestrictedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dayta);

		getSupportActionBar().setTitle(getIntent().getStringExtra("DayOfWeek"));
//		OR
/*
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		View abl = getLayoutInflater().inflate(R.layout.actionbar_layout,null);
		txtDayOfWeek = abl.findViewById(R.id.mytext);

		Bundle bundle = getIntent().getExtras();
		Log.e("USERTODAYTA",bundle.getString("DayOfWeek"));
		txtDayOfWeek.setText(bundle.getString("DayOfWeek"));
		getSupportActionBar().setCustomView(abl);
*/
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		initViews();

	}

	public void initViews()	{
		btnAddAdmin = findViewById(R.id.btnAddAdmin);
		btnAddStudent = findViewById(R.id.btnAddStudent);
	}

	@Override
	protected void onStart() {
		super.onStart();
		btnAddStudent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String ukey = "User"+String.valueOf(studentCount++);
				mStudentRef.child(ukey).setValue("User1Name");
			}
		});

		btnAddAdmin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String akey = "Admin"+String.valueOf(adminCount++);
				mAdminRef.child(akey).setValue("Admin1Name");
			}
		});
	}
}
