package com.example.eilishvds.fitmap;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.navigation.Navigation;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocatieMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocatieMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocatieMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private GoogleMap mMap;
    private MapView map_view;
    private View rootview;
    private static final int MY_REQUEST_INT = 117;
    private final float zoomlevel = 10f;
    private ArrayList locationPoints = new ArrayList();
    private Polyline locationLine;

    private FirebaseFirestore db;
    private Map<String, Object> map;
    private int markerteller = 0;
    private int routeteller_route;

    public LocatieMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocatieMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocatieMapFragment newInstance(String param1, String param2) {
        LocatieMapFragment fragment = new LocatieMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getActivity().getWindow().setLayout((int) (width), (int) (height));

        db = FirebaseFirestore.getInstance();

        map = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_locatie_map, container, false);
        return rootview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        map_view = (MapView) rootview.findViewById(R.id.mapViewLocatie);

        if (map_view != null) {
            map_view.onCreate(null);
            map_view.onResume();
            map_view.getMapAsync(this);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onMapClick(LatLng latLng) {
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
            mMap.setMyLocationEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setScrollGesturesEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.setBuildingsEnabled(true);
            mMap.getCameraPosition();
        }

        mMap.setOnMyLocationClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnPolylineClickListener(this);

        LatLng plaats = new LatLng(50, 4);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(plaats));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(plaats, zoomlevel));

        db.collection("RouteBeschrijving").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }

                    routeteller_route = list.size();
                    Toast toast = Toast.makeText(getContext(), "Route: " +routeteller_route, Toast.LENGTH_LONG);
                    toast.show();

                    Log.d(TAG, list.toString());
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //locationPoints.clear();
                try{
                    LatLng latlngLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latlngLocation).title("LocatieMarker"));
                    locationPoints.add(latlngLocation);
                    markerteller = markerteller + 1;
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latlngLocation));

                    if (locationPoints.size() == 2) {
                        LatLng origin = (LatLng) locationPoints.get(0);
                        LatLng dest = (LatLng) locationPoints.get(1);
                        locationLine = mMap.addPolyline(new PolylineOptions().clickable(true).add(origin, dest));
                    }
                    else if (locationPoints.size() > 2) {
                        List<LatLng> points = locationLine.getPoints();
                        points.add(latlngLocation);
                        locationLine.setPoints(points);
                    }


                    GeoPoint geoPoint = new GeoPoint(latlngLocation.latitude, latlngLocation.longitude);
                    /**
                    Context context = getContext();
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, geoPoint.toString(), duration);
                    toast.show();
                    */
                    mMap.clear();

                    map.put("Point" + markerteller, geoPoint);

                    db.collection("RoutePoints").document("Route" + routeteller_route)
                            .set(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    Context context = getContext();
                                    int duration = Toast.LENGTH_SHORT;

                                    Toast toast = Toast.makeText(context, "DocumentSnapshot successfully written!", duration);
                                    toast.show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                    Context context = getContext();
                                    int duration = Toast.LENGTH_SHORT;

                                    Toast toast = Toast.makeText(context, "Error writing document", duration);
                                    toast.show();
                                }
                            });
                } catch (Exception e){
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getContext(), "OnLocationChanged fout: " + e, duration);
                    toast.show();
                }

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
        });

    }


    public void annuleer(View v){
        Navigation.findNavController(v).navigate(R.id.action_locatieMap_to_annuleerActiviteit);
    }

    public void stopActiviteit(View v){
        Navigation.findNavController(v).navigate(R.id.action_locatieMap_to_infoActiviteit);
    }
}
