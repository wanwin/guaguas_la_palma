package marrero.hamad.darwin.guaguaslapalma.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.ArrayList;
import java.util.List;

import marrero.hamad.darwin.guaguaslapalma.R;
import marrero.hamad.darwin.guaguaslapalma.model.ItemData;
import marrero.hamad.darwin.guaguaslapalma.view.adapter.SpinnerAdapter;
import marrero.hamad.darwin.guaguaslapalma.view.listener.CalloutTouchListener;

import static com.esri.arcgisruntime.mapping.view.LocationDisplay.DataSourceStatusChangedEvent;
import static com.esri.arcgisruntime.mapping.view.LocationDisplay.DataSourceStatusChangedListener;

public class BusStopsMapActivity extends AppCompatActivity {

    private MapView mapView;
    private LocationDisplay locationDisplay;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_stops_map);
        mapView = (MapView) findViewById(R.id.mapView);
        spinner = (Spinner) findViewById(R.id.spinner);
        final ArcGISMap map = new ArcGISMap();
        map.setBasemap(Basemap.createStreets());
        map.setInitialViewpoint(createViewpoint());
        String busStopsURL = getResources().getString(R.string.busStopsLayer);
        String busLinesURL = getResources().getString(R.string.busLinesLayer);
        ServiceFeatureTable busStopsFeatureTable = new ServiceFeatureTable(busStopsURL);
        ServiceFeatureTable busLinesFeatureTable = new ServiceFeatureTable(busLinesURL);
        busStopsFeatureTable.setFeatureRequestMode(ServiceFeatureTable.FeatureRequestMode.ON_INTERACTION_CACHE);
        FeatureLayer busStopsFeatureLayer = new FeatureLayer(busStopsFeatureTable);
        FeatureLayer busLinesFeatureLayer = new FeatureLayer(busLinesFeatureTable);
        busStopsFeatureLayer.setMinScale(200000);
        map.getOperationalLayers().add(busLinesFeatureLayer);
        map.getOperationalLayers().add(busStopsFeatureLayer);
        mapView.setMap(map);
        mapView.setOnTouchListener(new CalloutTouchListener(this, mapView));
        //mapView.setOnTouchListener(new CalloutTouchListener(this, mapView));
        locationDisplay = mapView.getLocationDisplay();
        addListenerToLocationDisplay();
        List<ItemData> list = new ArrayList<>();
        populateSpinnerArrayList(list);
        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_layout, R.id.optionTextView, list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (locationDisplay.isStarted())
                            locationDisplay.stop();
                        break;
                    case 1:
                        if (!locationDisplay.isStarted())
                            locationDisplay.startAsync();
                        break;
                    case 2:
                        locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
                        if (!locationDisplay.isStarted())
                            locationDisplay.startAsync();
                        break;
                    case 3:
                        locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
                        if (!locationDisplay.isStarted())
                            locationDisplay.startAsync();
                        break;
                    case 4:
                        locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
                        if (!locationDisplay.isStarted())
                            locationDisplay.startAsync();
                        break;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }


    private Viewpoint createViewpoint() {
        double X_COORDINATE = 1987209.861358;
        double Y_COORDINATE = 3333189.492432;
        return new Viewpoint(new Point(-X_COORDINATE, Y_COORDINATE, getWebMercator()), 475000);
    }

    private SpatialReference getWebMercator() {
        return SpatialReferences.getWebMercator();
    }

    private void addListenerToLocationDisplay() {
        locationDisplay.addDataSourceStatusChangedListener(new DataSourceStatusChangedListener() {
            private int requestCode = 2;
            String[] reqPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION};
            @Override
            public void onStatusChanged(DataSourceStatusChangedEvent dataSourceStatusChangedEvent) {
                if (dataSourceStatusChangedEvent.isStarted() || noErrorReported(dataSourceStatusChangedEvent))
                    return;
                boolean permissionCheck1 = isPermissionGranted(reqPermissions[0]);
                boolean permissionCheck2 = isPermissionGranted(reqPermissions[1]);
                if (!(permissionCheck1 && permissionCheck2)) {
                    requestPermissionToUser();
                }
                else {
                    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
                    Throwable error = getError(dataSourceStatusChangedEvent);
                    String message = String.format("Error in DataSourceStatusChangedListener: %s", error.getMessage());
                    Toast.makeText(BusStopsMapActivity.this, message, Toast.LENGTH_LONG).show();
                    spinner.setSelection(0, true);
                }
            }

            private boolean noErrorReported(DataSourceStatusChangedEvent dataSourceStatusChangedEvent) {
                //noinspection ThrowableResultOfMethodCallIgnored
                return dataSourceStatusChangedEvent.getError() == null;
            }

            private boolean isPermissionGranted(String reqPermission) {
                return ContextCompat.checkSelfPermission(BusStopsMapActivity.this, reqPermission) ==
                        PackageManager.PERMISSION_GRANTED;
            }

            private void requestPermissionToUser() {
                ActivityCompat.requestPermissions(BusStopsMapActivity.this, reqPermissions, requestCode);
            }
        });
    }

    private Throwable getError(DataSourceStatusChangedEvent dataSourceStatusChangedEvent) {
        return dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            locationDisplay.startAsync();
        } else {
            Toast.makeText(BusStopsMapActivity.this, getResources().getString(R.string.location_permission_denied), Toast
                    .LENGTH_SHORT).show();
            spinner.setSelection(0, true);
        }
    }

    private void populateSpinnerArrayList(List<ItemData> list) {
        list.add(new ItemData("Detener geolocalización", R.drawable.locationdisplaydisabled));
        list.add(new ItemData("Activar geolocalización", R.drawable.locationdisplayon));
        list.add(new ItemData("Recentrar", R.drawable.locationdisplayrecenter));
        list.add(new ItemData("Activar navegación", R.drawable.locationdisplaynavigation));
        list.add(new ItemData("Activar brújula", R.drawable.locationdisplayheading));
    }

    @Override
    protected void onPause(){
        mapView.pause();
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mapView.resume();
    }
}