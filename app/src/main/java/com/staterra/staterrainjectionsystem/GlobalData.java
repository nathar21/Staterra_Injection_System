package com.staterra.staterrainjectionsystem;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Terrell on 1/11/2015.
 */
public class GlobalData implements Parcelable {

    private String month = "ND";
    private String day = "ND";
    private String year = "ND";
    private String hour = "ND";
    private String minute = "ND";
    private String second = "ND";
    private String tankTemp = "ND";
    private String irrigationTemp = "ND";
    private String nutrientAmountLeft = "ND";
    private String nutrientAmountUsed = "ND";
    private String batteryLife = "ND";
    private String tamper = "ND";

    public String getTamper() {
        return tamper;
    }

    public void setTamper(String tamper) {
        this.tamper = tamper;
    }

    public String getNutrientAmountUsed() {
        return nutrientAmountUsed;
    }

    public void setNutrientAmountUsed(String nutrientAmountUsed) {
        this.nutrientAmountUsed = nutrientAmountUsed;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getTankTemp() {
        return tankTemp;
    }

    public void setTankTemp(String tankTemp) {
        this.tankTemp = tankTemp;
    }

    public String getIrrigationTemp() {
        return irrigationTemp;
    }

    public void setIrrigationTemp(String irrigationTemp) {
        this.irrigationTemp = irrigationTemp;
    }

    public String getNutrientAmountLeft() {
        return nutrientAmountLeft;
    }

    public void setNutrientAmountLeft(String nutrientAmountLeft) {
        this.nutrientAmountLeft = nutrientAmountLeft;
    }

    public String getBatteryLife() {
        return batteryLife;
    }

    public void setBatteryLife(String batteryLife) {
        this.batteryLife = batteryLife;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(month);
        out.writeString(day);
        out.writeString(year);
        out.writeString(hour);
        out.writeString(minute);
        out.writeString(second);
        out.writeString(tankTemp);
        out.writeString(irrigationTemp);
        out.writeString(nutrientAmountLeft);
        out.writeString(nutrientAmountUsed);
        out.writeString(batteryLife);
        out.writeString(tamper);
    }

    public static final Parcelable.Creator<GlobalData> CREATOR = new Creator<GlobalData>() {
        public GlobalData createFromParcel(Parcel source) {
            GlobalData gd = new GlobalData();
            gd.month = source.readString();
            gd.day = source.readString();
            gd.year = source.readString();
            gd.hour = source.readString();
            gd.minute = source.readString();
            gd.second = source.readString();
            gd.tankTemp = source.readString();
            gd.irrigationTemp = source.readString();
            gd.nutrientAmountLeft = source.readString();
            gd.nutrientAmountUsed = source.readString();
            gd.batteryLife = source.readString();
            gd.tamper = source.readString();
            return gd;
        }
        public GlobalData[] newArray(int size) {
            return new GlobalData[size];
        }
    };

    public void setSystemData(String[] data){
        tamper = data[0];
        batteryLife = data[1];
        nutrientAmountUsed = data[2];
        nutrientAmountLeft = data[3];
    }
}
