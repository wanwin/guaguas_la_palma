package marrero.hamad.darwin.guaguaslapalma.activities;

import android.support.annotation.Nullable;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.Adapter;
import android.widget.ListView;
import marrero.hamad.darwin.guaguaslapalma.R;
import marrero.hamad.darwin.guaguaslapalma.db.GuaguasLaPalmaDB;
import marrero.hamad.darwin.guaguaslapalma.model.BusLineAdapter;

public class BusLineActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks{

    ListView busLinesList;
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_lines);
        busLinesList = (ListView) findViewById(R.id.busLinesList);
        getSupportLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        String[] columns = {"id", "name"};
        return new CursorLoader(this, null, columns, null, null, null) {
            @Override
            public Cursor loadInBackground() {
                GuaguasLaPalmaDB connection = new GuaguasLaPalmaDB(getContext(), "BusDB",
                        null, 1);
                SQLiteDatabase db = connection.getReadableDatabase();
                //return db.query("Bus_Lines", projection, null, null, null, null, null, null);
                return db.rawQuery("SELECT id as _id,name FROM Bus_Lines", null);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader loader, Object data){
        Cursor cursor = (Cursor) data;
        cursor.moveToFirst();
        /*SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),
                R.layout.bus_lines, cursor, new String[]{"_id", "name"},
                new int[]{android.R.layout.simple_list_item_1}, 0);*/
        BusLineAdapter adapter = new BusLineAdapter(getApplicationContext(), cursor, 0);
        busLinesList.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader loader){
        adapter.swapCursor(null);
    }
}
