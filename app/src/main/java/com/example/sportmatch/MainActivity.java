package com.example.sportmatch;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ImageView backg, googleLogo;
    LinearLayout splashtext, hometext, bottons;
    Animation frombottom;

    FirebaseAuth auth;
    FirebaseDatabase database;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 20;

    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);


    //TODO: DUPA SIGN IN SAU SIGN UP DE LEGAT CU FEEDUL
//
//    TODO: Elena: prima pag cu 2 butoane log in si sign up
//    TODO: Elena: pagina de register(frontendul la ce a facut Cata)
//    TODO: Debora: terminat profile details + edit
//    TODO: Debora: log out
//    TODO: Raluca: Admin
//    TODO: Cata: Ia previewEvent si adauga chat si view member list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //sursa: https://www.youtube.com/watch?v=k_OJt71wEbc&t=260s&ab_channel=DesignWithHassan
        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);

        backg = findViewById(R.id.backG);
        splashtext = findViewById(R.id.splashtext);
        hometext = findViewById(R.id.hometext);
        bottons = findViewById(R.id.bottons);
        googleLogo = findViewById(R.id.googleLogo);

        backg.animate().translationY(-2000).setDuration(600).setStartDelay(0);
        splashtext.animate().translationY(140).alpha(0).setDuration(300).setStartDelay(0);
        hometext.startAnimation(frombottom);
        bottons.startAnimation(frombottom);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Button buttonLogin = (Button)findViewById(R.id.button_login);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        Button buttonRegister = (Button)findViewById(R.id.button_register);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        googleLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                googleSignIn();
            }
        });

