package marrero.hamad.darwin.guaguaslapalma.view.listener;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
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
import com.esri.arcgisruntime.mapping.GeoElement;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import marrero.hamad.darwin.guaguaslapalma.R;


public class CalloutTouchListener extends DefaultMapViewOnTouchListener{

    private Callout callout;
    private Context context;

    public CalloutTouchListener(Context context, MapView mapView) {
        super(context, mapView);
        this.context = context;
    }

    @Override
    public boolean onSingleTapConfirmed(final MotionEvent e) {
        callout = mMapView.getCallout();
        if(callout.isShowing()){
            callout.dismiss();
        }
        identifyTouchedGeoElement(e);
        return super.onSingleTapConfirmed(e);
    }

    private void identifyTouchedGeoElement(MotionEvent motionEvent) {
        android.graphics.Point screenPoint = createPoint((int) motionEvent.getX(), (int) motionEvent.getY());
        final Point arcGISClickPoint = mMapView.screenToLocation(createPointWithRoundedCoordinates(screenPoint.x,
                screenPoint.y));
        int tolerance = 10;
        double mapTolerance = tolerance * mMapView.getUnitsPerDensityIndependentPixel();
        final ArcGISMap map = this.mMapView.getMap();
        Envelope envelope = new Envelope(arcGISClickPoint.getX() - mapTolerance, arcGISClickPoint.getY() - mapTolerance,
                arcGISClickPoint.getX() + mapTolerance, arcGISClickPoint.getY() + mapTolerance, map.getSpatialReference());
        final QueryParameters query = new QueryParameters();
        query.setGeometry(envelope);
        final ListenableFuture<List<IdentifyLayerResult>> identifyFuture = mMapView.identifyLayersAsync(screenPoint, 20,
                false);
        identifyFuture.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    List<IdentifyLayerResult> identifyLayersResults = identifyFuture.get();
                    if (identifyLayersResults.size() > 0) {
                        IdentifyLayerResult identifyLayerResult = identifyLayersResults.get(0);
                        if (identifyLayerResult.getElements().size() > 0) {
                            GeoElement topmostElement = identifyLayerResult.getElements().get(0);
                            if (topmostElement instanceof Feature) {
                                Feature identifiedFeature = (Feature) topmostElement;
                                ServiceFeatureTable featureTable = (ServiceFeatureTable) identifiedFeature.getFeatureTable();
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
                                            while (iterator.hasNext()) {
                                                feature = iterator.next();
                                                Map<String, Object> attr = feature.getAttributes();
                                                Set<String> keys = attr.keySet();
                                                for (String key : keys) {
                                                    if (key.equals("PARADA") || key.equals("LINEAS") ||
                                                        key.equals("Linea") || key.equals("Recorrido")) {
                                                        Object value = attr.get(key);
                                                        calloutContent.append(key + ": " + value + "\n");
                                                    }
                                                }
                                                calloutContent.append("\n");
                                                Envelope envelope = feature.getGeometry().getExtent();
                                                mMapView.setViewpointGeometryAsync(envelope, 200);
                                                callout.setLocation(arcGISClickPoint);
                                                callout.setContent(calloutContent);
                                                callout.show();
                                            }
                                        } catch (Exception e) {
                                            Log.e(context.getResources().getString(R.string.app_name),
                                                    "No se ha seleccionado un elemento: " + e.getMessage());
                                        }
                                    }
                                });
                            }
                        }
                    }
                } catch (InterruptedException | ExecutionException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    @NonNull
    private android.graphics.Point createPoint(int x, int y) {
        return new android.graphics.Point(x, y);
    }

    @NonNull
    private android.graphics.Point createPointWithRoundedCoordinates(int x, int y) {
        return new android.graphics.Point(Math.round(x), Math.round(y));
    }
}