package com.example.eilishvds.fitmap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.okhttp.Route;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import androidx.navigation.Navigation;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, registrerenFragment.OnFragmentInteractionListener, wachtwoordVergetenFragment.OnFragmentInteractionListener, homeFragment.OnFragmentInteractionListener, settingsFragment.OnFragmentInteractionListener, aanmakenActiviteit.OnFragmentInteractionListener, emailWijzigenFragment.OnFragmentInteractionListener, wachtwoordWijzigenFragment.OnFragmentInteractionListener, popupFragment.OnFragmentInteractionListener, LocatieMapFragment.OnFragmentInteractionListener, annuleerActiviteitFragment.OnFragmentInteractionListener, tekenMapFragment.OnFragmentInteractionListener, annuleerActiviteitTekenFragment.OnFragmentInteractionListener, infoActiviteitFragment.OnFragmentInteractionListener, GewichtFragment.OnFragmentInteractionListener, RegioFragment.OnFragmentInteractionListener{

    private FirebaseAnalytics mFirebaseAnalytics;

    private EditText edittext1;
    private EditText edittext2;
    private EditText edittext3;
    private EditText edittext4;
    private CheckBox checkbox;
    private boolean checked;
    private TextView textview;
    private ScrollView scrollView;
    private CardView cardView;
    private Spinner spinner;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioGroup radioGroup;

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
    private tekenMapFragment tekenMap = new tekenMapFragment();
    private annuleerActiviteitTekenFragment annuleerTekenActiviteit = new annuleerActiviteitTekenFragment();
    private infoActiviteitFragment infoActiviteit = new infoActiviteitFragment();
    private GewichtFragment gewicht = new GewichtFragment();
    private RegioFragment regio = new RegioFragment();

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private Map<String, Object> map;
    private Map<String, Object> map2;
    private Map<String, Object> map3;

    private int routeteller_beschrijving = 0;
    private int routeteller_gegevens = 0;
    private int routeteller_route;

    private Date startTijd;
    private Date stopTijd;
    public long elapsedTijd;

    private final int EARTH_RADIUS = 6371;
    private double startLat;
    private double startLong;
    private double endLat;
    private double endLong;
    private double distance = 1;
    private Polyline line;
    private float zoomlevel = 10f;
    private GoogleMap mMap;

    /**
    private long startTime = 0L;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;
    private String tijd = "";

    private Handler customHandler = new Handler();
    private Runnable updateTimerThread = new Runnable() {
        @Override
        public String run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            customHandler.postDelayed(this, 0);

            tijd = mins + ":" + secs + ":" + milliseconds;

            return tijd;
        }
    };
     */

    private ArrayList<String> titelRoutes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        map = new HashMap<>();
        map2 = new HashMap<>();
        map3 = new HashMap<>();
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
    public void bevestingAnnulatieLocatie(View v){
        berekenRouteTeller();
        annuleerActiviteit.bevesting(v);
    }

    public void annuleerAnnulatieLocatie(View v){
        annuleerActiviteit.annulleer(v);
    }

    public void activiteitLocatie(View v){
        edittext1 = (EditText) findViewById(R.id.edit_titelActiviteit);
        edittext2 = (EditText) findViewById(R.id.edit_beschrijvingActiviteit);
        radioButton1 = (RadioButton) findViewById(R.id.rdb_wandelen);
        radioButton2 = (RadioButton) findViewById(R.id.rdb_lopen);
        radioButton3 = (RadioButton) findViewById(R.id.rdb_fietsen);

        String titel = edittext1.getText().toString();
        String beschrijving = edittext2.getText().toString();
        String middel;

        if (radioButton1.isChecked()){
            middel = "Wandelen";
        }
        else if (radioButton2.isChecked()){
            middel = "Lopen";
        }
        else if (radioButton3.isChecked()) {
            middel = "Fietsen";
        } else {
            middel = "";
        }

        if (!ValideerAanmakenActiviteit(titel, beschrijving, middel)) {
            return;
        }

        try{
            map.put("UID", user.getUid());
            map.put("middel", middel);
            map.put("titel", titel);
            map.put("beschrijving", beschrijving);
        } catch (Exception e){
            Toast.makeText(this, "Exception: " + e,
                    Toast.LENGTH_SHORT).show();
        }

        routeteller_beschrijving = routeteller_beschrijving + 1;

        db.collection("RouteBeschrijving").document("Route" + routeteller_beschrijving)
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, "DocumentSnapshot successfully written!", duration);
                        toast.show();

                        Navigation.findNavController(findViewById(R.id.fragment)).navigate(R.id.action_aanmakenActiviteit_to_locatieMap);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, "Error writing document", duration);
                        toast.show();
                    }
                });

        startTijd = Calendar.getInstance().getTime();

        Toast toast = Toast.makeText(getApplicationContext(), "Start tijd: " + startTijd, Toast.LENGTH_SHORT);
        toast.show();
    }

    private boolean ValideerAanmakenActiviteit(String titel, String beschrijving, String middel) {
        boolean valid = true;

        if (titel.isEmpty()) {
            edittext1 = (EditText) findViewById(R.id.edit_titelActiviteit);
            edittext1.setError("This field is required");
            valid = false;
        }
        if (beschrijving.isEmpty()) {
            edittext2 = (EditText) findViewById(R.id.edit_beschrijvingActiviteit);
            edittext2.setError("This field is required");
            valid = false;
        }
        if (middel.isEmpty()){
            radioButton3 = (RadioButton) findViewById(R.id.rdb_fietsen);
            radioButton3.setError("this field is required");
            valid = false;
        }

        return valid;
    }

    public void annuleerTekenMap(View v){
        tekenMap.annuleer(v);
    }

    public void activiteitTeken(View v){
        edittext1 = (EditText) findViewById(R.id.edit_titelActiviteit);
        edittext2 = (EditText) findViewById(R.id.edit_beschrijvingActiviteit);
        radioButton1 = (RadioButton) findViewById(R.id.rdb_wandelen);
        radioButton2 = (RadioButton) findViewById(R.id.rdb_lopen);
        radioButton3 = (RadioButton) findViewById(R.id.rdb_fietsen);

        String titel = edittext1.getText().toString();
        String beschrijving = edittext2.getText().toString();
        String middel;

        if (radioButton1.isChecked()){
            middel = "Wandelen";
        }
        else if (radioButton2.isChecked()){
            middel = "Lopen";
        }
        else if (radioButton3.isChecked()) {
            middel = "Fietsen";
        } else {
            middel = "";
        }


        if (!ValideerAanmakenActiviteit(titel, beschrijving, middel)) {
            return;
        }

        try{
            map.put("UID", user.getUid());
            map.put("middel", middel);
            map.put("titel", titel);
            map.put("beschrijving", beschrijving);
        } catch (Exception e){
            Toast.makeText(MainActivity.this, "Exception: " + e,
                    Toast.LENGTH_SHORT).show();
        }

        routeteller_beschrijving = routeteller_beschrijving + 1;
        db.collection("RouteBeschrijving").document("Route" + routeteller_beschrijving)
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, "DocumentSnapshot successfully written!", duration);
                        toast.show();

                        Navigation.findNavController(findViewById(R.id.fragment)).navigate(R.id.action_aanmakenActiviteit_to_tekenMap);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, "Error writing document", duration);
                        toast.show();
                    }
                });
    }

    public void bevestingAnnulatieTeken(View v){
        berekenRouteTeller();
        annuleerTekenActiviteit.bevesting(v);
    }

    public void annuleerAnnulatieTeken(View v){
        annuleerTekenActiviteit.annulleer(v);
    }

    public void stopActiviteitTeken(View v){
        tekenMap.stopActiviteit(v);
    }
    public void stopActiviteitLocatie(View v){
        stopTijd = Calendar.getInstance().getTime();

        Toast toast = Toast.makeText(getApplicationContext(), "Stop tijd: " + stopTijd, Toast.LENGTH_SHORT);
        toast.show();

        locatieMap.stopActiviteit(v);

        berekenElapsedTime(startTijd, stopTijd);
    }

    private void berekenElapsedTime(Date startTijd, Date stopTijd) {
        elapsedTijd = stopTijd.getTime() - startTijd.getTime();

        Toast toast = Toast.makeText(getApplicationContext(), "elapsed tijd: " + elapsedTijd, Toast.LENGTH_SHORT);
        toast.show();

        infoActiviteit.BerekenTussentijd(elapsedTijd);
    }

    public void bekijkProfiel(View v){
        kijkOfRouteMoetWordenOpgeslagen();

        infoActiviteit.naarProfiel(v);
    }

    private void kijkOfRouteMoetWordenOpgeslagen() {
        checkbox = (CheckBox) findViewById(R.id.check_opslaan);

        if (checkbox.isChecked()) {
            //infoActiviteit.tussenTijd = elapsedTijd;

            /*
            Toast toast = Toast.makeText(getApplicationContext(), "tussen tijd: " + infoActiviteit.tussenTijd, Toast.LENGTH_SHORT);
            toast.show();
            */
        } else {

            berekenRouteTeller();
        }
    }

    private void berekenRouteTeller() {
        db.collection("RouteBeschrijving").whereEqualTo("UID", user.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }

                    routeteller_route = list.size();
                    Toast toast = Toast.makeText(getApplicationContext(), "Route: " + routeteller_route, Toast.LENGTH_LONG);
                    toast.show();

                    Log.d(TAG, list.toString());

                    VerwijderRoute();

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());

                    Toast toast = Toast.makeText(getApplicationContext(), "Route teller wordt niet berekend: ", Toast.LENGTH_LONG);
                    toast.show();

                    routeteller_route = 0;

                }
            }
        });

    }

    private void VerwijderRoute() {
        Toast toast = Toast.makeText(getApplicationContext(), "De huidige route is: "+ routeteller_route, Toast.LENGTH_SHORT);
        toast.show();


        db.collection("RouteBeschrijving").document("Route" + routeteller_route)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");

                        Toast toast = Toast.makeText(getApplicationContext(), "DocumentSnapshot successfully deleted!", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

        db.collection("RoutePoints").document("Route" + routeteller_route)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");

                        Toast toast = Toast.makeText(getApplicationContext(), "DocumentSnapshot successfully deleted!", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

        db.collection("RouteGegevens").document("Route" + routeteller_route)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");

                        Toast toast = Toast.makeText(getApplicationContext(), "DocumentSnapshot successfully deleted!", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public void nieuweActiviteit(View v){
        kijkOfRouteMoetWordenOpgeslagen();

        infoActiviteit.nieuweActiviteit(v);
    }

    public void GewichtInstellen(View v){
        instellingen.gewichtInstellen(v);
    }

    public void annuleerGewichtInstellen(View v){
        gewicht.AnnuleerGewichtWijzigen(v);
    }

    public void GewichtAanpassen(View v){
        //gewicht.SchrijfGewichtWeg(v);
        DocumentReference docRef2 = db.collection("GebruikersInfo").document(user.getUid());
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                if (task2.isSuccessful()) {
                    DocumentSnapshot document2 = task2.getResult();
                    assert document2 != null;

                    edittext1 = (EditText) findViewById(R.id.edit_gewichtInstellen_gewicht);
                    String huidgGewicht = edittext1.getText().toString();
                    if (document2.exists()) {

                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, "het document bestaat", duration);
                        toast.show();
                        Log.d(TAG, "DocumentSnapshot data: " + document2.getData());
                        UpdateDocument(huidgGewicht);
                    } else {
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, "het lukt", duration);
                        toast.show();
                        Log.d(TAG, "No such document");
                        MaakNieuwDocumentAan(huidgGewicht);
                    }
                } else {
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, "Kan niet verbinden", duration);
                    toast.show();
                    Log.d(TAG, "get failed with ", task2.getException());
                }
            }
        });
    }

    private void MaakNieuwDocumentAan(String huidgGewicht) {
        try{
            map2.put("UID", user.getUid());
            map2.put("Gewicht", huidgGewicht);
        } catch (Exception e){
            Toast.makeText(MainActivity.this, "Exception: " + e,
                    Toast.LENGTH_SHORT).show();
        }

        db.collection("GebruikersInfo").document(user.getUid())
                .set(map2)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, "DocumentSnapshot successfully written!", duration);
                        toast.show();

                        Navigation.findNavController(findViewById(R.id.fragment)).navigate(R.id.action_gewicht_to_instellingen);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, "Error writing document", duration);
                        toast.show();
                    }
                });
    }

    private void UpdateDocument(String gewich) {
        DocumentReference updateRef = db.collection("GebruikersInfo").document(user.getUid());

        updateRef
                .update("Gewicht", gewich)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        Navigation.findNavController(findViewById(R.id.fragment)).navigate(R.id.action_gewicht_to_instellingen);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    public void RegioInstellen(View v){
        instellingen.regioInstellen(v);
    }

    public void annuleerRegioInstellen(View v){
        regio.AnnuleerRegioWijzigen(v);
    }

    public void RegioAanpassen(View v){
        DocumentReference docRef3 = db.collection("GebruikersInfo").document(user.getUid());
        docRef3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task3) {
                if (task3.isSuccessful()) {
                    DocumentSnapshot document3 = task3.getResult();
                    assert document3 != null;

                    spinner = (Spinner) findViewById(R.id.spinner_regioInstellen_gewicht);

                    String huidigeRegio = spinner.getSelectedItem().toString();

                    if (document3.exists()) {
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, "het document bestaat", duration);
                        toast.show();
                        Log.d(TAG, "DocumentSnapshot data: " + document3.getData());
                        UpdateDocumentRegio(huidigeRegio);
                    } else {
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, "het lukt", duration);
                        toast.show();
                        Log.d(TAG, "No such document");
                        MaakNieuwDocumentAanRegio(huidigeRegio);
                    }
                } else {
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, "Kan niet verbinden", duration);
                    toast.show();
                    Log.d(TAG, "get failed with ", task3.getException());
                }
            }
        });
    }

    private void MaakNieuwDocumentAanRegio(String huidigeRegio) {
        try{
            map3.put("UID", user.getUid());
            map3.put("Regio", huidigeRegio);
        } catch (Exception e){
            Toast.makeText(MainActivity.this, "Exception: " + e,
                    Toast.LENGTH_SHORT).show();
        }

        db.collection("GebruikersInfo").document(user.getUid())
                .set(map3)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, "DocumentSnapshot successfully written!", duration);
                        toast.show();

                        Navigation.findNavController(findViewById(R.id.fragment)).navigate(R.id.action_regio_to_instellingen);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, "Error writing document", duration);
                        toast.show();
                    }
                });
    }

    private void UpdateDocumentRegio(String huidigeRegio) {
        DocumentReference updateRef = db.collection("GebruikersInfo").document(user.getUid());

        updateRef
                .update("Regio", huidigeRegio)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        Navigation.findNavController(findViewById(R.id.fragment)).navigate(R.id.action_regio_to_instellingen);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }
}