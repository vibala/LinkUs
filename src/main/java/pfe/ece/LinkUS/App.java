package pfe.ece.LinkUS;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserRepository;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App implements CommandLineRunner {

    private UserRepository usersRepository;

    public static void main( String[] args ) {
        System.getProperties().put("server.port","9999");
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
