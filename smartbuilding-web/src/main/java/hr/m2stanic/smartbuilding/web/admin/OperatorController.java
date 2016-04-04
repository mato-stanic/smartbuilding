package hr.m2stanic.smartbuilding.web.admin;

import hr.m2stanic.smartbuilding.core.action.*;
import hr.m2stanic.smartbuilding.core.appuser.AppUser;
import hr.m2stanic.smartbuilding.core.appuser.AppUserManager;
import hr.m2stanic.smartbuilding.web.dto.DTOUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/operator")
@SessionAttributes("ms")
public class OperatorController {


    @Autowired
    private AppUserManager userManager;

    @Autowired
    private UserActionManager userActionManager;


    @RequestMapping("/list")
    public String listOpearators(Model model) {

//        List<Operator> operators = operatorManager.getAllOperators();
//        model.addAttribute("operators", operators);
        return "admin/operator/operator-list";
    }



}
