package pfe.ece.LinkUS.Component;

import org.springframework.context.ApplicationEvent;

/**
 * Created by Vignesh on 1/3/2017.
 */
public class OnSendingCompleteEvent extends ApplicationEvent {

    private final String[] recipent_addresses;
    private final String subject;
    private final String bodyText;


    public OnSendingCompleteEvent(Object source,String[] recipient_addresses,String subject,String bodyText) {
        /*source is the object on which the event initially occurred */
        super(source);
        this.recipent_addresses = recipient_addresses;
        this.subject = subject;
        this.bodyText = bodyText;
    }


    public String[] getRecipent_addresses() {
        return recipent_addresses;
    }

    public String getSubject() {
        return subject;
    }

    public String getBodyText() {
        return bodyText;
    }
}
