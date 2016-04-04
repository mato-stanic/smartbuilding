package hr.m2stanic.smartbuilding.web.admin;


import hr.m2stanic.smartbuilding.web.thymeleaf.Layout;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hr.m2stanic.smartbuilding.core.appuser.AppUserManager;

@Controller
@RequestMapping(value = "/admin")
public class LoginController {

    @Autowired
    private AppUserManager appUserManager;

    @RequestMapping(value = "/login")
    @Layout("admin/layouts/empty")
    public String login(RedirectAttributes ra) {
        return appUserManager.getLoggedInUser() != null ? "redirect:/admin/" : "admin/login";
    }


    @RequestMapping("/logout")
    public String logout(RedirectAttributes ra) {
        SecurityUtils.getSubject().logout();
        return "redirect:/admin/";
    }

}
