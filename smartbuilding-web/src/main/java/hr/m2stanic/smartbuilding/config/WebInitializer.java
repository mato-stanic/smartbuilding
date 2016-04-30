package hr.m2stanic.smartbuilding.config;

import hr.m2stanic.smartbuilding.web.SmartBuildingCaptcha;
import hr.m2stanic.smartbuilding.web.NoCacheFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.EnumSet;

public class WebInitializer implements WebApplicationInitializer {
    public static final String DISPATCHER_CONTEXT_NAME = "DISPATCHER_CONTEXT";

    public static final String ROOT_CONTEXT_NAME = "ROOT_CONTEXT";

    @Override
    public void onStartup(ServletContext container) {
        // Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
//        rootContext.setDisplayName(ROOT_CONTEXT_NAME);
        rootContext.register(DataConfig.class, AppConfig.class);


        // Manage the lifecycle of the root application context
        container.addListener(new ContextLoaderListener(rootContext));


        // Create the dispatcher servlet's Spring application context
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
//        dispatcherContext.setDisplayName(DISPATCHER_CONTEXT_NAME);
        dispatcherContext.setServletContext(container);
        dispatcherContext.setParent(rootContext);
//        dispatcherContext.scan("hr.m2stanic.smartbuilding.web");
        dispatcherContext.register(WebDispatcherConfig.class, SecurityConfig.class);


        // Register and map the dispatcher servlet
        DispatcherServlet dispatcherServlet = new DispatcherServlet(dispatcherContext);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/*");


        // Register and map the captcha servlet
        ServletRegistration.Dynamic captcha = container.addServlet("captcha", new SmartBuildingCaptcha());
        dispatcher.setLoadOnStartup(100);
        captcha.setInitParameter("width", "250");
        captcha.setInitParameter("height", "75");
//        captcha.setInitParameter("ttl", "60000");
        captcha.addMapping("/captcha");

        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        container.addFilter("characterEncodingFilter", encodingFilter).addMappingForUrlPatterns(null, false, "/*");


        FilterRegistration.Dynamic noCacheFilter = container.addFilter("noCacheFilter", new NoCacheFilter());
        noCacheFilter.addMappingForUrlPatterns(null, false, "/*");

        container.addFilter("shiroFilter", new DelegatingFilterProxy("shiroFilter", dispatcherContext))
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC, DispatcherType.INCLUDE,
                        DispatcherType.ERROR), false, "/*");

    }

}