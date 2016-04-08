package hr.m2stanic.smartbuilding.web.admin;

import hr.m2stanic.smartbuilding.core.apartment.Apartment;
import hr.m2stanic.smartbuilding.core.apartment.ApartmentLayout;
import hr.m2stanic.smartbuilding.core.apartment.ApartmentManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/admin/user")
public class ApartmentController {

    @Autowired
    private ApartmentManager apartmentManager;


    @RequestMapping("/apartmentLayout/simple")
    @ResponseBody
    public String changeApartmentLayout(@RequestParam Long apartmentId,
                                        @RequestParam String roomToChange,
                                        @RequestParam Boolean state, RedirectAttributes ra) {
        try {
            Apartment apartment = apartmentManager.getApartment(apartmentId);
            ApartmentLayout apartmentLayout = apartmentManager.getApartmentRoomStates(apartment);
            switch (roomToChange) {
                case "living_room":
                    apartmentLayout.setLivingRoom(state);
                    break;
                case "kitchen":
                    apartmentLayout.setKitchen(state);
                    break;
                case "bathroom":
                    apartmentLayout.setBathroom(state);
                    break;
                case "bedroom":
                    apartmentLayout.setBedroom(state);
                    break;
                case "hallway":
                    apartmentLayout.setHallway(state);
                    break;
            }
            apartmentManager.save(apartmentLayout);
            return "SUCCESS";

        } catch (Exception e) {
            return "ERROR";
        }

    }

    @ModelAttribute("mainNavSel")
    public MainNavigationItem getMainNavigationSelection() {
        return MainNavigationItem.USERS;
    }

}
