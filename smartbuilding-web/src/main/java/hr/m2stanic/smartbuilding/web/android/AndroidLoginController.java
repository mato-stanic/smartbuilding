package hr.m2stanic.smartbuilding.web.android;


import hr.m2stanic.smartbuilding.core.apartment.Apartment;
import hr.m2stanic.smartbuilding.core.apartment.ApartmentLayout;
import hr.m2stanic.smartbuilding.core.apartment.ApartmentManager;
import hr.m2stanic.smartbuilding.core.appuser.AppUser;
import hr.m2stanic.smartbuilding.core.appuser.AppUserManager;
import hr.m2stanic.smartbuilding.web.thymeleaf.Layout;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(value = "/android/admin")
@Slf4j
public class AndroidLoginController {

    @Autowired
    private AppUserManager appUserManager;

    @Autowired
    private ApartmentManager apartmentManager;


    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ApartmentLayout> login(@RequestParam String username,
                                         @RequestParam String password,
                                         RedirectAttributes ra) {

        log.info("got request to login, username = {}, password = {}", username, password);
        AppUser appUser = appUserManager.getByUsername(username);
        if(appUser != null){
            if(appUser.getPassword().equals(password)){
                ApartmentLayout apartmentLayout = apartmentManager.getApartmentRoomStates(appUser.getApartment());
                return new ResponseEntity<>(apartmentLayout, HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
