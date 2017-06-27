package marrero.hamad.darwin.guaguaslapalma.db;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class GuaguasLaPalmaDB extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "BusDB.db";
    private static final int DATABASE_VERSION = 3;

    public GuaguasLaPalmaDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }
}