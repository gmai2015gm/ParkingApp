package com.example.smartpark.APIHelpers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.smartpark.Models.ParkingLot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParkingLotHelper
{
    Context context;
    RequestQueue queue;
    public static final String TAG = "APICALLS";

    public ParkingLotHelper(Context context, RequestQueue queue)
    {
        this.context = context;
        this.queue = queue;
    }

    public interface ArrayCallbackFunction
    {
        void onSuccess(ArrayList<ParkingLot> result);
    }

    public interface SingleCallbackFunction
    {
        void onSuccess(ParkingLot result);
    }

    private ParkingLot parseLot(JSONObject json) throws JSONException {
        //Make the new one based on whats in the JSON
        ParkingLot newLot = new ParkingLot(
                json.getString("id"),
                json.getString("entryName"),
                (float) json.getDouble("latitude"),
                (float) json.getDouble("longitude"),
                json.getDouble("avgCleanliness"),
                json.getDouble("avgSafety"),
                json.getDouble("avgAvailability"),
                null
        );

        return newLot;
    }
    private ArrayList<ParkingLot> parseLots(JSONObject json) throws JSONException
    {
        //
        ArrayList<ParkingLot> result = new ArrayList<>();

        //Grab our array of lots
        JSONArray lots = json.getJSONArray("lots");

        for (int i = 0; i < lots.length(); i++) {
            ParkingLot currLot = parseLot(lots.getJSONObject(i));
            result.add(currLot);
        }

        Log.d(TAG + "Lots", "" + result);

        return result;
    }
    public void getAllLots(ArrayCallbackFunction callback)
    {
        Log.d(TAG, "Making Request");

        JsonObjectRequest r = new JsonObjectRequest(
            Request.Method.GET,
        "https://smartpark-api.onrender.com/lots",
  null,
            response -> {
                //Populate the result
                ArrayList<ParkingLot> result = null;

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
                        result = parseLots(response);
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

    public void getLotByID(String id, SingleCallbackFunction callback)
    {
        Log.d(TAG, "Making Request");

        JsonObjectRequest r = new JsonObjectRequest(
            Request.Method.GET,
            "https://smartpark-api.onrender.com/lots/" + id,
            null,
            response -> {
                //Populate the result
                ParkingLot result = null;

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
                        JSONObject rawLot = response.getJSONObject("lot");
                        result = parseLot(rawLot);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
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
