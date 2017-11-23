package com.example.chala.group12_hw08;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chala on 4/4/2017.
 */

public class WeatherUtil {
    static public class WeatherJSONParser{
        static String parseWeather(String in) throws JSONException {
            String its= "empty";

            JSONArray root=new JSONArray(in);

            for(int i=0;i<root.length();i++){
                JSONObject wet=root.getJSONObject(i);
                String it=wet.getString("Key");
                if(i==0){
                    its=it;
                }
                break;
            }
            return its;
        }
    }
}

