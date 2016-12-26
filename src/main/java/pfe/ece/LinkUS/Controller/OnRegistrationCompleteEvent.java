package pfe.ece.LinkUS.Controller;

import org.springframework.context.ApplicationEvent;

import java.util.Locale;

/**
 * Created by Vignesh on 12/9/2016.
 */
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private final Locale locale;
    private final String appUrl;
    private final String registred_email;

    public String getRegistred_email() {
        return registred_email;
    }

    private int step_no;

    public int getStep_no() {
        return step_no;
    }

    public void setStep_no(int step_no) {
        this.step_no = step_no;
    }

    public OnRegistrationCompleteEvent(String registered_email, Locale locale, String appUrl, int step_no) {
        super(registered_email);
        // client local information : ex: en_US
        this.locale = locale;
        this.appUrl = appUrl;
        this.registred_email = registered_email;
        this.step_no = step_no;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getAppUrl() {
        return appUrl;
    }


}
