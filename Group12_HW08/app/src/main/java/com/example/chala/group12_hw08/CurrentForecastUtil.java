package com.example.chala.group12_hw08;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chala on 4/4/2017.
 */

public class CurrentForecastUtil {
    static public class CurrentForecastJSONParser{
        static ArrayList<Weather> parseCurrentForecast(String in) throws JSONException {
            ArrayList<Weather> wt=new ArrayList<Weather>();

            JSONArray root=new JSONArray(in);

            for(int i=0;i<root.length();i++){
                JSONObject obj = (JSONObject) root.get(i);
                /*Weather w=Weather.create(obj);*/
                Weather w=new Weather();
                String tim = (String) obj.get("LocalObservationDateTime");
                //tim=tim.substring(0,10);
                String weaText=(String) obj.get("WeatherText");
                Integer weaIcon=(Integer) obj.get("WeatherIcon");
                JSONObject temp=(JSONObject) obj.get("Temperature");
                JSONObject metric=(JSONObject) temp.get("Metric");
                JSONObject imper=(JSONObject) temp.get("Imperial");
                Double vl=(Double) imper.get("Value");
                String value1=(String) vl.toString();
                String ip=value1+"°F";
                Double val=(Double) metric.get("Value");
                String value=val.toString();
                String unit=(String) metric.get("Unit");
                String met=value+"°"+unit;
                w.setLocalObservationDateTime(tim);
                Log.d("demo","time is "+w.getLocalObservationDateTime());
                w.setWeatherText(weaText);
                w.setWeatherIcon(weaIcon);
                w.setMetric(met);
                w.setImp(ip);
                Log.d("demo","w is "+w);
                wt.add(w);
                Log.d("demo","wt is "+wt);
            }
            Log.d("demo","util of curFore "+wt);
            return wt;
        }
    }
}