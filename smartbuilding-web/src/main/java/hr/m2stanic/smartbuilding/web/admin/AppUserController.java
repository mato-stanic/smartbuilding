package hr.m2stanic.smartbuilding.web.admin;

import hr.m2stanic.smartbuilding.core.action.*;
import hr.m2stanic.smartbuilding.core.apartment.Admin;
import hr.m2stanic.smartbuilding.core.apartment.Apartment;
import hr.m2stanic.smartbuilding.core.apartment.ApartmentManager;
import hr.m2stanic.smartbuilding.core.appuser.AppUser;
import hr.m2stanic.smartbuilding.core.appuser.AppUserManager;
import hr.m2stanic.smartbuilding.core.security.Role;
import hr.m2stanic.smartbuilding.core.security.RoleManager;
import hr.m2stanic.smartbuilding.core.security.RoleScope;
import hr.m2stanic.smartbuilding.web.dto.DTOUtil;
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
    private ApartmentManager apartmentManager;

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private UserActionManager userActionManager;


    @RequestMapping("/list")
    public String listUsers(Model model, Long apartmentId) {

        List<AppUser> appUsers = getUsers(apartmentId);
        if (apartmentId != null) model.addAttribute("apartment", apartmentManager.getApartment(apartmentId));
        model.addAttribute("userList", appUsers);
        return "admin/user/user-list";
    }


    private List<AppUser> getUsers(@RequestParam(required = false) Long apartmentId) {
        return apartmentId != null ? appUserManager.getApartmentUsers(apartmentId) : appUserManager.getAllUsers();
    }


    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String showUserForm(Model model, @RequestParam(required = false) Long id, @RequestParam(required = false) Long apartmentId) {

        UserDTO userDTO = getUser(id, apartmentId);
        fillEditUserModel(model, apartmentId, userDTO);
        return "admin/user/user-form";
    }


    private void fillEditUserModel(Model model, Long apartmentId, UserDTO userDTO) {

        model.addAttribute("userDTO", userDTO);
        List<AppUser> appUsers = getUsers(apartmentId);
        model.addAttribute("userList", appUsers);

        Apartment apartment = apartmentId != null ? apartmentManager.getApartment(apartmentId) : null;
        if (apartment != null) model.addAttribute("company", apartment);

        model.addAttribute("roles", roleManager.getRoles(apartment != null && apartment instanceof Admin ? RoleScope.ADMIN : RoleScope.TENANT));
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String processUserForm(Model model, @Valid @ModelAttribute UserDTO userDTO, BindingResult result,
                                  @RequestParam(required = false) Long apartmentId) {

        if (result.hasErrors()) {
            fillEditUserModel(model, apartmentId, userDTO);
            return "admin/user/user-form";
        }
        try {
            AppUser loggedInUser = appUserManager.getLoggedInUser();
            AppUser appUser = DTOUtil.fromDTO(userDTO);
            boolean isNew = appUser.getId() == null;
            appUser.setRole(getDefaultRole(appUser.getApartment() instanceof Admin ? RoleScope.ADMIN : RoleScope.TENANT));
            appUser = appUserManager.save(appUser);
            UserAction action = isNew ? new AppUserAddedAction(loggedInUser, appUser) : new AppUserUpdatedAction(loggedInUser, appUser);
            userActionManager.save(action);
            if (loggedInUser.getRole().getScope().equals(RoleScope.ADMIN))
                return "redirect:/admin/user/list?apartmentId=" + appUser.getApartment().getId();
            else
                return "redirect:/admin/user/list?apartmentId=" + loggedInUser.getApartment().getId();

        } catch (DataIntegrityViolationException daoe) {
            result.addError(new ObjectError("name", "User sa istim korisnickim imenom već postoji!"));
            fillEditUserModel(model, apartmentId, userDTO);
            return "admin/user/user-form";
        }
    }


    @RequestMapping("/delete")
    public String deleteUser(@RequestParam Long id, @RequestParam(required = false) Long apartmentId, RedirectAttributes ra) {
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
        return "redirect:/admin/user/list" + (apartmentId != null ? "?apartmentId=" + apartmentId : "");
    }


    @RequestMapping("/update-acc")
    @ResponseBody
    public String updateEmailAndPassword(String email, String password) {
        try {
            AppUser loggedInUser = appUserManager.getLoggedInUser();
            loggedInUser.setEmail(email);
            loggedInUser.setPassword(password);
            appUserManager.save(loggedInUser);

            UserAction userAction = new AppUserUpdatedAction(loggedInUser, loggedInUser);
            userActionManager.save(userAction);
            return "SUCCESS";

        } catch (Exception e) {
           log.error("Failed to change email and/or password!", e);
            return "Greška prilikom promjene email adrese i/ili passworda!";
        }

    }




    public UserDTO getUser(Long id, Long apartmentId) {
        AppUser appUser = id != null ? appUserManager.getUser(id) : new AppUser();
        if (appUser.getId() == null && apartmentId != null) {
            Apartment apartment = apartmentManager.getApartment(apartmentId);
            appUser.setApartment(apartment);
            appUser.setRole(getDefaultRole(RoleScope.TENANT));
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
