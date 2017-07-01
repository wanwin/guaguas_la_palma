package marrero.hamad.darwin.guaguaslapalma.view.listener;

import android.content.Context;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import marrero.hamad.darwin.guaguaslapalma.R;


public class CalloutTouchListener extends DefaultMapViewOnTouchListener{

    private Callout callout;
    private Context context;
    private ServiceFeatureTable featureTable;

    public CalloutTouchListener(Context context, MapView mapView, ServiceFeatureTable featureTable) {
        super(context, mapView);
        this.context = context;
        this.featureTable = featureTable;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        callout = mMapView.getCallout();
        if(callout.isShowing()){
            callout.dismiss();
        }
        final Point clickPoint = mMapView.screenToLocation(new android.graphics.Point(Math.round(e.getX()),
                Math.round(e.getY())));
        int tolerance = 10;
        double mapTolerance = tolerance * mMapView.getUnitsPerDensityIndependentPixel();
        ArcGISMap map = this.mMapView.getMap();
        Envelope envelope = new Envelope(clickPoint.getX() - mapTolerance, clickPoint.getY() - mapTolerance,
                clickPoint.getX() + mapTolerance, clickPoint.getY() + mapTolerance, map.getSpatialReference());
        QueryParameters query = new QueryParameters();
        query.setGeometry(envelope);
        final ListenableFuture<FeatureQueryResult> future = featureTable.queryFeaturesAsync(query,
                ServiceFeatureTable.QueryFeatureFields.LOAD_ALL);
        future.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    FeatureQueryResult result = future.get();
                    Iterator<Feature> iterator = result.iterator();
                    TextView calloutContent = new TextView(context.getApplicationContext());
                    calloutContent.setTextColor(Color.BLACK);
                    calloutContent.setSingleLine(false);
                    calloutContent.setVerticalScrollBarEnabled(true);
                    calloutContent.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
                    calloutContent.setMovementMethod(new ScrollingMovementMethod());
                    calloutContent.setLines(5);
                    Feature feature;
                    while (iterator.hasNext()){
                        feature = iterator.next();
                        Map<String, Object> attr = feature.getAttributes();
                        Set<String> keys = attr.keySet();
                        for(String key:keys){
                            if (key.equals("PARADA") || key.equals("LINEAS")){
                                Object value = attr.get(key);
                                calloutContent.append(key + ": " + value + "\n");
                            }
                        }
                        Envelope envelope = feature.getGeometry().getExtent();
                        mMapView.setViewpointGeometryAsync(envelope, 200);
                        // show CallOut
                        callout.setLocation(clickPoint);
                        callout.setContent(calloutContent);
                        callout.show();
                    }
                } catch (Exception e) {
                    Log.e(context.getResources().getString(R.string.app_name),
                            "Select feature failed: " + e.getMessage());
                }
            }
        });
        return super.onSingleTapConfirmed(e);
    }
}