package pfe.ece.linkus.Controller;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import pfe.ece.LinkUS.Repository.OtherMongoDBRepo.UserRepository;
import pfe.ece.LinkUS.Service.UserEntityService.UserService;

/**
 * Created by Vignesh on 12/20/2016.
 */
@Profile("test")
@Configuration
public class ServiceTestConfig {

    @Bean
    @Primary
    public UserService userService(){
        return Mockito.mock(UserService.class);
    }

    @Bean
    @Primary
    public UserRepository userRepository(){
        return Mockito.mock(UserRepository.class);
    }

}
