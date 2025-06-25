package myProject.Traineeship_controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    public HomeController() {
        System.out.println(">>> HomeController activated");
    }

    @GetMapping("/")
    public String home() {
        System.out.println(">>> Home endpoint called");
        return "homepage";
    }
}
