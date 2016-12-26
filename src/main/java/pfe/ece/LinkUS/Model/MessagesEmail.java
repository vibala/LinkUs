package pfe.ece.LinkUS.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Locale;

/**
 * Created by Vignesh on 12/10/2016.
 */
@Component
public class MessagesEmail {

    @Autowired
    private MessageSource messageSource;

    private MessageSourceAccessor accessor;

    @PostConstruct
    private void init(){
        accessor = new MessageSourceAccessor(messageSource, Locale.ENGLISH.US);
    }

    public String getMessage(String code){
        return accessor.getMessage(code);
    }
}
