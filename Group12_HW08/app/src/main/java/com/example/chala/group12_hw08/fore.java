package com.example.chala.group12_hw08;

import java.util.HashMap;

/**
 * Created by chala on 4/5/2017.
 */

public class fore {
    String min,max,minf,maxf,day,nyt,mobl,date;
    Integer day_icon,nyt_icon;

    public fore() {
    }

    public String getMinf() {
        return minf;
    }

    public void setMinf(String minf) {
        this.minf = minf;
    }

    public String getMaxf() {
        return maxf;
    }

    public void setMaxf(String maxf) {
        this.maxf = maxf;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMobl() {
        return mobl;
    }

    public void setMobl(String mobl) {
        this.mobl = mobl;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }


    public String getNyt() {
        return nyt;
    }

    public void setNyt(String nyt) {
        this.nyt = nyt;
    }

    public Integer getDay_icon() {
        return day_icon;
    }

    public void setDay_icon(Integer day_icon) {
        this.day_icon = day_icon;
    }

    public Integer getNyt_icon() {
        return nyt_icon;
    }

    public void setNyt_icon(Integer nyt_icon) {
        this.nyt_icon = nyt_icon;
    }

    @Override
    public String toString() {
        return "fore{" +
                "min='" + min + '\'' +
                ", max='" + max + '\'' +
                ", minf='" + minf + '\'' +
                ", maxf='" + maxf + '\'' +
                ", day='" + day + '\'' +
                ", nyt='" + nyt + '\'' +
                ", mobl='" + mobl + '\'' +
                ", date='" + date + '\'' +
                ", day_icon=" + day_icon +
                ", nyt_icon=" + nyt_icon +
                '}';
    }
}
