package pfe.ece.LinkUS.Config;


import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

/***
 * Base class for Spring Data MongoDB configuration extending AbstractMongoConfiguration.
 */
@Configuration
@PropertySource(value = {"classpath:application.properties"})
public class MyMongoConfig extends AbstractMongoConfiguration {

    @Value("${spring.data.mongodb.host}")
    private String host;
    @Value("${spring.data.mongodb.port}")
    private Integer port;
    @Value("${spring.data.mongodb.username}")
    private String username;
    @Value("${spring.data.mongodb.database}")
    private String database;
    @Value("${spring.data.mongodb.password}")
    private String password;
    @Value("${spring.data.mongodb.uri}")
    private MongoClientURI uri;


    /***
     * Return the name of the database
     * @return
     */
    @Override
    public String getDatabaseName() {
        return database;
    }


    /***
     * Return the MongoClient
     * @return MongoClient
     * @throws Exception
     */
    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient(uri);
    }
}