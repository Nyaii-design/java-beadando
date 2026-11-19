package hu.nje.beadando;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FooldalController {

    @GetMapping("/")
    public String fooldal() {
        return "index";
    }
}
