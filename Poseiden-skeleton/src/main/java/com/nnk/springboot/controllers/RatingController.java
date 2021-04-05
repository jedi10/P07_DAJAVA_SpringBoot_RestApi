package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Api(tags = {"Rating Controller"})
@Tag(name = "Rating Controller", description = "Private Resources")
@Slf4j
@Controller
@RequestMapping("/rating/")
public class RatingController {

    private RatingRepository ratingRepository;

    private String dataName = "Rating";

    public RatingController(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @ApiOperation(value = "Get List of Rating")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String home(Model model, HttpServletRequest request, HttpServletResponse response)
    {
        log.info("Get {} List on URI: '{}' : RESPONSE STATUS: '{}'",
                dataName,
                request.getRequestURI(),
                response.getStatus());
        model.addAttribute("ratings", ratingRepository.findAll());
        return "rating/list";
    }

    @ApiOperation(value = "Go to Creation Rating Form")
    @GetMapping(value = "add")
    public String addRatingForm(Rating rating, Model model,
                                HttpServletRequest request, HttpServletResponse response)
    {
        log.info("Go to {} Creation Form on URI: '{}' : RESPONSE STATUS: '{}'",
                dataName,
                request.getRequestURI(),
                response.getStatus());
        model.addAttribute("rating", new Rating());
        return "rating/add";
    }

    @ApiOperation(value = "Rating Creation Validation")
    @PostMapping(value = "validate")
    public String validate(@Valid @ModelAttribute("rating") Rating rating,
                           BindingResult result,
                           Model model,
                           HttpServletRequest request, HttpServletResponse response) {
        if(result.hasErrors()){
            log.warn("{} Creation Error on URI: '{}': Error Field(s): '{}' : RESPONSE STATUS: '{}'",
                    dataName,
                    request.getRequestURI(),
                    result.getFieldErrors().stream()
                            .map(e-> e.getField().toUpperCase())
                            .distinct()
                            .collect(Collectors.joining(", ")),
                    response.getStatus());
            return "rating/add";
        }
        Rating ratingCreated = ratingRepository.save(rating);
        log.info("{} Creation on URI: '{}' : {} Created '{}' : RESPONSE STATUS: '{}'",
                dataName,
                request.getRequestURI(),
                dataName,
                ratingCreated.getId() + " " + ratingCreated.getFitchRating(),
                response.getStatus());
        model.addAttribute("ratings", ratingRepository.findAll());
        return "rating/list";
    }

    @ApiOperation(value = "Go to Update Rating Form")
    @GetMapping(value= "update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id,
                                 Model model,
                                 HttpServletRequest request, HttpServletResponse response) {
        String errorMessage = "Invalid rating Id: there is no rating with Id: ";
        Rating rating = ratingRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(errorMessage + id));
        log.info("Go to {} Update Form on URI: '{}': RESPONSE STATUS: '{}'",
                dataName,
                request.getRequestURI(),
                response.getStatus());
        model.addAttribute("rating", rating);
        return "rating/update";
    }

    @ApiOperation(value = "Rating Update Validation")
    @PostMapping(value = "update/{id}")
    public String updateRating(@PathVariable("id") Integer id,
                               @Valid @ModelAttribute("rating") Rating rating,
                               BindingResult result,
                               Model model,
                               HttpServletRequest request, HttpServletResponse response) {
        if(result.hasErrors()){
            log.warn("{} Update Error on URI: '{}': Error Field(s):'{}' : RESPONSE STATUS: '{}'",
                    dataName,
                    request.getRequestURI(),
                    result.getFieldErrors().stream()
                            .map(e-> e.getField().toUpperCase())
                            .distinct()
                            .collect(Collectors.joining(", ")),
                    response.getStatus());
            return "rating/update";
        }
        rating.setId(id);
        Rating ratingUpdated = ratingRepository.save(rating);
        log.info("{} Update on URI: '{}' : {} Updated '{}' : RESPONSE STATUS: '{}'",
                dataName,
                request.getRequestURI(),
                dataName,
                ratingUpdated.getId() + " " + ratingUpdated.getFitchRating(),
                response.getStatus());
        model.addAttribute("ratings", ratingRepository.findAll());
        return "rating/list";
    }

    @ApiOperation(value = "Delete Rating")
    @RolesAllowed("ADMIN")
    @GetMapping(value ="delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model,
                               HttpServletRequest request, HttpServletResponse response) {
        Optional<Rating> rating = ratingRepository.findById(id);
        if(rating.isPresent()){
            ratingRepository.deleteById(id);
            log.info("Delete {} on URI: '{}' : RESPONSE STATUS: '{}'",
                    dataName,
                    request.getRequestURI(),
                    response.getStatus());
        } else {
            log.warn("No {} was deleted on URI: '{}' : RESPONSE STATUS: '{}'",
                    dataName,
                    request.getRequestURI(),
                    response.getStatus());
        }
        model.addAttribute("ratings", ratingRepository.findAll());
        return "rating/list";
    }
}
