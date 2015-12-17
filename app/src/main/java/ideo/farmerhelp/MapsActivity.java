package ideo.farmerhelp;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;


import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.zzc;
import com.google.android.gms.common.api.zzl;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;

import android.database.sqlite.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMarkerDragListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, android.location.LocationListener, GoogleMap.OnMapClickListener
{

    List<LatLng> points = new ArrayList<LatLng>();
    PolygonOptions test = new PolygonOptions();
    Polygon polygon;
    int areaM;
    long areaMi;
    String areaStr;
    TextView tv;
    private GoogleApiClient mGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    TextView txtlat;
    String lat;
    String longi;
    LatLng myLocation;
    protected String latitude, longitude;
    protected boolean gps_Enabled, network_enabled;
    private static final String TAG = "LocationManager";
    private Context mContext;
    //private Listener mListener;
    private android.location.LocationManager mLocationManager;
    private boolean mRecordLocation;
    double pLong;
    double pLat;
    LatLng position;
    Marker loc;
    boolean tracking = false;
    DatabaseActivity db = new DatabaseActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) { //On map create
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
              .findFragmentById(R.id.map); //Create a support map fragment
               mapFragment.getMapAsync(this);
        tv = (TextView) findViewById(R.id.area);

       LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //LocationListener ll = new myLocationListener();
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        //db.create();
        //Intent intent = new Intent(this, DatabaseActivity.class);
        //startActivity(intent);

        //DatabaseActivity db = new DatabaseActivity();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


    public class myLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if(location != null)
            {
                pLong = location.getLongitude();
                pLat = location.getLatitude();
                position = new LatLng(pLat, pLong);
                //tv = (TextView) findViewById(R.id.area);
                //tv.setText(""+position);

            }
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        if(location != null)
        {
            pLong = location.getLongitude();
            pLat = location.getLatitude();
            position = new LatLng(pLat, pLong);
            mMap = mapFragment.getMap();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
            if(tracking)
            {


                if (position != null) {
                    //mMap = mapFragment.getMap();
                    loc = mMap.addMarker(new MarkerOptions()
                            .position(position)
                            .draggable(false)
                            .title("Location"));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 5));

                }

                points.add(position);
            }
        }
    }
    public void buttonOnClick(View v) {
        if(polygon != null) {
            polygon.remove();
            mMap.clear();

        }
        test = null;
        test = new PolygonOptions();
        points.clear();
        tv.setText("");


        startActivity(new Intent(v.getContext(), DatabaseActivity.class));
        db.AddItems(v);
    }

    public void startTracking(View v)
    {
        tracking = true;
    }

    public void getArea(View v)
    {

        test.addAll(points);
        polygon = mMap.addPolygon(test);
        //polygon.setPoints(points);
        polygon.setFillColor(Color.BLUE);

        areaM= (int)Math.round((SphericalUtil.computeArea(points)));
        //areaMi = Math.round(0.38610*(SphericalUtil.computeArea(points));
        areaStr = ""+areaM;
        tv = (TextView) findViewById(R.id.area);
        tv.setText(areaStr + " square metres."+"\n"+(int)Math.round((areaM/4048.84))+" acres.");
        tracking = false;

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }


    @Override
    public void onMarkerDrag(Marker marker)
    {
        //Do nothing
    }

    @Override
    public void onMarkerDragStart(Marker marker)
    {
        //Do nothing
    }
    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Update marker locations in array
        //polygon.remove();
        polygon.setPoints(points); //Update polygon

        //Calculate and print new area
        areaM = (int)Math.round(SphericalUtil.computeArea(points));
        //areaMi = Math.round(0.38610*(SphericalUtil.computeArea(points));
        areaStr = ""+areaM;
        tv.setText((areaStr) + " square metres."+"\n"+(areaM/4048.84)+" acres.");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }


}

