package marrero.hamad.darwin.guaguaslapalma.view.activity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import marrero.hamad.darwin.guaguaslapalma.R;
import marrero.hamad.darwin.guaguaslapalma.db.GuaguasLaPalmaDB;
import marrero.hamad.darwin.guaguaslapalma.view.adapter.BusLineAdapter;
import marrero.hamad.darwin.guaguaslapalma.view.adapter.CustomCursorAdapter;

public class BusLineActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks{

    ListView busLinesList;
    CustomCursorAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_lines);
        busLinesList = (ListView) findViewById(R.id.busLinesList);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, null, null, null, null, null) {
            @Override
            public Cursor loadInBackground() {
                GuaguasLaPalmaDB connection = new GuaguasLaPalmaDB(getContext());
                SQLiteDatabase db = connection.getReadableDatabase();
                String query = "SELECT route_id as _id, route_short_name, route_long_name, " +
                        "route_color, route_text_color FROM routes ORDER BY route_short_name ASC";
                return db.rawQuery(query, null);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader loader, Object data){
        Cursor cursor = (Cursor) data;
        cursor.moveToFirst();
        adapter = new BusLineAdapter(getApplicationContext(), cursor, 0);
        busLinesList.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader loader){
        adapter.swapCursor(null);
    }
}
