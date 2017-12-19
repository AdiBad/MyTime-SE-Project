package mytime.snu.com.mytime;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.util.Calendar.MONDAY;
import static java.util.Calendar.getInstance;

public class AdminDashboardActivity extends AppCompatActivity{

	Button btnSearch;

	CalendarView pickDate;

	Intent openIntent;

	TextView pickTime;

	TextView txtDate;

	int dayOfWeek;
	private String weekDay;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_dashboard);
		String s = getIntent().getStringExtra("AdminName");
		getSupportActionBar().setTitle("Welcome, "+s+"!");
		initViews();

		if (Build.VERSION.SDK_INT >= 19) {
			Calendar cal = Calendar.getInstance();

	//		setting the current day as the minDate of the calendar
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			Date minDate = cal.getTime();
			pickDate.setMinDate(minDate.getTime());

	//		setting the end of the current month as the maxDate of the calendar
			int toMonth = (cal.get(Calendar.MONTH)+1);
			cal.set(cal.get(Calendar.YEAR), toMonth, cal.get(Calendar.DAY_OF_MONTH));
			Date maxDate = cal.getTime();
			pickDate.setMaxDate(maxDate.getTime());
		}

		pickDate.setFirstDayOfWeek(MONDAY);

		pickDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
			@Override
			public void onSelectedDayChange(@NonNull CalendarView pickDate, int year, int month, int dayOfMonth) {
				txtDate.setText(dayOfMonth+" "+new DateFormatSymbols().getMonths()[month]+" "+year);
				Calendar calendar = Calendar.getInstance();
				calendar.set(year, month, dayOfMonth);
				dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
				SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
				weekDay = dayFormat.format(calendar.getTime());
			}
		});

		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//				buttonOnClick(view);
				Log.e("LOGGG","Entered btnSearch OnCLick");
				if(txtDate.getText().equals("Pick a date below:"))
					txtDate.setError("Select date from calendar");
				else	{
					openIntent=new Intent(getBaseContext(), AdminSearchResultActivity.class);
					openIntent.putExtra("SelectedDay",String.valueOf((dayOfWeek+5)%7));
					openIntent.putExtra("DayName",weekDay);
//					Log.e("SELECTED",String.valueOf((dayOfWeek+5)%7));
					startActivity(openIntent);
				}
			}
		});
	}


	void initViews()	{
		pickDate = findViewById(R.id.pickDate);
		pickTime = findViewById(R.id.pickTime);
		txtDate = findViewById(R.id.txtDate);
		btnSearch = findViewById(R.id.btnSearch);
	}
}
