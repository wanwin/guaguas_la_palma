package marrero.hamad.darwin.guaguaslapalma.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import marrero.hamad.darwin.guaguaslapalma.R;

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
                intent = new Intent(MainActivity.this, BusStopActivity.class);
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
