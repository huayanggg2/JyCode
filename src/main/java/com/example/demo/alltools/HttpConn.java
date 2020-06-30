package com.example.demo.alltools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Base64;

public class HttpConn {
    public JSONArray getJson(String requestURL) {
        StringBuffer buffer = new StringBuffer();
        URLConnection httpConn = null;
        BufferedReader reader = null;
        String username = "admin";
        String password = "oneapm";
        String author = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        JSONArray ja = null;
        URL url = null;
        try {
            url = new URL(requestURL);

            httpConn = (HttpURLConnection) url.openConnection();
            ((HttpURLConnection) httpConn).setRequestMethod("GET");
            httpConn.setRequestProperty("Content-Type", "text/plain");
            httpConn.setRequestProperty("charset", "UTF-8");
            httpConn.setRequestProperty("Authorization", author);
            httpConn.connect();
            reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            JSONObject ob = new JSONObject(buffer.toString());
            ja = ob.getJSONArray("result");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
                return ja;
    }


}