package com.start_up.dev.apilinkus.TimeLine;

/**
 * Created by Huong on 15/01/2017.
 */

public class TLObjectDate {
    final String TAG;
    final Double NUMBER;
    final Double MIN;
     TLObjectDate NEXT=null;
     TLObjectDate BEFORE=null;
    final int TYPE; //0 pas de compo , 1 decompo forcement un NEXT, 2 decompo de decompo forcement un BEFORE

    public TLObjectDate(String TAG, Double NUMBER, Double MIN, int TYPE) {
        this.TAG = TAG;
        this.NUMBER = NUMBER;
        this.MIN = MIN;
        this.TYPE = TYPE;

    }
public static TLObjectDate getNextType01(TLObjectDate dateRef){
    TLObjectDate tmp;
    tmp=dateRef.NEXT;
    while (tmp!=null && tmp.TYPE!=0 && tmp.TYPE!=1){
        tmp=tmp.NEXT;
    }
    return tmp;
}
}
