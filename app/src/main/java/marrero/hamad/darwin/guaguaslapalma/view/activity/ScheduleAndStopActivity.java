package marrero.hamad.darwin.guaguaslapalma.view.activity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import marrero.hamad.darwin.guaguaslapalma.R;
import marrero.hamad.darwin.guaguaslapalma.db.GuaguasLaPalmaDB;
import marrero.hamad.darwin.guaguaslapalma.view.adapter.StopAdapter;

public class ScheduleAndStopActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks{

    private String routeID, routeName;
    private Spinner stopsSpinner;
    private StopAdapter stopAdapter;
    private ArrayList<String> stopIDArrayList;
    private LinearLayout scheduleAndStopLinearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedules_and_stops);
        scheduleAndStopLinearLayout = (LinearLayout) findViewById(R.id.scheduleAndStopLinearLayout);
        final ScheduleAndStopActivity activity = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            routeID = extras.getString("id");
            routeName = extras.getString("name");
        }
        setTitle(routeName);
        stopsSpinner = (Spinner) findViewById(R.id.stopsSpinner);
        getSupportLoaderManager().initLoader(0, null, this);
        stopsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSupportLoaderManager().restartLoader(1, null, activity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader onCreateLoader(final int id, Bundle args) {
        return new CursorLoader(this, null, null, null, null, null) {
            @Override
            public Cursor loadInBackground() {
                GuaguasLaPalmaDB connection = new GuaguasLaPalmaDB(getContext());
                SQLiteDatabase db = connection.getReadableDatabase();
                String query;
                if (id == 0) {
                    query = "SELECT DISTINCT stops.stop_id as _id, stops.stop_name FROM trips " +
                            "INNER JOIN stop_times ON stop_times.trip_id = trips.trip_id " +
                            "INNER JOIN stops ON stops.stop_id = stop_times.stop_id " +
                            "WHERE route_id = ? AND trip_headsign = ? " +
                            "ORDER BY stop_sequence ASC;";
                            return db.rawQuery(query, new String[]{routeID, routeName});
                }
                else{
                    String stopID = stopIDArrayList.get(stopsSpinner.getSelectedItemPosition());
                    query = "SELECT stop_times.arrival_time, calendar.days FROM stop_times " +
                            "INNER JOIN trips ON stop_times.trip_id = trips.trip_id " +
                            "INNER JOIN calendar ON trips.service_id = calendar.service_id " +
                            "WHERE stop_id = ? AND trips.trip_headsign == ? AND arrival_time != \"\" " +
                            "ORDER BY days_order, arrival_time;";
                            return db.rawQuery(query, new String[]{stopID, routeName});
                }

            }
        };
    }

    @Override
    public void onLoadFinished(Loader loader, Object data){
        Cursor cursor = (Cursor) data;
        cursor.moveToFirst();
        int id = loader.getId();
        if (id == 0){
            stopIDArrayList = new ArrayList<>();
            while (!cursor.isAfterLast()){
                final String stopID = cursor.getString(cursor.getColumnIndex("_id"));
                stopIDArrayList.add(stopID);
                cursor.moveToNext();
            }
            cursor.moveToFirst();
            stopAdapter = new StopAdapter(getApplicationContext(), cursor, 0);
            stopsSpinner.setAdapter(stopAdapter);
        }
        else{
            scheduleAndStopLinearLayout.removeViews(1, scheduleAndStopLinearLayout.getChildCount() - 1);
            HashMap<String, TextView> daysHashMap = new HashMap<>();
            while (!cursor.isAfterLast()){
                String days = cursor.getString(cursor.getColumnIndex("days"));
                String hour = cursor.getString(cursor.getColumnIndex("arrival_time"));
                String schedule;
                if (!daysHashMap.containsKey(days)){
                    TextView daysTextView = new TextView(ScheduleAndStopActivity.this);
                    daysTextView.setText(days);
                    daysTextView.setTextSize(24);
                    daysTextView.setTextColor(ContextCompat.getColor(getApplicationContext(),
                            R.color.black));
                    daysTextView.setTypeface(null, Typeface.BOLD);
                    scheduleAndStopLinearLayout.addView(daysTextView);
                    TextView scheduleTextView = new TextView(ScheduleAndStopActivity.this);
                    scheduleTextView.setTextSize(24);
                    scheduleTextView.setTextColor(ContextCompat.getColor(getApplicationContext(),
                            R.color.black));
                    scheduleAndStopLinearLayout.addView(scheduleTextView);
                    daysHashMap.put(days, scheduleTextView);
                }
                String scheduleTextViewText = daysHashMap.get(days).getText().toString();
                if (scheduleTextViewText.equals("")) {
                    schedule = daysHashMap.get(days).getText().toString() + hour;
                }else
                    schedule = daysHashMap.get(days).getText().toString() + " " + hour;
                daysHashMap.get(days).setText(schedule);
                cursor.moveToNext();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader loader){
        stopAdapter.swapCursor(null);
    }
}
