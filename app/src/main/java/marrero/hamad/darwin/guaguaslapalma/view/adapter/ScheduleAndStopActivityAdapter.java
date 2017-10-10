package marrero.hamad.darwin.guaguaslapalma.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import marrero.hamad.darwin.guaguaslapalma.R;

public class ScheduleAndStopActivityAdapter extends CustomCursorAdapter{

    private LayoutInflater mInflater;

    public ScheduleAndStopActivityAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.stops_spinner_layout, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView stopsTextView = (TextView) view.findViewById(R.id.stopsTextView);
        final String stop_name = cursor.getString(cursor.getColumnIndex("stop_name"));
        stopsTextView.setText(stop_name);
        stopsTextView.setTextColor(Color.BLACK);
    }
}
