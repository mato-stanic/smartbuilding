package hr.m2stanic.smartbuilding.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import hr.m2stanic.smartbuilding.core.apartment.ApartmentManager;
import hr.m2stanic.smartbuilding.core.security.RoleManager;
import hr.m2stanic.smartbuilding.web.thymeleaf.ThymeleafLayoutInterceptor;
import hr.m2stanic.smartbuilding.web.converters.*;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.joda.DateTimeFormatterFactory;
import org.springframework.format.datetime.joda.JodaTimeFormatterRegistrar;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "hr.m2stanic.smartbuilding.web")
@PropertySource(value = {"classpath:/smartbuilding.common.properties", "classpath:/smartbuilding.properties"})
public class WebDispatcherConfig extends WebMvcConfigurerAdapter {


    @Autowired
    private ObjectMapper jsonObjectMapper;


    @Autowired
    private WebSecurityManager securityManager;

    @Autowired
    private ApartmentManager apartmentManager;

    @Autowired
    private RoleManager roleManager;


    @Override
    public void addFormatters(FormatterRegistry registry) {
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver paging = new PageableHandlerMethodArgumentResolver();
        paging.setOneIndexedParameters(true);
        paging.setPageParameterName("page");
        paging.setSizeParameterName("limit");
        paging.setOneIndexedParameters(true);
        paging.setMaxPageSize(50);
        argumentResolvers.add(paging);
    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        StringHttpMessageConverter httpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        converters.add(httpMessageConverter);
        converters.add(new ByteArrayHttpMessageConverter());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(jsonObjectMapper);
        converters.add(converter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        ThymeleafLayoutInterceptor publicLayoutInterceptor = new ThymeleafLayoutInterceptor();
        publicLayoutInterceptor.setDefaultLayout(ThymeleafLayoutInterceptor.USER_DEFAULT_LAYOUT);
        registry.addInterceptor(publicLayoutInterceptor).addPathPatterns("/**").excludePathPatterns("/admin/**");

        ThymeleafLayoutInterceptor adminLayoutInterceptor = new ThymeleafLayoutInterceptor();
        adminLayoutInterceptor.setDefaultLayout(ThymeleafLayoutInterceptor.ADMIN_DEFAULT_LAYOUT);
        registry.addInterceptor(adminLayoutInterceptor).addPathPatterns("/admin/**");

    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/res/**").addResourceLocations("/res/");

    }


    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("/WEB-INF/localization/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(10);
        return messageSource;
    }


    @Bean
    public TemplateResolver templateResolver() {
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setCacheable(false);
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(1);
        return resolver;
    }


    @Bean
    public SpringTemplateEngine templateEngine() {

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setMessageSource(messageSource());
        return templateEngine;
    }


    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setCharacterEncoding("UTF-8");
        resolver.setTemplateEngine(templateEngine());
        return resolver;
    }


    @Bean
    public LocaleResolver localeResolver() {
        FixedLocaleResolver localeResolver = new FixedLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("me"));
        return localeResolver;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(4194304); //4 MB
        multipartResolver.setDefaultEncoding("UTF-8");
        return multipartResolver;
    }

    @Bean
    public ObjectMapper jsonObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        return mapper;
    }


    @Bean
    public FormattingConversionService mvcConversionService() {
        // Use the DefaultFormattingConversionService but do not register defaults
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);

        // Ensure @NumberFormat is still supported
        conversionService.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());

        // Register date conversion with a specific global format
        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterFactory("dd.MM.yyyy HH:mm").createDateTimeFormatter();
        DateTimeFormatter dateFormatter = new DateTimeFormatterFactory("dd.MM.yyyy").createDateTimeFormatter();

        JodaTimeFormatterRegistrar registrar = new JodaTimeFormatterRegistrar();
        registrar.setDateFormatter(dateFormatter);
        registrar.setTimeFormatter(dateTimeFormatter);
        registrar.setDateTimeFormatter(dateTimeFormatter);
        registrar.registerFormatters(conversionService);

        conversionService.addConverter(new CompanyDTOConverter(apartmentManager));
        conversionService.addConverter(new RoleDTOConverter(roleManager));
        return conversionService;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter() {

        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setLoginUrl("/admin/login");
        shiroFilter.setSuccessUrl("/admin/");
        shiroFilter.setSecurityManager(securityManager);

        Map<String, String> definitionsMap = new LinkedHashMap<>();

        definitionsMap.put("/admin/login", "authc");
        definitionsMap.put("/admin", "authc, perms[admin-console:access]");
        definitionsMap.put("/admin/**", "authc, perms[admin-console:access]");
        definitionsMap.put("/admin/logout", "logout");

        shiroFilter.setFilterChainDefinitionMap(definitionsMap);

        return shiroFilter;
    }


}
