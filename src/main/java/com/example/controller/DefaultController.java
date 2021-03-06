package com.example.controller;

import java.util.Arrays;
import java.util.List;

import com.example.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DefaultController {
	
	@GetMapping("/")
	public String defaultPage() {
		return "/home";
	}

    @GetMapping("/home")
    public String home() {
        return "/home";
    }

    @GetMapping("/admin")
    public String admin() {
        return "/admin";
    }

    @GetMapping("/user")
    public String user() {
        return "/user";
    }

    @GetMapping("/about")
    public String about() {
        return "/about";
    }

	@GetMapping("/login")
    public ModelAndView login(ModelMap model) {
		ModelAndView mv = new ModelAndView("login");
		List<User> users = Arrays.asList();
		User user = new User();
		user.setFirstName("hiren");
		user.setLastName("pandit");
		user.setUsername("hiren123");
		user.setPassword("hiren123");
		user.setEmail("hiren.pandit@gmail.com");
		mv.addObject("users", users);
    	mv.addObject("message", "hello World!");
    	return mv;
    }

    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }

}
