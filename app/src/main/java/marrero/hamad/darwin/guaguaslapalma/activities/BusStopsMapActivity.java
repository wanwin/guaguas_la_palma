package marrero.hamad.darwin.guaguaslapalma.activities;

import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
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
import marrero.hamad.darwin.guaguaslapalma.model.ItemData;
import marrero.hamad.darwin.guaguaslapalma.model.SpinnerAdapter;
import marrero.hamad.darwin.guaguaslapalma.R;
import android.support.annotation.NonNull;

import static com.esri.arcgisruntime.mapping.view.LocationDisplay.*;

public class BusStopsMapActivity extends AppCompatActivity {

    private MapView mapView;
    private FeatureLayer featureLayer;
    private LocationDisplay locationDisplay;
    private Spinner spinner;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_stops);
        mapView = (MapView) findViewById(R.id.mapView);
        spinner = (Spinner) findViewById(R.id.spinner);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);
        final ArcGISMap map = new ArcGISMap();
        map.setBasemap(Basemap.createStreets());
        map.setInitialViewpoint(createViewpoint());
        String serviceFeatureTableURL = getResources().getString(R.string.busStopsLayer);
        ServiceFeatureTable featureTable = new ServiceFeatureTable(serviceFeatureTableURL);
        featureTable.setFeatureRequestMode(ServiceFeatureTable.FeatureRequestMode.ON_INTERACTION_CACHE);
        featureLayer = new FeatureLayer(featureTable);
        featureLayer.setMinScale(200000);
        //PopupDefinition popupDefinition = new PopupDefinition(featureLayer);
        //featureLayer.setPopupDefinition(popupDefinition);
        map.getOperationalLayers().add(featureLayer);
        mapView.setMap(map);
        /*mapView.addDrawStatusChangedListener(new DrawStatusChangedListener() {
            @Override
            public void drawStatusChanged(DrawStatusChangedEvent drawStatusChangedEvent) {
                if(drawStatusChangedEvent.getDrawStatus() == DrawStatus.IN_PROGRESS){
                    progressBar.setVisibility(View.VISIBLE);
                    Log.d("drawStatusChanged", "spinner visible");
                }else if (drawStatusChangedEvent.getDrawStatus() == DrawStatus.COMPLETED){
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });*/
        locationDisplay = mapView.getLocationDisplay();
        addListenerToLocationDisplay();
        ArrayList<ItemData> list = new ArrayList<>();
        populateSpinnerArrayList(list);
        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_layout, R.id.txt, list);
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
                    String message = String.format("Error in DataSourceStatusChangedListener: %s", dataSourceStatusChangedEvent
                            .getSource().getLocationDataSource().getError().getMessage());
                    Toast.makeText(BusStopsMapActivity.this, message, Toast.LENGTH_LONG).show();
                    spinner.setSelection(0, true);
                }
            }

            private boolean noErrorReported(DataSourceStatusChangedEvent dataSourceStatusChangedEvent) {
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

    private void populateSpinnerArrayList(ArrayList<ItemData> list) {
        list.add(new ItemData("Parar", R.drawable.locationdisplaydisabled));
        list.add(new ItemData("Activar", R.drawable.locationdisplayon));
        list.add(new ItemData("Recentrar", R.drawable.locationdisplayrecenter));
        list.add(new ItemData("Navegación", R.drawable.locationdisplaynavigation));
        list.add(new ItemData("Brújula", R.drawable.locationdisplayheading));
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