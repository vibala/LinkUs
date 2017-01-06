package pfe.ece.LinkUS.Component;

import com.mailjet.client.MailjetClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import pfe.ece.LinkUS.Model.MessagesEmail;
import pfe.ece.LinkUS.Service.TokenService.VerificationTokenService;
import pfe.ece.LinkUS.Service.UserEntityService.UserService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by Vignesh on 1/3/2017.
 */
@Component
public class MailSenderListener implements ApplicationListener<OnSendingCompleteEvent> {

    private static final Logger LOGGER = Logger.getLogger(MailSenderListener.class);
    @Autowired
    private UserService userservice;
    @Autowired
    private VerificationTokenService tokenservice;
    @Autowired
    private MessagesEmail messages;
    private JavaMailSenderImpl sender;
    private final String usernameFromEmail = "twentyonebala@gmail.com";
    private final String MJ_APIKEY_PUBLIC = "6a3627c75cf706d27c178df62950e436";
    private final String MJ_APIKEY_PRIVATE = "967d30f5c7e40ae3efb0d81905d274e2";
    private MailjetClient client;


    public void configureMailSender(){
        sender = new JavaMailSenderImpl();
        sender.setHost("in-v3.mailjet.com");
        sender.setUsername(MJ_APIKEY_PUBLIC);
        sender.setPassword(MJ_APIKEY_PRIVATE);

        Properties props = new Properties();
        props.put( "mail.smtp.auth", "true" );
        props.put("mail.smtp.port","587");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.trust", "in-v3.mailjet.com");
        props.put("mail.smtp.starttls.enable","true");

        sender.setJavaMailProperties(props);
    }

    public void sendMail(OnSendingCompleteEvent event){
        String[] recipient_addresses = event.getRecipent_addresses();
        String subject = event.getSubject();
        String bodyText = event.getBodyText();
        try{
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(usernameFromEmail);
            helper.setTo(recipient_addresses);
            helper.setSubject(subject);
            helper.setText(bodyText);
            sender.send(message);
        }catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onApplicationEvent(OnSendingCompleteEvent event) {
        configureMailSender();
        sendMail(event);
    }
}
