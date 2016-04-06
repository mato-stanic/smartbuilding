package hr.m2stanic.smartbuilding.config;

import com.sun.mail.util.MailSSLSocketFactory;
import hr.m2stanic.smartbuilding.core.CacheConstants;
import hr.m2stanic.smartbuilding.core.InitSampleData;
import hr.m2stanic.smartbuilding.core.files.DefaultFileManager;
import hr.m2stanic.smartbuilding.core.files.FileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Configuration
@PropertySource(value = {"classpath:/smartbuilding.common.properties"})
//@PropertySource(value = {"classpath:/smartbuilding.common.properties", "classpath:/smartbuilding.properties"})
@ComponentScan(basePackages = {"hr.m2stanic.smartbuilding.core"})
@Slf4j
public class AppConfig {

    @Autowired
    Environment env;


    @Bean
    public InitSampleData initSampleData() {
        return new InitSampleData();
    }

    @Bean
    public FileManager fileManager() {
        return new DefaultFileManager(env.getProperty("files.root.dir.path"));
    }


    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(50);
        return executor;
    }


    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        Properties props = new Properties();
        if (env.getProperty("mail.smtp.starttls.enable") != null) {
            props.put("mail.smtp.starttls.enable", env.getProperty("mail.smtp.starttls.enable"));
        }
        if (env.getProperty("mail.smtp.ssl.enable") != null) {
            props.put("mail.smtp.ssl.enable", env.getProperty("mail.smtp.ssl.enable"));
        }
        if (env.getProperty("mail.smtp.auth") != null) {
            props.put("mail.smtp.auth", env.getProperty("mail.smtp.auth"));
        }
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.socketFactory", sf);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        mailSender.setJavaMailProperties(props);

        mailSender.setHost(env.getProperty("mail.host"));
        mailSender.setPort(Integer.parseInt(env.getProperty("mail.port")));
        mailSender.setProtocol(env.getProperty("mail.protocol"));
        mailSender.setUsername(env.getProperty("mail.username"));
        mailSender.setPassword(env.getProperty("mail.password"));
        return mailSender;
    }

    @PostConstruct
    public void init() {
        initSampleData().init();
    }


}
