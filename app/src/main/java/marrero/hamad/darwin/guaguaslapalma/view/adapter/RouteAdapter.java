package marrero.hamad.darwin.guaguaslapalma.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import marrero.hamad.darwin.guaguaslapalma.R;
import marrero.hamad.darwin.guaguaslapalma.view.activity.ScheduleAndStopActivity;

public class RouteAdapter extends CustomCursorAdapter{

    private LayoutInflater mInflater;

    public RouteAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.routes_listitem, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView routeTextView = (TextView) view.findViewById(R.id.routeTextView);
        final String id = cursor.getString(cursor.getColumnIndex("_id"));
        final String name = cursor.getString(cursor.getColumnIndex("trip_headsign"));
        routeTextView.setText(name);
        routeTextView.setTextColor(Color.BLACK);
        routeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = createIntent(context, ScheduleAndStopActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                context.startActivity(intent);
            }
        });
    }
}
