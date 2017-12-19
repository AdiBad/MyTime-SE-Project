package mytime.snu.com.mytime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminSearchResultActivity extends AppCompatActivity {

	DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
	DatabaseReference mUsernameRef = mRootRef.child("Student");
	TextView txtUsers;
	ListView listAvailability;
	private int substringSize;
	private SlotAdapter slotAdapter;
	private int size = 24;
	private int selectedDay;
	int[][] finalcode;
	private ProgressBar pbar;
	private LinearLayout layoutHeadings;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_search_result);
		getSupportActionBar().setTitle(getIntent().getStringExtra("DayName"));
		initViews();
//		substringSize = Integer.parseInt(getIntent().getStringExtra("SelectedDuration"));
	}

	@Override
	protected void onStart() {
		super.onStart();

		final ValueEventListener valueEventListener=mUsernameRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				String crude;

				//essentially, only 50 users can be accepted (data split for only 50 items in array)

				String Mondaysplit[]; 		String Mondata[] 		= new String[50];
				String Tuesdaysplit[]; 		String Tuesdata[] 	= new String[50];
				String Wednesdaysplit[];	String Wednesdata[] 	= new String[50];
				String Thursdaysplit[]; 	String Thursdata[] 	= new String[50];
				String Fridaysplit[]; 		String Fridata[] 		= new String[50];
				String Saturdaysplit[]; 	String Saturdata[] 	= new String[50];
				String Sundaysplit[]; 		String Sundata[] 		= new String[50];

				try{

//					0 means te slot is available, 1 means the slot is occupied
					crude=dataSnapshot.getValue().toString();
					System.out.println(" Crude = "+crude);

					//split crude data wherever Monday= appears so that we have to extract the next 25 digits only

					Mondaysplit=crude.split(	"Monday=");
					Tuesdaysplit=crude.split(	"Tuesday=");
					Wednesdaysplit=crude.split("Wednesday=");
					Thursdaysplit=crude.split(	"Thursday=");
					Fridaysplit=crude.split(	"Friday=");
					Saturdaysplit=crude.split(	"Saturday=");
					Sundaysplit=crude.split(	"Sunday=");

					int j=0, numberofusers=Mondaysplit.length-1;int temp;

					//50 users, 25 digits to be stored (26 taken just to have last value null, may/not be used)

					finalcode=new int [7][size]; //will store OR'd digits for entire week

					for(int i=0;i<7;i++) {
						for(j = 0; j < size; j++) {
							finalcode[i][j] = 0;       //0 means unoccupied slot, simple initializing
						}
						Log.e("ADMIN", "finalcode: "+finalcode[i].toString());
					}

					//taking 25 digits from each split of the week

					int i=1; j=0;
					while(i<Mondaysplit.length)
					{
						Log.e("ADMIN","Entered while loop "+i);
						Mondata[j] =	Mondaysplit[i].	substring(0,size);

						Tuesdata[j] =	Tuesdaysplit[i].	substring(0,size);

						Wednesdata[j] =Wednesdaysplit[i].substring(0,size);

						Thursdata[j] =	Thursdaysplit[i].	substring(0,size);

						Fridata[j] =	Fridaysplit[i].	substring(0,size);
						//Log.e("FRIDATA",Fridata[i]);

						Saturdata[j] =	Saturdaysplit[i].	substring(0,size);

						Sundata[j++] =	Sundaysplit[i++].	substring(0,size);
					}
					//give finalcode the value of occupied/unoccupied as each day of the week for each user
//					i is the number of users
//					j is the number of time slots per day(24)
//					k is the number of days per week (7)
//					individual for loops for overlapping slots of each day separately
					for(i=0;i<=Mondaysplit.length && Mondata[i]!=null;i++)
					{
						for(j = 0; j < size; j++) {
							finalcode[0][j] |= Integer.parseInt(String.valueOf(Mondata[i].charAt(j)));
						}
						for(j=0;j<size;j++)
						{
							temp=Integer.parseInt(String.valueOf(Tuesdata[i].charAt(j)));
							finalcode[1][j]|=temp;
						}
						for(j=0;j<size;j++)
						{
							temp=Integer.parseInt(String.valueOf(Wednesdata[i].charAt(j)));
							finalcode[2][j]|=temp;
						}
						for(j=0;j<size;j++)
						{
							temp=Integer.parseInt(String.valueOf(Thursdata[i].charAt(j)));
							finalcode[3][j]|=temp;
						}
						for(j=0;j<size;j++)
						{
							temp=Integer.parseInt(String.valueOf(Fridata[i].charAt(j)));
							finalcode[4][j]|=temp;
						}
						for(j=0;j<size;j++)
						{
							temp=Integer.parseInt(String.valueOf(Saturdata[i].charAt(j)));
							finalcode[5][j]|=temp;
						}
						for(j=0;j<size;j++)
						{
							temp=Integer.parseInt(String.valueOf(Sundata[i].charAt(j)));
							finalcode[6][j]|=temp;
						}
					}

					for(i=0;i<7;i++) {          //i=7 has to be there but it shows one row less if i!=8

						System.out.println("");
						for (j = 0; j < size; j++) {
							System.out.printf("\t" + finalcode[i][j]);//0 means unoccupied slot
						}
							System.out.println("");
					}

					txtUsers.setText("Students scanned: "+numberofusers);
					slotAdapter = new SlotAdapter(getBaseContext(),finalcode[selectedDay]);
					listAvailability.setAdapter(slotAdapter);
					pbar.setVisibility(View.GONE);
					layoutHeadings.setVisibility(View.VISIBLE);
					txtUsers.setVisibility(View.VISIBLE);

				}catch(Exception e)
				{
					System.out.println("Exception handled");
				}

			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});

	}

	private void initViews() {
		layoutHeadings = findViewById(R.id.layoutHeadings);
		txtUsers = findViewById(R.id.txtUsers);
		listAvailability = findViewById(R.id.listAvailability);
		String user = getIntent().getStringExtra("UserName");
		pbar = findViewById(R.id.pbar);
		selectedDay = Integer.parseInt(getIntent().getStringExtra("SelectedDay"));
	}
}
