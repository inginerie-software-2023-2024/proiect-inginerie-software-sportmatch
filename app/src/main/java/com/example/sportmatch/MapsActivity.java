package com.example.sportmatch;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{
    GoogleMap mMap;
    public static final int REQUEST_CODE_MAPS_ACTIVITY = 1001;
    String activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference locRef = database.getReference("SportLocations");
        List<SportLocation> locations = new ArrayList<>();

        String selectedSport = getIntent().getStringExtra("selectedSport");
        String selectedLoc = getIntent().getStringExtra("selectedLoc");
        activity = getIntent().getStringExtra("Activity");

        locRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SportLocation spLoc = null;
                for (DataSnapshot locSnapshot : snapshot.getChildren()) {
                    SportLocation sportLocation = locSnapshot.getValue(SportLocation.class);
                    if(Objects.equals(sportLocation.getSport().getSportName(), selectedSport)){
                        locations.add(sportLocation);
                        if(Objects.equals(sportLocation.getLocationName(), selectedLoc)){
                            spLoc = sportLocation;
                        }
                    }
                }

                if(Objects.equals(activity, "CreateEvent") || Objects.equals(activity, "EditEventDetails")){
                    //sursa: chat gpt
                    //adaug marcatori pe harta
                    for(SportLocation loc: locations){
                        LatLng address = new LatLng(loc.getLatitude(), loc.getLongitude());
                        Marker marker = mMap.addMarker(new MarkerOptions().position(address).title(loc.getLocationName()));
                        marker.setTag(loc);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(address));
                    }

                    // seteaza camera la locatia selectata
                    if(spLoc != null) {
                        LatLng selectedLoc = new LatLng(spLoc.getLatitude(), spLoc.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLoc, 15));
                    }
                }
                else if(Objects.equals(activity, "EventPreview") || Objects.equals(activity, "EventDetailsFeed")){
                    LatLng selectedLoc = new LatLng(spLoc.getLatitude(), spLoc.getLongitude());
                    Marker marker = mMap.addMarker(new MarkerOptions().position(selectedLoc).title(spLoc.getLocationName()));
                    marker.setTag(spLoc);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLoc, 15));
                }

                mMap.setOnMarkerClickListener(marker -> {
                    SportLocation location = (SportLocation) marker.getTag();
                    if (location != null) {
                        showLocationInfoPopup(location);
                    }
                    return true;
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showLocationInfoPopup(SportLocation location) {//sursa: chatgpt
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(location.getLocationName());

        if(Objects.equals(activity, "CreateEvent") || Objects.equals(activity, "EditEventDetails")){
            StringBuilder descriptionBuilder = new StringBuilder();
            descriptionBuilder.append(location.getStreetName())
                    .append(" ").append(location.getNumber())
                    .append(", Sector").append(location.getSector());

            builder.setMessage(descriptionBuilder.toString());

            builder.setPositiveButton("Select Location", (dialog, which) -> {
                if(Objects.equals(activity, "CreateEvent")){
                    Intent resultIntent = new Intent(MapsActivity.this, CreateEventActivity.class);
                    resultIntent.putExtra("selectedLocation", location.getLocationName());
                    setResult(RESULT_OK, resultIntent);
                    dialog.dismiss();
                    finish();
                }
                else if(Objects.equals(activity, "EditEventDetails")){
                    Intent resultIntent = new Intent(MapsActivity.this, EditEventDetails.class);
                    resultIntent.putExtra("selectedLocation", location.getLocationName());
                    setResult(RESULT_OK, resultIntent);
                    dialog.dismiss();
                    finish();
                }

            });
        }

        else if(Objects.equals(activity, "EventPreview") || Objects.equals(activity, "EventDetailsFeed")){
            StringBuilder descriptionBuilder = new StringBuilder();
            descriptionBuilder.append(location.getStreetName())
                    .append(" ").append(location.getNumber())
                    .append(", Sector").append(location.getSector());

            builder.setMessage(descriptionBuilder.toString());

            builder.setPositiveButton("OK", (dialog, which) -> {
                if(Objects.equals(activity, "EventPreview")){
                    Intent resultIntent = new Intent(MapsActivity.this, EventPreview.class);
                    setResult(RESULT_OK, resultIntent);
                    dialog.dismiss();
                    finish();
                }
                else if(Objects.equals(activity, "EventDetailsFeed")){
                    Intent resultIntent = new Intent(MapsActivity.this, EventDetailsActivity.class);
                    setResult(RESULT_OK, resultIntent);
                    dialog.dismiss();
                    finish();
                }

            });
        }




        builder.show();
    }
}
