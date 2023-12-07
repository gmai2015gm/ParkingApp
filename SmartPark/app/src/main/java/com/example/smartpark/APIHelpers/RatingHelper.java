package com.example.smartpark.APIHelpers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.smartpark.Models.ParkingLot;
import com.example.smartpark.Models.Rating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class RatingHelper
{
    Context context;
    RequestQueue queue;
    public static final String TAG = "APICALLS";

    public RatingHelper(Context context, RequestQueue queue)
    {
        this.context = context;
        this.queue = queue;
    }
    public interface ArrayCallbackFunction
    {
        void onSuccess(ArrayList<Rating> result);
    }

    public interface SingleCallbackFunction
    {
        void onSuccess(Rating result);
    }
    private Rating parseRating(JSONObject rawRating) throws JSONException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.ENGLISH);

        return new Rating(
                rawRating.getString("_id"),
                rawRating.getString("username"),
                rawRating.getString("parkingLot"),
                rawRating.getInt("cleanliness"),
                rawRating.getInt("safety"),
                rawRating.getInt("availability"),
                LocalDateTime.parse(rawRating.getString("timestamp"), formatter),
                rawRating.getString("notes")
        );
    }

    private ArrayList<Rating> parseRatings(JSONObject response) throws JSONException {
        ArrayList<Rating> result = new ArrayList<>();

        JSONArray ratings = response.getJSONArray("ratings");

        for (int i = 0; i < ratings.length(); i++) {
            JSONObject currRating = ratings.getJSONObject(i);
            result.add(parseRating(currRating));
        }

        return result;
    }
    public void getRatingsByLot(String lotID, ArrayCallbackFunction callback)
    {
        JsonObjectRequest r = new JsonObjectRequest(
                Request.Method.GET,
                "https://smartpark-api.onrender.com/ratings/getAll/" + lotID,
                null,
                response -> {
                    //Populate the result
                    ArrayList<Rating> result = null;

                    //Make sure it succeeded
                    int success = 0;

                    try {
                        success = response.getInt("success");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    Log.d(TAG, "" + success);

                    if (success == 1)
                    {
                        try {
                            result = parseRatings(response);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else
                    {
                        try {
                            Log.d(TAG + "Error", response.getString("message"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    //Send it to the callback
                    callback.onSuccess(result);
                }, error -> {
            Log.d(TAG, "getAllLots: "+ error);
        });
        queue.add(r);
    }

    public void getRatingsByLot(ParkingLot lot, ArrayCallbackFunction callback)
    {
        getRatingsByLot(lot.ID, callback);
    }

    public void getRatingByID(String ratingID, SingleCallbackFunction callback)
    {
        JsonObjectRequest r = new JsonObjectRequest(
                Request.Method.GET,
                "https://smartpark-api.onrender.com/ratings/get/" + ratingID,
                null,
                response -> {
                    //Populate the result
                    Rating result = null;

                    //Make sure it succeeded
                    int success = 0;

                    try {
                        success = response.getInt("success");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    if (success == 1)
                    {
                        //Parse the lot and get out
                        try {
                            JSONObject rawRating = response.getJSONObject("rating");
                            result = parseRating(rawRating);
                        } catch (JSONException e) {
                            result = null;
                        }
                    }
                    else
                    {
                        //Print off the Error message
                        try {
                            Log.d(TAG + "Error", response.getString("message"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    //Send it to the callback
                    callback.onSuccess(result);
                }, error -> {

        }
        );
        queue.add(r);
    }
}
