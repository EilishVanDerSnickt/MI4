package com.example.eilishvds.fitmap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class viewPager_tab1 extends Fragment {

    private View rootview;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    public viewPager_tab1(){
        //constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        user = mAuth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootview =  inflater.inflate(R.layout.viewpager_tab1, container, false);

        VultextView();

        return rootview;
    }

    private void VultextView() {
        db.collection("RouteBeschrijving").whereEqualTo("UID", user.getUid())
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
                                
                                toonInTextView(document, list, textView1, textView2, textView3);
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

    @SuppressLint("SetTextI18n")
    private void toonInTextView(QueryDocumentSnapshot document, List<String> list, TextView textView1, TextView textView2, TextView textView3) {
        try {
            textView1 = rootview.findViewById(R.id.tab_1_titel);
            textView2 = rootview.findViewById(R.id.tab_1_beschrijving);
            textView3 = rootview.findViewById(R.id.tab_1_middel);
            int i = 1;

            for (String s : list) {
                if (i == 1) {
                    String value = document.getString("titel");
                    textView1.setText("Titel: " + value);

                } else if (i == 2) {
                    String value = document.getString("beschrijving");
                    textView2.setText("Beschrijving: " + value);
                } else if (i == 3){
                    String value = document.getString("middel");
                    textView3.setText("Middel: " + value);
                }
                i = i + 1;
            }
        }catch (Exception e){
            Context context = getContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, "Exception: " + e, duration);
            toast.show();
        }
    }
}
