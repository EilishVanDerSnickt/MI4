package com.example.eilishvds.fitmap;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class viewPager_tab3 extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView map_view;
    private View rootview;
    private static final int MY_REQUEST_INT = 117;
    private Polyline line;
    private float zoomlevel = 10f;

    private FirebaseFirestore db;

    public viewPager_tab3(){
        //constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.viewpager_tab3, container, false);

        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        map_view = (MapView) rootview.findViewById(R.id.mapViewTab3);

        if (map_view != null) {
            map_view.onCreate(null);
            map_view.onResume();
            map_view.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, MY_REQUEST_INT);

            }
            Toast.makeText(this.getContext(), "You have no permissions",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setScrollGesturesEnabled(false);
            mMap.getUiSettings().setZoomGesturesEnabled(false);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setIndoorLevelPickerEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.setBuildingsEnabled(true);
            mMap.getCameraPosition();
        }

        DocumentReference docRef = db.collection("RoutePoints").document("Route" + 1);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        List<String> list = new ArrayList<>();
                        Map<String, Object> map = document.getData();

                        if (map != null){
                            for (Map.Entry<String, Object> entry : map.entrySet()){
                                list.add(entry.getValue().toString());
                            }
                        }

                        tekenMarker(document, list);
                    } else {
                        Log.d(TAG, "No such document");
                        Context context = getContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, "No such document", duration);
                        toast.show();

                        LatLng plaats = new LatLng(50, 4);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(plaats));

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    Context context = getContext();
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, "get failed with " + task.getException(), duration);
                    toast.show();

                    LatLng plaats = new LatLng(50, 4);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(plaats));
                }
            }
        });
        // [END get_document]
    }

    private void tekenMarker(DocumentSnapshot document, List<String> list) {
        int i = 1;
        try {
            for (String s : list) {
                GeoPoint punt1 = document.getGeoPoint("Point" + i);

                i = i + 1;

                GeoPoint punt2 = document.getGeoPoint("Point" + i);

                assert punt1 != null;
                LatLng origin = new LatLng(punt1.getLatitude(), punt1.getLongitude());
                LatLng dest = null;
                if (punt2 != null) {
                    dest = new LatLng(punt2.getLatitude(), punt2.getLongitude());
                }

                line = mMap.addPolyline(new PolylineOptions().clickable(false).add(origin, dest));
            }
        } catch (Exception e) {
            Context context = getContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, e.toString(), duration);
            toast.show();
        }


        LatLng plaats = new LatLng(document.getGeoPoint("Point1").getLatitude(), document.getGeoPoint("Point" + document.getData().size()).getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(plaats, zoomlevel));
    }
}
