package com.apps.gruz.korektorgraficzny.Core;

/**
 * Created by Marcin on 01.02.2017.
 */

public class Pdata {
    private int id;
    private String name;
    private int[] bands;

    public Pdata(int id, String name, int[] bands) {
        this.id = id;
        this.name = name;
        this.bands = bands;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pdata(String name, int[] bands) {

        this.name = name;
        this.bands = bands;
    }

    public Pdata() {
        name = null;
        bands = new int[5];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getBands() {
        return bands;
    }

    public void setBands(int[] bands) {
        this.bands = bands;
    }

    public void setBands(int band1,int band2, int band3, int band4, int band5){
        this.bands = new int[5];
        this.bands[0] = band1;
        this.bands[1] = band2;
        this.bands[2] = band3;
        this.bands[3] = band4;
        this.bands[4] = band5;
    }
}
