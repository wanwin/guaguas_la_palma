package marrero.hamad.darwin.guaguaslapalma.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import marrero.hamad.darwin.guaguaslapalma.R;
import marrero.hamad.darwin.guaguaslapalma.view.activity.RouteActivity;

public class BusLineAdapter extends CustomCursorAdapter{

    public BusLineAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.bus_lines_listitem, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView busLineTextView = (TextView) view.findViewById(R.id.busLineTextView);
        TextView circle = (TextView) view.findViewById(R.id.circularTextView);
        final String busLineID = cursor.getString(cursor.getColumnIndex("_id"));
        final String busLineNumber = cursor.getString(cursor.getColumnIndex("route_short_name"));
        final String busLineName = cursor.getString(cursor.getColumnIndex("route_long_name"));
        int busLineTextColor = getColorFromString(cursor, "route_text_color");
        int busLineBackgroundColor = getColorFromString(cursor, "route_color");
        circle.setText(busLineNumber);
        circle.setTextColor(busLineTextColor);
        circle.setBackgroundColor(busLineBackgroundColor);
        busLineTextView.setText(busLineName);
        busLineTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = createIntent(context, RouteActivity.class);
                intent.putExtra("busLineID", busLineID);
                intent.putExtra("busLineNumber", busLineNumber);
                intent.putExtra("busLineName", busLineName);
                context.startActivity(intent);
            }
        });
    }


    private int getColorFromString(Cursor cursor, String colorString) {
        return Color.parseColor("#" + cursor.getString(cursor.getColumnIndex(colorString)));
    }
}
