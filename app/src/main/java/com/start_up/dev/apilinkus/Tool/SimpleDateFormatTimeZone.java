package com.start_up.dev.apilinkus.Tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Huong on 29/01/2017.
 */

public class SimpleDateFormatTimeZone extends SimpleDateFormat{

    public Date parseWithTimeZone(String dateString,String timeZone) throws ParseException {
            this.setTimeZone(TimeZone.getTimeZone(timeZone));
        return super.parse(dateString);
    }
    public SimpleDateFormatTimeZone(String s){
        super(s);
    }

    public String formatWithTimeZone(Date date,String timeZone){
        if(timeZone!=null)
            this.setTimeZone(TimeZone.getTimeZone(timeZone));
        return super.format(date);

    }
}
