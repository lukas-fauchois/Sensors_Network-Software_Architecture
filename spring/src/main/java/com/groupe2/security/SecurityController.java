package com.groupe2.security;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class SecurityController {
    //login page
    @GetMapping(value="/login")
    public String login() {
        return("login");
    }

    //403 access denied page
    @GetMapping(value = "/403")
    public Model accesssDenied(Model model, Principal user) {
        return model;

    }
}
