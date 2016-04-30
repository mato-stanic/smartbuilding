package hr.m2stanic.smartbuilding.web;


import hr.m2stanic.smartbuilding.web.admin.MainNavigationItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping({"/"})
@SessionAttributes({"consumption", "result"})
public class CalculationController {


    public static final String TERMS_AGREED_KEY = "TERMS_AGREED_KEY";


    @RequestMapping("/")
    public String processStep1(Model model, HttpServletRequest request , RedirectAttributes ra,
                               @RequestParam(required = false) MenuOption menuOption ) {
        log.info("blaa log");
        if(menuOption == null || menuOption.equals(MenuOption.HOME))
            return "public/home";
        else if(menuOption.equals(MenuOption.HOW))
            return "public/how";
        else if(menuOption.equals(MenuOption.CONTACT))
            return "redirect:/kontakt";

        return "public/home";
    }







    @ModelAttribute("mainNavSel")
    public MainNavigationItem getMainNavigationSelection() {
        return MainNavigationItem.INDEX;
    }



}
