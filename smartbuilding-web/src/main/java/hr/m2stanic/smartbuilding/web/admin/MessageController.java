package hr.m2stanic.smartbuilding.web.admin;


import hr.m2stanic.smartbuilding.core.apartment.Admin;
import hr.m2stanic.smartbuilding.core.apartment.Apartment;
import hr.m2stanic.smartbuilding.core.apartment.ApartmentManager;
import hr.m2stanic.smartbuilding.core.appuser.AppUser;
import hr.m2stanic.smartbuilding.core.appuser.AppUserManager;
import hr.m2stanic.smartbuilding.core.messages.Message;
import hr.m2stanic.smartbuilding.core.messages.MessageManager;
import hr.m2stanic.smartbuilding.web.thymeleaf.Layout;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Slf4j
@Controller
@RequestMapping("/admin/messages")
public class MessageController {

    @Autowired
    private MessageManager messageManager;

    @Autowired
    private AppUserManager appUserManager;

    @Autowired
    private ApartmentManager apartmentManager;

    @RequestMapping("/list")
    public String showMessages(Model model, @PageableDefault(size = 20) Pageable pageable) {

        AppUser loggedInUser = appUserManager.getLoggedInUser();

        List<? extends Apartment> recipients = loggedInUser.getApartment() instanceof Admin ? apartmentManager.getAllTenantApartments() : apartmentManager.getAllApartments();
        model.addAttribute("recipients", recipients);

        Page<Message> receivedMessages = messageManager.getReceivedMessages(loggedInUser.getApartment(), pageable);
        model.addAttribute("receivedMessages", receivedMessages);

        Page<Message> sentMessages = messageManager.getSentMessages(loggedInUser.getApartment(), pageable);
        model.addAttribute("sentMessages", sentMessages);

        return "admin/messages/message-list";
    }


    @RequestMapping("/received")
    @Layout("admin/layouts/empty-content")
    public String getReceivedMessages(Model model, @PageableDefault(size = 20) Pageable pageable) {

        AppUser loggedInUser = appUserManager.getLoggedInUser();
        Page<Message> receivedMessages = messageManager.getReceivedMessages(loggedInUser.getApartment(), pageable);
        model.addAttribute("receivedMessages", receivedMessages);
        return "admin/messages/received";
    }


    @RequestMapping("/sent")
    @Layout("admin/layouts/empty-content")
    public String getSentMessages(Model model, @PageableDefault(size = 20) Pageable pageable) {

        AppUser loggedInUser = appUserManager.getLoggedInUser();
        Page<Message> sentMessages = messageManager.getSentMessages(loggedInUser.getApartment(), pageable);
        model.addAttribute("sentMessages", sentMessages);
        return "admin/messages/sent";
    }


    @RequestMapping("/send")
    @ResponseBody
    public String sendMessage(@RequestParam String title, @RequestParam String body, @RequestParam Long recipientId) {

        AppUser loggedInUser = appUserManager.getLoggedInUser();
        try {
            Apartment recipient = apartmentManager.getApartment(recipientId);
            Message message = new Message(null, LocalDateTime.now(), title, body, recipient, loggedInUser, false);
            messageManager.save(message);
            return "SUCCESS";
        } catch (Exception e) {
            log.error("Failed to save message", e);
            return e.getMessage();
        }
    }

    @RequestMapping("/mark-read")
    @ResponseBody
    public String markMessageRead(@RequestParam Long msgId) {

        try {
            AppUser loggedInUser = appUserManager.getLoggedInUser();
            Message message = messageManager.getMessage(msgId);
            if (loggedInUser.getApartment().getId().equals(message.getRecipient().getId())) {
                if (!message.isRead()) {
                    message.setRead(true);
                    messageManager.save(message);
                }
            }
            return "SUCCESS";
        } catch (Exception e) {
            log.error("Failed to mark message read", e);
            return e.getMessage();
        }
    }


    @ModelAttribute("mainNavSel")
    public MainNavigationItem getMainNavigationSelection() {
        return MainNavigationItem.MESSAGES;
    }

}
