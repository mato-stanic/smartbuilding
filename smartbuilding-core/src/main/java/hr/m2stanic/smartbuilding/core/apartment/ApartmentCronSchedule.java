package hr.m2stanic.smartbuilding.core.apartment;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mato on 26.04.16..
 */
@Slf4j
@EnableScheduling
@Service
public class ApartmentCronSchedule implements CronProcessor{

    @Autowired
    private DefaultApartmentManager apartmentManager;


    //* */1 * * * * -> old one working
    @Scheduled(cron = "0 0/1 * * * ?")
    public void checkApartmentCrons() {


        String currentDay="";
        String time = "";
        Calendar calendar = Calendar.getInstance();
        time = new SimpleDateFormat("HH:mm").format(calendar.getTime());
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        log.info("Check cron task for apartments {}", new SimpleDateFormat("HH:mm:ss").format(calendar.getTime()));
        switch (day) {
            case Calendar.MONDAY:currentDay="monday";break;
            case Calendar.TUESDAY:currentDay="tuesday";break;
            case Calendar.WEDNESDAY:currentDay="wednesday";break;
            case Calendar.THURSDAY:currentDay="thursday";break;
            case Calendar.FRIDAY:currentDay="friday";break;
            case Calendar.SATURDAY:currentDay="saturday";break;
            case Calendar.SUNDAY:currentDay="sunday";break;
        }

        List<ApartmentCronJob> apartmentCronJobs = apartmentManager.getAllCronJobs();
        for (ApartmentCronJob apartmentCronJob : apartmentCronJobs) {
            List<String> daysList = apartmentCronJob.getDays();
            if(daysList.contains(currentDay)){
                if(apartmentCronJob.getTime().equals(time))
                {
                    ApartmentLayout apartmentLayout = apartmentManager.getApartmentRoomStates(apartmentCronJob.getApartment());
                    Boolean state;
                    if(apartmentCronJob.getAction().equals("turnOn"))
                        state = true;
                    else
                        state = false;

                    switch (apartmentCronJob.getRoom()) {
                        case "livingroom":
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
                }
            }
        }
    }
}
