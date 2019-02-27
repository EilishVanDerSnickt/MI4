package com.example.eilishvds.fitmap;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, registrerenFragment.OnFragmentInteractionListener, wachtwoordVergetenFragment.OnFragmentInteractionListener, homeFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void activityMain(View v){
        //eerst controleren of de inloggegevens juist zijn
        Navigation.findNavController(v).navigate(R.id.action_login_to_home);
    }

    public void registreerNu(View v){
        Navigation.findNavController(v).navigate(R.id.action_login_to_registreren);
    }


    public void  wachtwoord(View v){
        Navigation.findNavController(v).navigate(R.id.action_login_to_wachtwoordVergeten);
    }

    public void registreer(View v){
        //eerst controleren of account is aangemaakt
        Navigation.findNavController(v).navigate(R.id.action_registreren_to_login);
    }

    public void aanmelden(View v){
        Navigation.findNavController(v).navigate(R.id.action_registreren_to_login);
    }

    public void wachtwoordVergeten(View v){
        //eerst controleren of de email juist is
        Navigation.findNavController(v).navigate(R.id.action_wachtwoordVergeten_to_login);
    }

    public void annuleerWachtwoordVergeten(View v){
        Navigation.findNavController(v).navigate(R.id.action_wachtwoordVergeten_to_login);
    }


}
