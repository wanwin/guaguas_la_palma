package marrero.hamad.darwin.guaguaslapalma;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;

public class BusStops extends AppCompatActivity {

    private MapView mapView;
    double X_COORDINATE = 1987209.861358;
    double Y_COORDINATE = 3333189.492432;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_stops);
        mapView = (MapView) findViewById(R.id.mapView);
        ArcGISMap map = new ArcGISMap();
        map.setBasemap(Basemap.createStreets());
        map.setInitialViewpoint(createViewpoint());
        FeatureLayer featureLayer = new FeatureLayer(createServiceFeatureTable());
        map.getOperationalLayers().add(featureLayer);
        mapView.setMap(map);
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

    private Viewpoint createViewpoint() {
        return new Viewpoint(new Point(-X_COORDINATE, Y_COORDINATE, getWebMercator()), 475000);
    }

    private SpatialReference getWebMercator() {
        return SpatialReferences.getWebMercator();
    }

    private ServiceFeatureTable createServiceFeatureTable() {
        return new ServiceFeatureTable(getResources().getString(R.string.busStopsLayer));
    }
}
