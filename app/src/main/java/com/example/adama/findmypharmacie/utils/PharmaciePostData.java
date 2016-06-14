package com.example.adama.findmypharmacie.utils;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.adama.findmypharmacie.models.Pharmacie;


import org.json.JSONArray;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by adama on 13/06/2016.
 */
public class PharmaciePostData extends AsyncTask<String, Void, JSONArray> {

    HttpURLConnection urlConnection;
    private String URI;

    URL url = null;
    Pharmacie pharmacie;

    public PharmaciePostData(String url, Pharmacie p)
    {
        this.URI = url;
        this.pharmacie = p;
    }

    @Override
    protected JSONArray doInBackground(String... params) {
        try {
            HashMap<String, String> parametres = new HashMap<>();

            parametres.put("chaine", "chaine="+this.pharmacie.getName()+","+this.pharmacie.getAddress()+","+this.pharmacie.getTelephone()+"," +
                    ""+this.pharmacie.getLongitude()+","+this.pharmacie.getLatitude()+","+this.pharmacie.getAccracy()+"");

            parametres.put("imei", pharmacie.getEmei());

            url = new URL(URI + "?" + getDataString(parametres));
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);

            urlConnection.connect();

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int responseCode = 0;
        try {
            responseCode = urlConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("Etat web service","\nSending 'GET' request to URL : " + url);
        Log.d("Etat web service","Response Code : " + responseCode);

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;

            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            System.out.println(response.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(JSONArray re) {
    }

    private String getDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
