package com.example.chala.group12_hw08;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by chala on 4/5/2017.
 */

public class cityAsyncTask extends AsyncTask<String ,Void, ArrayList<City>> {
    cityDetails cdet;

    public cityAsyncTask(cityDetails cdet) { this.cdet=cdet;
    }

    @Override
    protected ArrayList<City> doInBackground(String... params) {
        try{
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statusCode = con.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader=new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb=new StringBuilder();
                String line=reader.readLine();
                while(line!=null){
                    sb.append(line);
                    line=reader.readLine();
                }
                return cityUtil.cityJSONParser.parsecity(sb.toString());
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<City> cities) {
        super.onPostExecute(cities);
        if(cities!=null){
            cdet.cdetails(cities);
        }else{
            cdet.nocity();
        }

    }

    public interface cityDetails{
        public void cdetails(ArrayList<City> key);
        public void nocity();
    }
}
