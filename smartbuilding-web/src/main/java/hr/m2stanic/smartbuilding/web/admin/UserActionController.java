package hr.m2stanic.smartbuilding.web.admin;

import hr.m2stanic.smartbuilding.core.security.Permission;
import hr.m2stanic.smartbuilding.core.security.RoleScope;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hr.m2stanic.smartbuilding.core.action.UserAction;
import hr.m2stanic.smartbuilding.core.action.UserActionManager;
import hr.m2stanic.smartbuilding.core.appuser.AppUser;
import hr.m2stanic.smartbuilding.core.appuser.AppUserManager;
import hr.m2stanic.smartbuilding.core.company.Company;
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


    private Company resolveCompany(Long companyId, AppUser loggedInUser) {
        Company company = null;
        if (loggedInUser.getCompany() instanceof UserGroup) company = loggedInUser.getCompany();
        else if (companyId != null) company = companyManager.getCompany(companyId);
        return company;
    }

    @ModelAttribute("mainNavSel")
    public MainNavigationItem getMainNavigationSelection() {
        return MainNavigationItem.DASHBOARD;
    }

}
