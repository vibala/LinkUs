package pfe.ece.LinkUS.Config;


import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

/***
 * Base class for Spring Data MongoDB configuration extending AbstractMongoConfiguration.
 */
@Configuration
public class MyMongoConfig extends AbstractMongoConfiguration {

    private String host = "localhost";
    private Integer port = 27017;
    private String username = "";
    private String database = "mydb";
    private String password = "";
    private MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017");


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