package hr.m2stanic.smartbuilding.web;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.captcha.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.context.SpringWebContext;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Locale;


@Controller
@Slf4j
@SessionAttributes("questionForm")
@PropertySource(value = {"classpath:/smartbuilding.common.properties"})
//@PropertySource(value = {"classpath:/smartbuilding.common.properties", "classpath:/smartbuilding.properties"})
public class ContactController {


    @Autowired
    JavaMailSender mailSender;

    @Value("${mail.sender.address}")
    String emailSenderAddress;

    @Value("${mail.sender.name}")
    String emailSenderName;

    @Value("${contact.question.recipients}")
    String questionRecipients;

    @Autowired
    private SpringTemplateEngine templateEngine;


    @Autowired
    private ApplicationContext applicationContext;


    @RequestMapping(value = "/kontakt", method = RequestMethod.GET)
    public String showContact(Model model, RedirectAttributes ra) {

        QuestionForm questionForm = new QuestionForm();
        model.addAttribute("questionForm", questionForm);
        return "user/contact";
    }

    @RequestMapping(value = "/kontakt", method = RequestMethod.POST)
    public String sendMessage(Model model, @ModelAttribute("questionForm") @Valid QuestionForm questionForm, BindingResult result, HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra) {


        try {
            Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
            if (captcha == null || questionForm.captchaAnswer == null || !captcha.isCorrect(questionForm.captchaAnswer)) {
                result.addError(new FieldError("questionForm", "captchaAnswer", "Netočan kod"));
            }

            if (result.hasErrors()) {
                model.addAttribute("questionForm", questionForm);
                return "user/contact";
            }


            final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            final MimeMessageHelper message =
                    new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart

            message.setSubject("Pitanje korisnika sa smartbuilding");

            InternetAddress fromAddress = new InternetAddress(emailSenderAddress, emailSenderName);
            message.setFrom(fromAddress);
            message.setTo(questionRecipients.split(","));

            SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(), Locale.getDefault(), new HashMap<>(), applicationContext);

            ctx.setVariable("fullName", questionForm.fullName);
            ctx.setVariable("phoneNo", questionForm.phoneNo);
            ctx.setVariable("email", questionForm.email);
            ctx.setVariable("question", questionForm.question);
            final String htmlContent = templateEngine.process("email/user-question", ctx);

            message.setText(htmlContent, true); // true = isHtml

            mailSender.send(mimeMessage);
            ra.addFlashAttribute("flashMsg", "Dobili smo Vaše pitanje. Odgovor će biti poslan na email adresu koju ste upisali. Hvala!");
            return "redirect:/kontakt";
        } catch (Exception e) {
            log.error("Failed to send email!", e);
            ra.addFlashAttribute("flashMsg", "Nažalost, desila se greška prilikom slanja pitanja, pokušajte kasnije.");
            model.addAttribute("questionForm", questionForm);
            return "user/contact";
        }


    }


    @Getter
    @Setter
    class QuestionForm {

        private String fullName;
        private String phoneNo;

        @NotNull(message = "Morate unijeti email adresu")
        private String email;

        @NotNull(message = "Morate unijeti pitanje")
        private String question;

        private String captchaAnswer;
    }

    @ModelAttribute("activeMainNavItem")
    public MainNav activeMainNavItem(HttpSession session) {
        return MainNav.CONTACT;
    }



}
