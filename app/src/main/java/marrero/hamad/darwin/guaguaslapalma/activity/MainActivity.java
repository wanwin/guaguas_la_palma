package marrero.hamad.darwin.guaguaslapalma.activity;

import android.content.Intent;
import marrero.hamad.darwin.guaguaslapalma.R;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button busStopsButton = (Button) findViewById(R.id.busStopsButton);
        Button stopsAndSchedulesButton = (Button) findViewById(R.id.stopsAndSchedulesButton);
        busStopsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, BusStopsMapActivity.class);
                startActivity(intent);
            }
        });
        stopsAndSchedulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, BusLineActivity.class);
                startActivity(intent);
            }
        });
    }
}
