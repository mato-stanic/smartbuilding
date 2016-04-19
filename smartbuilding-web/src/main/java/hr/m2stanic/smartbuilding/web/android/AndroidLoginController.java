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
    public ResponseEntity<AppUser> login(@RequestParam String username,
                                         @RequestParam String password,
                                         RedirectAttributes ra) {

        log.info("got request to login, username = {}, password = {}", username, password);
        AppUser appUser = appUserManager.getByUsername(username);
        if(appUser != null){
            if(appUser.getPassword().equals(password)){
                return new ResponseEntity<>(appUser, HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/apartmentLayout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ApartmentLayout> apartmentLayout(@RequestParam String apartmentId,
                                                 RedirectAttributes ra) {

        log.info("got request to find apartmentLayout, id = {}", apartmentId);
        Long aptId = Long.valueOf(apartmentId);
        Apartment apartment = apartmentManager.getApartment(aptId);
        if(apartment != null){
            ApartmentLayout apartmentLayout = apartmentManager.getApartmentRoomStates(apartment);
            return new ResponseEntity<>(apartmentLayout, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @RequestMapping(value = "/apartmentLayout/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ApartmentLayout> apartmentLayoutEdit(@RequestParam String apartmentId,
                                                               @RequestParam String roomToChange,
                                                               @RequestParam String state,
                                                           RedirectAttributes ra) {

        log.info("got request to find apartmentLayout, id = {}", apartmentId);


        try {
            Long aptId = Long.valueOf(apartmentId);
            Boolean st = Boolean.valueOf(state);
            Apartment apartment = apartmentManager.getApartment(aptId);
            ApartmentLayout apartmentLayout = apartmentManager.getApartmentRoomStates(apartment);
            switch (roomToChange) {
                case "living_room":
                    apartmentLayout.setLivingRoom(st);
                    break;
                case "kitchen":
                    apartmentLayout.setKitchen(st);
                    break;
                case "bathroom":
                    apartmentLayout.setBathroom(st);
                    break;
                case "bedroom":
                    apartmentLayout.setBedroom(st);
                    break;
                case "hallway":
                    apartmentLayout.setHallway(st);
                    break;
            }
            apartmentManager.save(apartmentLayout);
            return new ResponseEntity<>(apartmentLayout, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }



}
