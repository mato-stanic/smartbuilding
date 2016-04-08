package hr.m2stanic.smartbuilding.web.admin;

import hr.m2stanic.smartbuilding.core.apartment.Apartment;
import hr.m2stanic.smartbuilding.core.apartment.ApartmentCronJob;
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

import java.util.List;

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

    @RequestMapping("/apartmentLayout/advanced")
    @ResponseBody
    public String changeApartmentLayoutCron(@RequestParam Long apartmentId,
                                            @RequestParam String roomToChange,
                                            @RequestParam String action,
                                            @RequestParam List<String> checkedDays,
                                            @RequestParam String time, RedirectAttributes ra) {
        try {
            Apartment apartment = apartmentManager.getApartment(apartmentId);
            List<ApartmentCronJob> apartmentCronJobs = apartmentManager.getApartmentCronJobsForRoom(apartment, roomToChange);
            boolean cronJobEqual = false;
            boolean dayListSameSize = false;

            for (ApartmentCronJob apartmentCronJob : apartmentCronJobs) {
                cronJobEqual = false;
                dayListSameSize = false;
                if(apartmentCronJob.getDays().size() == checkedDays.size())
                    dayListSameSize = true;
                else
                    dayListSameSize = false;

                if(dayListSameSize){
                    if(apartmentCronJob.getAction().equals(action) && apartmentCronJob.getTime().equals(time) && apartmentCronJob.getDays().containsAll(checkedDays)) {
                        cronJobEqual = true;
                        break;
                    }
                }
                else{
                    cronJobEqual = false;
                }



            }
            if(cronJobEqual)
                return "CRONEXISTS";
            else {
                ApartmentCronJob apartmentCronJob = new ApartmentCronJob();
                apartmentCronJob.setApartment(apartment);
                apartmentCronJob.setRoom(roomToChange);
                apartmentCronJob.setAction(action);
                apartmentCronJob.setDays(checkedDays);
                apartmentCronJob.setTime(time);
                apartmentManager.save(apartmentCronJob);
                return "SUCCESS";
            }

        } catch (Exception e) {
            return "ERROR";
        }

    }

    @ModelAttribute("mainNavSel")
    public MainNavigationItem getMainNavigationSelection() {
        return MainNavigationItem.USERS;
    }

}
