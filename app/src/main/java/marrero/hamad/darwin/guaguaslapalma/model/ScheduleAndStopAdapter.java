package marrero.hamad.darwin.guaguaslapalma.model;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import marrero.hamad.darwin.guaguaslapalma.R;

public class ScheduleAndStopAdapter extends CursorAdapter{
    private LayoutInflater mInflater;

    public ScheduleAndStopAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView busStopsAndScheduleTextView =
                (TextView) view.findViewById(R.id.schedulesAndStopsTextView);
        final String name = cursor.getString(cursor.getColumnIndex("name"));
        busStopsAndScheduleTextView.setText(name);
        busStopsAndScheduleTextView.setTextColor(Color.BLACK);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.schedules_and_stops_listitem, parent, false);
    }
}
