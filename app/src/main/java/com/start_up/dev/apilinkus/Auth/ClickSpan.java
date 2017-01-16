package com.start_up.dev.apilinkus.Auth;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


/**
 * Created by Vignesh on 11/8/2016.
 */

public class ClickSpan extends ClickableSpan {

    private OnClickListener mListener;

    public ClickSpan(OnClickListener listener){
        mListener = listener;
    }

    @Override
    public void onClick(View view) {
        if(mListener != null) mListener.onClick();
    }

    public interface OnClickListener{
        void onClick();
    }

    public static void clickify (TextView view, final String clickableText, final OnClickListener listener){
        Log.i("clickify","Passage par cette méthode");
        CharSequence text = view.getText();
        String string = text.toString();
        ClickSpan span = new ClickSpan(listener);

        int start = string.indexOf(clickableText);
        int end = start + clickableText.length();
        Log.i("clickify start = ","" + start);
        if(start == -1) return;

        if(text instanceof Spannable){
            ((Spannable) text).setSpan(span,start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else{
            SpannableString s = SpannableString.valueOf(text);
            s.setSpan(span,start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // Provides cursor positionning, scrolling and text selection functionnality in a TextView
        MovementMethod m = view.getMovementMethod();
        //  LinlkMovementMethod is a movement method that passes over link in the text buffer
        if((m == null ) || !(m instanceof LinkMovementMethod)){
            view.setMovementMethod(LinkMovementMethod.getInstance());
        }


    }
}
