package fesb.papac.marin.augmented_reality_poi;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dementor on 24.02.2018.
 */

public class PointOfInterestController implements  PointOfInterestService {

    ArrayList<PointOfInterest> listOfPOI;

    @Override
    public void getPlacesByType(String type, PointOfInterest poi) {

        String url = "https://maps.googleapis.com/maps/";
        String key = "AIzaSyBPkUvg6Xz17H4uK5rBl-Hf7K7ItOvjCUA";
        final String radius= "500";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);

        Call<HttpHelper> call = service.getNearbyPlaces(poi.getLatitude() + "," + poi.getLongitude(), radius,type, key);

        call.enqueue(new Callback<HttpHelper>() {

            @Override
            public void onResponse(Call<HttpHelper> call, Response<HttpHelper> response) {
                try {

                    // This loop will go through all the results and add marker on each location.
                    for (int i = 0; i < response.body().getResults().size(); i++) {
                        PointOfInterest poi = new PointOfInterest();
                        ResultAPI details = response.body().getResults().get(i);
                        poi.setGeometry(details.getGeometry());
                        poi.setData(details.getName());
                        listOfPOI.add(poi);
                    }
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<HttpHelper> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    public ArrayList<PointOfInterest> getListOfPOI() {
        return listOfPOI;
    }

    @Override
    public ArrayList<PointOfInterest> getAllPointsOfInterest(double lat, double lng) {
        try {
            String query = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                    "json?location=" + lat + "," + lng + "&radius=500" +
                    "&key=AIzaSyBPkUvg6Xz17H4uK5rBl-Hf7K7ItOvjCUA/";
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(query) //Базовая часть адреса
                    .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                    .build();
            PointOfInterest pointOfInterest = retrofit.create(PointOfInterest.class);
            listOfPOI.add(pointOfInterest);
        }
        catch (Exception e )
        {
            System.out.print(e);
        }
        return listOfPOI;
//        URL url= null;
//        listOfPOI = new ArrayList<>();
//        try {
//            url = new URL(query);
//
//
//            Scanner scan = new Scanner(url.openStream());
//            String str = new String();
//            while (scan.hasNext())
//                str += scan.nextLine();
//            scan.close();
//            JSONArray allResults = new JSONObject(str)
//                    .getJSONArray("results");
//            for (int i=0;i<allResults.length();i++)
//            {
//                JSONObject placeInfo = allResults.getJSONObject(i);
//                poi =new PointOfInterest();
////                place.setName(placeInfo.get("name").toString());
////                place.setLatitude(Double.parseDouble(placeInfo.getJSONObject("geometry").getJSONObject("location").get("lat").toString()));
////                place.setLongitude((Double) placeInfo.getJSONObject("geometry").getJSONObject("location").get("lng"));
////                place.setLocation((Double) placeInfo.getJSONObject("geometry").getJSONObject("location").get("lat"),(Double) placeInfo.getJSONObject("geometry").getJSONObject("location").get("lng"));
////                place.setDistance(setDistance(place,params[0][0],params[0][1]));
//                listOfPOI.add(poi);
//            }
//            return listOfPOI;
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return listOfPOI;

    }

    @Override
    public PointOfInterest getPointOfInterestByType(double lat, double lng,String type) {

        String query="https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                "json?location="+lat+","+lng+"&radius=500&type=" +type+
                "&key=AIzaSyBPkUvg6Xz17H4uK5rBl-Hf7K7ItOvjCUA";
        URL url= null;
//        ArrayList<Place> places = new ArrayList<>();
        try {
            url = new URL(query);


            Scanner scan = new Scanner(url.openStream());
            String str = new String();
            while (scan.hasNext())
                str += scan.nextLine();
            scan.close();
            JSONArray allResults = new JSONObject(str)
                    .getJSONArray("results");
            for (int i=0;i<allResults.length();i++)
            {
                JSONObject placeInfo = allResults.getJSONObject(i);
//                Place place =new Place();
//                place.setName(placeInfo.get("name").toString());
//                place.setLatitude(Double.parseDouble(placeInfo.getJSONObject("geometry").getJSONObject("location").get("lat").toString()));
//                place.setLongitude((Double) placeInfo.getJSONObject("geometry").getJSONObject("location").get("lng"));
//                place.setLocation((Double) placeInfo.getJSONObject("geometry").getJSONObject("location").get("lat"),(Double) placeInfo.getJSONObject("geometry").getJSONObject("location").get("lng"));
//                place.setDistance(setDistance(place,params[0][0],params[0][1]));
//                places.add(place);
            }
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
