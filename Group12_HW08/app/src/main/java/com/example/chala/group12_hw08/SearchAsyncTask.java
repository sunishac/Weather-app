package com.example.chala.group12_hw08;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by chala on 4/6/2017.
 */

public class SearchAsyncTask extends AsyncTask<String,Void,String> {
    IDat activity;

    public SearchAsyncTask(IDat activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
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
                return SearchUtil.searchJSONParser.searchWeather(sb.toString());
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
    protected void onPostExecute(String k) {
        super.onPostExecute(k);
        Log.d("demo","onpost "+k);
        if(k!=null){
            activity.keyOb(k);
        }else{
            activity.searchNothing();
        }
    }

    public interface IDat{
        public void keyOb(String key);
        public void searchNothing();
    }
}