package com.example.smartpark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.smartpark.APIHelpers.ParkingLotHelper;
import com.example.smartpark.Models.ParkingLot;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ParkingLotInfo extends AppCompatActivity implements OnMapReadyCallback{
    private static final int NOTIFICATION_ID = 2;
    // UI elements
    ListView lstParkingLots;
    GoogleMap map;
    Switch toggleView;

    ArrayList<ParkingLot>parkingLots;
    ParkingLotAdapter adapter;
    Spinner spSortOption;
    Spinner spSortOrder;

    // User's current longitude and latitude
    double lng, lat;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    String[] sortOptions = {"Sort By", "Cleanliness", "Safety", "Availability", "Distance"};
    String[] sortOrderOptions = {"Order", "Low to High", "High to Low"};
    String []sortOrderDistance = {"Order", "Furthest to Closest", "Closest to Furthest"};

    private static final String CHANNEL_ID = "SmartPark Notifications";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_parking_lot_info);

        // Initialize UI components and array adapters
        lstParkingLots = findViewById(R.id.lstParkingLots);
        toggleView = findViewById(R.id.toggleView);
        parkingLots = new ArrayList<>();
        adapter = new ParkingLotAdapter(this, parkingLots);

        spSortOption = findViewById(R.id.spSortOption);
        spSortOrder = findViewById(R.id.spSortOrder);

        // Set adapter to list view
        lstParkingLots.setAdapter(adapter);


        // Setup spinners with array adapters for sorting options
        ArrayAdapter<String> sortOptionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sortOptions);
        sortOptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSortOption.setAdapter(sortOptionAdapter);

        ArrayAdapter<String> sortOrderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sortOrderOptions);
        ArrayAdapter<String> sortOrderAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sortOrderDistance);
        sortOrderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortOrderAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spSortOrder.setAdapter(sortOrderAdapter);

        // Request location permissions if necessary
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }



        // Get the SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapReport);
        mapFragment.getMapAsync(this);

        // Set the initial state of the map and ListView
        mapFragment.getView().setVisibility(View.GONE);
        toggleView.setText("List View"); // Set initial text for the switch
        lstParkingLots.setVisibility(View.VISIBLE);

        // Initially, hide the sort order spinner and distance seekbar
        spSortOrder.setVisibility(View.GONE);


        // Set up the toggle switch listener
        toggleView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // When the switch is on, show the map and hide the list
                mapFragment.getView().setVisibility(View.VISIBLE);
                lstParkingLots.setVisibility(View.GONE);
                toggleView.setText("Map View");
            } else {
                // When the switch is off, show the list and hide the map
                mapFragment.getView().setVisibility(View.GONE);
                lstParkingLots.setVisibility(View.VISIBLE);
                toggleView.setText("List View");
            }
        });



        //Set up listener for search button to fetch parking lots
//        btnSearch.setOnClickListener(l->{
//            getUserLocationAndFetchParkingLots();
//        });

        getUserLocationAndFetchParkingLots();
        // Sorting options listener
        spSortOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = sortOptions[position];
//                sortParkingLots();
                switch (selectedOption) {
                    case "Cleanliness":
                    case "Safety":
                    case "Availability":
                        // Show sorting order spinner for these options
                        spSortOrder.setVisibility(View.VISIBLE);
                        spSortOrder.setAdapter(sortOrderAdapter);
                        break;
                    case "Distance":
                        // Show distance sorting order spinner for this option
                        spSortOrder.setVisibility(View.VISIBLE);
                        spSortOrder.setAdapter(sortOrderAdapter2);
                        break;
                    default:
                        // Hide sorting order spinner for other options
                        spSortOrder.setVisibility(View.GONE);
                        break;
                }

                //update list and map view when sort options are changed
                getUserLocationAndFetchParkingLots();
