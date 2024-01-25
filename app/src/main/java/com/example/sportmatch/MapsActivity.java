package com.example.sportmatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    SportLocation locatieglobala;
    int clickedStarPositionglobala;

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
                            locatieglobala = sportLocation;
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
                else if(Objects.equals(activity, "EventPreview") || Objects.equals(activity, "EventDetailsFeed") || Objects.equals(activity, "EventDetailsAdmin") || Objects.equals(activity, "EventDetailsParticipant")){
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

        View customView = getLayoutInflater().inflate(R.layout.mappopup, null);
        builder.setView(customView);

        builder.setTitle(location.getLocationName());

        TextView titleTextView = customView.findViewById(R.id.popupTitle);
        TextView messageTextView = customView.findViewById(R.id.popupMessage);

        ImageView star1 = customView.findViewById(R.id.star1);
        ImageView star2 = customView.findViewById(R.id.star2);
        ImageView star3 = customView.findViewById(R.id.star3);
        ImageView star4 = customView.findViewById(R.id.star4);
        ImageView star5 = customView.findViewById(R.id.star5);

        setStarClickListener(star1);
        setStarClickListener(star2);
        setStarClickListener(star3);
        setStarClickListener(star4);
        setStarClickListener(star5);

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

        else if(Objects.equals(activity, "EventPreview") || Objects.equals(activity, "EventDetailsFeed") || Objects.equals(activity, "EventDetailsAdmin") || Objects.equals(activity, "EventDetailsParticipant")){
            StringBuilder descriptionBuilder = new StringBuilder();
            descriptionBuilder.append(location.getStreetName())
                    .append(" ").append(location.getNumber())
                    .append(", Sector").append(location.getSector());

            builder.setMessage(descriptionBuilder.toString());

            builder.setPositiveButton("OK", (dialog, which) -> {
                locatieglobala.addReview(clickedStarPositionglobala);

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
                else if(Objects.equals(activity, "EventDetailsAdmin")){
                    Intent resultIntent = new Intent(MapsActivity.this, EventDetailsAdminActivity.class);
                    setResult(RESULT_OK, resultIntent);
                    dialog.dismiss();
                    finish();
                }
                else if(Objects.equals(activity, "EventDetailsParticipant")){
                    Intent resultIntent = new Intent(MapsActivity.this, EventdetailsParticipantActivity.class);
                    setResult(RESULT_OK, resultIntent);
                    dialog.dismiss();
                    finish();
                }

            });
        }

        builder.show();
    }

    /*public void setStarClickListener(ImageView star) {
        star.setOnClickListener(v -> {
            // Handle star click
            // You can access the clicked star and perform necessary actions
            // Update the star rating appearance here

            // Example: Toggle between empty and full star images
            if (isStarEmpty(star)) {
                star.setImageResource(R.drawable.starf);
            } else {
                star.setImageResource(R.drawable.stare);
            }

            // For more advanced scenarios, you might want to keep track of the star's state
            // using a variable or data structure.
        });
    }

    // Helper method to check if the star is currently empty
    private boolean isStarEmpty(ImageView star) {
        // Assuming that the empty star image resource is R.drawable.stare
        return star.getTag() == null || (int) star.getTag() == R.drawable.stare;
    }*/
    public void setStarClickListener(ImageView star) {
        star.setOnClickListener(v -> {
            toggleStarState(star);

            // Get the clicked star position based on variable name
            int clickedStarPosition = getStarPosition(star);
            clickedStarPositionglobala=clickedStarPosition;



            // Iterate over stars before the clicked star and set them to full
            setStarsBeforeClicked(star, clickedStarPosition);
        });
    }

    // Helper method to toggle between empty and full star images
    private void toggleStarState(ImageView star) {
        if (isStarEmpty(star)) {
            setFullStar(star);
        } else {
            setEmptyStar(star);
        }
    }

    // Helper method to set a star to full
    private void setFullStar(ImageView star) {
        star.setImageResource(R.drawable.starf);
        // Set the tag to indicate a full star
        star.setTag(R.drawable.starf);
    }

    // Helper method to set a star to empty
    private void setEmptyStar(ImageView star) {
        star.setImageResource(R.drawable.stare);
        // Set the tag to indicate an empty star
        star.setTag(R.drawable.stare);
    }

    // Helper method to check if the star is currently empty
    private boolean isStarEmpty(ImageView star) {
        // Assuming that the empty star image resource is R.drawable.stare
        return star.getTag() == null || (int) star.getTag() == R.drawable.stare;
    }

    // Helper method to get the position of a star based on alphabetical order
    private int getStarPosition(ImageView star) {
        // Get the variable name of the star
        String starVariableName = getResources().getResourceEntryName(star.getId());

        // Extract the numeric part from the variable name
        String numericPart = starVariableName.replaceAll("[^0-9]", "");

        // Convert the numeric part to an integer
        return Integer.parseInt(numericPart);
    }

    // Helper method to set stars before the clicked star to full
    private void setStarsBeforeClicked(ImageView clickedStar, int clickedStarPosition) {
        // Find the parent layout containing all stars
        ViewGroup parentLayout = (ViewGroup) clickedStar.getParent();

        // Iterate over stars and set them to full until the clicked star is reached
        for (int i = 0; i < parentLayout.getChildCount(); i++) {
            View child = parentLayout.getChildAt(i);

            if (child instanceof ImageView) {
                ImageView star = (ImageView) child;

                // Check if the star is before the clicked star
                if (getStarPosition(star) <= clickedStarPosition) {
                    setFullStar(star);
                }
                else {
                    setEmptyStar(star);
                }
            }
        }

    }


}
