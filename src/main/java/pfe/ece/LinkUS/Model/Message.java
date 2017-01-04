package pfe.ece.LinkUS.Model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Vignesh on 11/6/2016.
 */
@XmlRootElement(name = "message")
public class Message implements Serializable {

    private long id;
    private String subject;
    private String text;

    public Message() {}

    public Message(long id, String subject, String text) {
        this.id = id;
        this.subject = subject;
        this.text = text;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String toString() {
        return "Id:[" + this.getId() + "] Subject:[" + this.getSubject() + "] Text:[" + this.getText() + "]";
    }
}