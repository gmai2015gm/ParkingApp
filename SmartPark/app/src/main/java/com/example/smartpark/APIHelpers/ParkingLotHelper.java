package com.example.smartpark.APIHelpers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartpark.Models.ParkingLot;
import com.example.smartpark.ParkingLotAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParkingLotHelper
{
    public interface ParkingLotFetchListener {
        void onParkingLotsFetched(ArrayList<ParkingLot> parkingLots);
        void onError(VolleyError error);
    }

    public void fetchParkingLots(double userLat, double userLng, int radius, final GoogleMap map, final ParkingLotAdapter adapter,final ParkingLotFetchListener listener, Context context) {
        String url = buildPlacesApiUrl(userLat, userLng, radius);
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ArrayList<ParkingLot> parkingLots = parseParkingLots(response);
//                adapter.updateData(parkingLots);
//                updateMapView(parkingLots, map);
                listener.onParkingLotsFetched(parkingLots);
                Log.d("lots",parkingLots.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error+"");
            }

        });

        queue.add(request);
    }

    private String buildPlacesApiUrl(double lat, double lng, int radius) {
        return "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + lat + "," + lng + "&radius=" + radius + "&type=parking"+"&key=AIzaSyD7V0R2Wg0ZMU2fkxSYJmtdg5VtBKfS1Dk";
    }

    private ArrayList<ParkingLot> parseParkingLots(JSONObject jsonObject) {
        ArrayList<ParkingLot> parkingLots = new ArrayList<>();
        try {
            JSONArray results = jsonObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                ParkingLot lot = new ParkingLot();
                lot.setID(result.getString("place_id"));
                lot.name = result.getString("name");
                JSONObject location = result.getJSONObject("geometry").getJSONObject("location");
                lot.latitude = (float) location.getDouble("lat");
                lot.longitude = (float) location.getDouble("lng");

                if (result.has("rating")) {
                    lot.rating = result.getDouble("rating");
                } else {
                    lot.rating = 0; // Default value or handle as needed
                }
                // Set default values or calculate actual values for avgCleanliness, avgSafety, avgAvailability, etc.
                parkingLots.add(lot);
            }
        } catch (Exception e) {
            // Log error or handle it appropriately
        }
        return parkingLots;
    }

    private void updateMapView(ArrayList<ParkingLot> parkingLots, GoogleMap map) {

        for (ParkingLot lot : parkingLots) {
            LatLng position = new LatLng(lot.latitude, lot.longitude);
            map.addMarker(new MarkerOptions().position(position).title(lot.name));
        }
    }

}