//                sortParkingLots();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spSortOrder.setVisibility(View.GONE);
            }
        });
        // Sorting options listener for spSortOrder
        spSortOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Sort parking lots when order changes
                getUserLocationAndFetchParkingLots();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optionally handle this case if needed
            }
        });



    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
            // Check if the permission is granted.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                Log.d("maps", "getLocation: permissions NOW granted");

            } else { // Permission was denied...
                Log.d("maps", "getLocation: permissions NOT granted");
            }
            break;
        }
    }



    private void getUserLocationAndFetchParkingLots() {
        // Get the FusedLocationProviderClient for accessing location
//        ArrayList<ParkingLot>lots = new ArrayList<>();
        FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not already granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        // Get the last known location of the user
        locationProviderClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    Log.d("Testing", "" + location);
                    if (location != null) {
                        notifyUserOfLocation();

                        double userLat = location.getLatitude();
                        double userLng = location.getLongitude();

                        lng =userLng;
                        lat = userLat;
                        if (map != null) {
                            LatLng userLatLng = new LatLng(lat, lng);
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 5));
                            // Add a marker to show user's location
                            Marker userLocationMarker= map.addMarker(new MarkerOptions()
                                    .position(userLatLng)
                                    .title("My Location")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))); // Customizing the marker color (optional)


                            // Set a unique tag for the user's location marker
                            userLocationMarker.setTag("user_location");
