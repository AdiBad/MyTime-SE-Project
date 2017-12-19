package mytime.snu.com.mytime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class UserViewActivity extends AppCompatActivity {

	ListView listTtDay;
	DayTimetableAdapter dayTimetableAdapter;
	private UserDBHelper db;
	private String NetID;
	private Intent openIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_view);
		getSupportActionBar().setTitle(getIntent().getStringExtra("DayOfWeek")+"'s Timetable");
		initViews();
		listTtDay.setAdapter(dayTimetableAdapter);
	}

	private void initViews() {
		listTtDay = findViewById(R.id.listTtDay);
		String day = getIntent().getStringExtra("DayOfWeek");
		NetID = getIntent().getStringExtra("NetID");
		db = new UserDBHelper(this, NetID);
		dayTimetableAdapter = new DayTimetableAdapter(this, db.getAllDayRecords(day), db, day);
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
