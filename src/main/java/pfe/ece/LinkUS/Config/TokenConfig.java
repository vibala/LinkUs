package pfe.ece.LinkUS.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * Created by Vignesh on 12/15/2016.
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "pfe.ece.LinkUS.Repository.TokenMySQLRepo",
        entityManagerFactoryRef = "tokenEntityManager",
        transactionManagerRef = "tokenTransactionManager"
)
@PropertySource(value = {"classpath:application.properties"})
public class TokenConfig {

    @Value("${datasource.primary.jdbc.driverClassName}")
    String driverClassName;
    @Value("${datasource.primary.jdbc.url}")
    String url;
    @Value("${datasource.primary.jdbc.username}")
    String username;
    @Value("${datasource.primary.jdbc.password}")
    String password;

    @Value("${spring.jpa.properties.hibernate.dialect}")
    String dialectProperty;

    @Autowired
    private Environment env;

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean tokenEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(notificationTokenDataSource());
        em.setPackagesToScan(new String[] { "pfe.ece.LinkUS.Model" });

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto","update");
        properties.put("hibernate.dialect", dialectProperty);
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean
    public DataSource notificationTokenDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Primary
    @Bean
    public PlatformTransactionManager tokenTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(tokenEntityManager().getObject());
        return transactionManager;
    }
}
