package marrero.hamad.darwin.guaguaslapalma.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import marrero.hamad.darwin.guaguaslapalma.activity.RouteActivity;
import marrero.hamad.darwin.guaguaslapalma.R;

public class BusLineAdapter extends CustomCursorAdapter{

    public BusLineAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView busLineTextView = (TextView) view.findViewById(R.id.busLineTextView);
        final String id = cursor.getString(cursor.getColumnIndex("_id"));
        final String name = cursor.getString(cursor.getColumnIndex("name"));
        busLineTextView.setText(id + " " + name);
        /*if((cursor.getPosition() % 2 + 1) == 1) {
            busLineTextView.setBackgroundColor(Color.CYAN);
        }*/
        busLineTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = createIntent(context, RouteActivity.class);
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.bus_lines_listitem, parent, false);
    }
}