package hr.m2stanic.smartbuilding.core.action;

import hr.m2stanic.smartbuilding.core.apartment.ApartmentCronJob;
import hr.m2stanic.smartbuilding.core.appuser.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDateTime;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("APARTMENT_CRON_DELETED")
public class ApartmentCronJobDeletedAction extends UserAction {


    public ApartmentCronJobDeletedAction(AppUser loggedInUser, ApartmentCronJob apartmentCronJob) {
        super(null, LocalDateTime.now(), loggedInUser.getId(), loggedInUser.getUsername(), loggedInUser.getApartment().getId(), loggedInUser.getFullName(), ReferencedEntityType.CRON_JOB, apartmentCronJob.getId(), "");
    }

    @Override
    public String getDescription() {
        return "Izbrisan apartment cron job: " + getReferenceName();
    }
}