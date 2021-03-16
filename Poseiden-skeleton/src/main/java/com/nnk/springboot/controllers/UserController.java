package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.IUserDalService;
import com.nnk.springboot.web.dto.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping("/user/")
public class UserController {

    private IUserDalService userService;

    public UserController(IUserDalService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String home(Model model)
    {
        Collection<UserDTO> userList =  userService.findAll();
        model.addAttribute("users", userList);
        return "user/list";
    }

    @GetMapping(value = "add")
    public String addUser(UserDTO user, Model model) {
        model.addAttribute("user", new UserDTO());
        return "user/add";
    }

    @PostMapping(value = "validate")
    public String validate(@Valid @ModelAttribute("user") UserDTO user,
                           BindingResult result,
                           Model model) {
        if (!result.hasErrors()) {
            userService.create(user);
            model.addAttribute("users", userService.findAll());
            return "user/list";
        }
        return "user/add";
    }

    @GetMapping(value = "update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        UserDTO user = userService.findOne(id);
        user.setPassword("");
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        return "user/update";
    }

    @PostMapping(value = "update/{id}")
    public String updateUser(@PathVariable("id") Integer id,
                             @Valid @ModelAttribute("user") UserDTO user,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            return "user/update";
        }
        userService.update(user, id);
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    @RolesAllowed("ADMIN")
    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        userService.delete(id);
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }
}

//https://www.baeldung.com/spring-mvc-and-the-modelattribute-annotation
//https://www.codejava.net/frameworks/spring-boot/spring-boot-form-validation-tutorial
//https://asbnotebook.com/2020/04/11/spring-boot-thymeleaf-form-validation-example/
//https://stackabuse.com/spring-boot-thymeleaf-form-data-validation-with-bean-validator/

//https://mkyong.com/spring-boot/spring-rest-spring-security-example/