package marrero.hamad.darwin.guaguaslapalma.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import marrero.hamad.darwin.guaguaslapalma.activity.adapter.BusLineAdapter;
import marrero.hamad.darwin.guaguaslapalma.activity.adapter.CustomCursorAdapter;
import marrero.hamad.darwin.guaguaslapalma.db.GuaguasLaPalmaDB;
import marrero.hamad.darwin.guaguaslapalma.R;

public class BusLineActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks{

    ListView busLinesList;
    CustomCursorAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_lines);
        busLinesList = (ListView) findViewById(R.id.busLinesList);
        getSupportLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        //String[] columns = {"id", "name"};
        return new CursorLoader(this, null, null, null, null, null) {
            @Override
            public Cursor loadInBackground() {
                GuaguasLaPalmaDB connection = new GuaguasLaPalmaDB(getContext());
                SQLiteDatabase db = connection.getReadableDatabase();
                String query = "SELECT id as _id,name FROM Bus_Lines";
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
