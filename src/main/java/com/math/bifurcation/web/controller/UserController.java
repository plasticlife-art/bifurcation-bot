package com.math.bifurcation.web.controller;

import com.math.bifurcation.data.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Leonid Cheremshantsev
 */
@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("count", String.valueOf(userRepository.getCount()));
        model.addAttribute("users", userRepository.findAll());
        return "count";
    }
}
