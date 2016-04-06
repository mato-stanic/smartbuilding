package hr.m2stanic.smartbuilding.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories({"hr.m2stanic.smartbuilding.core"})
@PropertySource(value = {"classpath:/smartbuilding.common.properties"})
//@PropertySource(value = {"classpath:/smartbuilding.common.properties", "classpath:/smartbuilding.properties"})
@Slf4j
public class DataConfig {

    @Autowired
    Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.POSTGRESQL);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);

        Properties props = new Properties();
        props.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        props.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        props.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        props.put("hibernate.max_fetch_depth", "5");
        props.put("hibernate.event.merge.entity_copy_observer", "allow");
        props.put("hibernate.format_sql", "false");
        props.put("hibernate.connection.charSet", "UTF-8");

//        props.put("hibernate.cache.provider_class", "org.hibernate.cache.HashtableCacheProvider");
//        props.put("hibernate.cache.use_second_level_cache", "true");
//        props.put("hibernate.cache.use_query_cache", "true");
//        props.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
//        factory.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);


        factory.setJpaProperties(props);

        factory.setPackagesToScan("hr.m2stanic.smartbuilding.core");
        factory.setDataSource(dataSource());
        factory.afterPropertiesSet();

        return factory;

    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager jtm = new JpaTransactionManager();
        jtm.setEntityManagerFactory(entityManagerFactory().getObject());
        return jtm;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }


    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        try {
            ComboPooledDataSource dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass(env.getProperty("jdbc.driverClassName"));
            dataSource.setJdbcUrl(env.getProperty("jdbc.url"));
            dataSource.setUser(env.getProperty("jdbc.username"));
            dataSource.setPassword(env.getProperty("jdbc.password"));
            dataSource.setAcquireIncrement(1);
            dataSource.setMinPoolSize(5);
            dataSource.setMaxPoolSize(40);
            dataSource.setInitialPoolSize(10);
            dataSource.setMaxIdleTime(3600);
            dataSource.setPreferredTestQuery("SELECT 1");
//			dataSource.setConnectionTestStatement(env.getProperty("jdbc.validationQuery"));

            return dataSource;
        } catch (PropertyVetoException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
