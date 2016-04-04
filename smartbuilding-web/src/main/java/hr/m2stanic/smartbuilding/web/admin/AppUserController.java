package hr.m2stanic.smartbuilding.web.admin;

import hr.m2stanic.smartbuilding.core.company.Admin;
import hr.m2stanic.smartbuilding.web.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hr.m2stanic.smartbuilding.core.action.*;
import hr.m2stanic.smartbuilding.core.appuser.AppUser;
import hr.m2stanic.smartbuilding.core.appuser.AppUserManager;
import hr.m2stanic.smartbuilding.core.company.Company;
import hr.m2stanic.smartbuilding.core.company.CompanyManager;
import hr.m2stanic.smartbuilding.core.security.Role;
import hr.m2stanic.smartbuilding.core.security.RoleManager;
import hr.m2stanic.smartbuilding.core.security.RoleScope;
import hr.m2stanic.smartbuilding.web.dto.DTOUtil;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/user")
@SessionAttributes("userDTO")
public class AppUserController {


    @Autowired
    private AppUserManager appUserManager;

    @Autowired
    private CompanyManager companyManager;

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private UserActionManager userActionManager;


    @RequestMapping("/list")
    public String listUsers(Model model, Long apartmentId) {

        List<AppUser> appUsers = getUsers(apartmentId);
        if (apartmentId != null) model.addAttribute("apartment", companyManager.getCompany(apartmentId));
        model.addAttribute("userList", appUsers);
        return "admin/user/user-list";
    }


    private List<AppUser> getUsers(@RequestParam(required = false) Long companyId) {
        return companyId != null ? appUserManager.getCompanyUsers(companyId) : appUserManager.getAllUsers();
    }


    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String showUserForm(Model model, @RequestParam(required = false) Long id, @RequestParam(required = false) Long companyId) {

        UserDTO userDTO = getUser(id, companyId);
        fillEditUserModel(model, companyId, userDTO);
        return "admin/user/user-form";
    }


    private void fillEditUserModel(Model model, Long companyId, UserDTO userDTO) {

        model.addAttribute("userDTO", userDTO);
        List<AppUser> appUsers = getUsers(companyId);
        model.addAttribute("userList", appUsers);

        Company company = companyId != null ? companyManager.getCompany(companyId) : null;
        if (company != null) model.addAttribute("company", company);

        model.addAttribute("roles", roleManager.getRoles(company != null && company instanceof Admin ? RoleScope.ADMIN : RoleScope.OPERATOR));
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String processUserForm(Model model, @Valid @ModelAttribute UserDTO userDTO, BindingResult result,
                                  @RequestParam(required = false) Long companyId) {

        if (result.hasErrors()) {
            fillEditUserModel(model, companyId, userDTO);
            return "admin/user/user-form";
        }
        try {
            AppUser loggedInUser = appUserManager.getLoggedInUser();
            AppUser appUser = DTOUtil.fromDTO(userDTO);
            boolean isNew = appUser.getId() == null;
            appUser.setRole(getDefaultRole(appUser.getCompany() instanceof Admin ? RoleScope.ADMIN : RoleScope.OPERATOR));
            appUser = appUserManager.save(appUser);
            UserAction action = isNew ? new AppUserAddedAction(loggedInUser, appUser) : new AppUserUpdatedAction(loggedInUser, appUser);
            userActionManager.save(action);

            return "redirect:/admin/user/list?companyId=" + appUser.getCompany().getId();
        } catch (DataIntegrityViolationException daoe) {
            result.addError(new ObjectError("name", "User sa istim korisnickim imenom već postoji!"));
            fillEditUserModel(model, companyId, userDTO);
            return "admin/user/user-form";
        }
    }


    @RequestMapping("/delete")
    public String deleteUser(@RequestParam Long id, @RequestParam(required = false) Long companyId, RedirectAttributes ra) {
        try {
            AppUser loggedInUser = appUserManager.getLoggedInUser();
            AppUser appUser = appUserManager.getUser(id);
            if (appUser != null) {
                appUserManager.delete(id);
                UserAction action = new AppUserDeletedAction(loggedInUser, appUser);
                userActionManager.save(action);

            }
        } catch (Exception e) {
            ra.addFlashAttribute("flashMsg", "Ne mogu brisati korisnika jer postoje vezane tarife ili paketi!");
        }
        return "redirect:/admin/user/list" + (companyId != null ? "?companyId=" + companyId : "");
    }


    @RequestMapping("/update-acc")
    @ResponseBody
    public String updateEmailAndPassword(String email, String password) {
        try {
            AppUser loggedInUser = appUserManager.getLoggedInUser();
            loggedInUser.setEmail(email);
            loggedInUser.setPassword(password);
            appUserManager.save(loggedInUser);
            return "SUCCESS";

        } catch (Exception e) {
           log.error("Failed to change email and/or password!", e);
            return "Greška prilikom promjene email adrese i/ili passworda!";
        }

    }




    public UserDTO getUser(Long id, Long companyId) {
        AppUser appUser = id != null ? appUserManager.getUser(id) : new AppUser();
        if (appUser.getId() == null && companyId != null) {
            Company company = companyManager.getCompany(companyId);
            appUser.setCompany(company);
            appUser.setRole(getDefaultRole(RoleScope.OPERATOR));
        }
        return DTOUtil.toDTO(appUser);
    }

    private Role getDefaultRole(RoleScope scope) {
        List<Role> roles = roleManager.getRoles(scope);
        return roles.size() > 0 ? roles.get(0) : null;
    }


    @ModelAttribute("mainNavSel")
    public MainNavigationItem getMainNavigationSelection() {
        return MainNavigationItem.USERS;
    }

}