//        if(auth.getCurrentUser() != null){
//            Intent intent = new Intent(MainActivity.this, BottomNavActivity.class);
//            startActivity(intent);
//            finish();
//        }



        //BAZA DE DATE INFO SPORT & LOC

        DBManagement db = new DBManagement();

        Sport volei = new Sport("Volleyball", 14, 4);
        db.addSport(volei);
        Sport fotbal = new Sport("Football", 22, 6);
        db.addSport(fotbal);
        Sport handbal = new Sport("Handball", 28, 6);
        db.addSport(handbal);
        Sport tenis = new Sport("Tennis", 4, 2);
        db.addSport(tenis);
        Sport badminton = new Sport("Badminton", 4, 2);
        db.addSport(badminton);
        Sport pingpong = new Sport("Ping-Pong", 4, 2);
        db.addSport(pingpong);
        Sport baschet = new Sport("Basketball", 24, 10);
        db.addSport(baschet);
        Sport bowling = new Sport("Bowling", 15, 2);
        db.addSport(bowling);

        //VolleyBall
        SportLocation v1 =  new SportLocation("Sport Arena", "Splaiul Independenței", 313, 6, volei, 44.444160, 26.053760);
        db.addLocation(v1);
        SportLocation v2 =  new SportLocation("Complexul Tineretului", "Aleea Primo Nebiolo", 1, 1, volei, 44.471430, 26.072580);
        db.addLocation(v2);
        SportLocation v3 =  new SportLocation("Sport Arena Academia", "Oltetului", 30, 2, volei, 44.469710, 26.113990);
        db.addLocation(v3);
        SportLocation v4 =  new SportLocation("Parcul Tineretului", "Liviu Rebreanu", 30, 4, volei, 44.423670, 26.165430);
        db.addLocation(v4);
        SportLocation v5 =  new SportLocation("Arena Națională", "Bulevardul Decebal", 2, 3, volei, 44.428960, 26.133560);
        db.addLocation(v5);

        //Football
        SportLocation f1 =  new SportLocation("Arena Sport One", "Toporași", 2, 4, fotbal, 44.391000, 26.087150);
        db.addLocation(f1);
        SportLocation f2 =  new SportLocation("Sport Arena", "Splaiul Independenței", 313, 6, fotbal, 44.444160, 26.053760);
        db.addLocation(f2);
        SportLocation f3 =  new SportLocation("Terenuri Sportive ProSport", "Doctor Niculae D. Staicovici", 31, 5, fotbal, 44.431210, 26.079040);
        db.addLocation(f3);
        SportLocation f4 =  new SportLocation("Arena Soccer Park", "Soseaua Viilor", 44, 5, fotbal, 44.410900, 26.089320);
        db.addLocation(f4);
        SportLocation f5 =  new SportLocation("Fotbal Park Pipera", "Barajului", 14, 1, fotbal, 44.394180, 26.051280);
        db.addLocation(f5);

        //Handball
        SportLocation h1 =  new SportLocation("Complex Sportiv Național Lia Manoliu", "Bulevardul Basarabia", 7, 2, handbal, 44.434840, 26.162740);
        db.addLocation(h1);
        SportLocation h2 =  new SportLocation("Sport Arena", "Splaiul Independenței", 313, 6, handbal, 44.444160, 26.053760);
        db.addLocation(h2);
        SportLocation h3 =  new SportLocation("Arena Sport One", "Toporași", 2, 4, handbal,44.45630370391797, 26.204722774868106);
        db.addLocation(h3);
        SportLocation h4 =  new SportLocation("Parcul Carol", "Călărași", 0, 3, handbal, 44.46081186133024, 26.221720928713854);
        db.addLocation(h4);
        SportLocation h5 =  new SportLocation("Clubul Sportiv Municipal Bucureșt", "Măgurele", 26, 1, handbal, 44.400171287391125, 26.00303107448408);
        db.addLocation(h5);

        //Tennis
        SportLocation t1 =  new SportLocation("Tenis Club Pro", "Mihai Eminescu", 145, 2, tenis, 44.44728375974399, 26.10985147975427);
        db.addLocation(t1);
        SportLocation t2 =  new SportLocation("Stejarii Country Club", "Jandarmeriei", 31, 1, tenis, 44.50932377152183, 26.062127649065356);
        db.addLocation(t2);
        SportLocation t3 =  new SportLocation("Tennis Arena", "Lânărie", 44, 3, tenis, 44.415540159009474, 26.101995693248856);
        db.addLocation(t3);
        SportLocation t4 =  new SportLocation("Sport Arena", "Splaiul Independenței", 313, 6, tenis, 44.444160, 26.053760);
        db.addLocation(t4);
        SportLocation t5 =  new SportLocation("Arena Tennis Park", "Dezrobirii", 37, 5, tenis, 44.4406292045098, 26.025479122083304);
        db.addLocation(t5);

        //Badminton
        SportLocation d1 =  new SportLocation("Sport Arena", "Splaiul Independenței", 313, 6, badminton, 44.444160, 26.053760);
        db.addLocation(d1);
        SportLocation d2 =  new SportLocation("AFI Cotroceni", "Bd. Vasile Milea", 4, 6, badminton, 44.43469531067676, 26.054491593248624);
        db.addLocation(d2);
        SportLocation d3 =  new SportLocation("Club Arena", "Calea Floreasca", 16, 1, badminton, 44.46666936438844, 26.10212517790199);
        db.addLocation(d3);
        SportLocation d4 =  new SportLocation("Oxygen Wellness", "Calea 13 Septembrie", 221, 5, badminton, 44.423588485185476, 26.073407806740075);
        db.addLocation(d4);
        SportLocation d5 =  new SportLocation("Elvira Popescu Badminton Club", "Olteniței", 23, 4, badminton, 44.399100611621314, 26.11037325023596);
        db.addLocation(d5);

        //Ping-Pong
        SportLocation p1 =  new SportLocation("King Pong", "Episcopiei", 2, 3, pingpong, 44.44165967106486, 26.096741706486856);
        db.addLocation(p1);
        SportLocation p2 =  new SportLocation("Sport Arena", "Splaiul Independenței", 313, 6, pingpong, 44.444160, 26.053760);
        db.addLocation(p2);
        SportLocation p3 =  new SportLocation("Viva Club", "Sirenelor", 101, 5, pingpong, 44.423583872368525, 26.079681706740654);
        db.addLocation(p3);
        SportLocation p4 =  new SportLocation("Play Ping Pong", "Olari", 12, 2, pingpong, 44.43919978941502, 26.116953506736607);
        db.addLocation(p4);
        SportLocation p5 =  new SportLocation("Ping-Pong Club", "Doamnei", 9, 3, pingpong, 44.433319177612375, 26.100573291392646);
        db.addLocation(p5);

        //Basketball
        SportLocation b1 =  new SportLocation("Complex Sportiv Regal", "Zidurilor", 5, 5, baschet, 44.44575736023224, 26.134334611438167);
        db.addLocation(b1);
        SportLocation b2 =  new SportLocation("Arena Mall", "Bulevardul Vitan-Barzesti", 7, 4, baschet, 44.39462657536604, 26.142930295118695);
        db.addLocation(b2);
        SportLocation b3 =  new SportLocation("World Class Health Academy", "Calea Floreasca", 169, 1, baschet, 44.46674593153344, 26.10216809324848);
        db.addLocation(b3);
        SportLocation b4 =  new SportLocation("Academia de Baschet Hagi", "Foisorului ", 160, 3, baschet, 44.41657586486855, 26.122557098730994);
        db.addLocation(b4);
        SportLocation b5 =  new SportLocation("Sala Sporturilor Dinamo", "Şoseaua Ştefan cel Mare", 7, 2, baschet, 44.45298981911792, 26.10395059604185);
        db.addLocation(b5);

        //Bowling
        SportLocation w1 =  new SportLocation("Bucuresti Mall", "Calea Vitan", 55, 3, bowling, 44.41835315790387, 26.128415521446247);
        db.addLocation(w1);
        SportLocation w2 =  new SportLocation("Sun Plaza", "Calea Văcărești", 391, 4, bowling, 44.41400349093896, 26.11392603372387);
        db.addLocation(w2);
        SportLocation w3 =  new SportLocation("Mega Mall", "Bulevardul Pierre de Coubertin", 3, 2, bowling, 44.44146418139411, 26.150327764408292);
        db.addLocation(w3);
        SportLocation w4 =  new SportLocation("Plaza Romania", "Bd. Timișoara", 26, 6, bowling, 44.42627964052026, 26.018867037424865);
        db.addLocation(w4);
        SportLocation w5 =  new SportLocation("Liberty Center", "Progresului", 151, 5, bowling, 44.41952593496595, 26.073980506503336);
        db.addLocation(w5);


    }

    private void googleSignIn() {

        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_SIGN_IN){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken()); 

            }
            catch (Exception e){

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void firebaseAuth(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            String username = user.getEmail();
                            String fullName = user.getDisplayName();
                            String birthDate = String.format("%02d/%02d/%d", day, month, year);

                            FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username").equalTo(username).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {

                                        Intent intent = new Intent(MainActivity.this, BottomNavActivity.class);
                                        startActivity(intent);

                                    } else {
                                        User newUser = new User(username, "", birthDate, fullName);


                                        database.getReference().child("Users").child(user.getUid()).setValue(newUser);

                                        Intent intent = new Intent(MainActivity.this, BottomNavActivity.class);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    throw databaseError.toException();
                                }
                            });

                        }
                        else{

                            Toast.makeText(MainActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}