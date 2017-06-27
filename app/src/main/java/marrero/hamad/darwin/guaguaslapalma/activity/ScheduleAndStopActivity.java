package marrero.hamad.darwin.guaguaslapalma.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import marrero.hamad.darwin.guaguaslapalma.db.GuaguasLaPalmaDB;
import marrero.hamad.darwin.guaguaslapalma.R;

public class ScheduleAndStopActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks{

    TextView labourDaySchedules, labourDayStops;
    TextView saturdaySchedules, saturdayStops;
    TextView sundayAndHolidaySchedules, sundayAndHolidayStops;
    String routeID, routeName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedules_and_stops);
        Bundle extras = getIntent().getExtras();
        routeID = extras.getString("id");
        routeName = extras.getString("name");
        setTitle(routeName);
        labourDaySchedules = (TextView) findViewById(R.id.labourDaySchedules);
        labourDayStops = (TextView) findViewById(R.id.labourDayStops);
        saturdaySchedules = (TextView) findViewById(R.id.saturdaySchedules);
        saturdayStops = (TextView) findViewById(R.id.saturdayStops);
        sundayAndHolidaySchedules = (TextView) findViewById(R.id.sundayAndHolidaySchedules);
        sundayAndHolidayStops = (TextView) findViewById(R.id.sundayAndHolidayStops);
        initializeSchedulesTextViews();
        initializeStopsTextViews();
        getSupportLoaderManager().initLoader(0, null, this);
        getSupportLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader onCreateLoader(final int id, Bundle args) {
        //String[] columns = {"id", "name", "stops_order", "route_id"};
        return new CursorLoader(this, null, null, null, null, null) {
            @Override
            public Cursor loadInBackground() {
                GuaguasLaPalmaDB connection = new GuaguasLaPalmaDB(getContext());
                SQLiteDatabase db = connection.getReadableDatabase();
                Bundle extras = getIntent().getExtras();
                routeID = extras.getString("id");
                String query;
                if (id == 0){
                    query = "SELECT id as _id, hour, day_of_week FROM Schedules WHERE route_id=?";
                }
                else{
                    query = "SELECT id as _id, name FROM Stops WHERE route_id=?";
                }
                return db.rawQuery(query, new String[]{routeID});
            }
        };
    }

    @Override
    public void onLoadFinished(Loader loader, Object data){
        Cursor cursor = (Cursor) data;
        while (cursor.moveToNext()){
            if (loader.getId() == 0){
                String dayOfWeek = cursor.getString(cursor.getColumnIndex("day_of_week"));
                String hour = cursor.getString(cursor.getColumnIndex("hour"));
                String schedule;
                if (dayOfWeek.equals("midweek")){
                    schedule = labourDaySchedules.getText().toString() + " " + hour;
                    labourDaySchedules.setTextColor(Color.BLACK);
                    labourDaySchedules.setText(schedule);
                }
                if (dayOfWeek.equals("saturday")){
                    schedule = saturdaySchedules.getText().toString() + " " + hour;
                    saturdaySchedules.setTextColor(Color.BLACK);
                    saturdaySchedules.setText(schedule);
                }
                if (dayOfWeek.equals("sunday or holiday")){
                    schedule = sundayAndHolidaySchedules.getText().toString() + " " + hour;
                    sundayAndHolidaySchedules.setTextColor(Color.BLACK);
                    sundayAndHolidaySchedules.setText(schedule);
                }
            }
            else{
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String stops;
                if (cursor.isLast()){
                    stops = labourDayStops.getText().toString() + " " + name;
                }
                else{
                    stops = labourDayStops.getText().toString() + " " + name + ",";
                }
                labourDayStops.setText(stops);
                saturdayStops.setText(stops);
                sundayAndHolidayStops.setText(stops);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader loader){
        loader.reset();
    }

    private void initializeSchedulesTextViews() {
        labourDaySchedules.setText(R.string.checkout_times);
        saturdaySchedules.setText(R.string.checkout_times);
        sundayAndHolidaySchedules.setText(R.string.checkout_times);
    }

    private void initializeStopsTextViews() {
        labourDayStops.setText(R.string.stops);
        saturdayStops.setText(R.string.stops);
        sundayAndHolidayStops.setText(R.string.stops);
    }
}
