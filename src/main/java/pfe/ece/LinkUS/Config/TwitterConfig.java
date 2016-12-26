package pfe.ece.LinkUS.Config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * Created by Vignesh on 12/14/2016.
 */
@ConfigurationProperties(prefix="twitter")
@Component
public class TwitterConfig {

    //@Value("${twitter.consumerKey}")
    private String consumerKey;
    //@Value("${twitter.consumerSecret}")
    private String consumerSecret;

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }


}
