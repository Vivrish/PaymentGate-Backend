package cz.cvut.fit.tjv.paymentgate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Home page for API
 */
@RestController
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "Welcome to the payment gate API";
    }
}
