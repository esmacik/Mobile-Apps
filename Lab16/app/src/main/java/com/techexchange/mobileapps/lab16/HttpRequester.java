package com.techexchange.mobileapps.lab16;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpRequester {

    private static final String TAG = "HttpRequester";
    private static final String API_KEY = "0de01cede9a66ac8754f2d0f8867e95c";

    private ArrayList<GalleryItem> allPhotos = new ArrayList<>();

    public List<GalleryItem> getFlickrPhotos(){
        try {
            String urlString = Uri.parse("https://api.flickr.com/services/rest")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .build()
                    .toString();
            Log.d(TAG, "pingFlickr: Query string: " + urlString);
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            try {
                ByteArrayOutputStream oStream = new ByteArrayOutputStream();
                InputStream iStream = connection.getInputStream();
                byte[] buffer = new byte[1024];
                for (int bytesRead; (bytesRead = iStream.read(buffer)) > 0; ){
                    oStream.write(buffer, 0, bytesRead);
                }
                byte[] responseBytes = oStream.toByteArray();
                String responseString = new String(responseBytes);
                JSONObject json = new JSONObject(responseString);
                JSONArray photoArray = json.getJSONObject("photos").getJSONArray("photo");
                for (int i = 0; i < photoArray.length(); i++) {
                    JSONObject photo = photoArray.getJSONObject(i);
                    GalleryItem item = new GalleryItem();
                    item.setId(photo.getString("id"));
                    item.setCaption(photo.getString("title"));
                    item.setUrl(photo.getString("url_s"));
                    Log.d(TAG, "getFlickrPhotos: url "+item.getUrl());
                    allPhotos.add(item);
                }
                Log.d(TAG, "pingFlickr: " + json.toString());
            } finally {
                connection.disconnect();
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, "pingFlickr: Exception", e);
        } finally {
            return allPhotos;
        }
    }
}