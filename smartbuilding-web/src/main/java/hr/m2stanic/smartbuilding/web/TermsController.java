package hr.m2stanic.smartbuilding.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


@Controller
@Slf4j
@SessionAttributes("questionForm")
@PropertySource(value = {"classpath:/smartbuilding.common.properties"})
//@PropertySource(value = {"classpath:/smartbuilding.common.properties", "classpath:/smartbuilding.properties"})
public class TermsController {




    @RequestMapping(value = "/uslovi", method = RequestMethod.GET)
    public String showContact(Model model, RedirectAttributes ra) {

        return "user/terms";
    }


    @ModelAttribute("activeMainNavItem")
    public MainNav activeMainNavItem(HttpSession session) {
        return MainNav.TERMS;
    }

}
