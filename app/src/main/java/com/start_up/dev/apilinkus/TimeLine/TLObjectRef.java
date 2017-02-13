package com.start_up.dev.apilinkus.TimeLine;

/**
 * Created by Huong on 15/01/2017.
 */

public class TLObjectRef {
    double year=-1;
    double month=-1;
    double day_pair=-1;
    double day_impair=-1;
    double hour_pair=-1;
    double hour_impair=-1;

    public TLObjectRef() {
    }

    public void init(){
        this.year=-1;
        this.month=-1;
        this.day_pair=-1;
        this.day_impair=-1;
        this.hour_pair=-1;
        this.hour_impair=-1;
    }

    public void setRef(double year,double month,double day_pair,double day_impair,double hour_pair,double hour_impair){
        this.year=year;
        this.month=month;
        this.day_pair=day_pair;
        this.day_impair=day_impair;
        this.hour_pair=hour_pair;
        this.hour_impair=hour_impair;
    }
}
