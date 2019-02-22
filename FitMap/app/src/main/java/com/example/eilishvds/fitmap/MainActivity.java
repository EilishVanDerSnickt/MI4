package com.example.eilishvds.fitmap;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, registrerenFragment.OnFragmentInteractionListener, wachtwoordVergetenFragment.OnFragmentInteractionListener, homeFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
