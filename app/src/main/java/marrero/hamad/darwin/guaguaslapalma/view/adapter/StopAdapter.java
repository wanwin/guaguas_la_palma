package marrero.hamad.darwin.guaguaslapalma.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        String stopName = cursor.getString(cursor.getColumnIndex("stop_name"));
        StringBuilder modifiedStopName = new StringBuilder();
        modifiedStopName.append(" ");
        modifiedStopName.append(stopName.charAt(0));
        for (int i = 1; i < stopName.length(); i++) {
            if (!isPreviousCharacterHyphenOrSpace(stopName, i)){
                modifiedStopName.append(Character.toLowerCase(stopName.charAt(i)));
            }
            else {
                modifiedStopName.append(stopName.charAt(i));
            }
        }
        stopTextView.setText(modifiedStopName);
        stopTextView.setTextColor(Color.BLACK);
    }

    private boolean isPreviousCharacterHyphenOrSpace(String stopName, int i) {
        return (stopName.charAt(i - 1) == ' ') || (stopName.charAt(i - 1) == '-') || (stopName.charAt(i - 1) == '(');
    }
}
