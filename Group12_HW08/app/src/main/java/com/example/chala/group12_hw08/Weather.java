package com.example.chala.group12_hw08;

import java.io.Serializable;

/**
 * Created by chala on 4/4/2017.
 */

public class Weather implements Serializable {
    String LocalObservationDateTime,WeatherText,Metric,city,country,fav,Imp,keyfb;
    Integer WeatherIcon;

    public Weather() {
        this.city=null;
        this.country=null;
        this.LocalObservationDateTime = null;
        this.WeatherText = null;
        this.Metric = null;
        this.fav=null;
        this.WeatherIcon = 0;
        this.Imp=null;
        this.keyfb=null;
    }


    public String getLocalObservationDateTime() {
        return LocalObservationDateTime;
    }

    public void setLocalObservationDateTime(String localObservationDateTime) {
        LocalObservationDateTime = localObservationDateTime;
    }

    public String getWeatherText() {
        return WeatherText;
    }

    public void setWeatherText(String weatherText) {
        WeatherText = weatherText;
    }

    public String getMetric() {
        return Metric;
    }

    public void setMetric(String metric) {
        Metric = metric;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }

    public String getImp() {
        return Imp;
    }

    public void setImp(String imp) {
        Imp = imp;
    }

    public String getKeyfb() {
        return keyfb;
    }

    public void setKeyfb(String keyfb) {
        this.keyfb = keyfb;
    }

    public Integer getWeatherIcon() {
        return WeatherIcon;
    }

    public void setWeatherIcon(Integer weatherIcon) {
        WeatherIcon = weatherIcon;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "LocalObservationDateTime='" + LocalObservationDateTime + '\'' +
                ", WeatherText='" + WeatherText + '\'' +
                ", Metric='" + Metric + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", fav='" + fav + '\'' +
                ", Imp='" + Imp + '\'' +
                ", keyfb='" + keyfb + '\'' +
                ", WeatherIcon=" + WeatherIcon +
                '}';
    }
}
