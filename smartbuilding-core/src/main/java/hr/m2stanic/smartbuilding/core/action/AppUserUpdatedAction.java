package hr.m2stanic.smartbuilding.core.action;

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
@DiscriminatorValue("APP_USER_UPDATED")
public class AppUserUpdatedAction extends UserAction {

    public AppUserUpdatedAction(AppUser loggedInUser, AppUser appUser) {
        super(null, LocalDateTime.now(), loggedInUser.getId(), loggedInUser.getFullName(), loggedInUser.getApartment().getId(), loggedInUser.getApartment().getName(), ReferencedEntityType.APP_USER, appUser.getId(), appUser.getFullName());
    }

    @Override
    public String getDescription() {
        return "Ažuriran korisnik: " + getReferenceName();
    }
}