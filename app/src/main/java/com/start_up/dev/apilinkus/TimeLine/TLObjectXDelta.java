package com.start_up.dev.apilinkus.TimeLine;

/**
 * Created by Huong on 15/01/2017.
 */

public class TLObjectXDelta {
    Double year;
    Double month;
    Double day_pair;
    Double day_impair;
    Double hour_pair;
    Double hour_impair;

    public TLObjectXDelta(Double x_delta_year, Double x_delta_month, Double x_delta_day_pair, Double x_delta_day_impair, Double x_delta_hour_pair, Double x_delta_hour_impair) {
        this.year = x_delta_year;
        this.month = x_delta_month;
        this.day_pair = x_delta_day_pair;
        this.day_impair = x_delta_day_impair;
        this.hour_pair = x_delta_hour_pair;
        this.hour_impair = x_delta_hour_impair;
    }

    public void setXDelta(Double x_delta_year, Double x_delta_month, Double x_delta_day_pair, Double x_delta_day_impair, Double x_delta_hour_pair, Double x_delta_hour_impair) {
        this.year = x_delta_year;
        this.month = x_delta_month;
        this.day_pair = x_delta_day_pair;
        this.day_impair = x_delta_day_impair;
        this.hour_pair = x_delta_hour_pair;
        this.hour_impair = x_delta_hour_impair;
    }
}
