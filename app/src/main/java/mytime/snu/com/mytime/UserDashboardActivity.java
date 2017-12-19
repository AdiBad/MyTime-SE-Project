package mytime.snu.com.mytime;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class UserDashboardActivity extends AppCompatActivity implements OnChartValueSelectedListener {

	ArrayList<String> xVals;
	Button btnAdd;
	Button btnView;
	Intent openIntent;
	LinearLayout btnLayout;
	PieChart pieChart;
	private String selectedDay;
	String NetID;
	private String user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_dashboard);
		user = getIntent().getStringExtra("UserName");
		NetID = getIntent().getStringExtra("NetID");
		getSupportActionBar().setTitle("Welcome,  "+user+"!");
		initViews();
		initPieChart();
	}

	private void initPieChart() {

		pieChart.setUsePercentValues(false);
		ArrayList<Entry> yvalues = new ArrayList<Entry>(7);
		yvalues.add(new Entry(51.4f, 0));
		yvalues.add(new Entry(51.4f, 1));
		yvalues.add(new Entry(51.4f, 2));
		yvalues.add(new Entry(51.4f, 3));
		yvalues.add(new Entry(51.4f, 4));
		yvalues.add(new Entry(51.4f, 5));
		yvalues.add(new Entry(51.4f, 6));

		PieDataSet dataSet = new PieDataSet(yvalues,"");

		xVals = new ArrayList<String>();

		xVals.add("Monday");
		xVals.add("Tuesday");
		xVals.add("Wednesday");
		xVals.add("Thursday");
		xVals.add("Friday");
		xVals.add("Saturday");
		xVals.add("Sunday");

		PieData data = new PieData(xVals,dataSet);

		data.setValueFormatter(new DefaultValueFormatter(0));
		data.setDrawValues(false);

		pieChart.setData(data);
		pieChart.setDrawHoleEnabled(false);
		pieChart.setDescription("");
		dataSet.setColors(ColorThemes.VIBGYOR);
		data.setValueTextSize(13f);
		data.setValueTextColor(Color.BLACK);


		pieChart.setOnChartValueSelectedListener(this);
		pieChart.animateXY(1400, 1400);
		pieChart.getLegend().setEnabled(false);
	}

	@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
		selectedDay=pieChart.getXValue(e.getXIndex());
		btnLayout.setVisibility(View.VISIBLE);
	}

	@Override
	public void onNothingSelected() {
		btnLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onStart() {
		super.onStart();
		btnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openIntent=new Intent(getBaseContext(), UserAddActivity.class);
				if(openIntent!=null) {
					openIntent.putExtra("DayOfWeek", selectedDay);
					openIntent.putExtra("NetID",NetID);
					openIntent.putExtra("UserName",user);
				}
				startActivity(openIntent);
			}
		});

		btnView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openIntent=new Intent(getBaseContext(), UserViewActivity.class);
				if(openIntent!=null) {
					openIntent.putExtra("DayOfWeek", selectedDay);
					openIntent.putExtra("NetID",NetID);
					openIntent.putExtra("UserName",user);
				}
				startActivity(openIntent);
			}
		});
	}

	void initViews()	{
		btnAdd 	= findViewById(R.id.btnAdd);
		btnView 		= findViewById(R.id.btnView);
		btnLayout 	= findViewById(R.id.btnLayout);
		pieChart 	= findViewById(R.id.piechart);
	}

	@Override
	public void onBackPressed() {
		if((openIntent=new Intent(getBaseContext(), UserLoginPageActivity.class))!=null) {
			startActivity(openIntent);
		}
	}
}
