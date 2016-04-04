package hr.m2stanic.smartbuilding.web;

import hr.m2stanic.smartbuilding.web.dto.AgencyDTO;
import hr.m2stanic.smartbuilding.web.dto.DTOUtil;
import hr.m2stanic.smartbuilding.web.dto.UserGroupDTO;
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

import hr.m2stanic.smartbuilding.core.appuser.AppUser;
import hr.m2stanic.smartbuilding.core.appuser.AppUserManager;
import hr.m2stanic.smartbuilding.core.company.*;
import hr.m2stanic.smartbuilding.core.messages.MessageManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.*;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class EkipControllerAdvice {


    @Autowired
    private Environment env;


    @Autowired
    private AppUserManager appUserManager;

    @Autowired
    private CompanyManager companyManager;

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
        //StringTrimmerEditor poremeti prijavode sadrzaja, jer prazan string tretira kao null, a to znaci da uopce nema prijevoda
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }


    @ModelAttribute("loggedInUser")
    public AppUser getLoggedInUser() {
        return appUserManager.getLoggedInUser();
    }


    @ModelAttribute("agencies")
    public List<AgencyDTO> getAgencies() {
        return companyManager.getAllAgencies().stream().map(a -> DTOUtil.toDTO(a)).collect(Collectors.toList());
    }

    @ModelAttribute("userGroups")
    public List<UserGroupDTO> getUserGroups() {
        return companyManager.getAllOperatorGroups().stream().map(a -> DTOUtil.toDTO(a)).collect(Collectors.toList());
    }

    @ModelAttribute("baseUrl")
    public String getBaseUrl() {
//        String baseUrl = env.getProperty("application.base.url");
        String baseUrl;
        InetAddress address = null;

        try {
            address = getLocalHostLANAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        //todo: uncomment this crap prije prebacivanja na rpi
//        if(address != null){
//            baseUrl = "http://" + address.getHostAddress() + ":8080/smartbuilding/";
//        }
//        else{
//            baseUrl = env.getProperty("application.base.url");
//            if (!baseUrl.endsWith("/"))
//                baseUrl += "/";
//        }

        baseUrl = env.getProperty("application.base.url");
        if (!baseUrl.endsWith("/"))
            baseUrl += "/";

        System.out.println("base url: " + baseUrl);
        return baseUrl;
    }

    private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // Iterate all NICs (network interface cards)...
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // Iterate all IP addresses assigned to each card...
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {

                        if (inetAddr.isSiteLocalAddress()) {
                            // Found non-loopback site-local address. Return it immediately...
                            return inetAddr;
                        }
                        else if (candidateAddress == null) {
                            // Found non-loopback address, but not necessarily site-local.
                            // Store it as a candidate to be returned if site-local address is not subsequently found...
                            candidateAddress = inetAddr;
                            // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
                            // only the first. For subsequent iterations, candidate will be non-null.
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                // We did not find a site-local address, but we found some other non-loopback address.
                // Server might have a non-site-local address assigned to its NIC (or it might be running
                // IPv6 which deprecates the "site-local" concept).
                // Return this non-loopback candidate address...
                return candidateAddress;
            }
            // At this point, we did not find a non-loopback address.
            // Fall back to returning whatever InetAddress.getLocalHost() returns...
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        }
        catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }



    @ModelAttribute("unreadMsgCount")
    public Long unreadMsgCount() {
        AppUser user = appUserManager.getLoggedInUser();
        if (user != null) {
            return messageManager.getUnreadMessageCount(user.getCompany().getId());
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


