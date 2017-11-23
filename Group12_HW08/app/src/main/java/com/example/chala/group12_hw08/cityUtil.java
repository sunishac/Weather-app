package com.example.chala.group12_hw08;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.text.TextUtils.substring;

/**
 * Created by chala on 4/5/2017.
 */

public class cityUtil {
    static public class cityJSONParser{
        static ArrayList<City> parsecity(String in) throws JSONException {
            ArrayList<City> c=new ArrayList<City>();
            ArrayList<fore> fr=new ArrayList<fore>();

            JSONObject root=new JSONObject(in);
            JSONObject obj = (JSONObject) root.get("Headline");
            String txt=(String) obj.get("Text");
            String m=(String) obj.get("MobileLink");
            String d=(String) obj.get("EffectiveDate");
            d=d.substring(0,10);
            
            JSONArray dfore=(JSONArray) root.get("DailyForecasts");
            City ch=new City();
            ch.setHeadText(txt);
            ch.setMob(m);
            ch.setEdate(d);

            for(int i=0;i<dfore.length();i++){
                fore f=new fore();
                JSONObject temp=(JSONObject) dfore.get(i);
                String dat=(String) temp.get("Date");
                dat=dat.substring(0,10);

                JSONObject tmp=(JSONObject) temp.get("Temperature");
                JSONObject min=(JSONObject) tmp.get("Minimum");
                Double v=(Double) min.get("Value");
                Double vf=(v-32)/(1.8);
                String valuef=vf.toString();
                valuef=valuef.substring(0,4);
                String value=v.toString() ;
                /*String unit=(String) min.get("Unit");
                String mini=value+"°"+unit;*/
                JSONObject max=(JSONObject) tmp.get("Maximum");
                Double v1=(Double) max.get("Value");
                String value1=v1.toString();
                Double v1f=(v1-32)/(1.8);
                String value1f=v1f.toString();
                value1f=value1f.substring(0,4);
                /*String unit1=(String) max.get("Unit");
                String maxi=value1+"°"+unit1;*/
                JSONObject day=(JSONObject) temp.get("Day");
                int d1= (int) day.get("Icon");
                String d2=(String) day.get("IconPhrase");
                JSONObject ngt=(JSONObject) temp.get("Night");
                int n1= (int) ngt.get("Icon");
                String n2=(String) ngt.get("IconPhrase");
                String mb=(String) temp.get("MobileLink");
                f.setMobl(mb);
                /*f.setMin(mini);
                f.setMax(maxi);*/
                f.setMin(value);
                f.setMinf(valuef);
                f.setMax(value1);
                f.setMaxf(value1f);
                f.setDay(d2);
                f.setNyt(n2);
                f.setDay_icon(d1);
                f.setNyt_icon(n1);
                f.setDate(dat);
                Log.d("demo","f is "+f);
                fr.add(f);
            }
            ch.setForecast(fr);
            c.add(ch);
            Log.d("demo","c is "+c);

            return c;
        }
    }
}
