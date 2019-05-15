package com.example.eilishvds.fitmap;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.navigation.Navigation;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link infoActiviteitFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link infoActiviteitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class infoActiviteitFragment extends Fragment implements OnMapReadyCallback{
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

    private FirebaseFirestore db;
    private Map<String, Object> map;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private int routeteller_route = 1;

    private final int EARTH_RADIUS = 6371;
    private double startLat;
    private double startLong;
    private double endLat;
    private double endLong;
    private double distance = 1;

    private Polyline line;
    private float zoomlevel = 10f;

    private Date beginTijd;
    private Date eindeTijd;
    public long tussenTijd;

    public infoActiviteitFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment infoActiviteitFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static infoActiviteitFragment newInstance(String param1, String param2) {
        infoActiviteitFragment fragment = new infoActiviteitFragment();
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

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        map = new HashMap<>();

        user = mAuth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_info_activiteit, container, false);

        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        map_view = (MapView) rootview.findViewById(R.id.mapViewInfo);

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
            mMap.getUiSettings().setScrollGesturesEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setIndoorLevelPickerEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.setBuildingsEnabled(true);
            mMap.getCameraPosition();
        }

        Toast toast = Toast.makeText(getContext(), "De route_teller:  " + routeteller_route, Toast.LENGTH_LONG);
        toast.show();

        db.collection("RouteBeschrijving").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }

                    routeteller_route = list.size();
                    Toast toast = Toast.makeText(getContext(), "Route: " + routeteller_route, Toast.LENGTH_LONG);
                    toast.show();

                    Log.d(TAG, list.toString());
                    HaalDocumentOp();

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void HaalDocumentOp() {
        Toast toast = Toast.makeText(getContext(), "De route_teller:  " + routeteller_route, Toast.LENGTH_LONG);
        toast.show();

        DocumentReference docRef = db.collection("RoutePoints").document("Route" + routeteller_route);
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
                        Context context = getContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, "DocumentSnapshot data: " + document.getData(), duration);
                        toast.show();

                        distance = tekenMarker(document, list);
                        schrijfGegevensweg(distance);
                    } else {
                        Log.d(TAG, "No such document");
                        Context context = getContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, "No such document" + tussenTijd, duration);
                        toast.show();

                        LatLng plaats = new LatLng(50, 4);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(plaats));

                        schrijfGegevensweg(tussenTijd);
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

    public void schrijfGegevensweg(double distance) {
        try {
            map.put("UID", user.getUid());
            map.put("Km", distance);
            map.put("tijd (in minuten)", tussenTijd);
            map.put("Km per H", 0);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Exception: " + e,
                    Toast.LENGTH_SHORT).show();
        }

        int routeteller_gegevens = routeteller_route;

        db.collection("RouteGegevens").document("Route" + routeteller_gegevens)
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

    }

    private double tekenMarker(DocumentSnapshot document, List<String> list) {
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

                startLat = punt1.getLatitude();
                startLong = punt1.getLongitude();
                assert punt2 != null;
                endLong = punt2.getLongitude();
                endLat = punt2.getLatitude();

                distance = distance + distance(startLat, startLong, endLat, endLong);

                /**Context context = getContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, "KM: " + distance, duration);
                toast.show();
                 */

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

        return distance;
    }

    private double distance(double startLat, double startLong, double endLat, double endLong) {
        double dLat = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);

        double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    private double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
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

    public void naarProfiel(View v){
        Navigation.findNavController(v).navigate(R.id.action_infoActiviteit_to_home);
    }

    public void nieuweActiviteit(View v){
        Navigation.findNavController(v).navigate(R.id.action_infoActiviteit_to_aanmakenActiviteit);
    }
}