//                            map.addMarker(new MarkerOptions().position(userLatLng).title("Current Location"));
                        }

                        // Create an instance of ParkingLotHelper
                        RequestQueue queue = Volley.newRequestQueue(this);
                        ParkingLotHelper parkingLotHelper = new ParkingLotHelper(this, queue);

                        // Call getAllLots
                        parkingLotHelper.getAllLots(new ParkingLotHelper.ArrayCallbackFunction() {
                            @Override
                            public void onSuccess(ArrayList<ParkingLot> result) {
                                // Update ListView adapter and Google Map markers
                                Log.d("Testing", result.toString());
                                updateMapView(result);
                                updateListView(result);

//                                parkingLots.addAll(result);
//                                lots = result;
//                                parkingLots = result;




                            }
                        });

                    } else {
                        // Handle the case where location is null

                        // Create an instance of ParkingLotHelper
                        RequestQueue queue = Volley.newRequestQueue(this);
                        ParkingLotHelper parkingLotHelper = new ParkingLotHelper(this, queue);

                        // Call getAllLots
                        parkingLotHelper.getAllLots(new ParkingLotHelper.ArrayCallbackFunction() {
                            @Override
                            public void onSuccess(ArrayList<ParkingLot> result) {
                                // Update ListView adapter and Google Map markers
                                Log.d("Testing", result.toString());
                                updateMapView(result);
                                updateListView(result);

//                                parkingLots.addAll(result);
//                                parkingLots = result;


                            }
                        });
                    }
                })
                .addOnFailureListener(this, e -> {
                    // Handle the failure in getting location
                });


    }

    public void notifyUserOfLocation(){
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "MyApp Main Notification Channel",NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        //Make the permission
        Notification notification = new NotificationCompat.Builder(ParkingLotInfo.this,CHANNEL_ID)
                .setSmallIcon(android.R.drawable.btn_star_big_on)
                .setContentTitle("SmartPark")
                .setContentText("SmartPark is using your current location.")
                .build();
        notificationManager.notify(NOTIFICATION_ID,notification);
    }


    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setInfoWindowAdapter(new CustomInfoWindowAdapter(getApplicationContext()));
        for (ParkingLot lot:parkingLots) {
            float lat = lot.latitude;
            float longitude = lot.longitude;
            LatLng current = new LatLng(lat,longitude);
            googleMap.addMarker(new MarkerOptions().position(current).title(lot.name));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(current));
        }

    }


    //Method to update map view with new markers
    private void updateMapView(ArrayList<ParkingLot> parkingLots) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (map != null) {
//                    map.clear(); // Clear existing markers

                    for (ParkingLot lot : parkingLots) {
                        LatLng position = new LatLng(lot.latitude, lot.longitude);
                        Marker marker = map.addMarker(new MarkerOptions().position(position).title(lot.name));
                        marker.setTag(lot);  // Set the ParkingLot object as a tag
                    }

                }
            }
        });
    }

    // Method to update the list view with new data
    public void updateListView(ArrayList<ParkingLot> newParkingLots) {
        parkingLots.clear();
        parkingLots.addAll(newParkingLots);
        sortParkingLots();
        adapter.notifyDataSetChanged();
    }

    // Method to calculate distance between two points
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final double EARTH_RADIUS = 3959; // Radius of the Earth in miles
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double lon1Rad = Math.toRadians(lon1);
        double lon2Rad = Math.toRadians(lon2);

        // Calculate differences in coordinates
        double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
        double y = lat2Rad - lat1Rad;

        // Calculate distance
        double distance = Math.sqrt(x * x + y * y) * EARTH_RADIUS;

        return distance;
    }

    private void sortParkingLots() {
        String selectedOption = spSortOption.getSelectedItem().toString();
        String selectedOrder = spSortOrder.getSelectedItem().toString();
        Comparator<ParkingLot> comparator = null;

        switch (selectedOption) {
            case "Cleanliness":
                // Creating a comparator to sort ParkingLot objects by average cleanliness
                comparator = Comparator.comparingDouble(ParkingLot::getAvgCleanliness);
                break;
            case "Safety":
                // Creating a comparator to sort ParkingLot objects by average safety
                comparator = Comparator.comparingDouble(ParkingLot::getAvgSafety);
                break;
            case "Availability":
                // Creating a comparator to sort ParkingLot objects by average availability
                comparator = Comparator.comparingDouble(ParkingLot::getAvgAvailability);
                break;
            case "Distance":
                // Creating a comparator to sort ParkingLot objects by distance
                comparator = Comparator.comparingDouble(lot -> calculateDistance(lat, lng, lot.latitude, lot.longitude));
                break;
        }

        if (comparator != null) {
            if ("High to Low".equals(selectedOrder) ||"Furthest to Closest".equals(selectedOrder)) {
                // Reversing the comparator for a high to low sort order
                comparator = comparator.reversed();
            }
            // Sorting the parkingLots list based on the comparator
            Collections.sort(parkingLots, comparator);
            // Notifying the adapter that the data set has changed so it updates the ListView
            adapter.notifyDataSetChanged();
        }
    }

    static class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View mWindow;
        private Context mContext;

        // Constructor
        public CustomInfoWindowAdapter(Context context) {
            mContext = context;
            // Inflate the custom layout for the info window
            mWindow = LayoutInflater.from(context).inflate(R.layout.info_window_layout, null);
        }

        // Populates the info window view with the text of the marker
        private void renderWindowText(Marker marker, View view) {
            // Retrieve the ParkingLot object from the marker
            Object tag = marker.getTag();
            if(tag instanceof ParkingLot){
                ParkingLot p = (ParkingLot) marker.getTag();

                // Find and set text for each view in the info window layout
                TextView tvLotName = view.findViewById(R.id.tvMapLotName);
                TextView tvAvailability = view.findViewById(R.id.tvMapAvailability);
                TextView tvSafety = view.findViewById(R.id.tvMapSafety);
                TextView tvCleanliness = view.findViewById(R.id.tvMapCleanliness);

                tvLotName.setText("Lot Name: " + p.name);
                tvAvailability.setText("Availability: " + p.getAvgAvailability());
                tvSafety.setText("Safety: " + p.getAvgSafety());
                tvCleanliness.setText("Cleanliness: " + p.getAvgCleanliness());
            }

        }

        // Returns the entire view for the info window
        @Override
        public View getInfoWindow(Marker marker) {
            // Check if the marker is the user's location
            if ("user_location".equals(marker.getTag())) {
                // Return null to use the default info window for the user's location
                return null;
            }
            renderWindowText(marker, mWindow);
            return mWindow;
        }

        // Returns the contents inside the default info window frame
        @Override
        public View getInfoContents(Marker marker) {
            // Check if the marker is the user's location
            if ("user_location".equals(marker.getTag())) {
                // Return null to use the default info window for the user's location
                return null;
            }
            renderWindowText(marker, mWindow);
            return mWindow;
        }
    }




}


