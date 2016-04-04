package hr.m2stanic.smartbuilding.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PingPongController {

    @ResponseBody
    @RequestMapping("/ping")
    public String handlePing() {
        return "PONG";
    }
}
