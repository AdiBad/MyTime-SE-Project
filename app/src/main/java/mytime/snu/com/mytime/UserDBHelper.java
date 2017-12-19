package mytime.snu.com.mytime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by niharika on 13-Nov-17.
 */

public class UserDBHelper extends SQLiteOpenHelper {
	private static final String table_monday 			= "Monday";
	private static final String table_tuesday 		= "Tuesday";
	private static final String table_wednesday 		= "Wednesday";
	private static final String table_thursday 		= "Thursday";
	private static final String table_friday 			= "Friday";
	private static final String table_saturday 		= "Saturday";
	private static final String table_sunday 			= "Sunday";

//	private static final String column_day_of_week 	= "day_of_week";

	private static final String column_from_time		= "from_time";
	private static final String column_to_time		= "to_time";
	private static final String column_course_code 	= "course_code";
	private static final String column_course_name 	= "course_name";
	private static final String column_venue 			= "venue";
	private static final String column_instructor 	= "instructor";
	private int size = 24;
	int[][] finalcode;


	private String NetID;
	DatabaseReference mRootRef;
	DatabaseReference mUsernameRef;
	String[] fromTimes = new String[] {
		"08:00","08:30","09:00","09:30",
		"10:00","10:30","11:00","11:30",
		"12:00","12:30","13:00","13:30",
		"14:00","14:30","15:00","15:30",
		"16:00","16:30","17:00","17:30",
		"18:00","18:30","19:00","19:30"
	};
	private int selectedDay;


	public UserDBHelper(Context context, String NetID) {
		super(context,NetID,null,1);
		this.NetID = NetID;
		mRootRef = FirebaseDatabase.getInstance().getReference();
		mUsernameRef = mRootRef.child("Student").child(NetID);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		String createMondayTable = "create table "+ table_monday +" ( "
			+ column_from_time +" text, "+ column_to_time +" text, " + column_course_code
			+" text, " + column_course_name +" text, "+ column_venue +" text, "
			+ column_instructor + " text, primary key ("
			+ column_from_time + ", "+column_to_time + ") )";

		sqLiteDatabase.execSQL(createMondayTable);

		String createTuesdayTable = "create table "+ table_tuesday +" ( "
			+ column_from_time +" text, "+ column_to_time +" text, " + column_course_code
			+" text, " + column_course_name +" text, "+ column_venue +" text, "
			+ column_instructor + " text, primary key ("
			+ column_from_time + ", "+column_to_time + ") )";

		sqLiteDatabase.execSQL(createTuesdayTable);

		String createWednesdayTable = "create table "+ table_wednesday +" ( "
			+ column_from_time +" text, "+ column_to_time +" text, " + column_course_code
			+" text, " + column_course_name +" text, "+ column_venue +" text, "
			+ column_instructor + " text, primary key ("
			+ column_from_time + ", "+column_to_time + ") )";

		sqLiteDatabase.execSQL(createWednesdayTable);

		String createThursdayTable = "create table "+ table_thursday +" ( "
			+ column_from_time +" text, "+ column_to_time +" text, " + column_course_code
			+" text, " + column_course_name +" text, "+ column_venue +" text, "
			+ column_instructor + " text, primary key ("
			+ column_from_time + ", "+column_to_time + ") )";

		sqLiteDatabase.execSQL(createThursdayTable);

		String createFridayTable = "create table "+ table_friday +" ( "
			+ column_from_time +" text, "+ column_to_time +" text, " + column_course_code
			+" text, " + column_course_name +" text, "+ column_venue +" text, "
			+ column_instructor + " text, primary key ("
			+ column_from_time + ", "+column_to_time + ") )";

		sqLiteDatabase.execSQL(createFridayTable);

		String createSaturdayTable = "create table "+ table_saturday +" ( "
			+ column_from_time +" text, "+ column_to_time +" text, " + column_course_code
			+" text, " + column_course_name +" text, "+ column_venue +" text, "
			+ column_instructor + " text, primary key ("
			+ column_from_time + ", "+column_to_time + ") )";

		sqLiteDatabase.execSQL(createSaturdayTable);

		String createSundayTable = "create table "+ table_sunday +" ( "
			+ column_from_time +" text, "+ column_to_time +" text, " + column_course_code
			+" text, " + column_course_name +" text, "+ column_venue +" text, "
			+ column_instructor + " text, primary key ("
			+ column_from_time + ", "+column_to_time + ") )";

		sqLiteDatabase.execSQL(createSundayTable);

		//INITIALIZING FIREBASE WEEK = 0 FOR THIS USER

		mUsernameRef.child("Monday")		.setValue("000000000000000000000000");
		mUsernameRef.child("Tuesday")		.setValue("000000000000000000000000");
		mUsernameRef.child("Wednesday")	.setValue("000000000000000000000000");
		mUsernameRef.child("Thursday")	.setValue("000000000000000000000000");
		mUsernameRef.child("Friday")		.setValue("000000000000000000000000");
		mUsernameRef.child("Saturday")	.setValue("000000000000000000000000");
		mUsernameRef.child("Sunday")		.setValue("000000000000000000000000");


	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
		sqLiteDatabase.execSQL("drop table if exists "+ table_monday);
		sqLiteDatabase.execSQL("drop table if exists "+ table_tuesday);
		sqLiteDatabase.execSQL("drop table if exists "+ table_wednesday);
		sqLiteDatabase.execSQL("drop table if exists "+ table_thursday);
		sqLiteDatabase.execSQL("drop table if exists "+ table_friday);
		sqLiteDatabase.execSQL("drop table if exists "+ table_saturday);
		sqLiteDatabase.execSQL("drop table if exists "+ table_sunday);

		onCreate(sqLiteDatabase);
	}

