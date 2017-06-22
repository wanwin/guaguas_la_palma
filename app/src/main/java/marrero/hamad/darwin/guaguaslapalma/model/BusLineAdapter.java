package marrero.hamad.darwin.guaguaslapalma.model;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import marrero.hamad.darwin.guaguaslapalma.activities.RouteActivity;
import marrero.hamad.darwin.guaguaslapalma.R;

public class BusLineAdapter extends CursorAdapter{

    private LayoutInflater mInflater;

    public BusLineAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView busLineTextView = (TextView) view.findViewById(R.id.busLineTextView);
        final String id = cursor.getString(cursor.getColumnIndex("_id"));
        final String name = cursor.getString(cursor.getColumnIndex("name"));
        busLineTextView.setText(id + " " + name);
        busLineTextView.setTextColor(Color.BLACK);
        /*if((cursor.getPosition() % 2 + 1) == 1) {
            busLineTextView.setBackgroundColor(Color.CYAN);
        }*/
        busLineTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RouteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.bus_lines_listitem, parent, false);
    }

}
