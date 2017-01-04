package pfe.ece.LinkUS.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

import javax.sql.DataSource;

/**
 * Created by Vignesh on 12/12/2016.
 * Spring social configuration
 */

/***
 * Base class for the social network signIn
 */
public class SocialConfig implements SocialConfigurer{

    @Autowired
    private DataSource dataSource;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
        connectionFactoryConfigurer.addConnectionFactory(new FacebookConnectionFactory(
                environment.getProperty("facebook.clientId"),
                environment.getProperty("facebook.clientSecret")
        ));
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new UserIdSource() {
            @Override
            public String getUserId() {
                return SecurityContext.getCurrentUser().getId();
            }
        };
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        UsersConnectionRepository usersConnectionRepository = new JdbcUsersConnectionRepository(dataSource,connectionFactoryLocator, Encryptors.noOpText());
        if(getUserIdSource().getUserId() == null){
            throw new IllegalStateException("Uunable to get a ConnectionRepository: no user signed in");
        }
        usersConnectionRepository.createConnectionRepository(getUserIdSource().getUserId());
        return usersConnectionRepository;
    }

    @Bean
    @Scope(value="request", proxyMode= ScopedProxyMode.INTERFACES)
    public Facebook facebook(ConnectionRepository repository){
        // ConnectionRepository iis being asked for the primary connection that the current user has with Facebook
        Connection<Facebook> connection = repository.findPrimaryConnection(Facebook.class);
        // If a connection to FB is found, a call to getApi() retrueves a Fb insstance that is configured with the connection details
        // when the first connection was first established
        Facebook facebook = connection != null ? connection.getApi() : null;
        return facebook;
    }


    // We will use here the class ProviderSignInController for handling the provider user sign-in flow
    @Bean
    public ProviderSignInController providerSignInController(ConnectionFactoryLocator connectionFactoryLocator,UsersConnectionRepository usersConnectionRepository){
        return new ProviderSignInController(connectionFactoryLocator,usersConnectionRepository,new SimpleSignInAdapter());
    }
}
