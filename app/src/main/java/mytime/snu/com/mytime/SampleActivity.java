package mytime.snu.com.mytime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SampleActivity extends AppCompatActivity {
	String[] timeSlots = new String[] {
		"8:00","8:30","9:00","9:30",
		"10:00","10:30","11:00","11:30",
		"12:00","12:30","13:00","13:30",
		"14:00","14:30","15:00","15:30",
		"16:00","16:30","17:00","17:30",
		"18:00","18:30","19:00","19:30",
		"20:00"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sample);

		ListView sampleList = findViewById(R.id.sampleList);
		ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, timeSlots);
		sampleList.setAdapter(adapter);
	}
}