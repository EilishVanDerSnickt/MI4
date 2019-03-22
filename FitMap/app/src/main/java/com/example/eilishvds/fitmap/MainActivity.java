package com.example.eilishvds.fitmap;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, registrerenFragment.OnFragmentInteractionListener, wachtwoordVergetenFragment.OnFragmentInteractionListener, homeFragment.OnFragmentInteractionListener, settingsFragment.OnFragmentInteractionListener, aanmakenActiviteit.OnFragmentInteractionListener, emailWijzigenFragment.OnFragmentInteractionListener, wachtwoordWijzigenFragment.OnFragmentInteractionListener, popupFragment.OnFragmentInteractionListener, LocatieMapFragment.OnFragmentInteractionListener, annuleerActiviteitFragment.OnFragmentInteractionListener{

    private FirebaseAnalytics mFirebaseAnalytics;

    private EditText edittext1;
    private EditText edittext2;
    private EditText edittext3;
    private EditText edittext4;
    private CheckBox checkbox;
    private boolean checked;
    private TextView textview;
    private homeFragment home = new homeFragment();
    private LoginFragment login = new LoginFragment();
    private registrerenFragment registreren = new registrerenFragment();
    private wachtwoordVergetenFragment wachtwoordVergeten = new wachtwoordVergetenFragment();
    private settingsFragment instellingen = new settingsFragment();
    private aanmakenActiviteit activiteit = new aanmakenActiviteit();
    private emailWijzigenFragment emailWijzigen = new emailWijzigenFragment();
    private wachtwoordWijzigenFragment wachtwoordWijzigen = new wachtwoordWijzigenFragment();
    private popupFragment popup = new popupFragment();
    private LocatieMapFragment locatieMap = new LocatieMapFragment();
    private annuleerActiviteitFragment annuleerActiviteit = new annuleerActiviteitFragment();

    private FirebaseAuth mAuth;
    private FirebaseUser user;

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

    public void wachtwoordWijzigen(View v) {
        edittext1 = (EditText) findViewById(R.id.edit_wachtwoordWijzigen_emailadres);

        String email = edittext1.getText().toString();

        /**
         if (!validateFormWachtwoordWijzigen(email)){
         return;
         }
         */
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(MainActivity.this, "Email sent to " + mAuth.getCurrentUser().getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(findViewById(R.id.fragment)).navigate(R.id.action_wachtwoordWijzigen_to_instellingen);
                        }else{
                            Log.d(TAG, "Couldn't sent email");
                            Toast.makeText(MainActivity.this, "Couldn't sent email",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void activityMain(View v) {
        edittext1 = (EditText) findViewById(R.id.edit_login_emailadres);
        edittext2 = (EditText) findViewById(R.id.edit_login_wachtwoord);

        String email = edittext1.getText().toString();
        String wachtwoord = edittext2.getText().toString();

        if (!validateFormInloggen(email, wachtwoord)) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, wachtwoord)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            user = mAuth.getCurrentUser();
                            Navigation.findNavController(findViewById(R.id.fragment)).navigate(R.id.action_login_to_home);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void registreerNu(View v) {
        login.RegistreerNu(v);
    }


    public void wachtwoord(View v) {
        login.WachtwoordVergeten(v);
    }

    public void registreer(View v) {
        edittext1 = (EditText) findViewById(R.id.edit_registratie_voornaam);
        edittext2 = (EditText) findViewById(R.id.edit_registratie_emailadres);
        edittext3 = (EditText) findViewById(R.id.edit_registratie_wachtwoord);
        edittext4 = (EditText) findViewById(R.id.edit_registratie_wachtwoord2);
        checkbox = (CheckBox) findViewById(R.id.text_licentievoorwaarden);

        String naam = edittext1.getText().toString();
        String email = edittext2.getText().toString();
        String wachtwoord = edittext3.getText().toString();
        String wachtwoord2 = edittext4.getText().toString();

        if (checkbox.isChecked()) {
            checked = true;
        } else {
            checked = false;
        }

        if (!validateFormRegistreren(naam, email, wachtwoord, wachtwoord2, checked)) {
            return;
        }
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, wachtwoord)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();
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

    public void aanmelden(View v) {
        registreren.AnnuleerRegistratie(v);
    }

    public void wachtwoordVergeten(View v) {
        edittext1 = (EditText) findViewById(R.id.edit_wachtwoordVergeten_emailadres);

        String email = edittext1.getText().toString();

        /**
        if (!validateFormWachtwoordWijzigen(email)){
            return;
        }
*/
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(MainActivity.this, "Email sent to " + mAuth.getCurrentUser().getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(findViewById(R.id.fragment)).navigate(R.id.action_wachtwoordVergeten_to_login);
                        }else{
                            Log.d(TAG, "Couldn't sent email");
                            Toast.makeText(MainActivity.this, "Couldn't sent email",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void annuleerWachtwoordVergeten(View v) {
        wachtwoordVergeten.AnnuleerWachtwoordVergeten(v);
    }

    private Boolean validateFormRegistreren(String naam, String email, String wachtwoord, String wachtwoord2, boolean checked) {
        boolean valid = true;

        if (naam.isEmpty()) {
            edittext1 = (EditText) findViewById(R.id.edit_registratie_voornaam);
            edittext1.setError("This field is required");
            valid = false;
        }
        if (email.isEmpty()) {
            edittext2 = (EditText) findViewById(R.id.edit_registratie_emailadres);
            edittext2.setError("This field is required");
            valid = false;
        }
        if (wachtwoord.isEmpty()) {
            edittext3 = (EditText) findViewById(R.id.edit_registratie_wachtwoord);
            edittext3.setError("This field is required");
            valid = false;
        }
        if (wachtwoord2.isEmpty() || !wachtwoord2.equals(wachtwoord)) {
            edittext4 = (EditText) findViewById(R.id.edit_registratie_wachtwoord2);
            edittext4.setError("De wachtwoorden moeten hetzelfde zijn");
            valid = false;
        }
        if (!checked) {
            checkbox = (CheckBox) findViewById(R.id.text_licentievoorwaarden);
            checkbox.setError("You have to accept the license agreement");
            valid = false;
        }

        return valid;
    }

    private Boolean validateFormInloggen(String email, String wachtwoord){
        boolean valid = true;

        if (email.isEmpty()) {
            edittext1 = (EditText)findViewById(R.id.edit_login_emailadres);
            edittext1.setError("This field is required");
            valid = false;
        }
        if (wachtwoord.isEmpty()) {
            edittext2 = (EditText)findViewById(R.id.edit_login_wachtwoord);
            edittext2.setError("This field is required");
            valid = false;
        }

        return  valid;
    }

    public void openInstellingen(View v){
        home.instellingen(v);
    }

    public void startActiviteit(View v){
        home.activiteit(v);
    }

    public void annuleerActiviteit(View v){
        activiteit.home(v);
    }

    public void WachtwoordWijzigen(View v){
        instellingen.wachtwoordWijzigen(v);
    }

    public void EmailWijzigen(View v){
        instellingen.emailWijzigen(v);
    }

    public void AccountVerwijderen(View v){
        instellingen.accountVerwijderen(v);
    }

    public void annuleerInstellingen(View v){
        instellingen.AnnuleerInstellingen(v);
    }

    public void annuleerWijzigWachtwoord(View v){
        wachtwoordWijzigen.AnnuleerWachtwoordWijzigen(v);
    }

    public void annuleerWijzigEmail(View v){
        emailWijzigen.AnnuleerEmailWijzigen(v);
    }

    public void annuleerBevestiging(View v){
        popup.naarInstellingen(v);
    }

    public void bevesting(View v){
        user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Account " + mAuth.getCurrentUser().getEmail() + " is succesvol verwijdert",
                                    Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(findViewById(R.id.fragment)).navigate(R.id.action_popup_to_inloggen);
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Kan account niet verwijderen",
                                    Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(findViewById(R.id.fragment)).navigate(R.id.action_popup_to_instellingen);
                        }
                    }
                });
        // [END delete_user]

    }

    private Boolean validateFormWachtwoordWijzigen(String email){
        boolean valid = true;


        if (email.isEmpty()) {
            edittext1 = (EditText)findViewById(R.id.edit_wachtwoordWijzigen_emailadres);
            edittext1.setError("This field is required");
            valid = false;
        }

        return  valid;
    }

    public String getUserProfile() {
        // [START get_user_profile]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

           return email;
        }

        return "null";
        // [END get_user_profile]
    }

    public void emailIsGewijzigd(View v){
        // [START update_email]
        user = FirebaseAuth.getInstance().getCurrentUser();
        textview = (TextView) findViewById(R.id.edit_emailWijzigen_huidigEmail);
        edittext1 = (EditText) findViewById(R.id.edit_emailWijzigen_nieuwEmail);
        String huidigeEmail = textview.getText().toString();
        String nieuweEmail = edittext2.getText().toString();

        if (!validateFormEmailWijzigen(huidigeEmail, nieuweEmail)) {
            return;
        }

        user.updateEmail(nieuweEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Email adres is geupdate",
                                    Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(findViewById(R.id.fragment)).navigate(R.id.action_emailWijzigen_to_instellingen);
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Kan emailadres niet updaten",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END update_email]
    }

    private Boolean validateFormEmailWijzigen(String huidigeEmail, String nieuweEmail){
        boolean valid = true;

        if (nieuweEmail.isEmpty() || nieuweEmail == huidigeEmail) {
            edittext1 = (EditText)findViewById(R.id.edit_emailWijzigen_nieuwEmail);
            edittext1.setError("This field is required");
            valid = false;
        }

        return  valid;
    }

    public void LogOff(View v){
        // [START auth_sign_out]
        FirebaseAuth.getInstance().signOut();

        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            Navigation.findNavController(v).navigate(R.id.action_instellingen_to_login);
        }
        else {
            Toast.makeText(MainActivity.this, "Kan niet uitloggen",
                    Toast.LENGTH_SHORT).show();
        }
        // [END auth_sign_out]
    }

    public void annuleerlocatieMap(View v){
        locatieMap.annuleer(v);
    }
    public void bevestingAnnulatie(View v){
        annuleerActiviteit.bevesting(v);
    }

    public void annuleerAnnulatie(View v){
        annuleerActiviteit.annulleer(v);
    }

    public void activiteitLocatie(View v){
        activiteit.activiteitLocatie(v);
    }
}