	public boolean insertRecord(String dayOfWeek, String fromTime, String toTime, String courseCode,
										 String courseName, String roomNo, String instructor)	{

//		if(!validClassTime(dayOfWeek,fromTime,toTime))	{
//			return false;
//		}

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(column_from_time,fromTime);
		cv.put(column_to_time,toTime);
		cv.put(column_course_code,courseCode);
		cv.put(column_course_name,courseName);
		cv.put(column_venue,roomNo);
		cv.put(column_instructor,instructor);


		if(!(db.insert(getTableName(dayOfWeek),null,cv)<0))	{
			String alter_query;

			alter_query = "create table temp as select * from "+getTableName(dayOfWeek)+" order by "+column_from_time+" asc";
			db.execSQL(alter_query);

			alter_query = "drop table "+getTableName(dayOfWeek);
			db.execSQL(alter_query);

			alter_query = "create table "+getTableName(dayOfWeek)+" as select * from temp";
			db.execSQL(alter_query);

			alter_query = "drop table temp";
			db.execSQL(alter_query);


			//divide time slot into 1's and 0's

			StringBuffer daycode=new StringBuffer("0000000000000000000000000");
			int i=0,indexofromtime=0;
			for(i=0;i<24;i++) {
				if(fromTimes[i].compareTo(fromTime)>=0 && fromTimes[i].compareTo(toTime)<0) {
					daycode.setCharAt(i,'1');
				}
			}

			syncInsertIntoFirebase(daycode,dayOfWeek);
			
			return true;
		}
		return false;
	}

