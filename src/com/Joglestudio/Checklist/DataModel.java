package com.Joglestudio.Checklist;

import java.util.Calendar;

public class DataModel{
    private String itemName;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int imagePicked;
    private boolean isTicked;

    public static DataModel getUnpackedData(String string){
        DataModel dataModel = new DataModel();
        if (string.equals(""))
            return dataModel;
        String splitString[] = string.split("\n");
        dataModel.itemName = splitString[0];
        String splitString1[] = splitString[1].split(" ");
        dataModel.year = Integer.parseInt(splitString1[0]);
        dataModel.month = Integer.parseInt(splitString1[1]);
        dataModel.day = Integer.parseInt(splitString1[2]);
        dataModel.hour = Integer.parseInt(splitString1[3]);
        dataModel.minute = Integer.parseInt(splitString1[4]);
        dataModel.imagePicked = Integer.parseInt(splitString[2]);
        dataModel.isTicked = Boolean.parseBoolean(splitString[3]);
        return dataModel;
    }

    public static String getPackedString(DataModel datamodel){
        String string = new String("");
        string += datamodel.itemName+"\n";
        string += datamodel.year+" "+datamodel.month+" "+datamodel.day+" "+datamodel.hour+" "+datamodel.minute+"\n";
        string += datamodel.imagePicked+"\n";
        string += datamodel.isTicked;
        return string;
    }

    public String getItemName(){
        return itemName;
    }

    public void setItemName(String s){
        itemName = s;
    }

    public int getYear(){
        return year;
    }

    public void setYear(int y){
        year = y;
    }

    public int getMonth(){
        return month;
    }

    public void setMonth(int m){
        month = m;
    }

    public int getDay(){
        return day;
    }

    public void setDay(int d){
        day = d;
    }

    public int getHour(){
        return hour;
    }

    public void setHour(int h){
        hour = h;
    }

    public int getMinute(){
        return minute;
    }

    public void setMinute(int m){
        minute = m;
    }

    public int getImagePicked(){
        return  imagePicked;
    }

    public void setImagePicked(int p){
        imagePicked = p;
    }

    public boolean getIsTicked(){
        return isTicked;
    }

    public void setIsTicked(boolean i){
        isTicked = i;
    }
}