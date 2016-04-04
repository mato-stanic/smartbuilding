package hr.m2stanic.smartbuilding.web.admin;

import hr.m2stanic.smartbuilding.core.company.Apartment;
import hr.m2stanic.smartbuilding.core.security.RoleScope;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.m2stanic.smartbuilding.core.action.UserActionManager;
import hr.m2stanic.smartbuilding.core.appuser.AppUser;
import hr.m2stanic.smartbuilding.core.appuser.AppUserManager;
import hr.m2stanic.smartbuilding.core.company.CompanyManager;
import hr.m2stanic.smartbuilding.core.company.UserGroup;

import java.util.List;

@Controller
public class UserActionController {

    @Autowired
    private UserActionManager userActionManager;

    @Autowired
    private AppUserManager appUserManager;

    @Autowired
    private CompanyManager companyManager;


    @RequestMapping(value = {"/admin/", "/admin"})
    public String listUserActions(Model model,
                                  @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {


        AppUser loggedInUser = appUserManager.getLoggedInUser();
        loggedInUser.setLastLogin(new LocalDateTime());
        appUserManager.save(loggedInUser);
        if(loggedInUser.getRole().getScope().equals(RoleScope.ADMIN))
        {
            List<AppUser> userActivity = appUserManager.getAllUsersNotAdmin();
            model.addAttribute("userActivity", userActivity);
            return "admin/user-actions/action-list";
        }
        else
        {
            return "admin/user-actions/apt-layout";
        }


    }


    private Apartment resolveApartment(Long apartmentId, AppUser loggedInUser) {
        Apartment apartment = null;
        if (loggedInUser.getApartment() instanceof UserGroup) apartment = loggedInUser.getApartment();
        else if (apartmentId != null) apartment = companyManager.getApartment(apartmentId);
        return apartment;
    }

    @ModelAttribute("mainNavSel")
    public MainNavigationItem getMainNavigationSelection() {
        return MainNavigationItem.DASHBOARD;
    }

}