	private void syncInsertIntoFirebase(final StringBuffer daycode, final String dayOfWeek) {
		mUsernameRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				String crude;
				
				try{

					crude=dataSnapshot.getValue().toString();
//					System.out.println(" Crude = "+crude);
					StringBuffer userDay = new StringBuffer("000000000000000000000000");
					switch(selectedDay)
					{
						case 0:
							userDay=new StringBuffer(crude.split("Monday=")[1].substring(0,24));
							break;
						case 1:
							userDay=new StringBuffer(crude.split("Tuesday=")[1].substring(0,24));
							break;
						case 2:
							userDay=new StringBuffer(crude.split("Wednesday=")[1].substring(0,24));
							break;
						case 3:
							userDay=new StringBuffer(crude.split("Thursday=")[1].substring(0,24));
							break;
						case 4:
							userDay=new StringBuffer(crude.split("Friday=")[1].substring(0,24));
							break;
						case 5:
							userDay=new StringBuffer(crude.split("Saturday=")[1].substring(0,24));
							break;
						case 6:
							userDay=new StringBuffer(crude.split("Sunday=")[1].substring(0,24));
							break;
					}

					//CHANGE DAY OF WEEK ACCORDING TO NEWLY ENTERED USER DATA OF WEEK

					for(int i=0 ; i<daycode.length() ; i++)	{
						if(daycode.charAt(i)=='1')	{
							userDay.setCharAt(i,'1');
						}
					}
						//firebase update

					mUsernameRef.child(dayOfWeek).setValue(userDay.toString());


				}catch(Exception e)
				{
					System.out.println("Exception handled");
				}

			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});

	}

	private boolean validClassTime(String dayOfWeek,String fromTime, String toTime) {
		ArrayList<String> tableFromTimes = getFromTimes(dayOfWeek);
		ArrayList<String> tableToTimes = getToTimes(dayOfWeek);

		if(toTime.compareTo(tableFromTimes.get(0))<=0)	{
			return true;
		}
		else if(fromTime.compareTo(tableToTimes.get(tableToTimes.size()-1))>=0)	{
			return true;
		}

		for(int i=0 ; i<tableFromTimes.size()-1 ; i++)	{
			if(fromTime.compareTo(tableToTimes.get(i))>=0 && toTime.compareTo(tableFromTimes.get(i+1))<=0)	{
				return true;
			}
		}

		return false;
	}


	public void deleteRecord (String dayOfWeek, String fromTime, String toTime) {
		SQLiteDatabase db = this.getWritableDatabase();
		if((db.delete(getTableName(dayOfWeek),
						  column_from_time +" = ? and "+column_to_time +" = ?", new String[] {fromTime,toTime}))>0){
//					Log.e("TO_DELETE","This thing just ran again. 123123.");
			//			firebase modification

/*
				StringBuffer daycode = new StringBuffer("111111111111111111111111");

				for(int i = 0; i < 24; i++) {
					if(fromTimes[i].compareTo(fromTime) >= 0 && fromTimes[i].compareTo(toTime) < 0) {
						daycode.setCharAt(i, '0');
					}
				}
				syncDeleteIntoFirebase(daycode,dayOfWeek);
*/
		}
	}
	 public void syncDeleteIntoFirebase(final StringBuffer daycode, final String dayOfWeek)	{
		 mUsernameRef.addValueEventListener(new ValueEventListener() {
			 @Override
			 public void onDataChange(DataSnapshot dataSnapshot) {
				 String crude;

				 try{

					 crude=dataSnapshot.getValue().toString();
//					System.out.println(" Crude = "+crude);
					 StringBuffer userDay = new StringBuffer("000000000000000000000000");
					 switch(selectedDay)
					 {
						 case 0:
							 userDay=new StringBuffer(crude.split("Monday=")[1].substring(0,24));
							 break;
						 case 1:
							 userDay=new StringBuffer(crude.split("Tuesday=")[1].substring(0,24));
							 break;
						 case 2:
							 userDay=new StringBuffer(crude.split("Wednesday=")[1].substring(0,24));
							 break;
						 case 3:
							 userDay=new StringBuffer(crude.split("Thursday=")[1].substring(0,24));
							 break;
						 case 4:
							 userDay=new StringBuffer(crude.split("Friday=")[1].substring(0,24));
							 break;
						 case 5:
							 userDay=new StringBuffer(crude.split("Saturday=")[1].substring(0,24));
							 break;
						 case 6:
							 userDay=new StringBuffer(crude.split("Sunday=")[1].substring(0,24));
							 break;
					 }

					 //CHANGE DAY OF WEEK ACCORDING TO NEWLY ENTERED USER DATA OF WEEK
					 Log.e("TO_DELETE", userDay.toString());

					 for(int i=0 ; i<daycode.length() ; i++)	{
						 Log.e("TO_DELETE","loge1");
						 if(daycode.charAt(i)=='0')	{
						 	Log.e("TO_DELETE","loge2");
							 userDay.setCharAt(i,'0');
						 }
						 Log.e("TO_DELETE","loge3");
					 }
					 Log.e("TO_DELETE","loge4");
					 //firebase update

					 Log.e("TO_DELETE", dayOfWeek+" = "+userDay.toString());
					 mUsernameRef.child(dayOfWeek).setValue(userDay.toString());
					 return;


				 }catch(Exception e)
				 {
					 System.out.println("Exception handled");
				 }

			 }

			 @Override
			 public void onCancelled(DatabaseError databaseError) {}
		 });
	 }


	public ArrayList<TimeTableModel> getAllDayRecords(String dayOfWeek)	{
		ArrayList<TimeTableModel> timetable = new ArrayList<>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res =  db.rawQuery("select * from "+ getTableName(dayOfWeek), null );
		res.moveToFirst();

		while(!res.isAfterLast()){
			TimeTableModel tt = new TimeTableModel();
			tt.fromTime 	= res.getString(res.getColumnIndex(column_from_time));
			tt.toTime		= res.getString(res.getColumnIndex(column_to_time));
			tt.courseCode	= res.getString(res.getColumnIndex(column_course_code));
			tt.courseName	= res.getString(res.getColumnIndex(column_course_name));
			tt.venue			= res.getString(res.getColumnIndex(column_venue));
			tt.instructor	= res.getString(res.getColumnIndex(column_instructor));

			timetable.add(tt);
			if(res!=null)
				res.moveToNext();
		}

		return timetable;
	}

	public TimeTableModel getRecord(String dayOfWeek, String fromTime, String toTime)	{
		TimeTableModel tt = new TimeTableModel();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res =  db.rawQuery("select * from "+ getTableName(dayOfWeek)+" where "
											  + column_from_time +" = '"+fromTime +"' and " +column_to_time
											  + " = '"+toTime+"'", null );
		res.moveToFirst();
		tt.fromTime 	= res.getString(res.getColumnIndex(column_from_time));
		tt.toTime		= res.getString(res.getColumnIndex(column_to_time));
		tt.courseCode	= res.getString(res.getColumnIndex(column_course_code));
		tt.courseName	= res.getString(res.getColumnIndex(column_course_name));
		tt.venue			= res.getString(res.getColumnIndex(column_venue));
		tt.instructor	= res.getString(res.getColumnIndex(column_instructor));

		deleteRecord(dayOfWeek,fromTime,toTime);

		return tt;
	}

	public ArrayList<String> getFromTimes(String dayOfWeek)	{
		ArrayList<String> tableFromTimes = new ArrayList<>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res =  db.rawQuery("select "+column_from_time+" from "+ getTableName(dayOfWeek), null );
		res.moveToFirst();

		while(!res.isAfterLast()){
			tableFromTimes.add(res.getString(0));
		}
		return tableFromTimes;
	}


	public ArrayList<String> getToTimes(String dayOfWeek)	{
		ArrayList<String> tableToTimes = new ArrayList<>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res =  db.rawQuery("select "+column_to_time+" from "+ getTableName(dayOfWeek), null );
		res.moveToFirst();

		while(!res.isAfterLast()){
			tableToTimes.add(res.getString(0));
		}
		return tableToTimes;
	}

	private String getTableName(String dayOfWeek) {
		if(dayOfWeek.equalsIgnoreCase("Monday")) {
			selectedDay = 0;
			return table_monday;
		}
		else if(dayOfWeek.equalsIgnoreCase("Tuesday")) {
			selectedDay = 1;
			return table_tuesday;
		}
		else if(dayOfWeek.equalsIgnoreCase("Wednesday")) {
			selectedDay = 2;
			return table_wednesday;
		}
		else if(dayOfWeek.equalsIgnoreCase("Thursday")) {
			selectedDay = 3;
			return table_thursday;
		}
		else if(dayOfWeek.equalsIgnoreCase("Friday")) {
			selectedDay = 4;
			return table_friday;
		}
		else if(dayOfWeek.equalsIgnoreCase("Saturday")) {
			selectedDay = 5;
			return table_saturday;
		}
		else if(dayOfWeek.equalsIgnoreCase("Sunday")) {
			selectedDay = 6;
			return table_sunday;
		}

		return "";
	}
}
