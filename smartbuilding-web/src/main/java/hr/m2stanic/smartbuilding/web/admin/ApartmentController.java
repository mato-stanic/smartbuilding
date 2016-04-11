package hr.m2stanic.smartbuilding.web.admin;

import hr.m2stanic.smartbuilding.core.apartment.Apartment;
import hr.m2stanic.smartbuilding.core.apartment.ApartmentCronJob;
import hr.m2stanic.smartbuilding.core.apartment.ApartmentLayout;
import hr.m2stanic.smartbuilding.core.apartment.ApartmentManager;
import hr.m2stanic.smartbuilding.core.appuser.AppUser;
import hr.m2stanic.smartbuilding.core.appuser.AppUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/user")
public class ApartmentController {

    @Autowired
    private ApartmentManager apartmentManager;

    @Autowired
    private AppUserManager appUserManager;



    @RequestMapping("/apartmentLayout/simple")
    public String getSimple(Model model,
                            @RequestParam Long apartmentId,
                            RedirectAttributes ra) {
        Apartment apartment = apartmentManager.getApartment(apartmentId);
        ApartmentLayout apartmentLayout = apartmentManager.getApartmentRoomStates(apartment);
        model.addAttribute("apartmentLayout", apartmentLayout);
        return "admin/user-actions/apartment/apartmentLayoutSimple";

    }

    @RequestMapping("/apartmentLayout/simpleEdit")
    @ResponseBody
    public String changeApartmentLayout(@RequestParam String roomToChange,
                                        @RequestParam Boolean state, RedirectAttributes ra) {
        try {
            AppUser loggedInUser = appUserManager.getLoggedInUser();
            Apartment apartment = apartmentManager.getApartment(loggedInUser.getApartment().getId());
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
    public String getAdvanced(Model model,
                              @RequestParam Long apartmentId,
                              RedirectAttributes ra) {
        Apartment apartment = apartmentManager.getApartment(apartmentId);
        return "admin/user-actions/apartment/apartmentLayoutAdvanced";

    }

    @RequestMapping("/apartmentLayout/advancedEdit")
    @ResponseBody
    public String changeApartmentLayoutCron(Model model,
                                            @RequestParam String roomToChange,
                                            @RequestParam String action,
                                            @RequestParam List<String> checkedDays,
                                            @RequestParam String time, RedirectAttributes ra) {
        try {
            AppUser loggedInUser = appUserManager.getLoggedInUser();
            Apartment apartment = apartmentManager.getApartment(loggedInUser.getApartment().getId());
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

    @RequestMapping("/apartmentLayout/cronList")
    public String changeApartmentLayoutCron(Model model,
                                            @RequestParam Long apartmentId,
                                            RedirectAttributes ra) {
        try {
            Apartment apartment = apartmentManager.getApartment(apartmentId);
            List<ApartmentCronJob> cronJobs = apartmentManager.getApartmentCronJobs(apartment);
            modifyCronJobs(model, cronJobs);
            return "admin/user-actions/apartment/apartmentCronList";

        } catch (Exception e) {
            return "ERROR";
        }

    }

    @ModelAttribute("mainNavSel")
    public MainNavigationItem getMainNavigationSelection() {
        return MainNavigationItem.USERS;
    }


    private void modifyCronJobs(Model model, List<ApartmentCronJob> cronJobs) {
        for (ApartmentCronJob cronJob : cronJobs) {
            String room = cronJob.getRoom();
            switch (room) {
                case "livingroom":
                    cronJob.setRoom("Dnevna soba");
                    break;
                case "kitchen":
                    cronJob.setRoom("Kuhinja");
                    break;
                case "bathroom":
                    cronJob.setRoom("Kupaonica");
                    break;
                case "bedroom":
                    cronJob.setRoom("Spavaća soba");
                    break;
                case "hallway":
                    cronJob.setRoom("Hodnik");
                    break;
            }

            String action = cronJob.getAction();
            switch (action) {
                case "turnOn":
                    cronJob.setAction("Upali");
                    break;
                case "turnOff":
                    cronJob.setAction("Ugasi");
                    break;
            }
            List<String> days = cronJob.getDays();
            List<String> correctedDays = new ArrayList<>();
            for (String day : days) {
                switch (day) {
                    case "monday":
                        correctedDays.add("Ponedjeljak");
                        break;
                    case "tuesday":
                        correctedDays.add("Utorak");
                        break;
                    case "wednesday":
                        correctedDays.add("Srijeda");
                        break;
                    case "thursday":
                        correctedDays.add("Četvrtak");
                        break;
                    case "friday":
                        correctedDays.add("Petak");
                        break;
                    case "saturday":
                        correctedDays.add("Subota");
                        break;
                    case "sunday":
                        correctedDays.add("Nedjelja");
                        break;
                }
            }
            cronJob.setDays(correctedDays);
            model.addAttribute("cronJobs", cronJobs);
        }
    }

}
