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
import marrero.hamad.darwin.guaguaslapalma.activity.adapter.CustomCursorAdapter;
import marrero.hamad.darwin.guaguaslapalma.db.GuaguasLaPalmaDB;
import marrero.hamad.darwin.guaguaslapalma.activity.adapter.RouteAdapter;
import marrero.hamad.darwin.guaguaslapalma.R;

public class RouteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks{

    ListView routesList;
    CustomCursorAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routes);
        routesList = (ListView) findViewById(R.id.routesList);
        getSupportLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        //String[] columns = {"id", "code", "name", "line_id"};
        return new CursorLoader(this, null, null, null, null, null) {
            @Override
            public Cursor loadInBackground() {
                GuaguasLaPalmaDB connection = new GuaguasLaPalmaDB(getContext());
                SQLiteDatabase db = connection.getReadableDatabase();
                Bundle extras = getIntent().getExtras();
                String busLineID = extras.getString("id");
                String query = "SELECT id as _id, name FROM Routes WHERE line_id=?";
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