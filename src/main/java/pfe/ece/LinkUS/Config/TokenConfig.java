package pfe.ece.LinkUS.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
public class TokenConfig {
    @Autowired
    private Environment env;

    @Bean
    @Primary
    @ConfigurationProperties(prefix="datasource.primary")
    public LocalContainerEntityManagerFactoryBean tokenEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(notificationTokenDataSource());
        em.setPackagesToScan(new String[] { "pfe.ece.LinkUS.Model" });

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto","update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean
    @ConfigurationProperties(prefix="datasource.primary")
    public DataSource notificationTokenDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3311/linkusDB");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        return dataSource;
    }

    @Primary
    @Bean
    @ConfigurationProperties(prefix="datasource.primary")
    public PlatformTransactionManager tokenTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(tokenEntityManager().getObject());
        return transactionManager;
    }
}
