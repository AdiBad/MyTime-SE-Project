package mytime.snu.com.mytime;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by niharika on 12-Nov-17.
 */

public class DayTimetableAdapter extends BaseAdapter {

	private final ArrayList<TimeTableModel> timetable;
	private final UserDBHelper db;
	private final String day;
	Context context;

	private LinearLayout layoutDetails,layoutTweaks;
	private View listItemView;
	private TextView txtCourseCode, txtCourseName,txtFromTime, txtInstructor, txtVenue, txtToTime;
	private ImageView imgEdit,imgDelete;
	private Intent openIntent;


	public DayTimetableAdapter(Context context, ArrayList<TimeTableModel> timetable, UserDBHelper db, String day)	{
		this.context = context;
		this.timetable = timetable;
		this.db = db;
		this.day = day;
	}

	@Override
	public int getCount() {
		return timetable.size();
	}

	@Override
	public Object getItem(int i) {
		return timetable.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(final int i, View view, ViewGroup viewGroup) {
		initViews();

		TimeTableModel tt = timetable.get(i);

		txtFromTime.setText(tt.fromTime);
		txtToTime.setText(tt.toTime);

		txtCourseCode.setText(tt.courseCode);
		txtCourseName.setText(tt.courseName);
		txtVenue.setText(tt.venue);
		txtInstructor.setText(tt.instructor);

/*
		listItemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(layoutDetails.getVisibility()==View.VISIBLE) {
					layoutDetails.setVisibility(View.GONE);
					layoutTweaks.setVisibility(View.VISIBLE);

					imgDelete.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							TimeTableModel toRemove = (TimeTableModel)getItem(i);
							db.deleteRecord(day,toRemove.fromTime,toRemove.toTime);
							timetable.remove(toRemove);
							notifyDataSetChanged();
						}
					});

					imgEdit.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							TimeTableModel toEdit = (TimeTableModel)getItem(i);
							TimeTableModel editRecord = db.getRecord(day,toEdit.fromTime,toEdit.toTime);
							if((openIntent=new Intent(context, UserAddActivity.class))!=null)	{
								openIntent.putExtra("editRecord",editRecord);
								context.startActivity(openIntent);
							}
						}
					});
				}
				else if(layoutTweaks.getVisibility()==View.VISIBLE)	{
					layoutTweaks.setVisibility(View.GONE);
					layoutDetails.setVisibility(View.VISIBLE);
				}
			}
		});
*/

		return listItemView;
	}

	private void initViews() {
		if (Build.VERSION.SDK_INT >= 19) {
			listItemView = LayoutInflater.from(context).inflate(R.layout.card_view_layout, null);
		}	else	{
			listItemView = LayoutInflater.from(context).inflate(R.layout.local_timetable_item, null);
		}

		txtCourseCode 	= listItemView.findViewById(R.id.txtCourseCode);
		txtCourseName 	= listItemView.findViewById(R.id.txtCourseName);
		imgDelete		= listItemView.findViewById(R.id.imgDelete);
		imgEdit			= listItemView.findViewById(R.id.imgEdit);
		txtFromTime		= listItemView.findViewById(R.id.txtFromTime);
		txtInstructor 	= listItemView.findViewById(R.id.txtInstructor);
		txtToTime		= listItemView.findViewById(R.id.txtToTime);
		txtVenue 		= listItemView.findViewById(R.id.txtVenue);
		layoutDetails 	= listItemView.findViewById(R.id.layoutDetails);
		layoutTweaks 	= listItemView.findViewById(R.id.layoutTweaks);
	}
}
