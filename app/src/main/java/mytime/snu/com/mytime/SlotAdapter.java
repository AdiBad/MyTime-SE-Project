package mytime.snu.com.mytime;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by niharika on 18-Nov-17.
 */

public class SlotAdapter extends BaseAdapter {
	Context context;
	int[] daySlots;
	View timeSlotItemView;
	private TextView txtSlotName;
	private TextView txtSlotColor;
	private static int SLOT_OCCUPIED = 1;

	String[] timeWindows = {
		"08:00-08:30", "08:30-09:00", "09:00-09:30", "09:30-10:00",
		"10:00-10:30", "10:30-11:00", "11:00-11:30", "11:30-12:00",
		"12:00-12:30", "12:30-13:00", "13:00-13:30", "13:30-14:00",
		"14:00-14:30", "14:30-15:00", "15:00-15:30", "15:30-16:00",
		"16:00-16:30", "16:30-17:00", "17:00-17:30", "17:30-18:00",
		"18:00-18:30", "18:30-19:00", "19:00-19:30", "19:30-20:00"
	};

	public SlotAdapter(Context context, int[] daySlots) {
		this.context = context;
		this.daySlots = daySlots;
	}

	@Override
	public int getCount() {
		return 24;
	}

	@Override
	public Object getItem(int i) {
		return null;
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		timeSlotItemView = LayoutInflater.from(context).inflate(R.layout.time_slot_item_layout, null);
		initItems();

		txtSlotName.setText(timeWindows[i]);
			if(daySlots[i]==SLOT_OCCUPIED)	{
				txtSlotColor.setBackgroundColor(ContextCompat.getColor(context, R.color.loginBtnColor));
			}
			else	{
				txtSlotColor.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
		}

		return timeSlotItemView;
	}

	private void initItems() {
		txtSlotName 	= timeSlotItemView.findViewById(R.id.txtSlotName);
		txtSlotColor 	= timeSlotItemView.findViewById(R.id.txtSlotColor);
	}
}
