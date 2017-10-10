package marrero.hamad.darwin.guaguaslapalma.view.activity;

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
import marrero.hamad.darwin.guaguaslapalma.view.adapter.CustomCursorAdapter;
import marrero.hamad.darwin.guaguaslapalma.view.adapter.RouteAdapter;

public class RouteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks{

    ListView routesList;
    CustomCursorAdapter adapter;
    String busLineID, busLineNumber, busLineName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routes);
        Bundle extras = getIntent().getExtras();
        busLineID = extras.getString("busLineID");
        busLineNumber = extras.getString("busLineNumber");
        busLineName = extras.getString("busLineName");
        setTitle(busLineNumber + " " + busLineName);
        routesList = (ListView) findViewById(R.id.routesList);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, null, null, null, null, null) {
            @Override
            public Cursor loadInBackground() {
                GuaguasLaPalmaDB connection = new GuaguasLaPalmaDB(getContext());
                SQLiteDatabase db = connection.getReadableDatabase();
                String query = "SELECT DISTINCT route_id as _id, trip_headsign FROM trips " +
                        "WHERE route_id=?";
                return db.rawQuery(query, new String[]{busLineID});
            }
        };
    }

    @Override
    public void onLoadFinished(Loader loader, Object data){
        Cursor cursor = (Cursor) data;
        cursor.moveToFirst();
        adapter = new RouteAdapter(getApplicationContext(), cursor, 0);
        routesList.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader loader){
        adapter.swapCursor(null);
    }
}
