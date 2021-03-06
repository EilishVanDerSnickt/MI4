package com.example.eilishvds.fitmap;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.navigation.Navigation;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link homeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link homeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeFragment extends Fragment implements SearchView.OnQueryTextListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<String> RouteTitels;

    private OnFragmentInteractionListener mListener;

    private TabLayout tablayout;
    private ViewPager viewpager;
    private LinearLayout linearLayout;
    private TextView textView;
    private View rootview;
    private CardView cardView;
    private ListView listView;
    private ScrollView scrollView;
    private SearchView searchView;
    private String[] animallist;

    private ListViewAdapter adapter;
    private ArrayList<resultaten> arrayList = new ArrayList<resultaten>();

    private FirebaseFirestore db;

    public homeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
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

        getActivity().getWindow().setLayout((int) (width), (int)(height));

        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_home, container, false);

        /**
        animallist = new String[]{"Lion", "Tiger", "Dog",
                "Cat", "Tortoise", "Rat", "Elephant", "Fox",
                "Cow","Donkey","Monkey"};

        listView = (ListView) rootview.findViewById(R.id.list_view);

        for (int i = 0; i < animallist.length; i++){
            resultaten resultaat = new resultaten(animallist[i]);
            arrayList.add(resultaat);
        }
         */

        viewpager = (ViewPager)rootview.findViewById(R.id.view_pager);
        setupViewPager(viewpager);

        tablayout = (TabLayout)rootview.findViewById(R.id.tab_layout);
        tablayout.setupWithViewPager(viewpager);

        addTextViews();
        /**
        adapter = new ListViewAdapter(getContext(), arrayList);

        listView.setAdapter(adapter);
         */

        searchView = (SearchView)rootview.findViewById(R.id.search_view);
        //searchView.setOnQueryTextListener(this);

        return rootview;
    }

    private void addTextViews() {
        db.collection("RouteBeschrijving")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                List<String> list = new ArrayList<>();
                                Map<String, Object> map = document.getData();

                                if (map != null){
                                    for (Map.Entry<String, Object> entry : map.entrySet()){
                                        list.add(entry.getValue().toString());
                                    }
                                }
                                Context context = getContext();
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, document.getId() + " => " + document.getData(), duration);
                                toast.show();

                                toonInTextView(document, list, textView);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Context context = getContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, "Error getting documents: " + task.getException(), duration);
                            toast.show();
                        }
                    }
                });
    }

    private void toonInTextView(QueryDocumentSnapshot document, List<String> list, TextView textView) {
        try {
            linearLayout = (LinearLayout) rootview.findViewById(R.id.home_linear_layout);
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int i = 1;
            int j = 0;
            for (String s : list) {
                if (i % 2 == 1) {
                    String value = document.getString("titel");
                    cardView = new CardView(getContext());
                    this.linearLayout.addView(cardView);
                    textView = new TextView(getContext());
                    textView.setLayoutParams(lparams);
                    textView.setText(value);
                    textView.setLinksClickable(true);
                    cardView.addView(textView);
                    /**
                    resultaten resultaat = new resultaten(value);
                    arrayList.add(resultaat);
                    j = j + 1;
                     */
                }
                i = i + i;
            }
        }catch (Exception e){
            Context context = getContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, "Exception: " + e, duration);
            toast.show();
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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
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

    private void setupViewPager(ViewPager viewpager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new viewPager_tab1());
        adapter.addFragment(new viewPager_tab2());
        adapter.addFragment(new viewPager_tab3());
        viewpager.setAdapter(adapter);

    }

    public void instellingen(View v){
        Navigation.findNavController(v).navigate(R.id.action_home_to_settings);
    }

    public void activiteit(View v){
        Navigation.findNavController(v).navigate(R.id.action_home_to_aanmakenActiviteit);
    }
}
