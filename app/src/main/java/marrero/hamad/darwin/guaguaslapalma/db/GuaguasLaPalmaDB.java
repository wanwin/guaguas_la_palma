package marrero.hamad.darwin.guaguaslapalma.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class GuaguasLaPalmaDB extends SQLiteOpenHelper {

    private String sqlCreateBusLinesTable, sqlCreateRoutesTable, sqlCreateSchedulesTable,
            sqlCreateStopsTable;

    public GuaguasLaPalmaDB(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        sqlCreateBusLinesTable = "CREATE TABLE Bus_Lines (id INTEGER, name TEXT NOT NULL UNIQUE, " +
                "CONSTRAINT bus_lines_pk PRIMARY KEY (id))";
        sqlCreateRoutesTable = "CREATE TABLE Routes (id INTEGER AUTOINCREMENT, " +
                "code INTEGER NOT NULL, " + "name TEXT NOT NULL, " + "line_id INTEGER NOT NULL, "
                + "FOREIGN KEY(line_id) REFERENCES Bus_Lines(id), "
                + "CONSTRAINT routes_pk PRIMARY KEY (id))";
        sqlCreateSchedulesTable = "CREATE TABLE Schedules (id INTEGER AUTOINCREMENT" +
                ", hour TEXT NOT NULL, " + "route_id INTEGER NOT NULL, " +
                "day_of_week TEXT NOT NULL, " + "FOREIGN KEY(route_id) REFERENCES Routes(id), " +
                "CONSTRAINT schedules_pk PRIMARY KEY (id))";
        sqlCreateStopsTable = "CREATE TABLE Stops (id INTEGER, name TEXT NOT NULL, " +
                "route_id INTEGER NOT NULL, " + "stops_order INTEGER NOT NULL, " +
                "FOREIGN KEY(route_id) REFERENCES Routes(id), " +
                "CONSTRAINT stops_pk PRIMARY KEY (id))";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateBusLinesTable);
        db.execSQL(sqlCreateRoutesTable);
        db.execSQL(sqlCreateSchedulesTable);
        db.execSQL(sqlCreateStopsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Usuarios");
        db.execSQL(sqlCreateBusLinesTable);
    }


}