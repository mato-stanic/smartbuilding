package hr.m2stanic.smartbuilding.web;

import hr.m2stanic.smartbuilding.core.apartment.ApartmentManager;
import hr.m2stanic.smartbuilding.core.appuser.AppUser;
import hr.m2stanic.smartbuilding.core.appuser.AppUserManager;
import hr.m2stanic.smartbuilding.core.messages.MessageManager;
import hr.m2stanic.smartbuilding.web.dto.AdminDTO;
import hr.m2stanic.smartbuilding.web.dto.DTOUtil;
import hr.m2stanic.smartbuilding.web.dto.TenantsDTO;
import hr.m2stanic.smartbuilding.web.thymeleaf.ThymeleafLayoutInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class SmartBuildingControllerAdvice {


    @Autowired
    private Environment env;


    @Autowired
    private AppUserManager appUserManager;

    @Autowired
    private ApartmentManager apartmentManager;

    @Autowired
    private MessageManager messageManager;

    @Autowired
    private ServletContext context;



    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFoundError(HttpServletRequest req, Exception exception) {
        return processErrorRequest(req, "not-found", exception);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleError(HttpServletRequest req, Exception exception) {
        return processErrorRequest(req, "error", exception);
    }


    private ModelAndView processErrorRequest(HttpServletRequest req, String viewName, Exception exception) {
        log.error("Request: " + req.getRequestURL() + " raised exception!", exception);
        String pathInfo = req.getPathInfo();
        String layoutName = pathInfo.startsWith(
                "/admin") ? ThymeleafLayoutInterceptor.ADMIN_DEFAULT_LAYOUT : ThymeleafLayoutInterceptor.USER_DEFAULT_LAYOUT;

        ModelAndView mav = new ModelAndView(viewName);
        mav.addObject("exception", exception);
//        mav.addObject("url", req.getRequestURL());
//        mav.addObject(ThymeleafLayoutInterceptor.DEFAULT_VIEW_ATTRIBUTE_NAME, viewName);
//        mav.setViewName(layoutName);
        return mav;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }


    @ModelAttribute("loggedInUser")
    public AppUser getLoggedInUser() {
        return appUserManager.getLoggedInUser();
    }


    @ModelAttribute("apartments")
    public List<AdminDTO> getApartments() {
        return apartmentManager.getAllApartments().stream().map(a -> DTOUtil.toDTO(a)).collect(Collectors.toList());
    }

    @ModelAttribute("tenantApartments")
    public List<TenantsDTO> getTenantApartments() {
        return apartmentManager.getAllTenantApartments().stream().map(a -> DTOUtil.toDTO(a)).collect(Collectors.toList());
    }

    @ModelAttribute("baseUrl")
    public String getBaseUrl() {
        String baseUrl;

        baseUrl = env.getProperty("application.base.url");
        if (!baseUrl.endsWith("/"))
            baseUrl += "/";

        return baseUrl;
    }




    @ModelAttribute("unreadMsgCount")
    public Long unreadMsgCount() {
        AppUser user = appUserManager.getLoggedInUser();
        if (user != null) {
            return messageManager.getUnreadMessageCount(user.getApartment().getId());
        }
        return 0L;
    }

    private String currentVersion;

    @ModelAttribute("version")
    public String getVersion() {
        if (currentVersion == null) {
            InputStream inputStream = context.getResourceAsStream("/META-INF/MANIFEST.MF");
            try {
                Manifest manifest = new Manifest(inputStream);
                Attributes attributes = manifest.getMainAttributes();
                currentVersion = attributes.getValue("Implementation-Version");
            } catch (Exception e) {
                currentVersion = "Not available in dev mode (no manifest)";
            }
        }
        return currentVersion;
    }


}


