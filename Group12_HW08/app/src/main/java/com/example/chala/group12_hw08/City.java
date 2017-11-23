package com.example.chala.group12_hw08;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chala on 4/5/2017.
 */

public class City {
    String headText,mob,edate;
    ArrayList<fore> forecast;

    public City() {
        this.headText = headText;
        this.forecast=forecast;
        this.mob=mob;
        this.edate=edate;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getHeadText() {
        return headText;
    }

    public void setHeadText(String headText) {
        this.headText = headText;
    }

    public ArrayList<fore> getForecast() {
        return forecast;
    }

    public void setForecast(ArrayList<fore> forecast) {
        this.forecast = forecast;
    }

    @Override
    public String toString() {
        return "City{" +
                "headText='" + headText + '\'' +
                ", mob='" + mob + '\'' +
                ", edate='" + edate + '\'' +
                ", forecast=" + forecast +
                '}';
    }
}
