package com.example.chala.group12_hw08;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chala on 4/6/2017.
 */

public class SearchUtil {
    static public class searchJSONParser{
        static String searchWeather(String in) throws JSONException {
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

