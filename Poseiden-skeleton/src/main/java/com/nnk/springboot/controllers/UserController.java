package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.IUserDalService;
import com.nnk.springboot.web.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collection;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Api(tags = {"User Controller"})
@Tag(name = "User Controller", description = "Public and Private Resources")
@Slf4j
@Controller
@RequestMapping("/user/")
public class UserController {

    private IUserDalService userService;

    public UserController(IUserDalService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Public: Get List of User")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String home(Model model, HttpServletRequest request, HttpServletResponse response)
    {
        Collection<UserDTO> userList =  userService.findAll();
        model.addAttribute("users", userList);
        log.info("Get User List on URI: '{}' : RESPONSE STATUS: '{}'",
                request.getRequestURI(),
                response.getStatus());
        return "user/list";
    }

    @ApiOperation(value = "Public: Go to Creation User Form")
    @GetMapping(value = "add")
    public String addUser(UserDTO user, Model model,
                          HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("user", new UserDTO());
        log.info("Go to Creation User Form on URI: '{}' : RESPONSE STATUS: '{}'",
                request.getRequestURI(),
                response.getStatus());
        return "user/add";
    }

    @ApiOperation(value = "Public: Validate User Creation")
    @PostMapping(value = "validate")
    public String validate(@Valid @ModelAttribute("user") UserDTO user,
                           BindingResult result,
                           Model model,
                           HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            log.warn("User Creation Error on URI: '{}': Error Field(s): '{}' : RESPONSE STATUS: '{}'",
                    request.getRequestURI(),
                    result.getFieldErrors().stream()
                            .map(e-> e.getField().toUpperCase())
                            .distinct()
                            .collect(Collectors.joining(", ")),
                    response.getStatus());
            return "user/add";
        }
        User userCreated = userService.create(user);
        log.info("User Creation on URI: '{}' : User Created '{}' : RESPONSE STATUS: '{}'",
                request.getRequestURI(),
                userCreated.getId().toString() + " " + userCreated.getFullname(),
                response.getStatus());
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    @ApiOperation(value = "Private: Go to Update User Form")
    @GetMapping(value = "update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model,
                                 HttpServletRequest request, HttpServletResponse response) {
        UserDTO user = userService.findOne(id);
        user.setPassword("");
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        log.info("Go to Update User Form on URI: '{}': RESPONSE STATUS: '{}'",
                request.getRequestURI(),
                response.getStatus());
        return "user/update";
    }

    @ApiOperation(value = "Private: Validate User Update")
    @PostMapping(value = "update/{id}")
    public String updateUser(@PathVariable("id") Integer id,
                             @Valid @ModelAttribute("user") UserDTO user,
                             BindingResult result,
                             Model model,
                             HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            log.warn("Update User Error on URI: '{}': Error Field(s):'{}' : RESPONSE STATUS: '{}'",
                    request.getRequestURI(),
                    result.getFieldErrors().stream()
                            .map(e-> e.getField().toUpperCase())
                            .distinct()
                            .collect(Collectors.joining(", ")),
                    response.getStatus());
            return "user/update";
        }
        User userUpdated = userService.update(user, id);
        log.info("Update User on URI: '{}' : User Updated: '{}' : RESPONSE STATUS: '{}'",
                request.getRequestURI(),
                userUpdated.getId().toString() + " " + userUpdated.getFullname(),
                response.getStatus());
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    @ApiOperation(value = "Private: Delete User")
    @RolesAllowed("ADMIN")
    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model,
                             HttpServletRequest request, HttpServletResponse response) {
        userService.delete(id);
        model.addAttribute("users", userService.findAll());
        log.info("Delete User on URI: '{}' : RESPONSE STATUS: '{}'",
                request.getRequestURI(),
                response.getStatus());
        return "user/list";
    }
}

//unit test with error validation https://mkyong.com/spring-boot/spring-rest-validation-example/
//https://www.baeldung.com/spring-mvc-and-the-modelattribute-annotation
//https://www.codejava.net/frameworks/spring-boot/spring-boot-form-validation-tutorial
//https://asbnotebook.com/2020/04/11/spring-boot-thymeleaf-form-validation-example/
//https://stackabuse.com/spring-boot-thymeleaf-form-data-validation-with-bean-validator/

//https://mkyong.com/spring-boot/spring-rest-spring-security-example/

//Logging https://stackoverflow.com/questions/37710557/how-to-get-request-url-in-spring-boot-restcontroller

//List to string with join comma (lambda), https://dzone.com/articles/java-8-convert-list-to-string-comma-separated

//Swagger example https://www.programcreek.com/java-api-examples/euclidean_gcd/?api=io.swagger.annotations.ApiOperation