package com.example.exploro;

import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.exploro.controllers.MyLocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.maps.model.LatLng;

import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.net.URL;

public class MapsAPICaller {

    public enum PlaceTypes {
        ACCOUNTING("accounting"),
        AIRPORT("airport"),
        AMUSEMENT_PARK("amusement_park"),
        AQUARIUM("aquarium"),
        ART_GALLERY("art_gallery"),
        ATM("atm"),
        BAKERY("bakery"),
        BANK("bank"),
        BAR("bar"),
        BEAUTY_SALON("beauty_salon"),
        BICYCLE_STORE("bicycle_store"),
        BOOK_STORE("book_store"),
        BOWLING_ALLEY("bowling_alley"),
        BUS_STATION("bus_station"),
        CAFE("cafe"),
        CAMPGROUND("campground"),
        CAR_DEALER("car_dealer"),
        CAR_RENTAL("car_rental"),
        CAR_REPAIR("car_repair"),
        CAR_WASH("car_wash"),
        CASINO("casino"),
        CEMETERY("cemetery"),
        CHURCH("church"),
        CITY_HALL("city_hall"),
        CLOTHING_STORE("clothing_store"),
        CONVENIENCE_STORE("convenience_store"),
        COURTHOUSE("courthouse"),
        DENTIST("dentist"),
        DEPARTMENT_STORE("department_store"),
        DOCTOR("doctor"),
        DRUGSTORE("drugstore"),
        ELECTRICIAN("electrician"),
        ELECTRONICS_STORE("electronics_store"),
        EMBASSY("embasyy"),
        FIRE_STATION("fire_station"),
        FLORIST("florist"),
        FUNERAL_HOME("funeral_home"),
        FURNITURE_STORE("furniture_store"),
        GAS_STATION("gas_station"),
        GYM("gym"),
        HAIR_CARE("hair_care"),
        HARDWARE_STORE("hardware_store"),
        HINDU_TEMPLE("hindu_temple"),
        HOME_GOODS_STORE("home_goods_store"),
        HOSPITAL("hospital"),
        INSURANCE_AGENCY("insurance_agency"),
        JEWELRY_STORE("jewelry_store"),
        LAUNDRY("laundry"),
        LAWYER("lawyer"),
        LIGHT_RAIL_STATION("light_rail_station"),
        LIQUOR_STORE("liquor_store"),
        LOCAL_GOVERNMENT_OFFICE("local_government_office"),
        LOCKSMITH("locksmith"),
        LODGING("lodging"),
        MEAL_DELIVERY("meal_delivery"),
        MEAL_TAKEAWAY("meal_takeaway"),
        MOSQUE("mosque"),
        MOVIE_RENTAL("movie_rental"),
        MOVIE_THEATER("movie_theater"),
        MOVING_COMPANY("moving_company"),
        MUSEUM("museum"),
        NIGHT_CLUB("night_club"),
        PAINTER("painter"),
        PARK("park"),
        PARKING("parking"),
        PET_STORE("pet_store"),
        PHARMACY("pharmacy"),
        PHYSIOTHERAPIST("physiotherapist"),
        PLUMBER("plumber"),
        POLICE("police"),
        POST_OFFICE("post_office"),
        PRIMARY_SCHOOL("primary_school"),
        REAL_ESTATE_AGENCY("real_estate_agency"),
        RESTAURANT("restaurant"),
        ROOFING_CONTRACTOR("roofing_contractor"),
        RV_PARK("rv_park"),
        SCHOOL("school"),
        SECONDARY_SCHOOL("secondary_school"),
        SHOE_STORE("shoe_store"),
        SHOPPING_MALL("shopping_mall"),
        SPA("spa"),
        STADIUM("stadium"),
        STORAGE("storage"),
        STORE("store"),
        SUBWAY_STATION("subway_station"),
        SUPERMARKET("supermarket"),
        SYNAGOGUE("synagogue"),
        TAXI_STAND("taxi_stand"),
        TOURIST_ATTRACTION("tourist_attraction"),
        TRAIN_STATION("train_station"),
        TRANSIT_STATION("transit_station"),
        TRAVEL_AGENCY("travel_agency"),
        UNIVERSITY("university"),
        VETERINARY_CARE("veterinary_care"),
        ZOO("zoo");

        private final String text;

        PlaceTypes(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }

    }

    //region getPlacesFromQuery Method

    /** Use Text Search google maps api to get all places nearby in a radius of a location
     *  by specified type that also matches search query sorted by rating.
     *
     * @param query - Search query to pull results from
     * @param currentlyOpen - If place has to be currently open or not
     * @param radius - How far away from location the places can be retrieved from in meters
     * @param type - The type of places to retrieve
     * @param limit - The maximum limit of places to retrieve from query
     * @return - Found places matching parameters
     */
    public JSONArray getPlacesFromQuery(String query, boolean currentlyOpen, int radius, PlaceTypes type, int limit) {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
        url += "&location=" + MyLocationListener.currentLocation.latitude + "," + MyLocationListener.currentLocation.longitude;
        //url += "&opennow=" + currentlyOpen;
        url += "&radius=" + radius;
        url += "&type=" + type.toString();
        url += "&key=" + BuildConfig.MAPS_API_KEY;

        OkHttpClient requestClient = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder().url(url).get().build();
        Response response = null;
        try {
            response = requestClient.newCall(request).execute();
            JSONObject responseBody = new JSONObject(response.body().string());
            String status = responseBody.getString("status");

            if (!status.equals("OK"))
                return null;

            JSONArray results = responseBody.getJSONArray("results");
            JSONArray returnArray = new JSONArray();

            if (results.length() <= 0)
                return null;

            // Sort Results based on rating
            List<JSONObject> resultList = new ArrayList<JSONObject>();
            for (int i = 0; i < results.length(); i++)
                resultList.add(results.getJSONObject(i));

            Collections.sort(resultList, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String valA = new String();
                    String valB = new String();
                    try {
                        if (!a.has("rating")) return -1;
                        if (!b.has("rating")) return 1;
                        valA = valA + (a.getDouble("rating") * a.getInt("user_ratings_total"));
                        valB = valB + (b.getDouble("rating") * b.getInt("user_ratings_total"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    float fa = new Float(valA);
                    float fb = new Float(valB);
                    if (fa == fb) return 0;
                    else if (fa > fb) return 1;
                    return -1;
                }
            });

            Collections.reverse(resultList);
            limit = (limit < resultList.size()) ? limit : resultList.size();
            for (int i = 0; i < limit; i++)
                returnArray.put(resultList.get(i));

            return returnArray;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //endregion

    //region getImageFromPhotoReference Method

    /**
     * Retrieve url for a place image in String format from its photo reference key
     *
     * @param photoRef - Photo reference key obtained from TextSearch API call
     * @return - Returns image url as a String
     */
    public byte[] getImageURLFromPhotoReference(String photoRef, int maxWidth, int maxHeight) {
        String imageUrlAPI = "https://maps.googleapis.com/maps/api/place/photo?";
        imageUrlAPI += "photo_reference=" + photoRef;
        imageUrlAPI += "&maxwidth=" + maxWidth;
        imageUrlAPI += "&maxheight=" + maxHeight;
        imageUrlAPI += "&key=" + BuildConfig.MAPS_API_KEY;

        OkHttpClient requestClient = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(imageUrlAPI)
                .addHeader("Accept", "application/vnd.collection+json")
                .get()
                .build();
        Response response = null;

        try {
            response = requestClient.newCall(request).execute();
            byte[] result = response.body().bytes();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //endregion

}
