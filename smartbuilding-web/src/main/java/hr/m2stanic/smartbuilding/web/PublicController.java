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
public class PublicController {


    public static final String TERMS_AGREED_KEY = "TERMS_AGREED_KEY";


    @RequestMapping("/")
    public String indexPage(Model model, HttpServletRequest request , RedirectAttributes ra,
                               @RequestParam(required = false) MenuOption menuOption ) {
        if(menuOption == null || menuOption.equals(MenuOption.HOME))
            return "redirect:/home";
        else if(menuOption.equals(MenuOption.HOW))
            return "redirect:/how";
        else if(menuOption.equals(MenuOption.CONTACT))
            return "redirect:/kontakt";

        return "public/home";
    }

    @RequestMapping("/home")
    public String indexHomePage(Model model, HttpServletRequest request , RedirectAttributes ra,
                               @RequestParam(required = false) MenuOption menuOption ) {
        return "public/home";
    }

    @RequestMapping("/how")
    public String indexHowPage(Model model, HttpServletRequest request , RedirectAttributes ra,
                                @RequestParam(required = false) MenuOption menuOption ) {
        return "public/how";
    }







    @ModelAttribute("mainNavSel")
    public MainNavigationItem getMainNavigationSelection() {
        return MainNavigationItem.INDEX;
    }



}
