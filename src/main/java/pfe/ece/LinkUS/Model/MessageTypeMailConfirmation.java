package pfe.ece.LinkUS.Model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Vignesh on 12/18/2016.
 */
@XmlRootElement(name = "messages_confirmation")
public class MessageTypeMailConfirmation extends Message {

    public boolean user_registred;

    public MessageTypeMailConfirmation(long id, String subject, String text,boolean user_registred) {
        super(id,subject,text);
        this.user_registred = user_registred;
    }

    public boolean isUser_registred() {
        return user_registred;
    }

    public void setUser_registred(boolean user_registred) {
        this.user_registred = user_registred;
    }
}
