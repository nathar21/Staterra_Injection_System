package com.staterra.staterrainjectionsystem;

import java.util.GregorianCalendar;

/**
 * Created by Terrell on 1/11/2015.
 */
public class GlobalData {

    private GregorianCalendar dateTime;
    private int tankTemp;
    private int irrigationTemp;
    private int nutrientAmountLeft;
    private int batteryLife;

    public GregorianCalendar getDateTime() {
        return dateTime;
    }

    public void setDateTime(GregorianCalendar dateTime) {
        this.dateTime = dateTime;
    }

    public int getTankTemp() {
        return tankTemp;
    }

    public void setTankTemp(int tankTemp) {
        this.tankTemp = tankTemp;
    }

    public int getIrrigationTemp() {
        return irrigationTemp;
    }

    public void setIrrigationTemp(int irrigationTemp) {
        this.irrigationTemp = irrigationTemp;
    }

    public int getNutrientAmountLeft() {
        return nutrientAmountLeft;
    }

    public void setNutrientAmountLeft(int nutrientAmountLeft) {
        this.nutrientAmountLeft = nutrientAmountLeft;
    }

    public int getBatteryLife() {
        return batteryLife;
    }

    public void setBatteryLife(int batteryLife) {
        this.batteryLife = batteryLife;
    }
}
