package pfe.ece.LinkUS.Config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import pfe.ece.LinkUS.Service.CurrentUserService.CurrentUserDetailsService;

import javax.sql.DataSource;

/**
 * Created by Vignesh on 11/11/2016.
 */

/***
 * Base class for OAuth 2.0 server configuration grouping authorization server configuration
 * and resource server configuration
 */
@Configuration
@Order(1)
public class OAuth2ServerConfig {

    private static final String RESOURCE_ID = "oauth2-resource";
    private static final Logger LOGGER = Logger.getLogger(OAuth2ServerConfig.class);


    /*****************************************/
    /*          RESOURCE  SERVER             */
    /****************************************/

    /***
     * Base configuration for resource server configuration
     */
    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends
            ResourceServerConfigurerAdapter {

        private String oauthClass = "com.mysql.jdbc.Driver";
        private String oauthUrl   = "jdbc:mysql://localhost:3306/linkusDB";

        /***
         * Return the token store that stores informations availaible in the resource server
         * @return
         */
        @Bean
        public TokenStore tokenStore() {
            DataSource tokenDataSource = DataSourceBuilder.create()
                    .driverClassName(oauthClass)
                    .username("linkus")
                    .password("linkus")
                    .url(oauthUrl)
                    .build();
            return new JdbcTokenStore(tokenDataSource);
        }


        /***
         * Return the token store that stores all the tokens and the oauth client details
         * @param resources
         */
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources
                    .tokenStore(tokenStore())
                    .resourceId(RESOURCE_ID);

        }

        /***
         * List the requests that are allowed to be sent to the server in which way
         * @param http
         * @throws Exception
         */
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST,"/user").permitAll()
                    .antMatchers(HttpMethod.POST,"/user/registration").permitAll()
                    .antMatchers(HttpMethod.GET,"/registrationConfirm").permitAll()
                    .antMatchers(HttpMethod.POST,"/facebook/login").permitAll()
                    .antMatchers(HttpMethod.POST,"/twitter/login").permitAll()
                    .antMatchers(HttpMethod.POST,"/oauth/token").permitAll()
                    .antMatchers(HttpMethod.GET,"/images*").permitAll()
                    .antMatchers(HttpMethod.GET,"/oauth/revoke-token").authenticated()
                    .antMatchers("/users/**").hasRole("ADMIN")
                    .anyRequest().authenticated();
        }

    }

    /***********************************************/
    /**         AUTHORIZATION SERVER              **/
    /***********************************************/

    /***
     * Base class for the configuration of the authorization server
     *
     */
    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends
            AuthorizationServerConfigurerAdapter {

        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        @Autowired
        private CurrentUserDetailsService userDetailsService;

        private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


        /***
         * Configure the security of the authorization server
         * @param oauthServer
         * @throws Exception
         */
        @Override
        public void configure(
                AuthorizationServerSecurityConfigurer oauthServer)
                throws Exception {
            oauthServer
                    .tokenKeyAccess("permitAll()")
                    .checkTokenAccess("isAuthenticated()");
        }

        /***
         * Configure the list of client applications that are allowed to sent an access and refresh token request
         * and the way they can send a request to the server
         * @param clients
         * @throws Exception
         */
        @Override
        public void configure(ClientDetailsServiceConfigurer clients)
                throws Exception {
            clients.inMemory().withClient("clientapp")
                    .secret("123456")
                    .authorizedGrantTypes(
                            "password","authorization_code","refresh_token")
                    .scopes("read","write")
                    .accessTokenValiditySeconds(3600*24*365)
                    .refreshTokenValiditySeconds(3600*24*365);
        }


        /***
         * Configure the token endpoint which is the endpoint on the authorization server where the client
         * application exchanges the authorization code, client ID and client secret, for an access token.
         * @param endpoints
         * @throws Exception
         */
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints)
                throws Exception {
            endpoints
                    .tokenStore(tokenStore())
                    .authenticationManager(authenticationManager);
        }

        /***
         * Return the token store that is used to store the tokens and the app client details
         * @return tokenstore
         */
        @Bean
        public TokenStore tokenStore() {
            return new JdbcTokenStore(dataSource());
        }

        @Value("classpath:schema.sql")
        private Resource schemaScript;

        /***
         * Use to set up a database during initialization and clean up a database during destruction.
         * @param dataSource
         * @return DataSourceInitializer
         */
        @Bean
        public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
            DataSourceInitializer initializer = new DataSourceInitializer();
            initializer.setDataSource(dataSource);
            initializer.setDatabasePopulator(databasePopulator());
            return initializer;
        }

        /***
         * Configure the database populator used to populate, initialize, or clean up a database.
         * @return
         */
        private DatabasePopulator databasePopulator() {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(schemaScript);
            return populator;
        }


        /**
         * Return the token store that stores all the tokens and the oauth client details
         * @return datasource
         */
        @Bean
        public DataSource dataSource() {
            String oauthClass = "com.mysql.jdbc.Driver";
            String oauthUrl = "jdbc:mysql://localhost:3306/linkusDB";

            DataSource dataSource = DataSourceBuilder.create()
                    .driverClassName(oauthClass)
                    .username("linkus")
                    .password("linkus")
                    .url(oauthUrl)
                    .build();
            return dataSource;
        }

    }

}
