package marrero.hamad.darwin.guaguaslapalma.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.widget.CursorAdapter;

public abstract class CustomCursorAdapter extends CursorAdapter{

    LayoutInflater inflater;

    CustomCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    Intent createIntent(Context context, Class cls){
        Intent intent = new Intent(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
}
