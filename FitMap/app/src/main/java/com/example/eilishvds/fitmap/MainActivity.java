package com.example.eilishvds.fitmap;

import android.content.Context;
import android.net.Uri;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

import androidx.navigation.Navigation;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, registrerenFragment.OnFragmentInteractionListener, wachtwoordVergetenFragment.OnFragmentInteractionListener, homeFragment.OnFragmentInteractionListener {

    private FirebaseAnalytics mFirebaseAnalytics;

    private EditText edittext1;
    private EditText edittext2;
    private EditText edittext3;
    private EditText edittext4;
    private CheckBox checkbox;
    private boolean checked;
    private homeFragment home = new homeFragment();
    private LoginFragment login = new LoginFragment();
    private registrerenFragment registreren = new registrerenFragment();
    private wachtwoordVergetenFragment wachtwoordVergeten = new  wachtwoordVergetenFragment();

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    public void activityMain(View v){
        edittext1 = (EditText)findViewById(R.id.edit_login_emailadres);
        edittext2 = (EditText)findViewById(R.id.edit_login_wachtwoord);

        String email = edittext1.getText().toString();
        String wachtwoord = edittext2.getText().toString();

        login.Inloggen(v, email, wachtwoord);
    }

    public void registreerNu(View v){
        login.RegistreerNu(v);
    }


    public void  wachtwoord(View v){
        login.WachtwoordVergeten(v);
    }

    public void registreer(View v){
        edittext1 = (EditText)findViewById(R.id.edit_registratie_voornaam);
        edittext2 = (EditText)findViewById(R.id.edit_registratie_emailadres);
        edittext3 = (EditText)findViewById(R.id.edit_registratie_wachtwoord);
        edittext4 = (EditText)findViewById(R.id.edit_registratie_wachtwoord2);
        checkbox = (CheckBox)findViewById(R.id.text_licentievoorwaarden);

        String naam = edittext1.getText().toString();
        String email = edittext2.getText().toString();
        String wachtwoord = edittext3.getText().toString();
        String wachtwoord2 = edittext4.getText().toString();

        if (checkbox.isChecked()){
            checked = true;
        }
        else{
            checked = false;
        }

        if (!validateForm(naam, email, wachtwoord, wachtwoord2, checked)) {
            return;
        }
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, wachtwoord)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            /*updateUI(user);*/
                            Navigation.findNavController(findViewById(R.id.fragment)).navigate(R.id.action_registreren_to_login);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            /*updateUI(null);*/
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]//

    }

    public void aanmelden(View v){
        registreren.AnnuleerRegistratie(v);
    }

    public void wachtwoordVergeten(View v){
       edittext1 = (EditText)findViewById(R.id.edit_wachtwoordVergeten_emailadres);

       String email = edittext1.getText().toString();

       wachtwoordVergeten.WachtwoordVergeten(email, v);
    }

    public void annuleerWachtwoordVergeten(View v){
        wachtwoordVergeten.AnnuleerWachtwoordVergeten(v);
    }

    private Boolean validateForm(String naam, String email, String wachtwoord, String wachtwoord2, boolean checked) {
        boolean valid = true;

        if (naam.isEmpty()) {
            edittext1 = (EditText)findViewById(R.id.edit_registratie_voornaam);
            edittext1.setError("This field is required");
            valid = false;
        }
        if (email.isEmpty()) {
            edittext2 = (EditText)findViewById(R.id.edit_registratie_emailadres);
            edittext2.setError("This field is required");
            valid = false;
        }
        if (wachtwoord.isEmpty()){
            edittext3 = (EditText)findViewById(R.id.edit_registratie_wachtwoord);
            edittext3.setError("This field is required");
            valid = false;
        }
        if (wachtwoord2.isEmpty() || !wachtwoord2.equals(wachtwoord)){
            edittext4 = (EditText)findViewById(R.id.edit_registratie_wachtwoord2);
            edittext4.setError("De wachtwoorden moeten hetzelfde zijn");
            valid = false;
        }
        if (!checked){
            checkbox = (CheckBox)findViewById(R.id.text_licentievoorwaarden);
            checkbox.setError("You have to accept the license agreement");
            valid = false;
        }

        return valid;
    }
}
