package mytime.snu.com.mytime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.*;

public class UserAddActivity extends AppCompatActivity {

	EditText courseName, courseCode, instructor, roomNo;
	ArrayAdapter<String> toAdapter,fromAdapter;
	Button btnAddClass;
	Spinner listFromTime;
	Spinner listToTime;
	ArrayList<String> tableFromTimes,tableToTimes;

	String[] timeSlots = new String[] {
		"08:00","08:30","09:00","09:30",
		"10:00","10:30","11:00","11:30",
		"12:00","12:30","13:00","13:30",
		"14:00","14:30","15:00","15:30",
		"16:00","16:30","17:00","17:30",
		"18:00","18:30","19:00","19:30",
		"20:00"};

	List<String> fromTimes;
	List<String> toTimes;

	ArrayList<String> allTimes = new ArrayList<>();
	private boolean addToDB;
	private UserDBHelper userDB;
	private String fromTime, toTime;
	private String NetID;
	private Intent openIntent;
	private TimeTableModel editRecord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_add);
		String day = getIntent().getStringExtra("DayOfWeek");
		getSupportActionBar().setTitle(day);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		initViews();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// handle arrow click here
		if (item.getItemId() == android.R.id.home) {
			finish(); // close this activity and return to preview activity (if there is any)
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStart() {
		NetID = getIntent().getStringExtra("NetID");
		if((editRecord = (TimeTableModel)getIntent().getSerializableExtra("editRecord"))!=null)	{
			courseCode.setText(editRecord.courseCode);
			courseName.setText(editRecord.courseName);
			instructor.setText(editRecord.instructor);
			roomNo.setText(editRecord.venue);

		}
		super.onStart();

		userDB = new UserDBHelper(UserAddActivity.this,NetID);

//		set array adapter for the 'From' Spinner
		fromAdapter = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_item_layout, fromTimes);
		listFromTime.setAdapter(fromAdapter);

//		set adapter for 'To' Spinner
		toAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item_layout,toTimes);
		listToTime.setAdapter(toAdapter);

		btnAddClass.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				fromTime 	= listFromTime.getSelectedItem().toString();
				toTime 		= listToTime.getSelectedItem().toString();

//				Log.e("DB_INPUT", fromTime+" "+toTime+" "+courseCode.getText().toString()+" "+courseName.getText().toString()+" "+
//					roomNo.getText().toString()+" "+instructor.getText().toString());

				if(courseCode.getText().toString().equals(""))	{
					courseCode.setError("Course Code cannot be empty");
				}
				else if(courseName.getText().toString().equals(""))	{
					courseName.setError("Course Name field cannot be empty");
				}
				else if(instructor.getText().toString().equals(""))	{
					instructor.setError("Instructor field cannot be empty");
				}
				else if(roomNo.getText().toString().equals(""))	{
					roomNo.setError("Room Number field cannot be empty");
				}
				else if(toTime.compareToIgnoreCase(fromTime)<0) {
//					Log.e("DB_INPUT",toTime.compareToIgnoreCase(fromTime)+"");
					Toast.makeText(UserAddActivity.this, "Enter The Valid Class Timings!", Toast.LENGTH_SHORT).show();
				}
				else	{

					if(isTimeValid(fromTime,toTime))	{

					if(userDB.insertRecord(getSupportActionBar().getTitle().toString(),
												  fromTime,
												  toTime,
												  courseCode.getText().toString(),
												  courseName.getText().toString(),
												  roomNo.getText().toString(),
												  instructor.getText().toString())) {
						Toast.makeText(UserAddActivity.this, "Class Added!", Toast.LENGTH_SHORT).show();
						if((openIntent = new Intent(getBaseContext(), UserDashboardActivity.class)) != null) {
							openIntent.putExtra("UserName", getIntent().getStringExtra("UserName"));
							openIntent.putExtra("NetID", NetID);
							startActivity(openIntent);
						}
					}

					}
					else	{
						Toast.makeText(UserAddActivity.this, "Could not add class! Verify all the fields", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	private boolean isTimeValid(String fromTime, String toTime) {

		return false;
	}


	private void initViews() {
		btnAddClass = findViewById(R.id.btnAddClass);
		listFromTime = findViewById(R.id.listFromTime);
		listToTime = findViewById(R.id.listToTime);
		courseName = findViewById(R.id.courseName);
		courseCode = findViewById(R.id.courseCode);
		instructor = findViewById(R.id.instructor);
		roomNo = findViewById(R.id.roomNo);

		for(String time : timeSlots)
			allTimes.add(time);

		fromTimes = allTimes.subList(0, allTimes.size() - 1);
		toTimes = allTimes.subList(1, allTimes.size());

		tableFromTimes = new ArrayList<>();
		tableToTimes = new ArrayList<>();

	}

	@Override
	public void onBackPressed() {
		if((openIntent = new Intent(getBaseContext(), UserDashboardActivity.class)) != null) {
			openIntent.putExtra("UserName", getIntent().getStringExtra("UserName"));
			openIntent.putExtra("NetID", getIntent().getStringExtra("NetID"));
			startActivity(openIntent);
		}
	}
}
