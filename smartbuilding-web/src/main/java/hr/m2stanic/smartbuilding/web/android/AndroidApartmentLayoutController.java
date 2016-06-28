package hr.m2stanic.smartbuilding.web.android;


import hr.m2stanic.smartbuilding.core.apartment.Apartment;
import hr.m2stanic.smartbuilding.core.apartment.ApartmentCronJob;
import hr.m2stanic.smartbuilding.core.apartment.ApartmentLayout;
import hr.m2stanic.smartbuilding.core.apartment.ApartmentManager;
import hr.m2stanic.smartbuilding.core.appuser.AppUser;
import hr.m2stanic.smartbuilding.core.appuser.AppUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(value = "/android/admin")
@Slf4j
public class AndroidApartmentLayoutController {

    @Autowired
    private AppUserManager appUserManager;

    @Autowired
    private ApartmentManager apartmentManager;


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

    @RequestMapping(value = "/apartmentLayout/editSimple", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ApartmentLayout> apartmentLayoutEdit(@RequestParam String apartmentId,
                                                               @RequestParam String roomToChange,
                                                               @RequestParam String state,
                                                           RedirectAttributes ra) {

        log.info("post request to change room state for apartment with id = {}", apartmentId);


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
                case "motionDetection":
                    apartmentLayout.setMotionDetection(st);
                    break;
            }
            apartmentManager.save(apartmentLayout);
            return new ResponseEntity<>(apartmentLayout, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/apartmentLayout/editAdvanced", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ApartmentCronJob> apartmentLayoutEditAdvanced(@RequestParam String apartmentId,
                                                               @RequestParam String roomToChange,
                                                               @RequestParam String action,
                                                               @RequestParam List<String> days,
                                                               @RequestParam String time,
                                                               RedirectAttributes ra) {

        log.info("post request to change room state for apartment with id = {}", apartmentId);

        try {
            Apartment apartment = apartmentManager.getApartment(Long.valueOf(apartmentId));
            List<ApartmentCronJob> apartmentCronJobs = apartmentManager.getApartmentCronJobsForRoom(apartment, roomToChange);
            boolean cronJobEqual = false;
            boolean dayListSameSize = false;

            for (ApartmentCronJob apartmentCronJob : apartmentCronJobs) {
                cronJobEqual = false;
                dayListSameSize = false;
                if(apartmentCronJob.getDays().size() == days.size())
                    dayListSameSize = true;
                else
                    dayListSameSize = false;

                if(dayListSameSize){
                    if(apartmentCronJob.getAction().equals(action) && apartmentCronJob.getTime().equals(time) && apartmentCronJob.getDays().containsAll(days)) {
                        cronJobEqual = true;
                        break;
                    }
                }
                else{
                    cronJobEqual = false;
                }
            }
            if(cronJobEqual)
                return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
            else {
                ApartmentCronJob apartmentCronJob = new ApartmentCronJob();
                apartmentCronJob.setApartment(apartment);
                apartmentCronJob.setRoom(roomToChange);
                apartmentCronJob.setAction(action);
                apartmentCronJob.setDays(days);
                apartmentCronJob.setTime(time);
                apartmentManager.save(apartmentCronJob);
                return new ResponseEntity<>(apartmentCronJob, HttpStatus.OK);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/apartmentLayout/cronList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ApartmentCronJob>> changeApartmentLayoutCron(@RequestParam String apartmentId,
                                            RedirectAttributes ra) {
        try {
            Apartment apartment = apartmentManager.getApartment(Long.valueOf(apartmentId));
            List<ApartmentCronJob> cronJobs = apartmentManager.getApartmentCronJobs(apartment);
            if(cronJobs.size() != 0){
                return new ResponseEntity<>(cronJobs, HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            log.error("Error fetching cron jobs for apartment {}, android request", apartmentId);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/apartmentLayout/deleteCron", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteApartmentCron(@RequestParam String cronJobId, RedirectAttributes ra) {
        try {
            ApartmentCronJob cronJob = apartmentManager.getAparatmentCronJob(Long.valueOf(cronJobId));
            log.info("Request to delete cron job {}, with id: {}", cronJob, cronJobId);

            if(cronJob != null){
                apartmentManager.deleteCronJob(Long.valueOf(cronJobId));
                return new ResponseEntity(HttpStatus.OK);
            }
            else
                return new ResponseEntity(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            log.error("Error fetching cron jobs for apartment {}, android request", cronJobId);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }




}
