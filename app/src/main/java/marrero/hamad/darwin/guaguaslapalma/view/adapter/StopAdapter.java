package marrero.hamad.darwin.guaguaslapalma.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import marrero.hamad.darwin.guaguaslapalma.R;

public class StopAdapter extends CustomCursorAdapter{

    private LayoutInflater mInflater;

    public StopAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.stops_spinner_layout, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView stopTextView = (TextView) view.findViewById(R.id.stopsTextView);
        final String stopName = cursor.getString(cursor.getColumnIndex("stop_name"));
        stopTextView.setText(stopName);
        stopTextView.setTextColor(Color.BLACK);
    }
}
